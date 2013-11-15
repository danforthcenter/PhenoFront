package src.ddpsc.database.tile;

import org.joda.time.DateTime;

import src.ddpsc.database.experiment.Experiment;
/**
 * The purpose of this class is to create a system utility for accessing Images on the lemnatec filesystem, obviously, this
 * could be a runtime Interface/Service as well. However it seems more intuitive to simply design for the
 * LemnaTec DBServer directory structure. That is:
 * 
 * ex:
 * /data/pgftp/LemnaTest/2013-10-30/blob129330
 * 
 * breakdown:
 * /data/fgftp/{DB Name}/{YYYY-MM-DD}/blob{ImageOID}
 * 
 * It is important to not that not all of the blobs will be Zip files containing images. There are other objects stored
 * in these blobs (hence, blob). If that is the case, we probably want to throw a custom exception.
 * 
 * This class can contain additional methods as seen fit.
 * @author shill
 *
 */
public final class TileFileLTSystemUtil {
	public final static String fileroot = "/data/pgftp";
	/**
	 * Returns absolute path to the blob of the image.
	 * 
	 * @param tile	Tile encasing the image. Returns the rawImage path, not the null Image path.
	 * @param date	DateTime format of the date the image was created.
	 * @param experiment	Experiment in which we can find this tile.
	 * @return
	 */
	public static final String getTileFilename(Tile tile, DateTime date, Experiment experiment){
		String ret = "";
		ret = fileroot + "/"+ experiment.getExperimentName() + "/" + date.toString("YYYY-MM-dd") + "/blob" + tile.getRawImageOid();
		return ret;
	}
	/**
	 * Returns the absolute path to the blob of the image.
	 * 
	 * @param imageOid	rawImageOid or nullImageOid representing the image that we are fetching.
	 * @param date	YYYY-mm-dd representing the day the image was created (same as snapshot)
	 * @param experiment	Experiment which the image exists in.
	 * @return
	 */
	public static final String getTileFilename(String imageOid, String date, Experiment experiment){
		String ret = "";
		ret = fileroot + "/"+ experiment.getExperimentName() + "/" + date + "/" + imageOid;
		return ret;
	}
}
