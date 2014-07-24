package src.ddpsc.database.experiment;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


/**
 * Maps each row of a SQL query to an {@link Experiment} object.
 * 
 * The SQL query in particular is that from ExperimentDaoImpl.findAll();
 * 
 * The query:
 * "SELECT datname FROM pg_database "
 *	+ "WHERE datistemplate = false "
 *		+ "AND datname != 'postgres' "
 *		+ "AND datname !='bacula'"
 * 
 * @author shill, cjmcentee
 */
public class ExperimentRowMapper implements RowMapper<Experiment>
{

	@Override
	public Experiment mapRow(ResultSet resultSet, int line) throws SQLException
	{
		// SQL query value names:
		//		datname
		
		Experiment experiment = new Experiment();
		experiment.setExperimentName(resultSet.getString("name"));
		
		return experiment;
	}
}