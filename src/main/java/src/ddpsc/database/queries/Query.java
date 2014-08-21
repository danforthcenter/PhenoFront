package src.ddpsc.database.queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.gson.Gson;


public class Query
{
	private static final DateTimeFormatter javascriptTimePickerFormat = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
	private static final DateTimeFormatter javascriptTimeJSONFormat = DateTimeFormat.forPattern("MMM dd, yyyy hh:mm:ss aa");
	
	// CSV labels
	public static String QUERY_ID = "query id";
	
	public static String EXPERIMENT = "experiment";
	public static String BARCODE = "barcode regex";
	public static String MEASUREMENT = "measurement regex";
	public static String START_TIME = "start time";
	public static String END_TIME = "end time";
	
	public static String WATERING = "include watering";
	public static String VISIBLE = "include visible";
	public static String FLUORESCENT = "include fluorescent";
	public static String INFRARED = "include infrared";
	
	public static final String CSV_HEADER =
					 QUERY_ID 
			+ "," + EXPERIMENT 
			+ "," + BARCODE 
			+ "," + MEASUREMENT 
			+ "," + START_TIME 
			+ "," + END_TIME 
			+ "," + WATERING
			+ "," + VISIBLE
			+ "," + FLUORESCENT
			+ "," + INFRARED
			+ "," + QueryMetadata.CSV_HEADER;
	
	public int id;
	public final String experiment;
	
	public final String barcode;
	public final String measurementLabel;
	public final Timestamp startTime;
	public final Timestamp endTime;
	
	public final boolean includeWatering;
	public final boolean includeVisible;
	public final boolean includeFluorescent;
	public final boolean includeInfrared;
	
	public QueryMetadata metadata;
	
	public Query(
			String experiment,
			String barcode,
			String measurementLabel,
			String startTime,
			String endTime,
			boolean includeWatering,
			boolean includeVisible,
			boolean includeFluorescent,
			boolean includeInfrared)
	{
		this.experiment = experiment == null ? "" : experiment;
		
		this.barcode = barcode == null ? "" : barcode;
		this.measurementLabel = measurementLabel == null ? "" : measurementLabel;
		this.startTime = interpretDateString(startTime);
		this.endTime = interpretDateString(endTime);
		
		this.includeWatering = includeWatering;
		this.includeVisible = includeVisible;
		this.includeFluorescent = includeFluorescent;
		this.includeInfrared = includeInfrared;
	}
	
	public Query(
			String experiment,
			String barcode,
			String measurementLabel,
			Timestamp startTime,
			Timestamp endTime,
			boolean includeWatering,
			boolean includeVisible,
			boolean includeFluorescent,
			boolean includeInfrared)
	{
		this.experiment = experiment == null ? "" : experiment;
		
		this.barcode = barcode == null ? "" : barcode;
		this.measurementLabel = measurementLabel == null ? "" : measurementLabel;
		this.startTime = startTime;
		this.endTime = endTime;
		
		this.includeWatering = includeWatering;
		this.includeVisible = includeVisible;
		this.includeFluorescent = includeFluorescent;
		this.includeInfrared = includeInfrared;
		
		this.metadata = new QueryMetadata();
	}
	
	@Override
	public String toString()
	{
		String queryDescription =
			  "Experiment='" + experiment + "', "
			+ "Barcode='" + barcode + "', "
			+ "Measurement Label='" + measurementLabel + "', "
			+ "Start Time='" + startTime + "', "
			+ "End Time='" + endTime + "', "
			+ "Include Watering='" + includeWatering + "', "
			+ "Include Visible='" + includeVisible + "', "
			+ "Include Fluorescent='" + includeFluorescent + "', "
			+ "Include Infrared='" + includeInfrared + "'";
		
		return queryDescription;
	}
	
	public static String toJSON(List<Query> queries)
	{
		Gson gson = new Gson();
		String json = gson.toJson(queries);
		return json;
	}
	
	public static String toCSV(List<Query> queries)
	{
		StringBuilder csv = new StringBuilder(CSV_HEADER);
		
		for (Query query : queries)
			csv.append("\n" + query.toCSV(false));
		
		return csv.toString();
	}
	
	public String toCSV(boolean includeHeader)
	{
		String values = ""
					   + id
				+ "," + experiment
				+ "," + barcode
				+ "," + measurementLabel
				+ "," + (startTime != null	?startTime.toString()	:"null start")
				+ "," + (endTime != null	?endTime.toString()		:"null end")
				+ "," + includeWatering
				+ "," + includeVisible
				+ "," + includeFluorescent
				+ "," + includeInfrared
				+ "," + metadata.toCSV(false);
		
		if (includeHeader)
			return CSV_HEADER + "\n" + values;
		else
			return values;
	}
	
	public static Query fromResultSet(ResultSet sqlResult) throws SQLException
	{
		Query query = new Query(
				sqlResult.getString(QueryDaoImpl.EXPERIMENT),
				sqlResult.getString(QueryDaoImpl.BARCODE),
				sqlResult.getString(QueryDaoImpl.MEASUREMENT),
				sqlResult.getTimestamp(QueryDaoImpl.START_TIME),
				sqlResult.getTimestamp(QueryDaoImpl.END_TIME),
				sqlResult.getBoolean(QueryDaoImpl.WATERING),
				sqlResult.getBoolean(QueryDaoImpl.VISIBLE),
				sqlResult.getBoolean(QueryDaoImpl.FLUORESCENT),
				sqlResult.getBoolean(QueryDaoImpl.INFRARED) );
		
		query.id = sqlResult.getInt(QueryDaoImpl.QUERY_ID);
		query.metadata = QueryMetadata.fromResultSet(sqlResult);
		
		return query;
	}
	
	private Timestamp interpretDateString(String timeString)
	{
		if (timeString != null && ! timeString.equals("")) {
			// DateTimeFormatter doesn't have a ".isParsable" method, so we have to do it the nasty way
			try {
				DateTime timeDate = javascriptTimePickerFormat.parseDateTime(timeString);
				return new Timestamp(timeDate.getMillis());
			}
			
			catch (IllegalArgumentException e) { // Try the other format
				DateTime timeDate = javascriptTimeJSONFormat.parseDateTime(timeString);
				return new Timestamp(timeDate.getMillis());
			}
		}
		
		else
			return null;
	}
}
