package src.ddpsc.results;

import java.io.IOException;
import java.io.OutputStream;

import net.lingala.zip4j.exception.ZipException;



/**
 * Class is responsible for reading a raw image and converting it to a Png Image.
 * 
 * The class maintains an extensive streaming library so that all of our files may be in memory and are never written to disk.
 * @author shill
 *
 */
public interface ImageService {
	public void nir2Png(String filename, OutputStream out) throws IOException, ZipException;
	public  void flou2Png(String filename, OutputStream out) throws IOException, ZipException;
	public void vis2Png(String filename, OutputStream out) throws IOException, ZipException;
}
