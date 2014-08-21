package src.ddpsc.database.snapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import src.ddpsc.database.tile.Tile;

/**
 * Model class for mapping the database table snapshot to a Java Object.
 * 
 * @author shill, cjmcentee
 */
public class Snapshot
{
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Fields
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public final int		id;
	public final String		experiment;
	
	public final String		plantBarcode;
	public final String		measurementLabel;
	public final String		carTag;
	public final Timestamp	timestamp;
	
	public final float		weightBefore;
	public final float		weightAfter;
	public final float		waterAmount;
	
	public final boolean	completed;
	
	private List<Tile>		tiles;
	private String			tag;
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Constructors
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public Snapshot(
			int			id,
			String		experiment,
			
			String		plantBarcode,
			String		measurementLabel,
			String		carTag,
			Timestamp	timeStamp,
			
			float		weightBefore,
			float		weightAfter,
			float		waterAmount,
			
			boolean		completed)
	{
		this.id = id;
		this.experiment = experiment;
		
		this.plantBarcode = plantBarcode;
		this.measurementLabel = measurementLabel;
		this.carTag = carTag;
		this.timestamp = timeStamp;
		
		this.weightBefore = weightBefore;
		this.weightAfter = weightAfter;
		this.waterAmount = waterAmount;
		
		this.completed = completed;
		
		this.tiles = new ArrayList<Tile>();
		this.tag = "";
	}
	
	
	@Override
	public String toString()
	{
		return "Snapshot ["
				+ "experiment=" + experiment + ", "
				+ "id=" + id + ", "
				+ "plantBarcode=" + plantBarcode + ", "
				+ "carTag=" + carTag + ", "
				+ "timeStamp=" + timestamp + ", "
				+ "weightBefore=" + weightBefore + ", "
				+ "weightAfter=" + weightAfter + ", "
				+ "waterAmount=" + waterAmount + ", " 
				+ "completed=" + completed + ", "
				+ "measurementLabel=" + measurementLabel + ", "
				+ "tiles=" + tiles + ", "
				+ "tag=" + tag
				+ "]";
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Static List Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	/**
	 * Returns the supplied collection of snapshots, but only those which have tiles loaded
	 * 
	 * @param		snapshots	List of snapshots to check for tiles
	 * @return					All the snapshots with tiles
	 */
	public static ArrayList<Snapshot> tiledOnly(Collection<Snapshot> snapshots)
	{
		// Remove snapshots with zero tiles
		ArrayList<Snapshot> snapshotsWithTiles = new ArrayList<Snapshot>(snapshots.size());

		for (Snapshot snapshot : snapshots)
			if (snapshot.getTiles().size() > 0)
				snapshotsWithTiles.add(snapshot);

		return snapshotsWithTiles;
	}
	
	public static List<Integer> getIds(List<Snapshot> snapshots)
	{
		List<Integer> ids = new ArrayList<Integer>(snapshots.size());
		for (Snapshot snapshot : snapshots)
			ids.add(snapshot.id);
		
		return ids;
	}
	
	public static List<Tile> getTiles(List<Snapshot> snapshots)
	{
		List<Tile> tiles = new ArrayList<Tile>();
		for (Snapshot snapshot : snapshots)
			tiles.addAll(snapshot.getTiles());
		
		return tiles;
	}
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// CSV
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public static final String ID				= "id";
	public static final String EXPERIMENT		= "experiment";
	
	public static final String BARCODE			= "plant barcode";
	public static final String MEASUREMENT		= "measurement label";
	public static final String TIMESTAMP		= "timestamp";
	
	public static final String CAR_TAG			= "car tag";
	public static final String COMPLETED		= "completed";
	
	public static final String WEIGHT_BEFORE	= "weight before";
	public static final String WEIGHT_AFTER		= "weight after";
	public static final String WATER_AMOUNT		= "water amount";
	
	public static final String TILES			= "tiles";
	public static final String TAG				= "tag";
	
	public static final String CSV_HEADER =
					EXPERIMENT
			+ "," + ID
			+ "," + BARCODE
			+ "," + CAR_TAG
			+ "," + TIMESTAMP
			+ "," + WEIGHT_BEFORE
			+ "," + WEIGHT_AFTER
			+ "," + WATER_AMOUNT
			+ "," + COMPLETED
			+ "," + MEASUREMENT
			+ "," + TAG
			+ "," + TILES
			+ "\n";
	/**
	 * Converts a series of snapshots into CSV form, includes headers
	 * 
	 * @return			csv form of a list of snapshots
	 */
	public static String toCSV(List<Snapshot> snapshots)
	{
		return toCSV(snapshots, true);
	}
	public static String toCSV(List<Snapshot> snapshots, boolean addHeader)
	{
		StringBuilder data = new StringBuilder();
		
		for (Snapshot snapshot : snapshots)
			data.append(snapshot.toCSV(false));
		
		if (addHeader)
			return CSV_HEADER + data.toString();
		else
			return data.toString();
	}
	
	/**
	 * Converts this snapshot to a comma separated string with labels for each value on the first line.
	 * 
	 * @return			csv form of the Snapshot with labels
	 */
	public String toCSV()
	{
		return toCSV(true);
	}
	public String toCSV(boolean addHeader)
	{
		StringBuilder data = new StringBuilder(
						experiment
				+ "," + id
				+ "," + plantBarcode
				+ "," + carTag
				+ "," + timestamp
				+ "," + weightBefore
				+ "," + weightAfter
				+ "," + waterAmount
				+ "," + completed
				+ "," + measurementLabel
				+ "," + tag);
		
		data.append(tilesNames());
		
		if (addHeader)
			return CSV_HEADER + data.toString();
		else
			return data.toString();
	}
	
	private String tilesNames()
	{
		StringBuilder tilesEntry = new StringBuilder("");
		if (tiles != null && tiles.size() > 0)
			tilesEntry.append("," + Tile.namesList(tiles, ";"));
		else
			tilesEntry.append(",");
		
		return tilesEntry.toString();
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Get/Set Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public void setTiles(List<Tile> tiles)
	{
		this.tiles = tiles;
	}
	
	public List<Tile> getTiles()
	{
		return this.tiles;
	}
	
	public Tile getTile(int index)
	{
		return this.tiles.get(index);
	}
	
	public void addTile(Tile tile)
	{
		this.tiles.add(tile);
	}
	
	public void setTag(String tag)
	{
		this.tag = tag;
	}
	
	public String getTag()
	{
		return tag;
	}
}
