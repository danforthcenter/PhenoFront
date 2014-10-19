package src.ddpsc.database.experiment;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;

import src.ddpsc.config.Config;
import src.ddpsc.database.snapshot.SnapshotDaoImpl;
import src.ddpsc.database.tagging.TaggingDao;
import src.ddpsc.database.tagging.TaggingDaoImpl;
import src.ddpsc.exceptions.MalformedConfigException;
import src.ddpsc.exceptions.ObjectNotFoundException;
import src.ddpsc.utility.StringOps;
import src.ddpsc.utility.Time;

/**
 * Fetches experiments from a SQL database
 * 
 * @author shill, cjmcentee
 */
public class ExperimentDaoImpl implements ExperimentDao
{
	
	private static final Logger log = Logger.getLogger(ExperimentDaoImpl.class);
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// LemnaTec database
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public static final String EXPERIMENT_TABLE = "ltdbs";
	
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String REMOVED = "removed";
	
	public static final String EXPERIMENT_QUERY_BASE = "SELECT "
			+ ID + ", "
			+ NAME + " "
			+ " FROM " + EXPERIMENT_TABLE;
			
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Fields
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	private DataSource experimentSource;
	
	private TaggingDao taggingData;
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// MySQL Accessing Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
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
		
		String getExtantExperiments = EXPERIMENT_QUERY_BASE
				+ " WHERE " + REMOVED + " = FALSE";

		List<Experiment> experimentList = experimentQuery(getExtantExperiments);
		taggingData.loadExperimentsWithTags(experimentList);
		
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
	 * @throws ObjectNotFoundException				Thrown if the experiment isn't found
	 */
	@Override
	public Experiment getByName(String experimentName)
			throws ObjectNotFoundException, CannotGetJdbcConnectionException
	{
		log.info("Attempting to find the experiment " + experimentName);
		
		String getByName = EXPERIMENT_QUERY_BASE
				+ " WHERE"
					+ " " + REMOVED + " = FALSE"
				+ " AND "
					+ " " + NAME + " ='" + experimentName + "'";
		
		List<Experiment> experimentList = experimentQuery(getByName);
		
		if (experimentList.size() == 0)
			throw new ObjectNotFoundException("The experiment " + experimentName + " could not be found.");
		
		else {
			Experiment experiment = experimentList.get(0);
			taggingData.loadExperimentsWithTags(Arrays.asList(new Experiment[]{experiment}));
			
			log.info("Found experiment with the name " + experimentName);
			return experiment;
		}
	}
	
	/**
	 * Modifies the supplied experiments to have metadata that matches the current state of the lemnatec database
	 * 
	 * @param experiments		Experiments to load with metadata
	 * 
	 * @throws	MalformedConfigException		Thrown if the config file for the lemnatec database cannot be read
	 * @throws	IOException						Thrown if the reading of the config file is interrupted
	 */
	@Override
	public void generateExperimentMetadata(Collection<Experiment> experiments) throws MalformedConfigException, IOException
	{
		log.info("Attempting to generate experiment metadata.");
		
		for (Experiment experiment : experiments) {
			experiment.numberSnapshots = getNumberSnapshots(experiment);
			experiment.numberTiles = getNumberTiles(experiment);
			experiment.lastUpdated = Time.now();
		}
		
		log.info("Successfully generated experiment metadata.");
	}
	
	private int getNumberSnapshots(Experiment experiment) throws MalformedConfigException, IOException
	{
		DataSource snapshotDataSource = Config.experimentDataSource(experiment.name);
		
		String getNumberSnapshots = "SELECT reltuples FROM pg_class WHERE relname = '" + SnapshotDaoImpl.SNAPSHOT_TABLE + "'";
		
		JdbcTemplate snapshotDatabase = new JdbcTemplate(snapshotDataSource);
		int numberSnapshots = snapshotDatabase.queryForInt(getNumberSnapshots);
		
		return numberSnapshots;
	}
	
	private int getNumberTiles(Experiment experiment) throws MalformedConfigException, IOException
	{
		DataSource tileDataSource = Config.experimentDataSource(experiment.name);
		
		String getNumberTiles = "SELECT reltuples FROM pg_class WHERE relname = '" + SnapshotDaoImpl.TILE_TABLE + "'";
		
		JdbcTemplate tileDatabase = new JdbcTemplate(tileDataSource);
		int numberTiles = tileDatabase.queryForInt(getNumberTiles);
		
		return numberTiles;
	}
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Helper Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	private List<Experiment> experimentQuery(String query)
	{
		JdbcTemplate experimentDatabase = new JdbcTemplate(experimentSource);
		List<Experiment> experimentList = experimentDatabase.query(query, new ExperimentRowMapper());
		
		return experimentList;
	}
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// MySQL Accessing Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public static Experiment fromResultSet(ResultSet rs) throws SQLException
	{
		int id = rs.getInt(ExperimentDaoImpl.ID);
		String name = rs.getString(ExperimentDaoImpl.NAME);
		
		Experiment experiment = new Experiment(id, name);
		
		return experiment;
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Get/Set Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	@Override
	public void setExperimentSource(DataSource experimentSource)
	{
		this.experimentSource = experimentSource;
	}
	
	@Override
	public void setTaggingData(TaggingDao taggingData)
	{
		this.taggingData = taggingData;
	}
}
