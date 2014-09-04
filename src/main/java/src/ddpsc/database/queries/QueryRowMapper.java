package src.ddpsc.database.queries;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

class QueryRowMapper implements RowMapper<Query>
{
	@Override
	public Query mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		Query query = QueryDaoImpl.fromResultSet(rs);
		return query;
	}
}