package src.ddpsc.database.experiment;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.CannotGetJdbcConnectionException;

import src.ddpsc.database.tagging.TaggingDao;
import src.ddpsc.exceptions.MalformedConfigException;
import src.ddpsc.exceptions.ObjectNotFoundException;

public interface ExperimentDao
{
	public void setExperimentSource(DataSource experimentSource);
	public void setTaggingData(TaggingDao taggingData);
	
	public Set<Experiment> findAll()
		throws CannotGetJdbcConnectionException;
	
	public Experiment getByName(String experimentName)
		throws ObjectNotFoundException, CannotGetJdbcConnectionException;
	
	void generateExperimentMetadata(Collection<Experiment> experiments)
			throws MalformedConfigException, IOException;
	
}
