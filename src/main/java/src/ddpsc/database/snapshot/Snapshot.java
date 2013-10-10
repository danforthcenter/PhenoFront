package src.ddpsc.database.snapshot;

import java.sql.Timestamp;
import java.util.List;

import src.ddpsc.database.tile.Tile;


/**
 * Model class for mapping the database table snapshot to a Java Object. In the MVC design pattern this 
 * represents the model.
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
    private int id;
    private List<Tile> tiles;
	public Snapshot(){ }
	public Snapshot(String plantBarcode, String carTag, Timestamp timeStamp,
			float weightBefore, float weightAfter, float waterAmount,
			boolean completed, int id) {
		super();
		this.plantBarcode = plantBarcode;
		this.carTag = carTag;
		this.timeStamp = timeStamp;
		this.weightBefore = weightBefore;
		this.weightAfter = weightAfter;
		this.waterAmount = waterAmount;
		this.completed = completed;
		this.id = id;
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
	
	public void setTiles(List<Tile> tiles){
		this.tiles = tiles;
	}
	
	public List<Tile> getTiles(){
		return this.tiles;
	}
	
	public Tile getTile(int idx){
		return this.tiles.get(idx);
	}
	
	public void setTile(Tile tile, int idx){
		this.tiles.set(idx, tile);
	}
	@Override
	public String toString() {
		return "Snapshot [plantBarcode=" + plantBarcode + ", carTag=" + carTag
				+ ", timeStamp=" + timeStamp + ", weightBefore=" + weightBefore
				+ ", weightAfter=" + weightAfter + ", waterAmount="
				+ waterAmount + ", completed=" + completed + ", id=" + id
				+ ", tiles=" + tiles + "]";
	}
	
	
	
}
