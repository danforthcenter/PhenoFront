package src.ddpsc.database.tile;

import java.util.ArrayList;
import java.util.List;

public class Tile
{
	// CSV labels
	public static final String ID				= "id";
	public static final String CAMERA			= "camera label";
	public static final String RAW_IMAGE_OID	= "raw image id";
	public static final String NULL_IMAGE_OID	= "null image id";
	public static final String DATA_FORMAT		= "data format";
	public static final String WIDTH			= "width";
	public static final String HEIGHT			= "height";
	public static final String FLIP_TYPE		= "flip type";
	public static final String FRAME			= "frame";
	
	private int		id;
	private String	cameraLabel;
	private int		rawImageOid; // maybe just go fetch the path
	private int		rawNullImageOid;
	private int		dataFormat;
	private int		width;
	private int		height;
	private int		rotateFlipType;
	private int		frame;
	private String	imagePath;
	private String	tag;
	
	public Tile()
	{
	}
	
	public Tile(
			String cameraLabel,
			int rawImageOid,
			int rawNullImageOid,
			int dataFormat,
			int width,
			int height,
			int rotateFlipType,
			int frame,
			String imagePath)
	{
		this.cameraLabel = cameraLabel;
		this.rawImageOid = rawImageOid;
		this.rawNullImageOid = rawNullImageOid;
		this.dataFormat = dataFormat;
		this.width = width;
		this.height = height;
		this.rotateFlipType = rotateFlipType;
		this.frame = frame;
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
				+ "imagePath=" + imagePath
				+ "]";
	}
	
	public static String toCSV(List<Tile> tiles, String delimiter)
	{
		String csv = "";
		for (Tile tile : tiles)
			csv += tile.cameraLabel + "_" + tile.rawImageOid + delimiter;
		
		return csv;
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
	// Get/Set Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public String getNullImagePath()
	{
		return this.imagePath + "/" + new Integer(this.rawNullImageOid).toString();
	}
	
	public String getRawImagePath()
	{
		return this.imagePath + "/" + new Integer(this.rawImageOid).toString();
	}
	
	public String getImagePath()
	{
		return this.imagePath;
	}
	
	public void setImagePath(String imagePath)
	{
		this.imagePath = imagePath;
	}
	
	public String getCameraLabel()
	{
		return cameraLabel;
	}
	
	public void setCameraLabel(String cameraLabel)
	{
		this.cameraLabel = cameraLabel;
	}
	
	public int getRawImageOid()
	{
		return rawImageOid;
	}
	
	public void setRawImageOid(int rawImageOid)
	{
		this.rawImageOid = rawImageOid;
	}
	
	public int getRawNullImageOid()
	{
		return rawNullImageOid;
	}
	
	public void setRawNullImageOid(int rawNullImageOid)
	{
		this.rawNullImageOid = rawNullImageOid;
	}
	
	public int getDataFormat()
	{
		return dataFormat;
	}
	
	public void setDataFormat(int dataFormat)
	{
		this.dataFormat = dataFormat;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public int getRotateFlipType()
	{
		return rotateFlipType;
	}
	
	public void setRotateFlipType(int rotateFlipType)
	{
		this.rotateFlipType = rotateFlipType;
	}
	
	public int getFrame()
	{
		return frame;
	}
	
	public void setFrame(int frame)
	{
		this.frame = frame;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
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
