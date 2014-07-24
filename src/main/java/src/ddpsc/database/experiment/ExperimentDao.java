package src.ddpsc.database.experiment;

import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.CannotGetJdbcConnectionException;

public interface ExperimentDao
{
	public Set<Experiment> findAll()
		throws CannotGetJdbcConnectionException;
	
	public void setExperimentSource(DataSource experimentSource);
}
