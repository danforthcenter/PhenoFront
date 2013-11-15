package src.ddpsc.results;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

public class Tester {
	
	public static void main(String [] args){
		try {
			dumb(args);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * We need to figure out how to get more than just vis images :/
	 * @param args
	 * @throws IOException
	 * @throws ZipException
	 */
	public static void dumb(String[] args) throws IOException, ZipException {

		String filename = "/data/pgftp/LemnaTest/2013-09-25/blob110851";
		ZipFile zipFile = new ZipFile(filename);
		int width = 2454;
		int height = 2048;

		FileHeader entry = zipFile.getFileHeader("data");
		InputStream entryStream = zipFile.getInputStream(entry);
		OutputStream out = new FileOutputStream(
				"/Users/shill/Documents/JSEE_workspace/PhenoFront/bin/test/analysis1.png");
		Bayer2Rgb.convertRawImage( entryStream , width, height, out);
		out.flush();
		out.close();
	}

	public static void changeArr(int[] arr) {
		arr[4] = 2;
	}

}
