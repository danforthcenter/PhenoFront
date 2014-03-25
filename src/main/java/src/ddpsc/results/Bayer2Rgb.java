package src.ddpsc.results;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;

import org.apache.commons.io.IOUtils;
import src.ddpsc.database.tile.Tile;

public final class Bayer2Rgb {
	final static int tHeaderSize = 106;
	final static byte[] tiffheader = new byte[] {
		    // I     I     42	0-3
		      0x49, 0x49, 0x2a, 0x00,
		    // ( offset to tags, 0 ) 4-7
		      0x08, 0x00, 0x00, 0x00,
		    // ( num tags ) 8-9
		      0x08, 0x00,
		   // ( newsubfiletype, 0 full-image )
		      (byte) 0xfe, 0x00, 0x04, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		    // ( image width ) 22 - 33
		      0x00, 0x01, 0x03, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		    // ( image height ) 34 - 45
		      0x01, 0x01, 0x03, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		    // ( bits per sample ) 46 - 57
		      0x02, 0x01, 0x03, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		    // ( Photometric Interpretation, 2 = RGB ) 
		      0x06, 0x01, 0x03, 0x00, 0x01, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00,
		    // ( Strip offsets, 8 )
		      0x11, 0x01, 0x03, 0x00, 0x01, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00,
		    // ( samples per pixel, 3 - RGB)
		      0x15, 0x01, 0x03, 0x00, 0x01, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00,
		    // ( Strip byte count )
		      0x17, 0x01, 0x04, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
	};
	final static String filename = "/Users/shill/Documents/JSEE_workspace/PhenoFront/bin/bayer2rgb/data.raw";
	final static int bpp = 8;
	
	/**
	 * Converts an image from BGGR Raw Bayer format to png. Writes the result to the passed output stream.
	 * Does not close the stream on completion. See overloaded methods for other input options.
	 * 
	 * @param path	Path to raw image.
	 * @param width	Width of image in pixels.
	 * @param height	Height of image in pixels.
	 * @param out	OutputStream to write the resulting png.
	 * @throws IOException
	 */
	public static void convertRawImage(String path, int width, int height, OutputStream out) throws IOException{
		InputStream is = new FileInputStream(new File(path));
		byte[] bayer= IOUtils.toByteArray(is); //raw image
		is.close();
	    int outSize = width * height * (bpp / 8) * 3 + tHeaderSize;
		byte[] rgb = dc1394_bayer_Bilinear(bayer, width, height, bpp);
		byte[] tiff = new byte[outSize];
		putTiff(tiff, width, height, 8);
		System.arraycopy(rgb, 0, tiff, tHeaderSize, rgb.length);			
        ByteArrayInputStream bytes = new ByteArrayInputStream(tiff);
        BufferedImage img = ImageIO.read(bytes);
        ImageIO.write( img, "png", out);
        out.flush();
	}
	
	
	/**
	 * Returns a png of the converted bayer raw image. The output is in RGB space.
	 * @see convertRawImage
	 * @param is Open InputStream to read the raw image from. Closes the InputStream on completion. Leaves output open.
	 * @param width
	 * @param height
	 * @param out
	 * @throws IOException
	 */
	public static void convertRawImage(InputStream is, int width, int height, OutputStream out) throws IOException{
		byte[] bayer= IOUtils.toByteArray(is); //raw image
		is.close();
	    int outSize = width * height * (bpp / 8) * 3 + tHeaderSize;		
		byte[] rgb = dc1394_bayer_Bilinear(bayer, width, height, bpp);
		byte[] tiff = new byte[outSize];
		putTiff(tiff, width, height, 8);
		System.arraycopy(rgb, 0, tiff, tHeaderSize, rgb.length);			
        ByteArrayInputStream bytes = new ByteArrayInputStream(tiff);
        if (! ImageIO.getImageReadersBySuffix("tiff").hasNext()){
        	System.err.println("[WARNING]: Bayer2Rgb Manually loaded Tiff reader.");
	        ImageIO.scanForPlugins();
	        IIORegistry.getDefaultInstance().registerApplicationClasspathSpis();
        }
        BufferedImage img = ImageIO.read(bytes);
        ImageIO.write( img, "png", out);
        out.flush();
	}
	
	/**
	 * @see convertRawImage
	 * @param tile	Tile object from the database. Assumes the tile's imagePath is correct. 
	 * @param out
	 * @throws IOException
	 */
	public static void convertRawImage(Tile tile, OutputStream out) throws IOException{
		int width = tile.getWidth();
		int height = tile.getHeight();
		String path = tile.getImagePath();
		InputStream is = new FileInputStream(new File(path));
		byte[] bayer= IOUtils.toByteArray(is); //raw image
		is.close();
	    int outSize = width * height * (bpp / 8) * 3 + tHeaderSize;		
		byte[] rgb = dc1394_bayer_Bilinear(bayer, width, height, bpp);
		byte[] tiff = new byte[outSize];
		putTiff(tiff, width, height, 8);
		System.arraycopy(rgb, 0, tiff, tHeaderSize, rgb.length);			
        ByteArrayInputStream bytes = new ByteArrayInputStream(tiff);
        BufferedImage img = ImageIO.read(bytes);
        ImageIO.write( img, "png", out);
        out.flush();
	}
        
	/**
	 * Code ported from OpenCV C code. Converts a raw BGGR bayer image to RGB format.
	 * Explanation of bayer interpolation: http://www.unc.edu/~rjean/demosaicing/demosaicing.pdf
	 * 
	 * @param bayer	Array of raw bayer bytes.
	 * @param rgb	Output array to place the RGB data.
	 * @param sizeX	Width of the image.
	 * @param sizeY	Height of the image.
	 */
	public static byte[] dc1394_bayer_Bilinear(byte[] bayer, int sizeX, int sizeY, int bpp)
	{
		final int bayerStep = sizeX; //iterative step
	    final int rgbStep = 3 * sizeX; //iterative step
	    int width = sizeX;
	    int height = sizeY;
	    int blue =  -1;
	    boolean start_with_green = false;
	    int outSize = width * height * (bpp / 8) * 3;
		byte[] rgb = new byte[outSize]; //result
	   // int rgbIndex = tHeaderSize + rgbStep + 3 + 1;
	    int rgbIndex = rgbStep + 3 + 1;
	    ClearBorders(rgb, sizeX, sizeY, 1, 0);
	  //  ClearBorders(rgb, sizeX, sizeY, 1, tHeaderSize);
	    int bayerIndex = 0;
	    height -= 2;
	    width -= 2;
	
	    for (; height-- > 0;  bayerIndex += bayerStep, rgbIndex+= rgbStep) {

	        int t0, t1;
	        int bayerEnd = bayerIndex + width;
	        int tmp, tmp1, tmp2, tmp3;
	        
	        if (start_with_green) {
	        	// bytes need to be cast to int to avoid overflow, each block does this.
	        	// bitmasking to avoid sign errors
	        	tmp = bayer[bayerIndex + 1] & (0xff);
	        	tmp1 = bayer[bayerStep * 2 + 1] &(0xff);
	        	t0 = (tmp + tmp1) / 2;
	        	
	        	tmp = bayer[bayerIndex + bayerStep] &(0xff);
	        	tmp1 = bayer[bayerIndex + bayerStep + 2] &(0xff) ;
	        	t1 = (tmp + tmp1) / 2;
	
	            rgb[rgbIndex - blue] = (byte) ( t0 ); 
	            rgb[0 + rgbIndex] = (byte) ( bayer[bayerStep + bayerIndex + 1] );
	            rgb[blue + rgbIndex] = (byte) ( t1 );
	            bayerIndex++;
	            rgbIndex += 3;
	        }

	        if (blue > 0) {
	            for (; bayerIndex <= bayerEnd - 2; bayerIndex += 2, rgbIndex += 6) {
	            	tmp = bayer[bayerIndex + 0] &(0xff);
	            	tmp1 = bayer[bayerIndex + 2] &(0xff);
	            	tmp2 = bayer[bayerIndex + bayerStep * 2] &(0xff);
	            	tmp3 = bayer[bayerIndex + bayerStep * 2 + 2] &(0xff);
	            	t0 = (tmp + tmp1 + tmp2 + tmp3 + 2) / 4;
	            	
	            	tmp = bayer[bayerIndex + 1] &(0xff);
	            	tmp1 =  bayer[bayerIndex + bayerStep] &(0xff);
	            	tmp2 = bayer[bayerIndex + bayerStep + 2] &(0xff);
	            	tmp3 = bayer[bayerIndex + bayerStep * 2 + 1] &(0xff);
	                t1 = (tmp + tmp1 + tmp2 + tmp3 + 2) / 4 ;
	                
	                rgb[rgbIndex + -1] = (byte) ( t0 );
	                rgb[rgbIndex + 0] = (byte) ( t1 );
	                rgb[rgbIndex + 1] = (byte) ( bayer[bayerIndex + bayerStep + 1] );
	                
	                tmp = bayer[bayerIndex + 2] &(0xff);
	                tmp1 = bayer[bayerIndex + bayerStep * 2 + 2] & 0xff;
	                t0 = (tmp + tmp1 + 1) /2;
	                
	                tmp = bayer[bayerIndex + bayerStep + 1] &(0xff);
	                tmp1 = bayer[bayerIndex + bayerStep + 3] &(0xff);
	                t1 = (tmp + tmp1 + 1) / 2 ;

	                rgb[rgbIndex + 2] = (byte) (t0);
	                rgb[rgbIndex + 3] = (byte) ( bayer[bayerIndex + bayerStep + 2] );
	                rgb[rgbIndex + 4] = (byte) (t1);
	            }
	        } else {
	            for (; bayerIndex <= bayerEnd - 2; bayerIndex += 2, rgbIndex += 6) {
	            	tmp = bayer[bayerIndex + 0] & 0xff;
	            	tmp1 = bayer[bayerIndex + 2] & 0xff;
	            	tmp2 = bayer[bayerIndex + bayerStep * 2] & 0xff;
	            	tmp3 = bayer[bayerIndex + bayerStep * 2 + 2] & 0xff;
            		t0 = (tmp + tmp1 + tmp2 + tmp3 + 2) / 4;
            		
	            	tmp = bayer[bayerIndex + 1] & 0xff;
	            	tmp1 = bayer[bayerIndex + bayerStep] &0xff;
	            	tmp2 = bayer[bayerIndex + bayerStep + 2] & 0xff;
	            	tmp3 = bayer[bayerIndex + bayerStep * 2 + 1] & 0xff;
	            	t1 = (tmp + tmp1 + tmp2 + tmp3 + 2) / 4;
	            	
	                rgb[rgbIndex + 1] = (byte) (t0); 
	                rgb[rgbIndex + 0] = (byte) (t1);
	                rgb[rgbIndex + -1] = (byte) ( bayer[bayerIndex + bayerStep + 1]  );

	                tmp = bayer[bayerIndex + 2] & 0xff;
	                tmp1 = bayer[bayerIndex + bayerStep * 2 + 2] & 0xff;
	                t0 = (tmp + tmp1 + 1) /2;
	                
	                tmp = bayer[bayerIndex + bayerStep + 1] &0xff;
	                tmp1 = bayer[bayerIndex + bayerStep + 3] &0xff;
	                t1 = (tmp + tmp1 + 1) /2 ;
	                
	                rgb[rgbIndex + 4] = (byte) (t0  );
	                rgb[rgbIndex + 3] = (byte) (bayer[bayerIndex + bayerStep + 2]  );
	                rgb[rgbIndex + 2] = (byte) (t1  );
	            }
	        }

	        if (bayerIndex < bayerEnd) {
	        	tmp = bayer[bayerIndex] & 0xff;
	        	tmp1 = bayer[bayerIndex + 2] &0xff;
	        	tmp2 = bayer[bayerIndex + bayerStep * 2] &0xff;
	        	tmp3 = bayer[bayerIndex + bayerStep * 2 + 2] &0xff;
        		t0 = (tmp + tmp1 + tmp2 + tmp3 + 2) / 4;
	        	
	        	tmp = bayer[bayerIndex + 1] & 0xff;
	        	tmp1 = bayer[bayerIndex + bayerStep] & 0xff;
	        	tmp2 = bayer[bayerIndex + bayerStep + 2] &0xff;
	        	tmp3 = bayer[bayerIndex + bayerStep * 2 + 1] &0xff;
	        	t1 = (tmp + tmp1 + tmp2 + tmp3) + 2 / 4;
	        	
	            rgb[rgbIndex - blue] = (byte) (t0);
	            rgb[rgbIndex + 0] = (byte) (t1);
	            rgb[rgbIndex + blue] = (byte) ( bayer[bayerIndex + bayerStep + 1] );
	            bayerIndex++;
	            rgbIndex += 3;
	        }
	        
	        bayerIndex -= width;
	        rgbIndex -= width * 3;
	      
	        blue = -blue;
	        start_with_green = !start_with_green;
	        
	    }
	    return rgb;
	}
	
	/**
	 * Important to note that passed arrays' values may be changed,, that change will be reflected.
	 * @param rgb
	 * @param sizeX
	 * @param sizeY
	 * @param w	width of border
	 */
	public static void ClearBorders(byte[] rgb, int sizeX, int sizeY, int w, int rgbIndex)
	{
	    int i, j;
	    // black edges are added with a width w:
	    i = 3 * sizeX * w - 1;
	    j = 3 * sizeX * sizeY - 1;
	    while (i >= 0) {
	        rgb[rgbIndex + i--] = 0;
	        rgb[rgbIndex + j--] = 0;
	    }

	    int low = sizeX * (w - 1) * 3 - 1 + w * 3;
	    i = low + sizeX * (sizeY - w * 2 + 1) * 3;
	    while (i > low) {
	        j = 6 * w;
	        while (j > 0) {
	            rgb[rgbIndex + i--] = 0;
	            j--;
	        }
	        i -= (sizeX - 2 * w) * 3;
	    }
	}
	
	/**
	 * Writes a tiff header to the passed array. Expects the space for the Tiff header to already be allocated.
	 * 
	 * @param rgb	Array to add the header to
	 * @param width	Width of the image	
	 * @param height	Height of the image	
	 * @param bpp	Bits per pixel (LemnaTec uses 8)
	 */
    public static void putTiff(byte[] rgb, int width, int height, int bpp)
    {
        int ulTemp=0;
        int sTemp=0;
        System.arraycopy(tiffheader, 0, rgb, 0, tHeaderSize);
        //sTemp = TIFF_HDR_NUM_ENTRY;
        sTemp = 8;
        //memcpy(rgb + 8, &sTemp, 2);
        
        rgb[8] = (byte) (sTemp);
        
        //width
        //memcpy(rgb + 10 + 1*12 + 8, &width, 4);

        rgb[10 + 12 * 1 + 8 + 0] = (byte) (width >> 0);
        rgb[10 + 12 * 1 + 8 + 1] = (byte) (width >> 8);
        //height
        //memcpy(rgb + 10 + 1*12 + 8, &width, 4);

        rgb[10 + 12 * 2 + 8 + 0] = (byte) (height >> 0);
        rgb[10 + 12 * 2 + 8 + 1] = (byte) (height >> 8);
      

        //bbp
        //memcpy(rgb + 10 + 3*12 + 8, &bpp, 2);
        rgb[10 + 12 * 3 + 8 + 0] = (byte) bpp; 
        
      
        //strip byte count
        //memcpy(rgb + 10 + 7*12 + 8, &ulTemp, 4);
        ulTemp = width * height * (bpp / 8) * 3;
        rgb[10 + 7 * 12 + 8 + 0] = (byte) (ulTemp >> 0);
        rgb[10 + 7 * 12 + 8 + 1] = (byte) (ulTemp >> 8);
        
        //strip offset
        //memcpy(rgb + 10 + 5*12 + 8, &sTemp, 2);
        sTemp = tHeaderSize;
        rgb[10 + 5 * 12 + 8] = (byte) (sTemp);      
    }
}
