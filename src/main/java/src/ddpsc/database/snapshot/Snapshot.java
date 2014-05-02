package src.ddpsc.database.snapshot;

import java.sql.Timestamp;
import java.util.List;

import src.ddpsc.database.tile.Tile;

/**
 * Model class for mapping the database table snapshot to a Java Object. In the
 * MVC design pattern this represents the model.
 * 
 * @author shill
 * 
 */

public class Snapshot {
	private String plantBarcode;
	private String carTag;
	private Timestamp timeStamp;
	private float weightBefore;
	private float weightAfter;
	private float waterAmount;
	private boolean completed;
	private String measurementLabel;
	private int id;
	private List<Tile> tiles;

	public Snapshot() {
	}

	public Snapshot(String plantBarcode, String carTag, Timestamp timeStamp,
			float weightBefore, float weightAfter, float waterAmount,
			boolean completed, int id, String measurementLabel) {
		super();
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

	public String getMeasurementLabel() {
		return measurementLabel;
	}

	public String getPlantBarcode() {
		return plantBarcode;
	}

	public void setPlantBarcode(String plantBarcode) {
		this.plantBarcode = plantBarcode;
	}

	public String getCarTag() {
		return carTag;
	}

	public void setCarTag(String carTag) {
		this.carTag = carTag;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	public float getWeightBefore() {
		return weightBefore;
	}

	public void setWeightBefore(float weightBefore) {
		this.weightBefore = weightBefore;
	}

	public float getWeightAfter() {
		return weightAfter;
	}

	public void setWeightAfter(float weightAfter) {
		this.weightAfter = weightAfter;
	}

	public float getWaterAmount() {
		return waterAmount;
	}

	public void setWaterAmount(float waterAmount) {
		this.waterAmount = waterAmount;
	}

	public boolean getCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTiles(List<Tile> tiles) {
		this.tiles = tiles;
	}

	public List<Tile> getTiles() {
		return this.tiles;
	}

	public Tile getTile(int idx) {
		return this.tiles.get(idx);
	}

	public void setTile(Tile tile, int idx) {
		this.tiles.set(idx, tile);
	}

	@Override
	public String toString() {
		return "Snapshot [plantBarcode=" + plantBarcode + ", carTag=" + carTag
				+ ", timeStamp=" + timeStamp + ", weightBefore=" + weightBefore
				+ ", weightAfter=" + weightAfter + ", waterAmount="
				+ waterAmount + ", completed=" + completed
				+ ", measurementLabel=" + measurementLabel + ", id=" + id
				+ ", tiles=" + tiles + "]";
	}

	public void setMeasurementLabel(String measurementLabel) {
		this.measurementLabel = measurementLabel;
	}

	/**
	 * Another version of toString, except instead of to a pure string it
	 * returns a CSV formatted string including labels on the first line. Does
	 * not handle null tiles. Adds newlines characters.
	 * 
	 * @return
	 */
	public String csvWriter() {
		String rep = "id,plant barcode,car tag,timestamp,weight before, weight after,water amount,completed,measurement label,tiles\n";
		System.out.println(tiles);
		String data;
		try {
			data = id + "," + plantBarcode + "," + carTag + "," + timeStamp
					+ "," + weightBefore + "," + weightAfter + ","
					+ waterAmount + "," + completed + "," + measurementLabel
					+ "," + Tile.toDelimited(tiles, ";") + "\n";
		} catch (NullPointerException e) {
			data = id + "," + plantBarcode + "," + carTag + "," + timeStamp
					+ "," + weightBefore + "," + weightAfter + ","
					+ waterAmount + "," + completed + "," + measurementLabel
					+ "\n";
		}
		return (rep + data);
	}

	/**
	 * Same as csvWriter except it excludes the header line.
	 * @return
	 */
	public String csvWriterWithWeights() {
		System.out.println(tiles);
		String data;
		try {
			data = id + "," + plantBarcode + "," + carTag + "," + timeStamp
					+ "," + weightBefore + "," + weightAfter + ","
					+ waterAmount + "," + completed + "," + measurementLabel
					+ "," + Tile.toDelimited(tiles, ";") + "\n";
		} catch (NullPointerException e) {
			data = id + "," + plantBarcode + "," + carTag + "," + timeStamp
					+ "," + weightBefore + "," + weightAfter + ","
					+ waterAmount + "," + completed + "," + measurementLabel
					+ "\n";
		}
		return (data);
	}
	
	/**
	 * Same as csvWriter except it excludes the header line.
	 * @return
	 */
	public String csvWriterNoHead() {
		System.out.println(tiles);
		String data;
		try {
			data = id + "," + plantBarcode + "," + carTag + "," + timeStamp
					+ completed + "," + measurementLabel
					+ "," + Tile.toDelimited(tiles, ";") + "\n";
		} catch (NullPointerException e) {
			data = id + "," + plantBarcode + "," + carTag + "," + timeStamp
					 + "," + completed + "," + measurementLabel
					+ "\n";
		}
		return (data);
	}

}
