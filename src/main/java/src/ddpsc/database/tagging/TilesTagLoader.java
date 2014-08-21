package src.ddpsc.database.tagging;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import src.ddpsc.database.tile.Tile;

class TilesTagLoader implements ResultSetExtractor<List<Tile>>
{
	List<Tile> tiles;
	public TilesTagLoader(List<Tile> tiles)
	{
		this.tiles = tiles;
	}
	
	private int indexById(int id, List<Tile> tiles)
	{
		for (int index = 0; index < tiles.size(); index++) {
			Tile tile = tiles.get(index);
			if (tile.id == id)
				return index;
		}
		
		return -1;
	}
	
	public List<Tile> extractData(ResultSet resultSet) throws SQLException, DataAccessException
	{
		// The query execute returns the following values in order
		//		tag_name
		//		tile_id
		
		// n^2, but who cares for now
		// TODO: SQL query return sorted by ID to make this log n
		while (resultSet.next()) {
			String tag = resultSet.getString(TaggingDaoImpl.TAG_NAME);
			int id = resultSet.getInt(TaggingDaoImpl.TILE_ID);
			int idIndex = indexById(id, this.tiles);
			
			if (idIndex != -1) {
				tiles.get(idIndex).tag = tag;
			}
		}
		
		return tiles;
	}
}