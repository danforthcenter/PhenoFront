package src.ddpsc.database.queries;

import java.util.ArrayList;
import java.util.List;

public class QueryFilter
{
	public String			experiment;
	
	public int				limit;
	
	public List<Integer>	excludedIds;
	
	public String			username;
	public boolean			onlyCommented;
	
	public QueryFilter(String experiment)
	{
		this.experiment = experiment;
		
		limit = -1;
		
		excludedIds = new ArrayList<Integer>();
		username = "";
		onlyCommented = false;
	}
	
	@Override
	public String toString()
	{
		return "Filter:"
				+ " Experiment: " + experiment
				+ ", Limit: " + limit
				+ ", Excluded Ids: " + excludedIds
				+ ", Only Username: " + username
				+ ", Only Commented: " + onlyCommented;
	}
}
