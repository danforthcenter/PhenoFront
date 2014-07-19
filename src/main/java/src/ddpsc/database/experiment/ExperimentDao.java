package src.ddpsc.database.experiment;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.CannotGetJdbcConnectionException;

/**
 * Data abstraction object for Experiments. These are represented in the LemnaTec database as lt_dbs.
 * 
 * @author shill, cjmcentee
 *
 */
public interface ExperimentDao
{
	public List<Experiment> findAll()
		throws CannotGetJdbcConnectionException;
	
	public Experiment findById(); // arguably useful
	
	public void setExperimentSource(DataSource experimentSource);
}
