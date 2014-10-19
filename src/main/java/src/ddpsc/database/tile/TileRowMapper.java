package src.ddpsc.database.tile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import src.ddpsc.database.snapshot.Snapshot;
import src.ddpsc.database.snapshot.SnapshotDaoImpl;

/**
 * This class creates a mapping from the database to our Snapshot object's fields. The inner class SnapshotExtractor
 * implements a spring utility function for this purpose, rather than putting it in it's own file, our application will
 * implement extractors as inner methods.
 * 
 * @see Snapshot
 * @see SnapshotDaoImpl.TILE_QUERY_VARIABLES
 * @see SnapshotDaoImpl
 * 
 * @author shill, cjmcentee
 */
public class TileRowMapper implements RowMapper<Tile>
{
	List<Snapshot> snapshots;
	public TileRowMapper(List<Snapshot> snapshots)
	{
		this.snapshots = snapshots;
	}
	
	private int indexById(int id, List<Snapshot> snapshots)
	{
		for (int index = 0; index < snapshots.size(); index++) {
			Snapshot snapshot = snapshots.get(index);
			if (snapshot.id == id)
				return index;
		}
		
		return -1;
	}
	
	@Override
	public Tile mapRow(ResultSet resultSet, int line) throws SQLException
	{
		int snapshotId = resultSet.getInt("snapshot_id");
		
		Tile tile = new Tile(
		
				snapshotId,
			resultSet.getInt(SnapshotDaoImpl.TILE_ID),
			
			resultSet.getString(SnapshotDaoImpl.CAMERA),
			resultSet.getInt(SnapshotDaoImpl.RAW_IMAGE_OID),
			resultSet.getInt(SnapshotDaoImpl.NULL_IMAGE_OID),
			resultSet.getInt(SnapshotDaoImpl.WIDTH),
			resultSet.getInt(SnapshotDaoImpl.HEIGHT),
			resultSet.getInt(SnapshotDaoImpl.DATA_FORMAT),
			resultSet.getInt(SnapshotDaoImpl.FRAME),
			resultSet.getInt(SnapshotDaoImpl.FLIP_TYPE) );
		
		int index = indexById(snapshotId, snapshots);
		if (index != -1)
			snapshots.get(index).addTile(tile);
		
		return tile;
	}
}