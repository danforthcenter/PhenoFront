package src.ddpsc.database.queries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import src.ddpsc.database.user.User;
import src.ddpsc.database.user.UserDaoImpl;
import src.ddpsc.utility.StringOps;

/**
 * Implementation of the query storage, retrieval, and commenting interface.
 * 
 * Retrieves data from a SQL database, defined by {@link metadataDataSource}.
 * 
 * Assumes the database has the structure of:
 * 		Tables:	
 * 			queries						describes each user made query
 * 			query_metadata				contains statistics about a given query, one-to-one relationship
 * 
 * 
 * 		Table `queries`:
 * 			`query_id`				INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
 * 															a unique id
 *			`metadata_id`			INT(10) UNSIGNED NOT NULL,
 *															the metadata associated with the query
 *			`experiment`			VARCHAR(255),			the experiment the snapshots are in
 *			`barcode_regex`			VARCHAR(255),			regex searching barcodes
 *			`measurement_regex`		VARCHAR(255),			regex searching measurement labels
 *			`start_time`			DATETIME,				the earliest time a snapshot could be made
 *			`end_time`				DATETIME,				the latest time a snapshot could be made
 *
 *			`include_watering`		BOOL NOT NULL,			whether the query included watering data
 *			`include_visible`		BOOL NOT NULL,			whether the query included visible images
 *			`include_fluorescent`	BOOL NOT NULL,			whether the query included fluorescent images
 *			`include_infrared`		BOOL NOT NULL,			whether the query included IR images
 * 
 * 
 * 		Table `query_metadata`:
 *			`metadata_id`			INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
 *															a unique id
 *			`query_id`				INT(10) UNSIGNED NOT NULL,
 *															the query associated with this metadata
 *			`user_id`				INT(10) UNSIGNED NOT NULL,
 *															the user who made the query
 *			
 *			`comment`				TEXT,					the user supplied comment
 *			
 *			`date_made`				DATETIME,				when the query was made
 *			`date_download_begin`	DATETIME,				when the query began downloading
 *			`date_download_complete`DATETIME,				when the download finished
 *			`interrupted`			BOOL,					whether the download was interrupted
 *			`missed_snapshots`		TEXT,					a list of snapshot ids that didn't get downloaded
 *		
 *			`bytes`					BIGINT,					how large the download was
 *			`number_snapshots`		INT,					how many snapshots the query contained
 *			`number_tiles`			INT,					how many tiles the query contained
 * 
 * 
 * 
 * @see Query
 * @see QueryRowMapper
 * @see QueryMetadataRowMapper
 * @see UserDaoImpl
 * 
 * @author cjmcentee
 */
public class QueryDaoImpl implements QueryDao
{
	private static final Logger log = Logger.getLogger(QueryDaoImpl.class);
	
	private DataSource metadataDataSource;
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// MySQL Table Description
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// The query table
	public static String QUERY_TABLE	= "queries";	// Table name
	
	public static String QUERY_ID		= "query_id";
	
	public static String EXPERIMENT		= "experiment";
	public static String BARCODE		= "barcode_regex";
	public static String MEASUREMENT	= "measurement_regex";
	public static String START_TIME		= "start_time";
	public static String END_TIME		= "end_time";
	
	public static String WATERING		= "include_watering";
	public static String VISIBLE		= "include_visible";
	public static String FLUORESCENT	= "include_fluorescent";
	public static String INFRARED		= "include_infrared";
	
	// The query metadata table
	public static String METADATA_TABLE	= "query_metadata";
	
	public static String METADATA_ID	= "metadata_id";
	public static String USER_ID		= "user_id";
	
	public static String COMMENT		= "comment";
	
	public static String DATE_MADE		= "date_made";
	public static String DOWNLOAD_BEGIN	= "date_download_begin";
	public static String DOWNLOAD_END	= "date_download_complete";
	public static String INTERRUPTED	= "interrupted";
	public static String MISSED_SNAPSHOTS= "missed_snapshots";
	
	public static String SIZE			= "bytes";
	public static String NUM_SNAPSHOTS	= "number_snapshots";
	public static String NUM_TILES		= "number_tiles";
	
	@Override
	public List<Query> getQueries(QueryFilter filter)
	{
		log.info("Attempting to retrieve queries for filter: " + filter);
		
		String getQueriesSuffix = ""
				+ " WHERE q."+EXPERIMENT + " = '" + filter.experiment + "' ";
		
		if (filter.excludedIds != null && filter.excludedIds.size() > 0)
			getQueriesSuffix += " AND q."+QUERY_ID + " NOT IN (" + StringOps.idsAsCSV(filter.excludedIds) + ") ";
		
		if ( ! filter.username.equals(""))
			getQueriesSuffix += " AND u."+User.USERNAME + " = '" + filter.username + "' ";
		
		if (filter.onlyCommented)
			getQueriesSuffix += " AND m."+COMMENT + " IS NOT NULL AND m."+COMMENT + " <> ''";
		
		getQueriesSuffix += " ORDER BY m."+DATE_MADE + " DESC ";
		
		if (filter.limit != -1)
			getQueriesSuffix += " LIMIT " + filter.limit;
		
		
		
		List<Query> queries = getQueries(getQueriesSuffix);
		
		log.info("Successfully retrieved queries for filter: " + filter);
		
		return queries;
	}
	
	@Override
	public int addQuery(Query query) throws SQLException
	{
		log.info("Attempting to add the query " + query + " to the databse.");
		
		// Add the query itself
		String addQuery = "INSERT INTO " + QUERY_TABLE + " "
				+ " ( "
					+ EXPERIMENT + ", "
					
					+ BARCODE + ", "
					+ MEASUREMENT + ", "
					+ START_TIME + ", "
					+ END_TIME + ", "
					
					+ WATERING + ", "
					+ VISIBLE + ", "
					+ INFRARED + ", "
					+ FLUORESCENT
				+ " ) "
				+ " VALUES ( "
					+ "'" + query.experiment + "', "
					
					+ "'" + query.barcode + "', "
					+ "'" + query.measurementLabel + "', "
					+ (query.startTime == null
						? null
						: "'" + query.startTime + "'") + ", "
					+ (query.endTime == null 
						? null
						: "'" + query.endTime + "'") + ", "
					
					+ query.includeWatering + ", "
					+ query.includeVisible + ", "
					+ query.includeInfrared + ", "
					+ query.includeFluorescent
				+ ") ";
		
		int queryId = addToQuerySystem(addQuery);
		
		// Add the query's associated metadata
		QueryMetadata metadata = query.metadata;
		boolean hasComment = metadata.comment != null && ! metadata.comment.equals("");
		String addMetadata = "INSERT INTO " + METADATA_TABLE + " "
				+ " ( "
					+ QUERY_ID + ", "
					+ USER_ID + ", "
					+ DATE_MADE + ", "
					+ NUM_SNAPSHOTS + ", "
					+ NUM_TILES;
		if (hasComment)
			addMetadata += ", " + COMMENT;
		
		addMetadata += ""
				+ " ) VALUES ( "
					+ "'" + queryId + "', "
					+ "'" + metadata.userId + "', "
					+ (metadata.dateMade == null 
						? null
						: "'" + metadata.dateMade + "'") + ", "
					+ "'" + metadata.numberSnapshots + "', "
					+ "'" + metadata.numberTiles + "' ";
		if (hasComment)
			addMetadata += ", " + "'" + metadata.comment + "'";
		addMetadata += ") ";
		
		int metadataId = addToQuerySystem(addMetadata);
		
		// Link the metadata back to the query
		String linkToQuery = "UPDATE " + QUERY_TABLE + " "
				+ " SET "
					+ METADATA_ID + " ='" + metadataId + "' "
				+ " WHERE "
					+ QUERY_ID + " ='" + queryId + "'";
		
		modifyQuerySystem(linkToQuery);
		
		log.info("Query " + query + " added to the database.");
		
		return queryId;
	}
	
	@Override
	public void setQueryComments(List<Integer> queryIds, String newComment)
	{
		log.info("Attempting to change comment on " + queryIds.size() + "-many queries to: '" + newComment + "'.");
		
		String changeComment = "UPDATE " + METADATA_TABLE
				+ " SET " + COMMENT + " = '" + newComment + "' "
				+ " WHERE " + QUERY_ID + " IN (" + StringOps.idsAsCSV(queryIds) + ")";
		
		modifyQuerySystem(changeComment);
		
		log.info("Changed the comment on " + queryIds.size() + "-many queries to: '" + newComment + "'.");
	}
	
	@Override
	public void setQueryComment(int queryId, String newComment)
	{
		if (queryId != -1)
			setQueryComments(Arrays.asList(new Integer[]{queryId}), newComment);
	}
	
	@Override
	public void setDownloadStart(int queryId, Timestamp time)
	{
		log.info("Attempting to change the interruption status on the query ID='" + queryId + "' to: '" + time + "'.");
		
		if (queryId != -1)
			setMetadataVariable(queryId, DOWNLOAD_BEGIN, time.toString());
		
		log.info("Changed the interruption status on the query ID='" + queryId + "' to: '" + time + "'.");
	}
	
	@Override
	public void setDownloadEnd(int queryId, Timestamp time)
	{
		log.info("Attempting to change the download finished time on the query ID='" + queryId + "' to: '" + time + "'.");
		
		if (queryId != -1)
			setMetadataVariable(queryId, DOWNLOAD_END, time.toString());
		
		log.info("Changed the download finished time on the query ID='" + queryId + "' to: '" + time + "'.");
	}
	
	@Override
	public void setInterrupted(int queryId, boolean wasInterrupted)
	{
		String wasInterrupedString = wasInterrupted ? "true" : "false";
		log.info("Attempting to change the interruption status on the query ID='" + queryId + "' to: '" + wasInterrupedString + "'.");
		
		if (queryId != -1) {
			String changeVariable = "UPDATE " + METADATA_TABLE
					+ " SET " + INTERRUPTED + " = " + wasInterrupted + ""
					+ " WHERE " + QUERY_ID + " ='" + queryId + "'"
					+ " LIMIT 1";
			
			log.info("Set metadata variable: [" + changeVariable + "]");
			modifyQuerySystem(changeVariable);
		}
		
		log.info("Changed the interruption status on the query ID='" + queryId + "' to: '" + wasInterrupedString + "'.");
	}
	
	@Override
	public void setMissedSnapshots(int queryId, List<Integer> missedSnapshots)
	{
		log.info("Attempting to change the missing snapshots on the query ID='" + queryId + "' to " + missedSnapshots + ".");
		
		String missedSnapshots_CSV = StringOps.idsAsCSV(missedSnapshots, false);
		if (queryId != -1)
			setMetadataVariable(queryId, MISSED_SNAPSHOTS, missedSnapshots_CSV);
		
		log.info("Changed the missing snapshots on the query ID='" + queryId + "' to " + missedSnapshots + ".");
	}
	
	@Override
	public void setQuerySize(int queryId, long bytes)
	{
		String bytesString = Long.toString(bytes);
		log.info("Attempting to change query size on the query ID='" + queryId + "' to: '" + bytesString + "'.");
		
		if (queryId != -1)
			setMetadataVariable(queryId, SIZE, bytesString);
		
		log.info("Changed the query size on the query ID='" + queryId + "' to: '" + bytesString + "'.");
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Getter/Setter Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public void setMetadataDataSource(DataSource dataSource)
	{
		metadataDataSource = dataSource;
	}
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Helper Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	private List<Query> getQueries(String mysqlQuerySuffix)
	{
		String getQueries = "SELECT "
				+ " q."+QUERY_ID + ", "
				+ " q."+EXPERIMENT + ", "
				+ " q."+BARCODE + ", "
				+ " q."+MEASUREMENT + ", "
				+ " q."+START_TIME + ", "
				+ " q."+END_TIME + ", "
				+ " q."+WATERING + ", "
				+ " q."+VISIBLE + ", "
				+ " q."+FLUORESCENT + ", "
				+ " q."+INFRARED + ", "
				+ " m."+METADATA_ID + ", "
				+ " m."+USER_ID + ", "
				+ " m."+COMMENT + ", "
				+ " m."+DATE_MADE + " AS date, "
				+ " m."+DOWNLOAD_BEGIN + ", "
				+ " m."+DOWNLOAD_END + ", "
				+ " m."+INTERRUPTED + ", "
				+ " m."+MISSED_SNAPSHOTS + ", "
				+ " m."+SIZE + ", "
				+ " m."+NUM_SNAPSHOTS + ", "
				+ " m."+NUM_TILES + ", "
				+ " u."+User.USERNAME + " "
			+ " FROM " + QUERY_TABLE + " AS q "
			+ " JOIN " + METADATA_TABLE + " AS m ON q."+QUERY_ID + " = m."+QUERY_ID + " "
			+ " JOIN " + User.TABLE + " AS u ON u."+ User.USER_ID + " = m."+USER_ID;
		
		getQueries += mysqlQuerySuffix;
		
		log.info("Get from query system: [" + getQueries + "]");
		
		JdbcTemplate queryDatabase = new JdbcTemplate(metadataDataSource);
		List<Query> queries = queryDatabase.query(getQueries, new QueryRowMapper());
		
		return queries;
	}
	
	private int modifyQuerySystem(String mysqlQuery)
	{
		log.info("Modify query system: [" + mysqlQuery + "]");
		
		JdbcTemplate metadataDatabase = new JdbcTemplate(metadataDataSource);
		return metadataDatabase.update(mysqlQuery);
	}
	
	private int addToQuerySystem(String mysqlAdd) throws SQLException
	{
		log.info("Add to query system: [" + mysqlAdd + "]"); 
		PreparedStatement addQueryStatement = metadataDataSource.getConnection().prepareStatement(mysqlAdd, Statement.RETURN_GENERATED_KEYS);  
		addQueryStatement.executeUpdate();
		
		ResultSet addedPrimaryKeyId = addQueryStatement.getGeneratedKeys();
		addedPrimaryKeyId.next();  
		int primaryKeyId = addedPrimaryKeyId.getInt(1);
		
		return primaryKeyId;
	}
	
	private void setMetadataVariable(int queryId, String variableName, String variableValue)
	{
		String changeVariable = "UPDATE " + METADATA_TABLE
				+ " SET " + variableName + " = " + (variableValue==null   ?null   :"'"+variableValue+"'")
				+ " WHERE " + QUERY_ID + " ='" + queryId + "'"
				+ " LIMIT 1";
		
		log.info("Set metadata variable: [" + changeVariable + "]");
		modifyQuerySystem(changeVariable);
	}
}
