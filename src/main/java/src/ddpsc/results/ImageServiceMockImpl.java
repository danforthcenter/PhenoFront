package src.ddpsc.results;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;

import src.ddpsc.database.experiment.Experiment;
import src.ddpsc.database.tile.Tile;
import src.ddpsc.database.tile.TileFileLTSystemUtil;

//TODO: Remove this and create a better ImageService implementation.
public class ImageServiceMockImpl implements ImageService {
	static String mockPath = "/Users/shill/Documents/JSEE_workspace/PhenoFront/bin/bayer2rgb/testimgs/";
	static String img1 = "duckweed.jpg";
	static String img2 = "duckweed1.jpg";
	static String img3 = "duckweedbg1.jpg";
	static String img4 = "duckweedbg2.jpg";
	static String img5 = "duckweedbg3.jpg";
	/**
	 * Operates on the zip file and finds the first image as an inputstream.
	 * @throws ZipException 
	 * @throws FileNotFoundException 
	 */
	public void zipToRaw(InputStream is, OutputStream os) throws ZipException, IOException {
		String filename = "/Users/shill/Documents/JSEE_workspace/PhenoFront/bin/test/blob240330";
		int width = 2454;
		int height = 2048;
		ZipFile zipFile = new ZipFile(filename);
		FileHeader entry = zipFile.getFileHeader("data");
		InputStream entryStream = zipFile.getInputStream(entry);
		OutputStream out = new FileOutputStream(
				"/Users/shill/Documents/JSEE_workspace/PhenoFront/bin/test/output.png");
		Bayer2Rgb.convertRawImage(entryStream, width, height, os); //write to outputstream
		out.flush();
		out.close(); 	
		
	}
	/**
	 * Accepts a tile and an open OutputStream. Finds the tile on the file system by using the date and experiment
	 * converts it to a png, and writes the png to the output stream. Leaves the OutputStream open.
	 * @param tile
	 * @param out
	 * @param date
	 * @param experiment
	 * @throws ZipException 
	 * @throws IOException 
	 */
	public static void tileWriter(Tile tile, OutputStream out, DateTime date, Experiment experiment) throws ZipException, IOException{
		String filename = TileFileLTSystemUtil.getTileFilename(tile, date, experiment);
		System.out.println(filename);
		ZipFile zipFile = new ZipFile(filename);
		FileHeader entry = zipFile.getFileHeader("data");
		InputStream entryStream = zipFile.getInputStream(entry);
		if (tile.getDataFormat() == 0){
			NirUtil.Nir2Png(entryStream, out);
		}
		if (tile.getDataFormat() == 1){
			Bayer2Rgb.convertRawImage( entryStream , tile.getWidth(), tile.getHeight(), out);
		}
		if (tile.getDataFormat() == 6){
			FlouUtil.flou2Png( entryStream, out);
		}

	}

	
	/**
	 * Returns a random image from /Users/shill/Documents/JSEE_workspace/PhenoFront/bin/bayer2rgb/testimgs/ as a byte array
	 * @return
	 * @throws IOException
	 */
	public static byte[] getImageBytes() throws IOException {
		String[] imgs = new String[5];
		imgs[0] = img1;
		imgs[1] = img2;
		imgs[2] = img3;
		imgs[3] = img4;
		imgs[4] = img5;
		Random rng = new Random(System.currentTimeMillis());
		String use = imgs[rng.nextInt(5)];
		System.out.println("Chose: " + use);
		InputStream is = new FileInputStream(new File(mockPath + use));
		byte[] ret = IOUtils.toByteArray(is);
		is.close();
		return ret;
	}

	/**
	 * Accepts an open InputStream to some raw image, and converts that image to a png, then returns that
	 * png as an OutputStream.
	 * 
	 * @param is	InputStream pointing to RawImage
	 */
	@Override
	public OutputStream rawToImage(InputStream is) {
		//take raw input stream, c
		return null;
	}

	@Override
	public InputStream zipToRaw(InputStream is) throws ZipException,
			IOException {
		return null;
	}

}
