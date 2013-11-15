package src.ddpsc.results;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

public final class Bayer2RgbOld {
	final static int tHeaderSize = 106;
	final static byte[] tiffheader = new byte[] {
		    // I     I     42	0-3
		      0x49, 0x49, 0x2a, 0x00,
		    // ( offset to tags, 0 ) 4-7
		      0x08, 0x00, 0x00, 0x00,
		    // ( num tags ) 8-9
		      0x08, 0x00,
		   // ( newsubfiletype, 0 full-image ) 10 - 21
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
	public static void main(String[] dicks){
		int width = 2454;
		int height = 2056;
		int bpp = 8;
		byte a = (byte) 25 + (byte) 55;
		System.out.println(a);
		byte b = (byte) 55;
		System.out.println(a + b);
		try{
			InputStream is = new FileInputStream(new File(filename));
			byte[] bayer= IOUtils.toByteArray(is); //raw image
			
		    int outSize = width * height * (bpp / 8) * 3 + tHeaderSize;
			byte[] rgb = new byte[outSize]; //result
			InputStream expect = new FileInputStream(new File("/Users/shill/Documents/JSEE_workspace/PhenoFront/bin/bayer2rgb/output.tiff"));
			byte[] exp = IOUtils.toByteArray(expect);
			expect.close();
			putTiff(rgb, width, height, 8);
			dc1394_bayer_Bilinear(bayer, rgb, width, height, null);
			OutputStream os = new FileOutputStream("/Users/shill/pls.tiff");
			os.write(rgb);
			os.flush();
			os.close();
			System.out.println("closed output");
			is.close();
			HashMap<String, Integer> offBy = new HashMap<String, Integer>();
			for (int i = 0; i < rgb.length; i++){
	        	if(rgb[i] != exp[i]){
	        		/*
	        		System.out.print(i +";");
	        		System.out.println(rgb[i-1] + ";" + rgb[i] + ";" + rgb[i+1]);
	        		System.out.println("\t"+exp[i-1] + ";" + exp[i] + ";" + exp[i+1]);
	        		*/
	        		//System.out.print(String.format("%02X", rgb[i]) + ";");
	        		//System.out.print(String.format("%02X ", exp[i]) + " ");
	        		
	        		if (offBy.containsKey(Integer.toString(exp[i] - rgb[i]))){
	        			offBy.put(Integer.toString((int) exp[i] - (int) rgb[i]), offBy.get(Integer.toString((int) exp[i] - (int) rgb[i])) + 1);
	        		} else{
	        			offBy.put(Integer.toString((int)exp[i] - (int)rgb[i]), 1);
	        		}
	        	}
	        }
			//System.out.println(offBy.toString());
			//byte[] rgb1 = new byte[outSize]; //result

			//putTiff(rgb1, width, height, 8);
			//dc1394_bayer_Bilinear(bayer, rgb1, width, height, offBy);
			
		}
		catch (IOException e){
			e.printStackTrace();
		}
		System.out.println((byte) 254);
		
	}
	// returns dc1394error_t
	// bayer and rgb are memmaps (2D array)
	// uint8_t *restrict rgb
	public static void dc1394_bayer_Bilinear(byte[] bayer, byte[] rgb, int sizeX, int sizeY, HashMap<String, Integer> map)
	{
		final int bayerStep = sizeX; //iterative step
	    final int rgbStep = 3 * sizeX; //iterative step
	    int width = sizeX;
	    int height = sizeY;
	    int blue =  -1;
	    boolean start_with_green = false;

	    int rgbIndex = tHeaderSize + rgbStep + 3 + 1;

	    //testing
	    //14789961;-123;1;118
		//-123;-127;118
	   
	    //actually does work so need 2 capture something
	    System.out.println("uhh" + rgb.length);
	    ClearBorders(rgb, sizeX, sizeY, 1, tHeaderSize);
	   
	    int bayerIndex = 0;
	    //explicity bayerIndex
	    height -= 2;
	    width -= 2;
	    //iterate until height = 0, each step move our pointer over
	    System.out.println(rgbIndex);
	    //previousy was itertaing on rgb pointer
	    
	    //adding rgbIndex and bayerIndex to all accessors, changing all uint8 to byte
	    for (; height-- > 0;  bayerIndex += bayerStep, rgbIndex+= rgbStep) {
	    	
	 	    if (rgbIndex == 14797730 ){
	 	    	System.out.println("breakpoint!!");
	 	    }
	        int t0, t1;
	        int bayerEnd = bayerIndex + width;
	        
	        if (start_with_green) {
	            /* OpenCV has a bug in the next line, which was
	               t0 = (bayer[0] + bayer[bayerStep * 2] + 1) >> 1; */
	            t0 = (bayer[bayerIndex + 1] + bayer[bayerIndex + bayerStep * 2 + 1] + 1) >> 1;
	            t1 = (bayer[bayerIndex + bayerStep] + bayer[bayerIndex + bayerStep + 2] + 1) >> 1;
	
	            rgb[rgbIndex - blue] = (byte) ( t0  ); 
	            rgb[0 + rgbIndex] = (byte) ( bayer[bayerStep + bayerIndex + 1]  );
	            rgb[blue + rgbIndex] = (byte) (t1  );
	            bayerIndex++;
	            rgbIndex += 3;
	        }

	        if (blue > 0) {
	            for (; bayerIndex <= bayerEnd - 2; bayerIndex += 2, rgbIndex += 6) {
	                t0 = (bayer[bayerIndex + 0]   + bayer[bayerIndex + 2]   + bayer[bayerIndex + bayerStep * 2]   +
	                      bayer[bayerIndex + bayerStep * 2 + 2]   + 2) >> 2;
	                t1 = (bayer[bayerIndex + 1]   + bayer[bayerIndex + bayerStep]   +
	                      bayer[bayerIndex + bayerStep + 2]   + bayer[bayerIndex + bayerStep * 2 + 1]   +
	                      2) >> 2;
	                rgb[rgbIndex + -1] = (byte) (t0  );
	                rgb[rgbIndex + 0] = (byte) (t1  );
	                rgb[rgbIndex + 1] = (byte) (bayer[bayerIndex + bayerStep + 1]  );

	                t0 = (bayer[bayerIndex + 2]  + bayer[bayerIndex + bayerStep * 2 + 2]  + 1) >> 1;
	                t1 = (bayer[bayerIndex + bayerStep + 1]  + bayer[bayerIndex + bayerStep + 3]  +
	                      1) >> 1;
	                rgb[rgbIndex + 2] = (byte) (t0  );
	                rgb[rgbIndex + 3] = (byte) (bayer[bayerIndex + bayerStep + 2]  );
	                rgb[rgbIndex + 4] = (byte) (t1  );
	            }
	        } else {
	            for (; bayerIndex <= bayerEnd - 2; bayerIndex += 2, rgbIndex += 6) {
	                t0 = (bayer[bayerIndex + 0]  + bayer[bayerIndex + 2]  + bayer[bayerIndex + bayerStep * 2]  +
	                      bayer[bayerIndex + bayerStep * 2 + 2]  + 2) >> 2;
	                t1 = (bayer[bayerIndex + 1]  + bayer[bayerIndex + bayerStep]  +
	                      bayer[bayerIndex + bayerStep + 2]  + bayer[bayerIndex + bayerStep * 2 + 1]  +
	                      2) >> 2;
	                rgb[rgbIndex + 1] = (byte) (t0  ); 
	                rgb[rgbIndex + 0] = (byte) (t1  );
	                rgb[rgbIndex + -1] = (byte) (bayer[bayerIndex + bayerStep + 1]  );

	                t0 = (bayer[bayerIndex + 2]  + bayer[bayerIndex + bayerStep * 2 + 2]  + 1) >> 1;
	                t1 = (bayer[bayerIndex + bayerStep + 1]  + bayer[bayerIndex + bayerStep + 3]  +
	                      1) >> 1;
	                rgb[rgbIndex + 4] = (byte) (t0  );
	                rgb[rgbIndex + 3] = (byte) (bayer[bayerIndex + bayerStep + 2]  );
	                rgb[rgbIndex + 2] = (byte) (t1  );
	            }
	        }

	        if (bayerIndex < bayerEnd) {
	            t0 = (bayer[bayerIndex + 0] + bayer[bayerIndex + 2] + bayer[bayerIndex + bayerStep * 2] +
	                  bayer[bayerIndex + bayerStep * 2 + 2] + 2) >> 2;
	            t1 = (bayer[bayerIndex + 1] + bayer[bayerIndex + bayerStep] +
	                  bayer[bayerIndex + bayerStep + 2] + bayer[bayerIndex + bayerStep * 2 + 1] +
	                  2) >> 2;
	            rgb[rgbIndex - blue] = (byte) (t0  );
	            rgb[rgbIndex + 0] = (byte) (t1  );
	            rgb[rgbIndex + blue] = (byte) (bayer[bayerIndex + bayerStep + 1]  );
	            bayerIndex++;
	            rgbIndex += 3;
	        }
	        
	        bayerIndex -= width;
	        rgbIndex -= width * 3;
	      
	        blue = -blue;
	        start_with_green = !start_with_green;
	        
	    }
	    return;
	}
	
	/**
	 * Important to note that passed arrays' values may be changed,, that change will be reflected.
	 * @param rgb
	 * @param sizeX
	 * @param sizeY
	 * @param w	no fuckin idea
	 */
	public static void ClearBorders(byte[] rgb, int sizeX, int sizeY, int w, int rgbIndex)
	{
	    int i, j;
	    // black edges are added with a width w:
	    i = 3 * sizeX * w - 1;
	    j = 3 * sizeX * sizeY - 1;
	    System.out.println(rgb[15136377] );
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
        System.out.print(rgb[10 + 12 * 1 + 8 + 0]);
        System.out.print(rgb[10 + 12 * 1 + 8 + 1]);
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
