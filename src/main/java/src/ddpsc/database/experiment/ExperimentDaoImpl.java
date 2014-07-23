package src.ddpsc.database.experiment;

import java.util.HashSet;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Fetches experiments from a SQL database
 * 
 * @author shill, cjmcentee
 */
public class ExperimentDaoImpl implements ExperimentDao
{
	private static final Logger log = Logger.getLogger(ExperimentDaoImpl.class);
	
	private DataSource experimentSource;
	
	/**
	 * Returns all the experiments from the database
	 * 
	 * @return			All the experiments available in the database
	 * 
	 * @throws CannotGetJdbcConnectionException		Thrown if the database can't be accessed
	 */
	@Override
	public HashSet<Experiment> findAll() throws CannotGetJdbcConnectionException
	{
		log.info("Attempting to find all experiments in the database.");
		
		String sql = "SELECT datname FROM pg_database "
				+ "WHERE datistemplate = false "
					+ "AND datname != 'postgres' "
					+ "AND datname !='bacula'";
		
		// String sql = "SELECT name FROM ltdbs WHERE removed = FALSE";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(experimentSource);
		List<Experiment> experimentList = jdbcTemplate.query(sql, new ExperimentRowMapper());
		
		log.info("All experiments in the database found.");
		
		return new HashSet<Experiment>(experimentList);
	}
	
	@Override
	public void setExperimentSource(DataSource experimentSource)
	{
		this.experimentSource = experimentSource;
	}
}
