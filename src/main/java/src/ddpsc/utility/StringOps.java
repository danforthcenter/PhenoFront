package src.ddpsc.utility;

import java.util.List;

public class StringOps
{
	public static String idsAsCSV(List<Integer> ids)
	{
		StringBuilder csv = new StringBuilder("");
		
		for (int i = 0; i < ids.size(); i++) {
			int id = ids.get(i);
			if (i != 0)
				csv.append(", ");
			csv.append("'" + id + "'");
		}
		
		return csv.toString();
	}
}
