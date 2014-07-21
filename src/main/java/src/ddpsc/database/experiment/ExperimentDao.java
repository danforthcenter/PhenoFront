package src.ddpsc.database.experiment;

import java.util.Set;

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
	public Set<Experiment> findAll()
		throws CannotGetJdbcConnectionException;
	
	public Experiment findById();
	
	public void setExperimentSource(DataSource experimentSource);
}
