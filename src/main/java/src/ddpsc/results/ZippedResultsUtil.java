package src.ddpsc.results;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;

import org.joda.time.DateTime;

import src.ddpsc.database.experiment.Experiment;
import src.ddpsc.database.snapshot.Snapshot;
import src.ddpsc.database.tile.Tile;
import src.ddpsc.database.tile.TileFileLTSystemUtil;

public final class ZippedResultsUtil {
	/**
	 * Utility that should be used to create an in-memory filestream
	 * representing the snapshots and their associated images. Does not close
	 * the output stream.
	 * 
	 * Snapshots in the archive are named: snapshotid_snapshot.csv -- Includes
	 * header line
	 * 
	 * Images in the archive are named: snapshotid_rawimageoid_cameralabel.png
	 * 
	 * @param out
	 *            Stream for the zipped file to be written to
	 * @param snapshots
	 *            List of snapshots to be written
	 * @throws IOException
	 * @see ImageService Snapshot Tile
	 */
	public static void ZipSnapshots(OutputStream out,
			ArrayList<Snapshot> snapshots, Experiment experiment) throws IOException {
		ZipOutputStream archive = new ZipOutputStream(out);
		for (Snapshot snapshot : snapshots) {
			String entryName = "snapshot" + snapshot.getId() + "/snapshot"
					+ snapshot.getId() + ".csv";
			ZipEntry entry = new ZipEntry(entryName);
			archive.putNextEntry(entry);
			archive.write(snapshot.csvWriter().getBytes());
			archive.flush(); // keep responsive
			for (Tile tile : snapshot.getTiles()) {
				// right now we have a png listed, but... this could change
				if( tile.getDataFormat() != 1){
					System.err.println("WARNING: Only printing out vis image.");
					continue;
				}
				String imgName = "snapshot" + snapshot.getId() + "/"
						+ tile.getCameraLabel() + "_" + snapshot.getId() + "_"
						+ tile.getRawImageOid() + ".png";
				ZipEntry imgEntry = new ZipEntry(imgName);
				archive.putNextEntry(imgEntry);
			
				try{
					ImageServiceMockImpl.tileWriter(tile, archive, new DateTime(snapshot.getTimeStamp()), experiment);
				} catch(Exception e){
					System.out.println(tile);
					e.printStackTrace();
				}
				archive.flush(); // keep responsive
			}
		}
		archive.finish();
		// don't call close, whoever calls us should close
	}
	
	public static void getTileArchive(Tile tile){
		String root = "/Users/shill/Documents/JSEE_workspace/PhenoFront/bin/test/"; //make better at some point
		String path = root + "blob" + tile.getRawImageOid();
	}

}
