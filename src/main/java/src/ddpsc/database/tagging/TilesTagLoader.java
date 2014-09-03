package src.ddpsc.database.tagging;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import src.ddpsc.database.tile.Tile;

class TilesTagLoader implements ResultSetExtractor<List<Tile>>
{
	Map<Integer, Tile> tiles;
	public TilesTagLoader(List<Tile> tiles)
	{
		this.tiles = Tile.getTileIdMap(tiles);
	}
	
	public List<Tile> extractData(ResultSet resultSet) throws SQLException, DataAccessException
	{
		// The query execute returns the following values in order
		//		tag_name
		//		tile_id
		while (resultSet.next()) {
			String tag = resultSet.getString(TaggingDaoImpl.TAG_NAME);
			int id = resultSet.getInt(TaggingDaoImpl.TILE_ID);
			tiles.get(id).tag = tag;
		}
		
		return new ArrayList<Tile>(tiles.values());
	}
}