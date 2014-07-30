package src.ddpsc.database.snapshot;

import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class CustomQuerySettings
{
	private static final DateTimeFormatter formatter
		= DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
	
	public final String barcode;
	public final String measurementLabel;
	public final Timestamp startTime;
	public final Timestamp endTime;
	public final String[] restrictedUsers;
	public final boolean includeWatering;
	
	public CustomQuerySettings(
			String barcode,
			String measurementLabel,
			String startTime,
			String endTime,
			String restrictedUsers,
			boolean includeWatering)
	{
		this.barcode = barcode == null ? "" : barcode;
		this.measurementLabel = measurementLabel == null ? "" : measurementLabel;
		this.startTime = interpretDateString(startTime);
		this.endTime = interpretDateString(endTime);
		this.restrictedUsers = interpretCSV(restrictedUsers);
		this.includeWatering = includeWatering;
	}
	
	@Override
	public String toString()
	{
		String queryDescription =
			"Barcode='" + barcode + "'" +
			"Measurement Label='" + measurementLabel + "'" +
			"Start Time='" + startTime + "'" +
			"End Time='" + endTime + "'" +
			"Include Watering='" + includeWatering + "'" +
			"Restricted Users=[";
		
		for (int i = 0; i < restrictedUsers.length; i++) {
			String user = restrictedUsers[i];
			
			if (i != 0)
				queryDescription += ", ";
			queryDescription += user;
		}
		queryDescription += "]";
		
		return queryDescription;
	}
	
	
	private Timestamp interpretDateString(String timeString)
	{
		if (timeString != null && ! timeString.equals("")) {
			DateTime timeDate = formatter.parseDateTime(timeString);
			return new Timestamp(timeDate.getMillis());
		}
		else
			return null;
	}
	
	private String[] interpretCSV(String usersCSV)
	{
		String[] usernames = usersCSV.split(",");
		
		String[] usernames_noWhitespace = new String[usernames.length];
		for (int i = 0; i < usernames.length; i++) {
			String username = usernames[i];
			usernames_noWhitespace[i] = username.replaceAll("\\s+","");
		}
		
		return usernames_noWhitespace;
	}
}
