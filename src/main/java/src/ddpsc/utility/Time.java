package src.ddpsc.utility;

import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Time
{
	public static final DateTimeFormatter javascriptTimePickerFormat = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
	public static final DateTimeFormatter javascriptTimeJSONFormat = DateTimeFormat.forPattern("MMM dd, yyyy hh:mm:ss aa");
	
	public static Timestamp now()
	{
		return new Timestamp(DateTime.now().getMillis());
	}
	
	public static Timestamp parseJSTimeStampForms(String timeString)
	{
		if (timeString != null && ! timeString.equals("")) {
			// DateTimeFormatter doesn't have a ".isParsable" method, so we have to do it the nasty way
			try {
				return parseJSTimePicker(timeString);
			}
			
			catch (IllegalArgumentException e) { // Try the other format
				return  parseJSONFormat(timeString);
			}
		}
		
		else
			return null;
	}
	
	public static Timestamp parseJSTimePicker(String timePickerFormattedString)
	{
		return new Timestamp(javascriptTimePickerFormat.parseDateTime(timePickerFormattedString).getMillis());
	}
	
	public static Timestamp parseJSONFormat(String jsonFormattedString)
	{
		return new Timestamp(javascriptTimeJSONFormat.parseDateTime(jsonFormattedString).getMillis());
	}
}
