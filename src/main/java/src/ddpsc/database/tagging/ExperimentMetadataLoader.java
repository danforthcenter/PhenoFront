package src.ddpsc.database.tagging;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import src.ddpsc.database.experiment.Experiment;
import src.ddpsc.database.tile.Tile;

class ExperimentMetadataLoader implements ResultSetExtractor<Boolean>
{
	Map<Integer, Experiment> experiments;
	public ExperimentMetadataLoader(Collection<Experiment> experiments)
	{
		this.experiments = Experiment.getExperimentIdMap(experiments);
	}
	
	public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException
	{
		while (resultSet.next()) {
			int id = resultSet.getInt(TaggingDaoImpl.EXPERIMENT_ID);
			
			Experiment matchingExperiment = experiments.get(id);
			matchingExperiment.numberSnapshots = resultSet.getInt(TaggingDaoImpl.NUMBER_SNAPSHOTS);
			matchingExperiment.numberTiles = resultSet.getInt(TaggingDaoImpl.NUMBER_TILES);
			matchingExperiment.lastUpdated = resultSet.getTimestamp(TaggingDaoImpl.LAST_UPDATED);
		}
		
		return resultSet.first();
	}
}