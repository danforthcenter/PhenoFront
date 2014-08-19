package src.ddpsc.database.tagging;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class SnapshotIdRowMapper implements RowMapper<Integer>
{
	@Override
	public Integer mapRow(ResultSet resultSet, int line) throws SQLException
	{
		// The order and names of queried variables 
		//		snapshot_id
		
		return resultSet.getInt(TaggingDaoImpl.SNAPSHOT_ID);
	}
}