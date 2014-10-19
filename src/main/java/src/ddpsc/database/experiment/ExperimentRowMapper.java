package src.ddpsc.database.experiment;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import src.ddpsc.database.queries.Query;


/**
 * Maps each row of a SQL query to an {@link Experiment} object.
 * 
 * @author shill, cjmcentee
 */
public class ExperimentRowMapper implements RowMapper<Experiment>
{
	@Override
	public Experiment mapRow(ResultSet resultSet, int line) throws SQLException
	{
		Experiment experiment = ExperimentDaoImpl.fromResultSet(resultSet);
		return experiment;
	}
}