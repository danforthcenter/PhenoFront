package src.ddpsc.database.queries;

import java.sql.Timestamp;
import java.util.List;

import org.joda.time.DateTime;

import src.ddpsc.utility.StringOps;
import src.ddpsc.utility.Time;

import com.google.gson.Gson;


public class Query
{
	// These variable names are encoded into the JSON that is used to communicate with the
	// client. If you change any of these variable names, the JavaScript client will also
	// have to have its corresponding variable names modified.
	// Particularly queryElement.js
	// This may seem cruel and unusual, but GSON is too easy to not use.
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
	
	public Query(String experiment, String barcode, String measurementLabel,
				String startTime, String endTime,
				boolean includeWatering, boolean includeVisible, boolean includeFluorescent, boolean includeInfrared)
	{
		this(experiment, barcode, measurementLabel,
			Time.parseJSTimeStampForms(startTime), Time.parseJSTimeStampForms(endTime),
			includeWatering, includeVisible, includeFluorescent, includeInfrared);
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
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Get / Set Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public QueryMetadata getMetadata()
	{
		return metadata;
	}

	public void setMetadata(QueryMetadata metadata)
	{
		this.metadata = metadata;
	}

	public String getExperiment()
	{
		return experiment;
	}

	public String getBarcode()
	{
		return barcode;
	}

	public String getMeasurementLabel()
	{
		return measurementLabel;
	}

	public Timestamp getStartTime()
	{
		return startTime;
	}

	public Timestamp getEndTime()
	{
		return endTime;
	}

	public boolean isIncludeWatering()
	{
		return includeWatering;
	}

	public boolean isIncludeVisible()
	{
		return includeVisible;
	}

	public boolean isIncludeFluorescent()
	{
		return includeFluorescent;
	}

	public boolean isIncludeInfrared()
	{
		return includeInfrared;
	}
}
