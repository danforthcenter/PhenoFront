package src.ddpsc.results;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

public class NirUtil {
	/**
	 * Converts the raw pixelmap of a NIR image to png. Expects the dimensions to be 320 x 254, this is the spec of the
	 * LemnaTec cameras. Expects the images to be 8bit grayscale. Leaves the OutputStream unclosed. Closes InputStream.
	 * @throws IOException 
	 */
	public final static int NIRWIDTH = 320;
	public final static int NIRHEIGHT = 254;
	public static void Nir2Png(InputStream in, OutputStream out) throws IOException{
		byte[] bytes = IOUtils.toByteArray(in);
		in.close();
		BufferedImage img = new BufferedImage(NIRWIDTH, NIRHEIGHT, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster grayRaster = img.getRaster();
		for (int x = 0; x < NIRHEIGHT; x++){
			for (int y = 0; y < NIRWIDTH; y++){
				grayRaster.setSample(y, x, 0, bytes[x * NIRWIDTH + y] );
			}
		}
	    ImageIO.write(img, "png", out);
	}
}
