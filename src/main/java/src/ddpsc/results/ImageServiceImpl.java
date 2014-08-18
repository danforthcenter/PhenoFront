package src.ddpsc.results;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
/**
 * This service is for processing each of the image types. This is the classes only responsibility.
 * There is a sister class which builds the 
 * @see TileFileLTSystemUtil.java Bayer2Rgb.java
 * @author shill
 *
 */
public class ImageServiceImpl implements ImageService{
	public final static int NIRWIDTH = 320;
	public final static int NIRHEIGHT = 254;
	public final static int FLOUWIDTH = 1388;
	public final static int FLOUHEIGHT = 1038;
	public final static int VISWIDTH = 2454;
	public final static int VISHEIGHT = 2056;
	
	public static void main(String[] args)
	{
		ImageServiceImpl imageConverter = new ImageServiceImpl();
		
		long startTime = System.currentTimeMillis();
		// Output some converted files
		String directory = "C:\\Development\\DDPSC\\PostgreSQL\\9.3\\data\\pgftp\\LemnaTest";
		
		List<File> files = listFilesForFolder(new File(directory + "\\2013-09-24"));
		
		for (File file : files) {
			String filename = file.getName();
			
			try {
				FileOutputStream output = new FileOutputStream(directory + "\\" + filename + ".png");
				imageConverter.vis2Png(directory + "\\2013-09-24\\" + filename, output);
			}
			catch (Exception e) {
				System.out.println("Cannot convert " + filename);
			}
		}
		
		long endTime = System.currentTimeMillis();
		long processingTime_ms = endTime - startTime;
		long processingTime_s  = (processingTime_ms / (1000)) % 60;
		long processingTime_m  = (processingTime_ms / (1000 * 60)) % 60;
		long processingTime_h  = processingTime_ms / (1000 * 60 * 60);
		System.out.println("\nProcessing took " + processingTime_h + "h " + processingTime_m + "m " + processingTime_s + "s");
		
	}
	
	public static List<File> listFilesForFolder(final File folder)
	{
		List<File> files = new ArrayList<File>();
		
	    for (final File fileEntry : folder.listFiles())
	        files.add(fileEntry);
	    
	    return files;
	}
	
	/**
	 * Converts the passed image to a png format. Expects the string to be prebuilt with the 
	 * LTFileSystem conversion. If a file does not exist return.
	 * 
	 * @throws ZipException 
	 * 
	 */
	@Override
	public void nir2Png(String filename, OutputStream out) throws IOException, ZipException {
		
		if (! new File(filename).exists()) {
			throw new FileNotFoundException(filename + " is not found.");
		}
		
		InputStream in = readZipImageEntry(filename);
		byte[] bytes = IOUtils.toByteArray(in);
		in.close();
		BufferedImage img = new BufferedImage(NIRWIDTH, NIRHEIGHT, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster grayRaster = img.getRaster();
		for (int x = 0; x < NIRHEIGHT; x++){
			for (int y = 0; y < NIRWIDTH; y++){
				grayRaster.setSample(y, NIRHEIGHT - x - 1, 0, bytes[x * NIRWIDTH + y] );
			}
		}
	    ImageIO.write(img, "png", out);
	}

	/**
	 * Converts a passed raw 16bit grayscale flou image to a png image. Closes input stream when it is read,
	 * does not close the output stream.
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 * @throws ZipException 
	 */
	@Override
	public void flou2Png(String filename, OutputStream out) throws IOException, ZipException{
		//I feel like there was a reason to silently erro before, I don't know why that was.
		if (! new File(filename).exists()){
			throw new FileNotFoundException(filename + " is not found.");
		}
		InputStream in = readZipImageEntry(filename);
		byte[] bytes = IOUtils.toByteArray(in);
		in.close();
		short[] pixelArray = toShorts(bytes);
		Point origin = new Point();
		DataBufferUShort dataBuffer = new DataBufferUShort(pixelArray, (FLOUWIDTH * FLOUHEIGHT), 0);
		int[] bandOffets = {0};
		ComponentSampleModel sampleModel = new ComponentSampleModel(DataBuffer.TYPE_USHORT, FLOUWIDTH, FLOUHEIGHT, 1, FLOUWIDTH, bandOffets);
		WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, origin);
		BufferedImage buffImg = new BufferedImage(FLOUWIDTH, FLOUHEIGHT, BufferedImage.TYPE_USHORT_GRAY);
		buffImg.setData(raster);
	    ImageIO.write(buffImg, "png", out);
		
	}
	/**
	 * Converts a vis image to png. Expects the raw image to use a bayer rggb filter. 
	 * 
	 * @see Bayer2Rgb.java
	 */
	@Override
	public void vis2Png(String filename, OutputStream out) throws IOException, ZipException {
		if (! new File(filename).exists()){
			throw new FileNotFoundException(filename + " is not found.");
		}
		Bayer2Rgb.convertRawImage(readZipImageEntry(filename), VISWIDTH, VISHEIGHT, out);
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
	
	/**
	 * Utility function that converts an array of bytes to an array of shorts, does not bias for signed integers.
	 * Moves the final 14 bit array by 1 bit to the left, would move by 2 (max possibility) but that can interfere
	 * with the sign bit. To avoid this we just let the first bit "exist". 
	 * (THANKS JAVA)
	 * @param bytes
	 * @return
	 */
	private static short[] toShorts(byte[] bytes) {	
		short[] shorts = new short[bytes.length/2];	   
	    for (int n = 0; n < bytes.length; n += 2){
	    	shorts[n/2] = (short) (bytes[n + 1]);
	    	shorts[n/2] = (short) ( (short) shorts[n/2] << (byte) 8);
	    	shorts[n/2] = (short) ( (short) shorts[n/2] | (short) (0xff) & bytes[n]);
	    	shorts[n/2] = (short) (shorts[n/2] << 1); //14 bit image, moving by 1 bit to generally brighten the image and avoid error with sign bit
	    } 
	    return shorts;
	}
}
