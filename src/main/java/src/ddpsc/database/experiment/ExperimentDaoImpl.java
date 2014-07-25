package src.ddpsc.database.experiment;

import java.util.HashSet;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;

import src.ddpsc.exceptions.ObjectNotFoundException;

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
		
		String sql = "SELECT name FROM ltdbs WHERE removed = FALSE";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(experimentSource);
		List<Experiment> experimentList = jdbcTemplate.query(sql, new ExperimentRowMapper());
		
		log.info("All experiments in the database found.");
		
		return new HashSet<Experiment>(experimentList);
	}
	
	/**
	 * Returns the experiment with the supplied name
	 * 
	 * @param	experimentName		The name of the returned experiment
	 * @return						The experiment matching the supplied name
	 * 
	 * 
	 * @throws CannotGetJdbcConnectionException		Thrown if the database can't be accessed
	 * @throws ObjectNotFoundException				Thrown if the expirement isn't found
	 */
	@Override
	public Experiment getByName(String experimentName)
			throws ObjectNotFoundException, CannotGetJdbcConnectionException
	{
		log.info("Attempting to find the experiment " + experimentName);
		
		String sql = "SELECT name FROM ltdbs"
				+ " WHERE"
					+ " removed = FALSE"
				+ " AND name ='" + experimentName + "'";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(experimentSource);
		List<Experiment> experimentList = jdbcTemplate.query(sql, new ExperimentRowMapper());
		
		if (experimentList.size() == 0)
			throw new ObjectNotFoundException("The experiment " + experimentName + " could not be found.");
		
		else {
			Experiment experiment = experimentList.get(0);
			
			log.info("Found experiment with the name " + experimentName);
			return experiment;
		}
	}
	
	@Override
	public void setExperimentSource(DataSource experimentSource)
	{
		this.experimentSource = experimentSource;
	}
}
