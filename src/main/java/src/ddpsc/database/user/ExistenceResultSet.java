package src.ddpsc.database.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * Determines whether the result set has any returned rows. Does no other processing.
 * 
 * @author cjmcentee
 */
public class ExistenceResultSet implements ResultSetExtractor<Boolean>
{
	public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException
	{
		return resultSet.first(); // Returns false if there are no rows
	}
}