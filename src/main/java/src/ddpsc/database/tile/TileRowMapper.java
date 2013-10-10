package src.ddpsc.database.tile;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

/**
 * This class creates a mapping from the database to our Snapshot object's fields. The inner class SnapshotExtractor
 * implements a spring utility function for this purpose, rather than putting it in it's own file, our application
 * will implement extractors as inner methods.
 * 
 * @throws SQLException
 * @see Snapshot
 * @author shill
 *
 */
public class TileRowMapper implements RowMapper<Tile>{

	@Override
	public Tile mapRow(ResultSet resultSet, int line) throws SQLException {
		try{
			TileExtractor tileExtractor = new TileExtractor();
			return tileExtractor.extractData(resultSet);
		}catch (SQLException e){
			throw e;
		}
	}
}
/**
 * Maps both TILE and TILED_IMAGE to the tile object.
 * @author shill
 *
 */
class TileExtractor implements ResultSetExtractor<Tile> {  
	public Tile extractData(ResultSet resultSet) throws SQLException,
			DataAccessException {
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