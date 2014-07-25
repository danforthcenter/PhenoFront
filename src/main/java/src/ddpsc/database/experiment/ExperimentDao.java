package src.ddpsc.database.experiment;

import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.CannotGetJdbcConnectionException;

import src.ddpsc.exceptions.ObjectNotFoundException;

public interface ExperimentDao
{
	public Set<Experiment> findAll()
		throws CannotGetJdbcConnectionException;
	
	public Experiment getByName(String experimentName)
		throws ObjectNotFoundException, CannotGetJdbcConnectionException;
	
	public void setExperimentSource(DataSource experimentSource);
}
