package src.ddpsc.results;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import src.ddpsc.database.experiment.Experiment;
import src.ddpsc.database.snapshot.Snapshot;
import src.ddpsc.database.tile.Tile;
import src.ddpsc.database.tile.TileFileLTSystemUtil;

/**
 * Manages the results and conversion of images. 
 * 
 * Since decoupling the image archiving and the results is pretty much impossible, we will allow that to be
 * another method associated with this class. Each type of archiving may be another 
 * 
 * @author shill
 * 
 */
public class ResultsBuilder
{
	private static final Logger log = Logger.getLogger(ResultsBuilder.class);
	
	/**
	 * For threads we could do something like spawn 1 thread per tile
	 * 
	 * each thread get's their own output stream
	 * 
	 * spawn threads, setup results stream, write in order of streams, write to
	 * closure,
	 * 
	 * this may seem slow but since all of the threads are concurrent the
	 * runtime will be the time of the slowest thread. (vs O(n)) which is fine.
	 */
	private OutputStream requestStream;
	
	private List<Snapshot> snapshots;
	private Experiment experiment;
	
	private boolean convertJPEG;
	
	private HashMap<String, InputStream> threadStreams;
	private ThreadGroup group;
	
	public ResultsBuilder(
			OutputStream out,
			List<Snapshot> snapshots,
			Experiment experiment,
			boolean convertJPEG)
	{
		this.requestStream = out;
		this.snapshots = snapshots;
		this.experiment = experiment;
		this.convertJPEG = convertJPEG;
		this.threadStreams = new HashMap<String, InputStream>();
		this.group = new ThreadGroup("Image Processors");
	}
	
	
	/**
	 * Handles the entire process of creating an archive, processing images, and writing to that archive.
	 * This type of method should probably be in a subclass and the parent be abstract.
	 * 
	 * @throws IOException
	 */
	public long writeZipArchive() throws IOException
	{
		long size_bytes = 0;
		ZipOutputStream archive = new ZipOutputStream(this.requestStream);
		
		// Add snapshots CSV file
		String snapshotCSV = "SnapshotInfo.csv";
		archive.putNextEntry(new ZipEntry(snapshotCSV));
		archive.write(Snapshot.toCSV(snapshots, true).getBytes());
		log.info("Snapshot CSV data added to the zip archive.");
		
		// Add tiles CSV file
		List<Tile> tiles = Snapshot.getTiles(snapshots);
		String tileCSV = "TileInfo.csv";
		archive.putNextEntry(new ZipEntry(tileCSV));
		archive.write(Tile.toCSV(tiles, true).getBytes());
		log.info("Tile CSV data added to the zip archive.");
		
		// Add images
		for (Snapshot snapshot : snapshots) {
			
			log.info("Adding snapshot " + snapshot.id + " to the zip archive.");
			String prefixName = "snapshot" + snapshot.id + "/";
			this.threadStreams.clear(); // Reset from previous snapshot processing
			
			this.processImages(
					snapshot.getTiles(),
					new DateTime(snapshot.timestamp),
					this.experiment,
					prefixName);
			
			for (String imageName : this.threadStreams.keySet()){
				try{
					ZipEntry nextImage = new ZipEntry(imageName);
					size_bytes += nextImage.getSize();
					archive.putNextEntry(nextImage);
					
					
					log.info("Waiting to write " + imageName + " to zip.");
					archive.write(IOUtils.toByteArray(this.threadStreams.get(imageName)));
					
					log.info(imageName + " written to zip.");
					archive.flush();
				}
				
				catch(java.util.zip.ZipException e){
					// TODO: Determine why this is thrown and how to handle it
					log.error("Uncaught zip error " + e.getMessage());
					System.err.println(e.getMessage());
				}
			}
			
			// Pauses all the open threads
			// TODO: Why are there any open threads
			try {
				log.info("Accessing this thread group.");
				synchronized (this.group) {
					log.info("In thread group for snapshot " + snapshot.id + " there are " + group.activeCount() + " open threads.");
					if (group.activeCount() > 0)
						group.wait();
				}
			}
			
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
		log.info("All snapshots added the zip archive.");
		
		archive.finish();
		log.info("Archive finished.");
		
		return size_bytes;
	}
	
	/**
	 * For each tile create a thread to convert the raw images and return.
	 * Opens an output stream for each thread, then writes each stream to the requestStream 
	 * until that stream is closed.
	 * 
	 * Currently threads handle all exceptions, including zip exceptions (which would mean corrupted or something)
	 * It is possible that these need to be handled and logged, in which case a listener should be implemented.
	 * 
	 * To handle null tiles, it simply returns. That is snapshots with no tiles associated with it.
	 * 
	 * TODO: Implement listener that monitors progress
	 * 
	 * @param tiles
	 * @param datetime
	 * @param experiment
	 * @param namePrefix	Prefix to be added to map names. This could be anything but was added for zip archives.
	 * @throws IOException 
	 */
	public void processImages(List<Tile> tiles, DateTime datetime, Experiment experiment, String namePrefix) throws IOException
	{
		if (tiles == null)
			return;
		
		ImageService imageConverter = new ImageService(convertJPEG);
		
		for (Tile tile : tiles) {
			
			PipedInputStream input = new PipedInputStream();
			PipedOutputStream threadedOutput = new PipedOutputStream(input);
			
			String imageName = namePrefix + tile.getName() + (convertJPEG ? ".jpg" : ".png");
			
			this.threadStreams.put(imageName, input);
			
			// TODO: No logging happens, nothing. This is something that needs to be discussed and addressed.
			log.info("Beginning image processing for tile " + imageName);
			ImageProcessor imageProcessor = new ImageProcessor(
					group,
					"Process Image",
					threadedOutput,
					tile,
					datetime,
					experiment,
					imageConverter);
			imageProcessor.start();
		}
	}
}


/**
 * Inner class which represents the thread state and all of the information threads need to know.
 * These threads check the type of image, convert it, and then write to the output stream.
 * 
 * @author shill
 */
class ImageProcessor extends Thread
{
	private static final Logger log = Logger.getLogger(ImageProcessor.class);
	
	private Tile tile;
	private DateTime date;
	private Experiment experiment;
	
	private OutputStream output;
	private ImageService imageConvert;
	
	public ImageProcessor(
			ThreadGroup group,
			String name,
			OutputStream output,
			Tile tile,
			DateTime date,
			Experiment experiment,
			ImageService imageConverter)
	{
		super(group, name);
		
		this.imageConvert = imageConverter;
		
		this.output = output;
		
		this.tile = tile;
		this.date = date;
		this.experiment = experiment;
	}
	
	@Override
	public void run()
	{
		log.info("Converting " + tile.getSpectrum() + " tile, " + tile.getName() + ".");
		
		try{
			String filename = TileFileLTSystemUtil.getTileFilename(tile, date, experiment);
			
			if (! new File(filename).exists())
				throw new FileNotFoundException(filename + " is not found.");
			
			InputStream input = readZipImageEntry(filename);
			
			if (tile.dataFormat == 0)
				imageConvert.toInfrared(input, output);
			
			else if (tile.dataFormat == 1)
				imageConvert.toVisible(input, output);
			
			else if (tile.dataFormat == 6)
				imageConvert.toFluorescent(input, output);
			
			
			output.flush();
			log.info("Tile " + filename + " has completed processing.");
		}
		
		
		
		catch (ZipException e) {
			e.printStackTrace();
		}
		catch(NullPointerException e) {
			e.printStackTrace();
			System.err.println("Tile " + tile.getName() + " does not exist on file system.");
		}
		catch(IOException e) {
			e.printStackTrace();
			System.err.println("IOException in Thread group.");
		}
		
		
		// If some previous exception is thrown we need to know so that we can close the threads stream
		try {
			output.close();
		}
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Utility function which handles the 3 lines of code for reading the raw image located in the
	 * LemnaTec Blob. This is a DDPSC LemnaTec system specific function.
	 * @param filename
	 * @return
	 * @throws ZipException
	 */
	private static InputStream readZipImageEntry(String filename) throws ZipException
	{
		ZipFile zipFile = new ZipFile(filename);
		FileHeader entry = zipFile.getFileHeader("data");
		
		return zipFile.getInputStream(entry);
	}
}
