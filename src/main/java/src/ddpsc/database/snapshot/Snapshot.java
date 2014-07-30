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
	public static String CSV_HEADER
		= "id,plant barcode,car tag,timestamp,weight before,weight after,water amount,completed,measurement label,tiles\n";
	public static String CSV_HEADER_NO_WEIGHTS
		= "id,plant barcode,car tag,timestamp,completed,measurement label\n";
	
	private	String		plantBarcode;
	private String		carTag;
	private Timestamp	timeStamp;
	private float		weightBefore;
	private float		weightAfter;
	private float		waterAmount;
	private boolean		completed;
	private String		measurementLabel;
	private int			id;
	private List<Tile>	tiles;
	
	public Snapshot()
	{
	}
	
	public Snapshot(
			String		plantBarcode,
			String		carTag,
			Timestamp	timeStamp,
			float		weightBefore,
			float		weightAfter,
			float		waterAmount,
			boolean		completed,
			int			id,
			String		measurementLabel)
	{
		this.plantBarcode = plantBarcode;
		this.carTag = carTag;
		this.timeStamp = timeStamp;
		this.weightBefore = weightBefore;
		this.weightAfter = weightAfter;
		this.waterAmount = waterAmount;
		this.completed = completed;
		this.measurementLabel = measurementLabel;
		this.id = id;
	}
	
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
	
	@Override
	public String toString()
	{
		return "Snapshot ["
				+ "plantBarcode=" + plantBarcode + ", "
				+ "carTag=" + carTag + ", "
				+ "timeStamp=" + timeStamp + ", "
				+ "weightBefore=" + weightBefore + ", "
				+ "weightAfter=" + weightAfter + ", "
				+ "waterAmount=" + waterAmount + ", " 
				+ "completed=" + completed + ", "
				+ "measurementLabel=" + measurementLabel + ", "
				+ "id=" + id + ", "
				+ "tiles=" + tiles
				+ "]";
	}
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// CSV Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	/**
	 * Converts a series of snapshots into CSV form, includes headers
	 * 
	 * @return			csv form of a list of snapshots
	 */
	public static String toCSV(List<Snapshot> snapshots) { return toCSV(snapshots, true); }
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
	public String toCSV() { return toCSV(true); }
	public String toCSV(boolean addHeader)
	{
		StringBuilder data = new StringBuilder(id
				+ "," + plantBarcode
				+ "," + carTag
				+ "," + timeStamp
				+ "," + weightBefore
				+ "," + weightAfter
				+ "," + waterAmount
				+ "," + completed
				+ "," + measurementLabel);

		if (tiles != null && tiles.size() > 0)
			data.append("," + Tile.toCSVString(tiles, ";") + "\n");
		else
			data.append("\n");
		
		if (addHeader)
			return CSV_HEADER + data.toString();
		else
			return data.toString();
	}

	/**
	 * Same as csvWriter except it excludes the weight measurements
	 * 
	 * @return			csv of the snapshot without the label line and missing the weight tags
	 */
	public String toCSV_noWeights() { return toCSV_noWeights(true); }
	public String toCSV_noWeights(boolean addHeader)
	{
		StringBuilder data = new StringBuilder(id
				+ "," + plantBarcode
				+ "," + carTag
				+ "," + timeStamp
				+ "," + completed
				+ "," + measurementLabel);
		 
		if (tiles != null && tiles.size() > 0)
			data.append("," + Tile.toCSVString(tiles, ";") + "\n");
		else
			data.append("\n");
		
		if (addHeader)
			return CSV_HEADER_NO_WEIGHTS + data.toString();
		else
			return data.toString();
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Get/Set Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public void setMeasurementLabel(String measurementLabel)
	{
		this.measurementLabel = measurementLabel;
	}
	
	public String getMeasurementLabel()
	{
		return measurementLabel;
	}
	
	public String getPlantBarcode()
	{
		return plantBarcode;
	}
	
	public void setPlantBarcode(String plantBarcode)
	{
		this.plantBarcode = plantBarcode;
	}
	
	public String getCarTag()
	{
		return carTag;
	}
	
	public void setCarTag(String carTag)
	{
		this.carTag = carTag;
	}
	
	public Timestamp getTimeStamp()
	{
		return timeStamp;
	}
	
	public void setTimeStamp(Timestamp timeStamp)
	{
		this.timeStamp = timeStamp;
	}
	
	public float getWeightBefore()
	{
		return weightBefore;
	}
	
	public void setWeightBefore(float weightBefore)
	{
		this.weightBefore = weightBefore;
	}
	
	public float getWeightAfter()
	{
		return weightAfter;
	}
	
	public void setWeightAfter(float weightAfter)
	{
		this.weightAfter = weightAfter;
	}
	
	public float getWaterAmount()
	{
		return waterAmount;
	}
	
	public void setWaterAmount(float waterAmount)
	{
		this.waterAmount = waterAmount;
	}
	
	public boolean getCompleted()
	{
		return completed;
	}
	
	public void setCompleted(boolean completed)
	{
		this.completed = completed;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
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
	
	public void setTile(Tile tile, int index)
	{
		this.tiles.set(index, tile);
	}
}
