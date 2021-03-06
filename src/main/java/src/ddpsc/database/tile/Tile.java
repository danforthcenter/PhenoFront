package src.ddpsc.database.tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tile
{
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Fields
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public final int		parentSnapshotId;
	public final int		id;
	
	public final String		cameraLabel;
	public final int		rawImageOid;
	public final int		rawNullImageOid;
	
	public final int		width;
	public final int		height;
	
	public final int		dataFormat;
	public final int		frame;
	
	public final int		rotateFlipType;
	
	public String			tag;
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Constructors
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public Tile(
			int snapshotId,
			int id,
			String cameraLabel,
			int rawImageOid,
			int rawNullImageOid,
			int width,
			int height,
			int dataFormat,
			int frame,
			int rotateFlipType)
	{
		this.parentSnapshotId = snapshotId;
		this.id = id;
		this.cameraLabel = cameraLabel;
		this.rawImageOid = rawImageOid;
		this.rawNullImageOid = rawNullImageOid;
		this.width = width;
		this.height = height;
		this.dataFormat = dataFormat;
		this.frame = frame;
		this.rotateFlipType = rotateFlipType;
		
		tag = "";
	}
	
	@Override
	public String toString()
	{
		return "Tile ["
				+ "cameraLabel=" + cameraLabel + ", "
				+ "rawImageOid=" + rawImageOid + ", "
				+ "rawNullImageOid=" + rawNullImageOid + ", "
				+ "dataFormat=" + dataFormat + ", "
				+ "width=" + width + ", "
				+ "height=" + height + ", "
				+ "rotateFlipType=" + rotateFlipType + ", "
				+ "frame=" + frame + ", "
				+ "]";
	}
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Static List Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public static Map<Integer, Tile> getTileIdMap(List<Tile> tiles)
	{
		Map<Integer, Tile> map = new HashMap<Integer, Tile>(tiles.size());
		for (Tile tile : tiles)
			map.put(tile.id, tile);
		
		return map;
	}
	
	public static List<Integer> getIds(List<Tile> tiles)
	{
		List<Integer> ids = new ArrayList<Integer>(tiles.size());
		for (Tile tile : tiles)
			ids.add(tile.id);
		return ids;
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// CSV
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public static final String SNAPSHOT_ID		= "parent snapshot id";
	public static final String ID				= "id";
	public static final String NAME				= "name";
	public static final String WIDTH			= "width";
	public static final String HEIGHT			= "height";
	public static final String SPECTRUM			= "spectrum";
	public static final String PERSPECTIVE		= "perspective";
	public static final String TAG				= "tag";
	
	public static final String CSV_HEADER = SNAPSHOT_ID
			+ "," + ID
			+ "," + WIDTH
			+ "," + HEIGHT
			+ "," + SPECTRUM
			+ "," + PERSPECTIVE
			+ "," + NAME
			+ "," + TAG
			+ "\n";
	
	public static String toCSV(List<Tile> tiles, boolean addHeader)
	{
		StringBuilder data = new StringBuilder();
		
		for (Tile tile : tiles)
			data.append(tile.toCSV(false));
		
		if (addHeader)
			return CSV_HEADER + data.toString();
		else
			return data.toString();
	}
	public static String toCSV(List<Tile> tiles)
	{
		return toCSV(tiles, true);
	}
	
	
	public String toCSV(boolean addHeader)
	{
		StringBuilder data = new StringBuilder(parentSnapshotId
				+ "," + id
				+ "," + width
				+ "," + height
				+ "," + getSpectrum()
				+ "," + getPerspective()
				+ "," + getName()
				+ "," + tag
				+ "\n");
		
		if (addHeader)
			return CSV_HEADER + data.toString();
		else
			return data.toString();
	}
	public String toCSV()
	{
		return toCSV(true);
	}
	
	public static String namesList(List<Tile> tiles, String delimiter)
	{
		String csv = "";
		for (Tile tile : tiles)
			csv += tile.cameraLabel + "_" + tile.rawImageOid + delimiter;
		
		return csv;
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Get/Set Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public String getName()
	{
		return cameraLabel + "_" + rawImageOid;
	}
	
	public String getPerspective()
	{
		return Integer.toString(frame);
	}
	
	public String getSpectrum()
	{
		if (dataFormat == 0)
			return "Near Infrared";
		else if (dataFormat == 1)
			return "Visible";
		else if (dataFormat == 6)
			return "Fluorescent";
		else
			return "Unknown";
	}

	public String getTag()
	{
		return tag;
	}

	public void setTag(String tag)
	{
		this.tag = tag;
	}

	public int getParentSnapshotId()
	{
		return parentSnapshotId;
	}

	public int getId()
	{
		return id;
	}

	public String getCameraLabel()
	{
		return cameraLabel;
	}

	public int getRawImageOid()
	{
		return rawImageOid;
	}

	public int getRawNullImageOid()
	{
		return rawNullImageOid;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public int getDataFormat()
	{
		return dataFormat;
	}

	public int getFrame()
	{
		return frame;
	}

	public int getRotateFlipType()
	{
		return rotateFlipType;
	}
}
