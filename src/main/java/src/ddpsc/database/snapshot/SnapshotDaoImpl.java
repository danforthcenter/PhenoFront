package src.ddpsc.database.snapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import src.ddpsc.database.tile.Tile;
import src.ddpsc.database.tile.TileRowMapper;
/**
 * This class is responsible for building snapshot queries. Each user request should be defined in here.
 * No insertions should be made as the postgresql database will be read only. 
 * 
 * Each query typically has three types:
 * 
 * standard (findBySnapshot) - Only returns snapshots, no tile information is returned and no additional filtering is done.
 * withTile - Returns each snapshot with the associated tiles. If there are no tiles it is not included.
 * 		 This is a slow method.
 * imageJobs - Returns each snapshot if and only if it is an image job.
 * 
 * There should be an external tool that adds entries to servlet-context.xml and wires up the dataSources.
 * This class should figure out dynamically which dataSource to connect to.
 * 
 * @author shill
 *
 */

//Note: when the interface is removed it breaks. don't remove the interface, just suck it up and use it.
public class SnapshotDaoImpl implements SnapshotDao {
	@Autowired  
	DataSource dataSource;
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource; //O:
	}
	
	/**
	 * Utility function for creating a snapshot query.
	 * @param sql
	 * @return
	 */
	public List<Snapshot> snapshotQueryWrapper(String sql){
		List<Snapshot> snapshotList = new ArrayList<Snapshot>();  
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);  
		snapshotList = jdbcTemplate.query(sql, new SnapshotRowMapper());  
		return snapshotList;  
	}
	
	/**
	 * Gets a snapshot without it's associated images.
	 */
	@Override
	public Snapshot findBySnapshotId(int id) {
		String sql = "select * from snapshot where id= " + id;  
		List<Snapshot> snapshotList = snapshotQueryWrapper(sql);
		return snapshotList.get(0);  
	}
	
	/**
	 * Finds the snapshot by the snapshot id, also loads all Tiles (images) associated
	 * to that ID as well.
	 * @param id
	 * @return
	 */
	@Override
	public Snapshot findWithTileBySnapshotId(int id){
		Snapshot result = this.findBySnapshotId(id);
		result.setTiles(this.findTiles(id));
		return result;
	}
	
	/**
	 * Gets a set of snapshots that occured after timestamp. It's probably important to note that
	 * if you are using a java Calendar implementation that months start at 0, and days start at 1.
	 * 
	 * @see GregorianCalendar, Timestamp
	 *
	 * @param Timestamp timestamp
	 * 
	 */
	@Override
	public List<Snapshot> findSnapshotAfterTimestamp(Timestamp timestamp){
		String sql = "Select * from snapshot WHERE time_stamp > '" + timestamp +"'";
		List<Snapshot> snapshotList = snapshotQueryWrapper(sql);
		return snapshotList;
	}
	
	
	/**
	 * Gets a set of snapshots and their images that occur after a timestamp.
	 * Only return snapshots which have tiles
	 * 
	 */
	@Override
	public List<Snapshot> findWithTileAfterTimestamp(Timestamp timestamp){
		List<Snapshot> snapshotList = this.findSnapshotAfterTimestamp(timestamp);
		Iterator<Snapshot> it = snapshotList.iterator();
		while(it.hasNext()){
			Snapshot current = it.next();
			List<Tile> curTiles = this.findTiles(current.getId());
			if (curTiles.size() == 0){
				it.remove();
			} else{
				current.setTiles(curTiles);
			}
		}
		return snapshotList;
	}
	
	
	/**
	 * Finds snapshots without associated tiles between before and after.
	 * 
	 * @param Timestamp before
	 * @param Timestamp after
	 * 
	 * @return List<Snapshot
	 */
	@Override
	public List<Snapshot> findSnapshotBetweenTimes(Timestamp before, Timestamp after){
		String sql = "Select * from snapshot WHERE time_stamp > '" + after +"' and time_stamp <'" + before +"'";
		List<Snapshot> snapshotList = snapshotQueryWrapper(sql);
		return snapshotList;
	}
	
	/**
	 * Includes tile data to the snapshots. Note, this is SLOW (tile lo
	 * 
	 * @param before
	 * @param after
	 * @return List<Snapshot>
	 */
	@Override
	public List<Snapshot> findWithTileBetweenTimes(Timestamp before, Timestamp after){
		List<Snapshot> snapshotList = this.findSnapshotBetweenTimes(before, after);
		Iterator<Snapshot> it = snapshotList.iterator();
		while(it.hasNext()){
			Snapshot current = it.next();
			List<Tile> curTiles = this.findTiles(current.getId());
			if (curTiles.size() == 0){
				it.remove();
			} else{
				current.setTiles(curTiles);
			}
		}
		return snapshotList;
	}
	
	/**
	 * Method used for getting tile information, mirror class as in the TileDaoImpl
	 * @param snapshotId
	 * @return
	 */
	public List<Tile> findTiles(int snapshotId){
		String sql= "SELECT tiled_image.camera_label, "
				+ "tile.raw_image_oid, tile.raw_null_image_oid, "
				+ "tile.dataformat, tile.width, tile.height, "
				+ "tile.rotate_flip_type, tile.frame "
				+ "FROM tiled_image, tile "
				+ "WHERE tiled_image.snapshot_id = " + snapshotId + " "
				+ "AND tile.tiled_image_id = tiled_image.id";
		List<Tile> tileList = new ArrayList<Tile>();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		tileList = jdbcTemplate.query(sql, new TileRowMapper());
		return tileList;
	}
	/**
	 * Finds all snapshots including tiles for the last N-entries in the database.
	 */
	@Override
	public List<Snapshot> findWithTileLastNEntries(int n) {
		List<Snapshot> snapshotList = this.findSnapshotLastNEntries(n);
		Iterator<Snapshot> it = snapshotList.iterator();
		while(it.hasNext()){
			Snapshot current = it.next();
			List<Tile> curTiles = this.findTiles(current.getId());
			if (curTiles.size() == 0){
				it.remove();
			} else{
				current.setTiles(curTiles);
			}
		}
		return snapshotList;
	}

	@Override
	public List<Snapshot> findSnapshotLastNEntries(int n) {
		String sql = "Select * from snapshot ORDER BY time_stamp DESC LIMIT " + n;
		List<Snapshot> snapshotList = snapshotQueryWrapper(sql);
		return snapshotList;
	}
	
	@Override
	public List<Snapshot> findSnapshotAfterTimestampImageJobs(
			Timestamp timestamp) {
		String sql = "Select * from snapshot WHERE time_stamp > '" + timestamp +"' AND water_amount = -1";
		List<Snapshot> snapshotList = snapshotQueryWrapper(sql);
		return snapshotList;
	}

	@Override
	public List<Snapshot> findSnapshotBetweenTimesImageJobs(Timestamp before,
			Timestamp after) {
		String sql = "Select * from snapshot WHERE time_stamp > '" + after +"' and time_stamp <'" + before +"' AND water_amount = -1";
		List<Snapshot> snapshotList = snapshotQueryWrapper(sql);
		return snapshotList;
	}

	@Override
	public List<Snapshot> findWithTileLastNEntriesImageJobs(int n) {
		String sql = "Select * from snapshot WHERE water_amount = -1 ORDER BY time_stamp DESC LIMIT " + n;
		List<Snapshot> snapshotList = snapshotQueryWrapper(sql);
		return snapshotList;
	}

	
}
