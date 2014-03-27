package src.ddpsc.results;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;

import net.lingala.zip4j.exception.ZipException;
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
public class ResultsBuilder {
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
	private ArrayList<Snapshot> snapshots;
	private Experiment experiment;
	private HashMap<String, InputStream> threadStreams;
	private ThreadGroup group;

	public ResultsBuilder(OutputStream out, ArrayList<Snapshot> snapshots,
			Experiment experiment) {
		//new DateTime(snapshot.getTimeStamp())
		this.requestStream = out;
		this.snapshots = snapshots;
		this.experiment = experiment;
		this.threadStreams = new HashMap<String, InputStream>();
		this.group = new ThreadGroup("Image Processors");
	}
	
	/**
	 * For each tile create a thread to convert the raw images and return.
	 * Opens an output stream for each thread, then writes each stream to the requestStream 
	 * until that stream is closed.
	 * 
	 * Currently threads handle all exceptions, including zip exceptions (which would mean corrupted or something)
	 * It is possible that these need to be handled and logged, in which case a listener should be implemented.
	 * TODO: Implement listener
	 * @param tiles
	 * @param datetime
	 * @param experiment
	 * @param namePrefix	Prefix to be added to map names. This could be anything but was added for zip archives.
	 * @throws IOException 
	 */
	public void processImages(ArrayList<Tile> tiles, DateTime datetime, Experiment experiment, String namePrefix) throws IOException {
		for (Tile tile : tiles) {
			PipedInputStream is = new PipedInputStream();
			OutputStream threadStream = new PipedOutputStream(is);
			String imgName = namePrefix 
					+ tile.getCameraLabel() + "_"
					+ tile.getRawImageOid() + ".png";
			this.threadStreams.put(imgName, is);
			
			//does not handle any ZipExceptions from reading data, they simply would not exist.
			// no logging happens, nothing. This is something that needs to be discussed and addressed.
			new ImageProcessor(group, "name", threadStream, tile, datetime, experiment).start();
		}

	}
	
	/**
	 * Handles the entire process of creating an archive, processing images, and writing to that archive.
	 * This type of method should probably be in a subclass and the parent be abstract.
	 * 
	 * @throws IOException
	 */
	public void writeZipArchive() throws IOException{
		ZipOutputStream archive = new ZipOutputStream(this.requestStream);

		for (Snapshot snapshot : snapshots) {
			String entryName = "snapshot" + snapshot.getId() + "/snapshot"
					+ snapshot.getId() + ".csv";
			String prefixName = "snapshot" + snapshot.getId() + "/snapshot"
					+ snapshot.getId() + "/";
			archive.putNextEntry(new ZipEntry(entryName));
			archive.write(snapshot.csvWriter().getBytes());
			archive.flush(); // keep responsive
			System.err.println("WARNING: casting to ZipOutputStream, may fail");
			
			this.processImages((ArrayList<Tile>) snapshot.getTiles(), new DateTime(snapshot.getTimeStamp()), this.experiment, prefixName);
			for(String key : this.threadStreams.keySet()){
				ZipEntry entry = new ZipEntry(key);
				archive.putNextEntry(entry);
				archive.write(IOUtils.toByteArray(this.threadStreams.get(key)));	
				archive.flush();
			}
			try {
				synchronized (this.group) {
					if (group.activeCount() > 0){
						group.wait();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		archive.finish();
	}
}

/**
 * Inner class which represents the thread state and all of the information threads need to know.
 * These threads check the type of image, convert it, and then write to the output stream.
 * @author shill
 *
 */
class ImageProcessor extends Thread{
	private OutputStream os;
	private Tile tile;
	private ImageService imageService;
	private DateTime date;
	private Experiment experiment;

	public ImageProcessor(ThreadGroup group, String name, OutputStream os, Tile tile, DateTime date, Experiment experiment){
		super(group, name);
		this.imageService = new ImageServiceImpl();
		this.os = os;
		this.tile = tile;
		this.date = date;
		this.experiment = experiment;
	}
	public ImageProcessor(String name, OutputStream os, Tile tile){
		super(name);
		this.os = os;
		this.tile = tile;
	}
	@Override
	public void run(){
		String filename = TileFileLTSystemUtil.getTileFilename(this.tile, this.date, this.experiment);
		try{
			if (tile.getDataFormat() == 0){
				this.imageService.nir2Png(filename, this.os);
			}
			else if (tile.getDataFormat() == 1){
				this.imageService.vis2Png( filename, this.os);
			}
			else if (tile.getDataFormat() == 6){
				this.imageService.flou2Png( filename, this.os);
			}
			this.os.flush();
		} catch (ZipException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		try{
			//recovers from zip exception
			this.os.close();
		}
		catch (IOException e){
			e.printStackTrace();
		} 
	}
}
