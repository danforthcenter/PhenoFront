package src.ddpsc.database.tile;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

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
		
		Tile tile = new Tile();
		tile.setCameraLabel(resultSet.getString(1));
		tile.setRawImageOid(resultSet.getInt(2));
		tile.setRawNullImageOid(resultSet.getInt(3));
		tile.setDataFormat(resultSet.getInt(4));
		tile.setWidth(resultSet.getInt(5));
		tile.setHeight(resultSet.getInt(6));
		tile.setRotateFlipType(resultSet.getInt(7));
		tile.setFrame(resultSet.getInt(8));
		return tile;
	}
}