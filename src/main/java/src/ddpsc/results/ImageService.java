package src.ddpsc.results;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.lingala.zip4j.exception.ZipException;


/**
 * Class is responsible for reading a raw image and converting it to a Java Image.
 * 
 * The class maintains an extensive streaming library so that all of our files may be in memory and are never written to disk.
 * @author shill
 *
 */
public interface ImageService {
			
	//we can change these from streams if we want.
	public InputStream zipToRaw(InputStream is) throws ZipException, IOException; //takes an inputstream of some zip file and positions the stream to the raw entry
	public OutputStream rawToImage(InputStream is); //takes an input stream
	
}
