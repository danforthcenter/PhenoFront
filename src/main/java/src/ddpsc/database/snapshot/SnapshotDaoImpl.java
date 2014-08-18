package src.ddpsc.database.snapshot;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import src.ddpsc.config.Config;
import src.ddpsc.database.queries.Query;
import src.ddpsc.database.tagging.TaggingDao;
import src.ddpsc.database.tile.Tile;
import src.ddpsc.database.tile.TileRowMapper;
import src.ddpsc.exceptions.MalformedConfigException;
import src.ddpsc.exceptions.ObjectNotFoundException;
import src.ddpsc.utility.StringOps;

/**
 * This class is responsible for building snapshot queries. Each user request should be defined in here.
 * 
 * No insertions should be made as the PostgreSQL database will be read only.
 * 
 * Each query typically has three types:
 * 
 * standard (findBySnapshot) - Only returns snapshots, no tile information is returned and no additional filtering is done.
 * withTile - Returns each snapshot with the associated tiles. If there are no tiles it is not included. This is a slow
 * method. imageJobs - Returns each snapshot if and only if it is an image job.
 * 
 * There should be an external tool that adds entries to servlet-context.xml and wires up the dataSources. This class should
 * figure out dynamically which dataSource to connect to.
 * 
 * @author shill
 * 
 */
public class SnapshotDaoImpl implements SnapshotDao
{
	private static final Logger log = Logger.getLogger(SnapshotDaoImpl.class);
	
	// From tiled_image table
	public static final String CAMERA			= "camera_label";
	// From tile table
	public static final String TILE_ID			= "id";
	public static final String RAW_IMAGE_OID	= "raw_image_oid";
	public static final String NULL_IMAGE_OID	= "raw_null_image_oid";
	public static final String DATA_FORMAT		= "dataformat";
	public static final String WIDTH			= "width";
	public static final String HEIGHT			= "height";
	public static final String FLIP_TYPE		= "rotate_flip_type";
	public static final String FRAME			= "frame";
	public static final String VISIBLE_DATA_TYPE		= "1";
	public static final String FLUORESCENT_DATA_TYPE	= "6";
	public static final String INFRARED_DATA_TYPE		= "0";
	
	
	private static final String TILE_QUERY_VARIABLES = "SELECT "
			+ "tiled_image.snapshot_id, "
			+ "tiled_image." + CAMERA + ", " 
			+ "tile." + RAW_IMAGE_OID + ", "
			+ "tile." + NULL_IMAGE_OID + ", "
			+ "tile." + DATA_FORMAT + ", "
			+ "tile." + WIDTH + ", "
			+ "tile." + HEIGHT + ", " 
			+ "tile." + FLIP_TYPE + ", "
			+ "tile." + FRAME + ", "
			+ "tile." + TILE_ID + " "
			+ "FROM tiled_image, tile ";
	
	public static final String SNAPSHOT_ID			= "id";
	public static final String ID_TAG				= "id_tag";
	public static final String CAR_TAG				= "car_tag";
	public static final String MEASUREMENT_LABEL	= "measurement_label";
	public static final String TIMESTAMP			= "time_stamp";
	public static final String WEIGHT_BEFORE		= "weight_before";
	public static final String WEIGHT_AFTER			= "weight_after";
	public static final String WATER_AMOUNT			= "water_amount";
	public static final String COMPLETED			= "completed";
	
	private static final String SNAPSHOT_QUERY_VARIABLES = "SELECT * FROM snapshot ";
	
	private String experiment;
	private DataSource snapshotDataSource;
	private TaggingDao taggingData;
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Snapshot Operations
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	/**
	 * Gets a snapshot by ID number
	 * 
	 * @param	id						The ID number of the returned snapshot
	 * @param	processingParameters	Additional query post-processing parameters 
	 * @return							The snapshot with the supplied ID number
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 * @throws	ObjectNotFoundException				Thrown if the specified object could not be found in the databsae
	 */
	@Override
	public Snapshot findById(int id)
			throws CannotGetJdbcConnectionException, ObjectNotFoundException
	{
		log.info("Attempting to find snapshot with ID='" + id + "'.");
		
		String findById = SNAPSHOT_QUERY_VARIABLES
				+ " WHERE id='" + id + "'";
		List<Snapshot> snapshots = snapshotQuery(findById);
		
		if (snapshots.size() == 0)
			throw new ObjectNotFoundException("Snapshot with the ID=" + id + " could not be found.");
		
		Snapshot snapshot = snapshots.get(0);
		
		log.info("Snapshot with ID='" + id + "' found.");
		return snapshot;
	}
	
	/**
	 * Gets all snapshots by the supplied ID numbers
	 * 
	 * @param	id						The ID number of the returned snapshot
	 * @param	processingParameters	Additional query post-processing parameters 
	 * @return							The snapshots with the supplied ID numbers
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 * @throws	ObjectNotFoundException				Thrown if the specified object could not be found in the databsae
	 */
	@Override
	public List<Snapshot> findById(List<Integer> ids)
			throws CannotGetJdbcConnectionException, ObjectNotFoundException
	{
		log.info("Attempting to find + " + ids.size() + "-many snapshots by ID.");
		
		String findById = SNAPSHOT_QUERY_VARIABLES
				+ " WHERE id in (";
		
		for (int i = 0; i < ids.size(); i++) {
			int id = ids.get(i);
			
			if (i != 0)
				findById += ", ";
			findById += "'" + id + "'";
		}
		findById += ")";
		List<Snapshot> snapshots = snapshotQuery(findById);
		
		log.info(ids.size() + "-many snapshots found by ID.");
		return snapshots;
	}
	
	/**
	 * Gets a set of snapshots that occurred after the supplied time. 
	 * 
	 * NOTE: If you are using a java Calendar implementation that months start at 0, and days start at 1.
	 * 
	 * @see GregorianCalendar
	 * @see Timestamp
	 * 
	 * @param	timestamp				The time before which all returned snapshots occurred
	 * @param	processingParameters	Parameters for addition processing
	 * @return							All snapshots occurring after timestamp
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	@Override
	public List<Snapshot> findAfterTimestamp(Timestamp timestamp)
			throws CannotGetJdbcConnectionException
	{
		log.info("Attempting to find snapshots after the time " + timestamp + ".");
		
		String getAfterTime = SNAPSHOT_QUERY_VARIABLES
				+ " WHERE time_stamp > '" + timestamp + "'";
		
		List<Snapshot> snapshots = snapshotQuery(getAfterTime);
		
		log.info("Snapshots after the time " + timestamp + " found.");
		return snapshots;
	}

	/**
	 * Returns all snapshots between startTime and endTime.
	 * 
	 * NOTE: If you are using a java Calendar implementation that months start at 0, and days start at 1.
	 * 
	 * @see GregorianCalendar
	 * @see Timestamp
	 * 
	 * @param	startTime				The earliest time a snapshot will be from in the returned list
	 * @param	endTime					The latest time a snapshot will be from in the returned list
	 * @param	processingParameters	Parameters for addition processing
	 * @return							Snapshots between startTime and endTime
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	@Override
	public List<Snapshot> findBetweenTimes(Timestamp startTime, Timestamp endTime)
			throws CannotGetJdbcConnectionException
	{
		log.info("Attempting to find snapshots between the times " + startTime + " and " + endTime + ".");
		
		String getBetweenTimes = SNAPSHOT_QUERY_VARIABLES
				+ " WHERE"
					+ " time_stamp > '" + startTime + "'"
				+ " AND time_stamp < '" + endTime   + "'";
		
		List<Snapshot> snapshots = snapshotQuery(getBetweenTimes);
		
		log.info("Snapshots between the times " + startTime + " and " + endTime + " found.");
		return snapshots;
	}

	/**
	 * Returns snapshots defined landing within a particular timespan, with particular barcodes and measurement labels.
	 * 
	 * Any null input variables will simply be ignored. If one end of the time span is null, then it becomes unbounded.
	 * If both are null, the it returns snapshots from any time.
	 * 
	 * The barcodes and measurement labels will match case-insensitive regular expressions.
	 * 
	 * The measurement label, in particular, anchors to the start of the string.
	 * 
	 * @param	querySettings			Settings defining a customized query
	 * @param	processingParameters	Parameters for addition processing
	 * @return							Snapshots meeting the specified criteria
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	@Override
	public List<Snapshot> executeCustomQuery(final Query querySettings)
			throws CannotGetJdbcConnectionException
	{
		log.info("Attempting to fulfill a custom snapshot query with the variables: " + querySettings + ".");
		
		List<Snapshot> snapshots;
		Timestamp startTime = querySettings.startTime;
		Timestamp endTime = querySettings.endTime;
		
		if (startTime == null && endTime != null)
			snapshots = findCustomQueryBeforeTime_HELPER(querySettings);
			
		else if (startTime != null && endTime == null)
			snapshots = findCustomQueryAfterTime_HELPER(querySettings);
		
		else if (startTime != null && endTime != null)
			snapshots = findCustomQueryBetweenTime_HELPER(querySettings);

		else
			snapshots = findCustomQueryAnyTime_HELPER(querySettings);
		
		log.info("Custom snapshot query fulfilled. " + snapshots.size() + "-many snapshots found. Variables: " + querySettings + ".");
		return snapshots;
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Setter / Getter Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public void setSnapshotExperiment(String experimentName) throws MalformedConfigException, IOException
	{
		this.experiment = experimentName;
		this.snapshotDataSource = Config.experimentDataSource(experimentName);
	}
	
	public void setTaggingData(TaggingDao tagging)
	{
		this.taggingData = tagging;
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Helper Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	private List<Snapshot> findCustomQueryBeforeTime_HELPER(final Query querySettings)
					throws CannotGetJdbcConnectionException
	{
		String sqlStatement = SNAPSHOT_QUERY_VARIABLES
				+ " WHERE "
					+ " time_stamp <= ? "
				+ " AND id_tag ~ ? "
				+ " AND measurement_label ~ ? "
				+ " AND completed = 't' ";
		
		if (querySettings.includeWatering == false)
			sqlStatement += " AND water_amount = -1 ";
		
		PreparedStatementSetter statementSetter = new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setTimestamp(1, querySettings.endTime);
				ps.setString(2, querySettings.barcode);
				ps.setString(3, querySettings.measurementLabel);
			}
		};
		
		return snapshotQuery(sqlStatement, statementSetter);
	}
	
	private List<Snapshot> findCustomQueryAfterTime_HELPER(final Query querySettings)
					throws CannotGetJdbcConnectionException
	{
		String sqlStatement = SNAPSHOT_QUERY_VARIABLES
				+ " WHERE "
					+ " time_stamp >= ? "
				+ " AND id_tag ~ ? "
				+ " AND measurement_label ~ ? "
				+ " AND completed = 't' ";
		
		if (querySettings.includeWatering == false)
			sqlStatement += " AND water_amount = -1 ";
		
		PreparedStatementSetter statementSetter = new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setTimestamp(1, querySettings.startTime);
				ps.setString(2, querySettings.barcode);
				ps.setString(3, querySettings.measurementLabel);
			}
		};
		
		return snapshotQuery(sqlStatement, statementSetter);
	}
	
	private List<Snapshot> findCustomQueryAnyTime_HELPER(final Query querySettings)
					throws CannotGetJdbcConnectionException
	{
		String sqlStatement = SNAPSHOT_QUERY_VARIABLES 
				+ " WHERE "
					+ " id_tag ~ ? "
				+ " AND measurement_label ~ ? "
				+ " AND completed = 't' ";
		
		if (querySettings.includeWatering == false)
			sqlStatement += " AND water_amount = -1 ";
		
		PreparedStatementSetter statementSetter = new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setString(1, querySettings.barcode);
				ps.setString(2, querySettings.measurementLabel);
			}
		};
		
		return snapshotQuery(sqlStatement, statementSetter);
	}
	
	private List<Snapshot> findCustomQueryBetweenTime_HELPER(final Query querySettings)
					throws CannotGetJdbcConnectionException
	{
		String sqlStatement = SNAPSHOT_QUERY_VARIABLES
				+ " WHERE "
					+ " time_stamp >= ? "
				+ " AND time_stamp <= ? "
				+ " AND id_tag ~ ? "
				+ " AND measurement_label ~ ? "
				+ " AND completed = 't' ";
		
		if (querySettings.includeWatering == false)
			sqlStatement += " AND water_amount = -1 ";
		
		PreparedStatementSetter statementSetter = new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setTimestamp(1, querySettings.startTime);
				ps.setTimestamp(2, querySettings.endTime);
				ps.setString(3, querySettings.barcode);
				ps.setString(4, querySettings.measurementLabel);
			}
		};
		
		return snapshotQuery(sqlStatement, statementSetter);
	}
	
	/**
	 * Executes a query against the snapshot database and returns the resulting snapshots.
	 * 
	 * The SQL Query must be compatible with the supplied prepared statement setter.
	 * 
	 * @param	sqlStatement			The SQL query compatible with the supplied prepared statement setter
	 * @param	preparedStatment		A prepared statement setter to inject values into the sqlQuery
	 * @param	processingParameters	Processing parameters, this method only uses the relevant post-processing parameters
	 * @return							The snapshots resulting from the query
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	private List<Snapshot> snapshotQuery(String sqlStatement, PreparedStatementSetter statementSetter)
			throws CannotGetJdbcConnectionException
	{
		JdbcTemplate snapshotDatabase = new JdbcTemplate(snapshotDataSource);
		List<Snapshot> snapshots = snapshotDatabase.query(sqlStatement, statementSetter, new SnapshotRowMapper());
		
		doPost(snapshots);
		
		return snapshots;
	}
	
	/**
	 * Executes a query against the snapshot database and returns the resulting snapshots.
	 * 
	 * @param	sqlQuery				The SQL query to look for snapshots
	 * @param	processingParameters	Processing parameters, this method only uses the relevant post-processing parameters
	 * @return							The snapshots resulting from the query
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	private List<Snapshot> snapshotQuery(String sqlQuery)
			throws CannotGetJdbcConnectionException
	{
		JdbcTemplate snapshotDatabase = new JdbcTemplate(snapshotDataSource);
		List<Snapshot> snapshots = snapshotDatabase.query(sqlQuery, new SnapshotRowMapper());
		
		doPost(snapshots);
		
		return snapshots;
	}
	
	/**
	 * Returns all the tiles associated with the supplied snapshot ID.
	 * 
	 * @param 	snapshot		The snapshot the tiles are linked to
	 * @return					All tiles with same snapshot ID as the supplied ID
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	private List<Tile> loadTiles(List<Snapshot> snapshots, boolean includeVisible, boolean includeFluorescent, boolean includeInfrared)
			throws CannotGetJdbcConnectionException
	{
		if (snapshots == null || snapshots.size() == 0)
			return new ArrayList<Tile>();
		
		String experiment = snapshots.get(0).getExperiment();
		
		String getTiles = TILE_QUERY_VARIABLES
				+ " WHERE tiled_image.snapshot_id in (" + StringOps.idsAsCSV(Snapshot.getIds(snapshots)) + ") "
				+ " AND tile.tiled_image_id = tiled_image.id "
				+ " AND tile."+DATA_FORMAT + " IN ";
		
		getTiles += " ( ";
				boolean addComma = false;
				if (includeVisible) {
					getTiles += "'" + VISIBLE_DATA_TYPE + "'";
					addComma = true;
				}
				
				if (includeFluorescent) {
					if (addComma)
						getTiles += ", ";
					getTiles += "'" + FLUORESCENT_DATA_TYPE + "'";
					addComma = true;
				}
				
				if (includeInfrared) {
					if (addComma)
						getTiles += ", ";
					getTiles += "'" + INFRARED_DATA_TYPE + "'";
					addComma = true;
				}
		getTiles += " ) ";
		
		JdbcTemplate tileDatabase = new JdbcTemplate(snapshotDataSource);
		List<Tile> tiles = tileDatabase.query(getTiles, new TileRowMapper(snapshots)); // loads tiles into snapshots
		
		taggingData.loadTilesWithTags(tiles, experiment);
		
		return tiles;
	}
	
	/**
	 * Modifies the snapshots based on the processing parameters
	 * 
	 * Intended to be called after retrieving the snapshots from the database, this post processing
	 * method fills in additional values on the snapshots that might be found in databases secondary
	 * to the snapshot database, or values that are often optional (e.g., loading the tiles on top of the snapshots)
	 * 
	 * @param snapshots				Snapshots from the last database access
	 * @param processingParameters	Parameters defining what additional processing must be done to the snapshots
	 */
	private void doPost(List<Snapshot> snapshots)
	{
		doPost(snapshots, true, true, true);
	}
	
	private void doPost(List<Snapshot> snapshots, boolean includeVisible, boolean includeFluorescent, boolean includeInfrared)
	{
		for (Snapshot snapshot : snapshots)
			snapshot.setExperiment(experiment);
		
		loadTags(snapshots);
		
		loadTiles(snapshots, includeVisible, includeFluorescent, includeInfrared);
	}
	
	private void loadTags(List<Snapshot> snapshots)
	{
		taggingData.loadSnapshotsWithTags(snapshots);
	}
}
