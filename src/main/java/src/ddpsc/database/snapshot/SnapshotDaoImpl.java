package src.ddpsc.database.snapshot;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import src.ddpsc.database.tile.Tile;
import src.ddpsc.database.tile.TileRowMapper;
import src.ddpsc.exceptions.ObjectNotFoundException;

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
	
	private static final String TILE_QUERY_VARIABLES = "SELECT "
			+ "tiled_image.camera_label, " 
			+ "tile.raw_image_oid, "
			+ "tile.raw_null_image_oid, "
			+ "tile.dataformat, "
			+ "tile.width, "
			+ "tile.height, " 
			+ "tile.rotate_flip_type, "
			+ "tile.frame "
			+ "FROM tiled_image, tile ";
	
	private static final String SNAPSHOT_QUERY_VARIABLES = "SELECT * FROM snapshot ";
	
	DataSource dataSource;
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Snapshot Operations
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public List<String> getBarcodes(int maxTags)
			throws CannotGetJdbcConnectionException
	{
		String getBarcodes = "SELECT id_tag FROM snapshot";
		List<String> barcodes = getTags(maxTags, getBarcodes); 
		
		return barcodes;
	}
	
	public List<String> getMeasurementLabels(int maxTags)
			throws CannotGetJdbcConnectionException
	{
		String getMeasurementLabels = "SELECT measurement_label FROM snapshot";
		
		return getTags(maxTags, getMeasurementLabels);
	}
	
	private List<String> getTags(int maxTags, String getTags)
			throws CannotGetJdbcConnectionException
	{
		JdbcTemplate database = new JdbcTemplate(dataSource);
		List<String> tags = database.query(getTags, new StringRowMapper());
		// Moving to and from a set removes duplicates
		List<String> uniqueTags = new ArrayList<String>(new HashSet<String>(tags));
		Collections.sort(uniqueTags);
		
		if (uniqueTags.size() <= maxTags)
			return uniqueTags;
		
		else {
			List<String> maxUniqueTags = new ArrayList<String>(maxTags);
			float ratio = uniqueTags.size() / maxTags; // greater than 1
			
			for (int i = 0; i < maxTags; i++)
				maxUniqueTags.add(uniqueTags.get((int) (i*ratio)));
			
			return maxUniqueTags;
		}
	}
	
	private class StringRowMapper implements RowMapper<String>
	{
		@Override
		public String mapRow(ResultSet resultSet, int line) throws SQLException
		{
			// Expects the first entry in each row to be a string
			// Ideally, the only entry in the row will be a string
			return resultSet.getString(1);
		}
	}
	
	/**
	 * Gets a snapshot by ID number, without it's associated images.
	 * 
	 * @param	id		The ID number of the returned snapshot
	 * @return			The snapshot with the supplied ID number
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 * @throws	ObjectNotFoundException				Thrown if the specified object could not be found in the databsae
	 */
	@Override
	public Snapshot findByID(int id)
			throws CannotGetJdbcConnectionException, ObjectNotFoundException
	{
		log.info("Attempting to find snapshot with ID='" + id + "'.");
		
		String findID = SNAPSHOT_QUERY_VARIABLES
				+ "WHERE id='" + id + "'";
		
		List<Snapshot> snapshots = snapshotQuery(findID);
		
		if (snapshots.size() == 0)
			throw new ObjectNotFoundException("Snapshot with the ID=" + id + " could not be found.");
		
		Snapshot snapshot = snapshots.get(0);
		
		log.info("Snapshot with ID='" + id + "' found.");
		return snapshot;
	}
	
	
	/**
	 * Using ID, returns the snapshot preloaded with all associated tiles.
	 * 
	 * @see Tile;
	 * 
	 * @param	id		ID of the returned snapshot
	 * @return			Snapshot, with the supplied ID, preloaded with tiles
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 * @throws	ObjectNotFoundException				Thrown if the specified object could not be found in the databsae
	 */
	@Override
	public Snapshot findByID_withTiles(int id)
			throws CannotGetJdbcConnectionException, ObjectNotFoundException
	{
		log.info("Attempting to find snapshot with ID='" + id + "' and preload tiles.");
		
		Snapshot snapshot = this.findByID(id);
		snapshot.setTiles(this.findTiles(id));
		
		log.info("Snapshot with ID='" + id + "' found and preload with tiles.");
		return snapshot;
	}

	/**
	 * Gets a set of snapshots that occurred after the supplied time. 
	 * 
	 * NOTE: If you are using a java Calendar implementation that months start at 0, and days start at 1.
	 * 
	 * @see GregorianCalendar
	 * @see Timestamp
	 * 
	 * @param	timestamp		The time before which all returned snapshots occurred
	 * @return					All snapshots occurring after timestamp
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	@Override
	public List<Snapshot> findAfterTimestamp(Timestamp timestamp)
			throws CannotGetJdbcConnectionException
	{
		log.info("Attempting to find snapshots after the time " + timestamp + ".");
		
		String getAfterTime = SNAPSHOT_QUERY_VARIABLES
				+ "WHERE time_stamp > '" + timestamp + "'";
		
		List<Snapshot> snapshots = snapshotQuery(getAfterTime);
		
		log.info("Snapshots after the time " + timestamp + " found.");
		return snapshots;
	}

	/**
	 * Gets a set of snapshots that occurred after the supplied time, preloaded with tiles.
	 * 
	 * NOTE: If you are using a java Calendar implementation that months start at 0, and days start at 1.
	 * 
	 * @see GregorianCalendar
	 * @see Timestamp
	 * @see Tile
	 * 
	 * @param	timestamp		The time before which all returned snapshots occurred
	 * @return					All snapshots occurring after timestamp
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	@Override
	public List<Snapshot> findAfterTimestamp_withTiles(Timestamp timestamp)
			throws CannotGetJdbcConnectionException
	{
		log.info("Attempting to find snapshots after the time " + timestamp + " and preload tiles.");
		
		List<Snapshot> snapshots = this.findAfterTimestamp(timestamp);
		loadTiles(snapshots);
		
		snapshots = Snapshot.tiledOnly(snapshots);
		
		log.info("Snapshots after the time " + timestamp + " found and preloaded with tiles.");
		return snapshots;
	}
	
	/**
	 * Returns snapshots after the supplied time that were image jobs only.
	 * 
	 * NOTE: If you are using a java Calendar implementation that months start at 0, and days start at 1.
	 * 
	 * @see GregorianCalendar
	 * @see Timestamp
	 * @see Tile
	 * 
	 * @param	timestamp		A time before any of the returned snapshots
	 * @return					The image-only job snapshots that come after the supplied time
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	@Override
	public List<Snapshot> findAfterTimestamp_imageJobs(Timestamp timestamp)
			throws CannotGetJdbcConnectionException
	{
		log.info("Attempting to find image only snapshots after the time " + timestamp + ".");
		
		String getAfterTimeImagesOnly = SNAPSHOT_QUERY_VARIABLES
				+ "WHERE "
					+ "time_stamp > '" + timestamp + "' "
				+ "AND water_amount = -1";
		
		List<Snapshot> snapshots = snapshotQuery(getAfterTimeImagesOnly);
		
		log.info("Image only snapshots after the time " + timestamp + " found.");
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
	 * @param	startTime		The earliest time a snapshot will be from in the returned list
	 * @param	endTime			The latest time a snapshot will be from in the returned list
	 * @return					Snapshots between startTime and endTime
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	@Override
	public List<Snapshot> findBetweenTimes(Timestamp startTime, Timestamp endTime)
			throws CannotGetJdbcConnectionException
	{
		log.info("Attempting to find snapshots between the times " + startTime + " and " + endTime + ".");
		
		String getBetweenTimes = SNAPSHOT_QUERY_VARIABLES
				+ "WHERE "
					+ "time_stamp > '" + startTime + "' "
				+ "AND time_stamp < '" + endTime   + "'";
		
		List<Snapshot> snapshots = snapshotQuery(getBetweenTimes);
		
		log.info("Snapshots between the times " + startTime + " and " + endTime + " found.");
		return snapshots;
	}

	/**
	 * Returns all snapshots between startTime and endTime, preloaded with tiles.
	 * 
	 * NOTE: If you are using a java Calendar implementation that months start at 0, and days start at 1.
	 * 
	 * @see GregorianCalendar
	 * @see Timestamp
	 * 
	 * @param	startTime		The earliest time a snapshot will be from in the returned list
	 * @param	endTime			The latest time a snapshot will be from in the returned list
	 * @return					Snapshots between startTime and endTime
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	@Override
	public List<Snapshot> findBetweenTimes_withTiles(Timestamp startTime, Timestamp endTime)
			throws CannotGetJdbcConnectionException
	{
		log.info("Attempting to find snapshots between the times " + startTime + " and " + endTime + " and preload the tiles.");
		
		List<Snapshot> snapshots = this.findBetweenTimes(startTime, endTime);
		loadTiles(snapshots);
		
		snapshots = Snapshot.tiledOnly(snapshots);
		
		log.info("Snapshots between the times " + startTime + " and " + endTime + " found and preloaded with tiles.");
		return snapshots;
	}
	
	/**
	 * Returns all snapshots between startTime and endTime that were image jobs only.
	 * 
	 * NOTE: If you are using a java Calendar implementation that months start at 0, and days start at 1.
	 * 
	 * @see GregorianCalendar
	 * @see Timestamp
	 * 
	 * @param	startTime		The earliest time a snapshot will be from in the returned list
	 * @param	endTime			The latest time a snapshot will be from in the returned list
	 * @return					The image-only job snapshots that come between the supplied times
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	@Override
	public List<Snapshot> findBetweenTimes_imageJobs(Timestamp startTime, Timestamp endTime)
			throws CannotGetJdbcConnectionException
	{
		log.info("Attempting to find image only snapshots between the times " + startTime + " and " + endTime + ".");
		
		String getBetweenTimesImageOnly = SNAPSHOT_QUERY_VARIABLES
				+ "WHERE "
					+ "time_stamp > '" + startTime + "' "
				+ "AND time_stamp <'" + endTime + "' "
				+ "AND water_amount = -1";
		
		List<Snapshot> snapshots = snapshotQuery(getBetweenTimesImageOnly);
		
		log.info("Image only snapshots between the times " + startTime + " and " + endTime + " found.");
		return snapshots;
	}

	/**
	 * Returns all the tiles associated with the supplied snapshot ID.
	 * 
	 * @param 	snapshotID		ID of the snapshot the tiles are linked to
	 * @return					All tiles with same snapshot ID as the supplied ID
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	public List<Tile> findTiles(int snapshotID)
			throws CannotGetJdbcConnectionException
	{
		log.info("Attempting to find tiles associated with the snapshot with ID='" + snapshotID + "'.");
		
		String getTilesBySnapshotID = TILE_QUERY_VARIABLES
				+ "WHERE "
					+ "tiled_image.snapshot_id = " + snapshotID + " "
				+ "AND tile.tiled_image_id = tiled_image.id";
		
		List<Tile> tiles = tileQuery(getTilesBySnapshotID);
		
		log.info("Tiles associated with the snapshot with ID='" + snapshotID + "' found.");
		return tiles;
	}
	
	/**
	 * Returns all the tiles associated with the supplied snapshot ID.
	 * 
	 * @param 	snapshot		The snapshot the tiles are linked to
	 * @return					All tiles with same snapshot ID as the supplied ID
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	public List<Tile> findTiles(Snapshot snapshot)
			throws CannotGetJdbcConnectionException
	{
		log.info("Attempting to find tiles associated with the snapshot " + snapshot.getID() + ".");
		
		String getTilesBySnapshotID = TILE_QUERY_VARIABLES
				+ "WHERE tiled_image.snapshot_id = " + snapshot.getID() + " "
				+ "AND tile.tiled_image_id = tiled_image.id";
		
		List<Tile> tiles = tileQuery(getTilesBySnapshotID);
		
		log.info("Tiles associated with the snapshot " + snapshot.getID() + " found.");
		return tiles;
	}
	
	/**
	 * Returns the last N-snapshots in the database.
	 * 
	 * @param	n			How many of the latest snapshots to return
	 * @return				The last N-many snapshots
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	@Override
	public List<Snapshot> findLastN(int n)
			throws CannotGetJdbcConnectionException
	{
		log.info("Attempting to find the last " + n + "-many snapshots.");
		
		String getSnapshots = SNAPSHOT_QUERY_VARIABLES
				+ "ORDER BY time_stamp DESC LIMIT " + n;
		
		List<Snapshot> snapshots = snapshotQuery(getSnapshots);
		
		log.info("Found the last " + n + "-many snapshots.");
		return snapshots;
	}
	
	/**
	 * Returns the last N-snapshots in the database, preloaded with tiles.
	 * 
	 * @param	n			How many of the latest snapshots to return
	 * @return				The last N-many snapshots
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	@Override
	public List<Snapshot> findLastN_withTiles(int n)
			throws CannotGetJdbcConnectionException
	{
		log.info("Attempting to find the last " + n + "-many snapshots and preload them with tiles.");
		
		List<Snapshot> snapshots = findLastN(n);
		loadTiles(snapshots);
		
		snapshots = Snapshot.tiledOnly(snapshots);
		
		log.info("Found the last " + n + "-many snapshots and preloaded them with tiles.");
		return snapshots;
	}
	
	/**
	 * Returns the last N-snapshots in the database that were image jobs only.
	 * 
	 * @param	n			How many of the latest snapshots to return
	 * @return				The last N-many snapshots, image jobs only
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	@Override
	public List<Snapshot> findLastN_imageJobs(int n)
			throws CannotGetJdbcConnectionException
	{
		log.info("Attempting to find the last " + n + "-many image job only snapshots.");
		
		String getLastNImageJobs = SNAPSHOT_QUERY_VARIABLES
				+ "WHERE water_amount = -1 "
				+ "ORDER BY time_stamp DESC LIMIT " + n;
		
		List<Snapshot> snapshots = snapshotQuery(getLastNImageJobs);
		
		log.info("Found the last " + n + "-many image only snapshots.");
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
	 * @param	startTime			Time before all returned snapshots
	 * @param	endTime				Time after all returned snapshots
	 * @param	plantBarcode		TODO: Figure out what this is, some kind of string-based ID
	 * @param	measurementLabel	TODO: Figure out what this is, another string-based ID
	 * @return						Snapshots meeting the specified criteria
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	@Override
	public List<Snapshot> findCustomQueryAnyTime_imageJobs(
			final Timestamp startTime,
			final Timestamp endTime,
			final String plantBarcode,
			final String measurementLabel)
					throws CannotGetJdbcConnectionException
	{
		log.info("Attempting to fulfill a custom snapshot query with the variables: "
				+ "StartTime = " 		+ startTime 		+ ", "
				+ "EndTime = " 			+ endTime			+ ", "
				+ "PlantBarcode = " 	+ plantBarcode 		+ ", "
				+ "MeasurementLabel = " + measurementLabel	+ ".");
		
		List<Snapshot> snapshots;
		
		if (startTime == null && endTime != null)
			snapshots = findCustomQueryBeforeTime_imageJobs_HELPER(endTime, plantBarcode, measurementLabel);
			
		else if (startTime != null && endTime == null)
			snapshots = findCustomQueryAfterTime_imageJobs_HELPER(startTime, plantBarcode, measurementLabel);
		
		else if (startTime != null && endTime != null)
			snapshots = findCustomQueryBetweenTime_imageJobs_HELPER(startTime, endTime, plantBarcode, measurementLabel);

		else
			snapshots = findCustomQueryAnyTime_imageJobs_HELPER(plantBarcode, measurementLabel);
		
		log.info("Custom snapshot query fulfilled. Variables: "
				+ "StartTime = " 		+ startTime 		+ ", "
				+ "EndTime = " 			+ endTime			+ ", "
				+ "PlantBarcode = " 	+ plantBarcode 		+ ", "
				+ "MeasurementLabel = " + measurementLabel	+ ".");
		
		return snapshots;
	}
	
	/**
	 * Returns snapshots defined landing within a particular timespan, with particular barcodes and measurement labels.
	 * Will preload snapshots with their associated tiles.
	 * 
	 * Any null input variables will simply be ignored. If one end of the time span is null, then it becomes unbounded.
	 * If both are null, the it returns snapshots from any time.
	 * 
	 * The barcodes and measurement labels will match case-insensitive regular expressions.
	 * 
	 * The measurement label, in particular, anchors to the start of the string.
	 * 
	 * @param	startTime			Time before all returned snapshots
	 * @param	endTime				Time after all returned snapshots
	 * @param	plantBarcode		TODO: Figure out what this is, some kind of string-based ID
	 * @param	measurementLabel	TODO: Figure out what this is, another string-based ID
	 * @return						Snapshots meeting the specified criteria
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	public List<Snapshot> findCustomQueryAnyTime_imageJobs_withTiles(
			Timestamp startTime,
			Timestamp endTime,
			String plantBarcode,
			String measurementLabel)
					throws CannotGetJdbcConnectionException
	{
		List<Snapshot> snapshots = this.findCustomQueryAnyTime_imageJobs(startTime, endTime, plantBarcode, measurementLabel);
		loadTiles(snapshots);
		
		return Snapshot.tiledOnly(snapshots);
	}
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Setter / Getter Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Helper Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	protected List<Snapshot> findCustomQueryBeforeTime_imageJobs_HELPER(
			final Timestamp endTime,
			final String plantBarcode,
			final String measurementLabel)
					throws CannotGetJdbcConnectionException
	{
		String sqlStatement = SNAPSHOT_QUERY_VARIABLES
				+ "WHERE "
					+ "time_stamp <= ? "
				+ "AND id_tag ~ ? "
				+ "AND measurement_label ~* ? "
				+ "AND completed = 't' "
				+ "AND water_amount = -1";
		
		PreparedStatementSetter statementSetter = new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setTimestamp(1, endTime);
				ps.setString(2, plantBarcode == null ? "" : plantBarcode);
				ps.setString(3, measurementLabel == null ? "" : measurementLabel);
			}
		};
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(sqlStatement, statementSetter, new SnapshotRowMapper());
	}
	
	protected List<Snapshot> findCustomQueryAfterTime_imageJobs_HELPER(
			final Timestamp startTime,
			final String plantBarcode,
			final String measurementLabel)
					throws CannotGetJdbcConnectionException
	{
		String sqlStatement = SNAPSHOT_QUERY_VARIABLES
				+ "WHERE "
					+ "time_stamp >= ? "
				+ "AND id_tag ~ ? "
				+ "AND measurement_label ~* ? "
				+ "AND completed = 't' "
				+ "AND water_amount = -1";
		
		PreparedStatementSetter statementSetter = new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setTimestamp(1, startTime);
				ps.setString(2, plantBarcode == null ? "" : plantBarcode);
				ps.setString(3, measurementLabel == null ? "" : measurementLabel);
			}
		};
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(sqlStatement, statementSetter, new SnapshotRowMapper());
	}
	
	protected List<Snapshot> findCustomQueryAnyTime_imageJobs_HELPER(
			final String plantBarcode,
			final String measurementLabel)
					throws CannotGetJdbcConnectionException
	{
		String sqlStatement = SNAPSHOT_QUERY_VARIABLES 
				+ "WHERE "
					+ "id_tag ~ ? "
				+ "AND measurement_label ~* ? "
				+ "AND completed = 't' "
				+ "AND water_amount = -1";
		
		PreparedStatementSetter statementSetter = new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setString(1, plantBarcode == null ? "" : plantBarcode);
				ps.setString(2, measurementLabel == null ? "" : measurementLabel);
			}
		};
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(sqlStatement, statementSetter, new SnapshotRowMapper());
	}
	
	protected List<Snapshot> findCustomQueryBetweenTime_imageJobs_HELPER(
			final Timestamp startTime,
			final Timestamp endTime,
			final String plantBarcode,
			final String measurementLabel)
					throws CannotGetJdbcConnectionException
	{
		String sqlStatement = SNAPSHOT_QUERY_VARIABLES
				+ "WHERE "
					+ "time_stamp >= ? "
				+ "AND time_stamp <= ? "
				+ "AND id_tag ~ ? "
				+ "AND measurement_label ~* ? "
				+ "AND completed = 't' "
				+ "AND water_amount = -1";
		
		PreparedStatementSetter statementSetter = new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setTimestamp(1, startTime);
				ps.setTimestamp(2, endTime);
				ps.setString(3, plantBarcode == null ? "" : plantBarcode);
				ps.setString(4, measurementLabel == null ? "" : measurementLabel);
			}
		};
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(sqlStatement, statementSetter, new SnapshotRowMapper());
	}
	
	/**
	 * Loads the tiles to the supplied snapshots
	 * 
	 * @param	snapshots		The snapshots to load tiles into
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	protected void loadTiles(List<Snapshot> snapshots)
			throws CannotGetJdbcConnectionException
	{
		for (Snapshot snapshot : snapshots)
			snapshot.setTiles(findTiles(snapshot));
	}
	
	/**
	 * Executes a query against the snapshot database and returns the resulting snapshots.
	 * 
	 * @param	sqlQuery	The SQL query to look for snapshots
	 * @return				The snapshots resulting from the query
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	protected List<Snapshot> snapshotQuery(String sqlQuery)
			throws CannotGetJdbcConnectionException
	{
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(sqlQuery, new SnapshotRowMapper());
	}
	
	/**
	 * Executes a query against the snapshot database and returns the resulting tiles.
	 * 
	 * @param	sqlQuery	The SQL query to look for tiles
	 * @return				The tiles resulting from the query
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is not accessible
	 */
	protected List<Tile> tileQuery(String sqlQuery)
			throws CannotGetJdbcConnectionException
	{
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(sqlQuery, new TileRowMapper());
	}
}
