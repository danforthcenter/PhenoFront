package src.ddpsc.database.experiment;

import java.util.HashSet;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Implements query to fetch experiment tables from database
 * 
 * @author shill, cjmcentee
 */
public class ExperimentDaoImpl implements ExperimentDao
{
	private static Logger log = Logger.getLogger("service");
	
	private DataSource experimentSource;
	
	
	@Override
	public HashSet<Experiment> findAll() throws CannotGetJdbcConnectionException
	{
		log.info("Attempting to find all experiments in the database.");
		
		String sql = "SELECT datname FROM pg_database "
				+ "WHERE datistemplate = false "
					+ "AND datname != 'postgres' "
					+ "AND datname !='bacula'";
		
		// String sql = "SELECT name from ltdbs where removed = FALSE";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(experimentSource);
		List<Experiment> experimentList = jdbcTemplate.query(sql, new ExperimentRowMapper());
		
		log.info("All experiments in the database found.");
		
		return new HashSet<Experiment>(experimentList);
	}

	@Override
	public Experiment findById()
	{
		// TODO Fill in SQL
		return null;
	}
	
	
	@Override
	public void setExperimentSource(DataSource experimentSource)
	{
		this.experimentSource = experimentSource;
	}
}
