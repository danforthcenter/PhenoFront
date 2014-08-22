package src.ddpsc.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringOps
{
	public static String idsAsCSV(List<Integer> ids, boolean quoteEachElement)
	{
		if (ids == null)
			return "";
		
		StringBuilder csv = new StringBuilder("");
		
		for (int i = 0; i < ids.size(); i++) {
			int id = ids.get(i);
			if (i != 0)
				csv.append(", ");
			if (quoteEachElement)
				csv.append("'" + id + "'");
			else
				csv.append(id);
		}
		
		return csv.toString();
	}
	
	public static String idsAsCSV(List<Integer> ids)
	{
		return idsAsCSV(ids, true);
	}
	
	public static List<Integer> CSVAsIds(String csv)
	{
		if (csv == null || csv.equals(""))
			return new ArrayList<Integer>();
		
		String[] ids_string = csv.split(",");
		List<Integer> ids = new ArrayList<Integer>();
		for (String id_string : ids_string)
			ids.add(Integer.parseInt(id_string.trim()));
		
		return ids;
	}
}
