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
			if (snapshot.getId() == id)
				return index;
		}
		
		return -1;
	}
	
	@Override
	public Tile mapRow(ResultSet resultSet, int line) throws SQLException
	{
		// Order and name of queried tile columns
		//		camera_label 
		//		raw_image_oid
		//		raw_null_image_oid
		//		dataformat
		//		width
		//		height 
		//		rotate_flip_type
		//		frame
		//		tile_image_id
		
		Tile tile = new Tile();
		
		tile.setId(resultSet.getInt(SnapshotDaoImpl.TILE_ID));
		tile.setCameraLabel(resultSet.getString(SnapshotDaoImpl.CAMERA));
		tile.setRawImageOid(resultSet.getInt(SnapshotDaoImpl.RAW_IMAGE_OID));
		tile.setRawNullImageOid(resultSet.getInt(SnapshotDaoImpl.NULL_IMAGE_OID));
		tile.setDataFormat(resultSet.getInt(SnapshotDaoImpl.DATA_FORMAT));
		tile.setWidth(resultSet.getInt(SnapshotDaoImpl.WIDTH));
		tile.setHeight(resultSet.getInt(SnapshotDaoImpl.HEIGHT));
		tile.setRotateFlipType(resultSet.getInt(SnapshotDaoImpl.FLIP_TYPE));
		tile.setFrame(resultSet.getInt(SnapshotDaoImpl.FRAME));
		
		int snapshotId = resultSet.getInt("snapshot_id");
		int index = indexById(snapshotId, snapshots);
		if (index != -1)
			snapshots.get(index).addTile(tile);
		
		return tile;
	}
}