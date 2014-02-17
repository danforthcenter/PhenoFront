package src.ddpsc.results;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

public class Tester {

	public static void main(String[] args) {
		try {
			dumb(args);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}
	/**
	 * We need to figure out how to get more than just vis images :/
	 * 
	 * @param args
	 * @throws IOException
	 * @throws ZipException
	 */
	public static void dumb(String[] args) throws IOException, ZipException {
		/**
		 * Things to try, 
		 * only look at first 14 bits (???)
		 * only look at last 14 bits (???)
		 * extrapolate 14 bits into 16 bit space
		 * 
		 */
		
		//old stuff
		String filename = "/data/pgftp/LemnaTest/2013-11-06/blob212853";
		int width = 1388;
		int height = 1038;
		//some shit i copied on the internet
		byte[] spot = IOUtils.toByteArray(new FileInputStream("/Users/shill/Documents/JSEE_workspace/PhenoFront/lib/data"));
		short[] pixelArray = toShorts(spot);
		//foreach short, value of  
		
	  
		Point origin = new Point();
		DataBufferUShort dataBuffer = new DataBufferUShort(pixelArray, (width * height), 0);
		int[] bandOffets = {0};
		ComponentSampleModel sampleModel = new ComponentSampleModel(DataBuffer.TYPE_USHORT, width, height, 1, width, bandOffets);
		WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, origin);
		

		BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);
		buffImg.setData(raster);
	    ImageIO.write(buffImg, "tiff", new File("/Users/shill/Documents/JSEE_workspace/PhenoFront/lib/b.tiff"));

	}

	public static void changeArr(int[] arr) {
		arr[4] = 2;
	}
	
	public static short[] toShorts(byte[] bytes) {
		
		short[] shorts = new short[bytes.length/2];
	   
	    for (int n = 0; n < bytes.length; n += 2){
	    	shorts[n/2] = bytes[n] ;
	    	shorts[n/2] = (short) ((short) shorts[n/2]  << (byte) 8 | bytes[n + 1]);
	    	shorts[n/2] = (short) (shorts[n/2] &0xff);
	    }
	    
	    return shorts;
	}
	public static short[] toShortsFrom14Bit(byte[] bytes) {
		
		short[] shorts = new short[bytes.length/2];
	    for (int n = 0; n < bytes.length; n += 2){
	    	shorts[n/2] = bytes[n + 1] ;
	    	shorts[n/2] = (short) ((short) shorts[n/2]  << (byte) 8 | bytes[n]);
	    	shorts[n/2] = (short) ((shorts[n/2] << 2) + 2);
	    } 
	    return shorts;
	}
	
	public static short toShort(byte[] data){
		short value = data[1];
		value = (short) ((value << 8) | data[0]);
		return value;
	}
	

}
