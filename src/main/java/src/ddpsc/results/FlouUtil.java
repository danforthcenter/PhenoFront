package src.ddpsc.results;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

/**
 * Class for working with flou images. As of now, just converts flou raw images to png.
 * @author shill
 *
 */
public class FlouUtil {
	public final static int FLOUWIDTH = 1388;
	public final static int FLOUHEIGHT = 1038;

	/**
	 * Converts a passed raw 16bit greyscale flou image to a png image. Closes input stream when it is read,
	 * does not close the output stream.
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void flou2Png(InputStream in, OutputStream out) throws IOException{
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
	 * Utility function that converts an array of bytes to an array of shorts, does not bias for signed integers.
	 * (THANKS JAVA)
	 * @param bytes
	 * @return
	 */

	public static short[] toShorts(byte[] bytes) {
		
		short[] shorts = new short[bytes.length/2];
	   
	    for (int n = 0; n < bytes.length; n += 2){
	    	shorts[n/2] = (short) (bytes[n + 1]);
	    	shorts[n/2] = (short) ( shorts[n/2] << (byte) 8);
	    	shorts[n/2] = (short) ( shorts[n/2] | (short) (0xff) & bytes[n]);
	    	shorts[n/2] *= 4; //14 bit image, need to shift by 2 bits.
	    }
	    return shorts;
	}
}
