package src.ddpsc.database.queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import src.ddpsc.database.user.User;

public class QueryMetadata
{
	// CSV labels
	public static String METADATA_ID	= "metadata id";
	public static String USER_ID		= "user id";
	
	public static String COMMENT		= "comment";
	
	public static String DATE_MADE		= "date made";
	public static String DOWNLOAD_BEGIN	= "date download begin";
	public static String DOWNLOAD_END	= "date download complete";
	public static String INTERRUPTED	= "interrupted";
	
	public static String SIZE			= "bytes";
	public static String NUM_SNAPSHOTS	= "number snapshots";
	public static String NUM_TILES		= "number tiles";
	
	public static final String CSV_HEADER =
					USER_ID
			+ "," + User.USERNAME
			+ "," + DATE_MADE
			+ "," + DOWNLOAD_BEGIN
			+ "," + DOWNLOAD_END
			+ "," + INTERRUPTED
			+ "," + SIZE
			+ "," + NUM_SNAPSHOTS
			+ "," + NUM_TILES;
	
	public final int userId;
	public final String username;
	public final Timestamp dateMade;
	public final int numberSnapshots;
	public final int numberTiles;
	
	public String comment;
	
	public Timestamp downloadBegin;
	public Timestamp downloadEnd;
	public boolean interrupted;
	public long bytes;
	
	QueryMetadata()
	{
		this.userId = 0;
		this.username = null;
		this.dateMade = null;
		
		this.numberSnapshots = 0;
		this.numberTiles = 0;
		
		this.comment = null;
	}
	
	public QueryMetadata(
			int userId,
			String username,
			Timestamp dateMade,
			int numberSnapshots,
			int numberTiles,
			String comment )
	{
		this.userId = userId;
		this.username = username;
		this.dateMade = dateMade;
		
		this.numberSnapshots = numberSnapshots;
		this.numberTiles = numberTiles;
		
		this.comment = comment;
	}
	
	public String toCSV(boolean includeHeader)
	{
		String values = ""
					   + userId
				+ "," + username
				+ "," + (dateMade != null		?dateMade.toString()		:"null date")
				+ "," + (downloadBegin != null	?downloadBegin.toString()	:"null begin")
				+ "," + (downloadEnd != null	?downloadEnd.toString()		:"null end")
				+ "," + interrupted
				+ "," + bytes
				+ "," + numberSnapshots
				+ "," + numberTiles;
		
		if (includeHeader)
			return CSV_HEADER + "\n" + values;
		else
			return values;
	}
	
	public static QueryMetadata fromResultSet(ResultSet sqlResult) throws SQLException
	{
		QueryMetadata metadata = new QueryMetadata(
				sqlResult.getInt(QueryDaoImpl.METADATA_ID),
				sqlResult.getString(User.USERNAME),
				sqlResult.getTimestamp("date"),
				sqlResult.getInt(QueryDaoImpl.NUM_SNAPSHOTS),
				sqlResult.getInt(QueryDaoImpl.NUM_TILES),
				sqlResult.getString(QueryDaoImpl.COMMENT));
		
		metadata.downloadBegin = sqlResult.getTimestamp(QueryDaoImpl.DOWNLOAD_BEGIN);
		metadata.downloadEnd = sqlResult.getTimestamp(QueryDaoImpl.DOWNLOAD_END);
		metadata.interrupted = sqlResult.getBoolean(QueryDaoImpl.INTERRUPTED);
		metadata.bytes = sqlResult.getLong(QueryDaoImpl.SIZE);
		
		return metadata;
	}
}
