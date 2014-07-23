package src.ddpsc.database.experiment;

import java.util.Collection;

/**
 * Class which holds the database Experiment object.
 * 
 * Has associated Dao, DaoImpl, and RowMapper classes. Users can access experiments, and the 
 * objects will be accessed mostly from that point.
 * 
 * This class should be used to load a list of experiments that exist within the LemnaTec system.
 * That is, it loads rows directly from the LTSystem database. Once loaded, authenticated users
 * should be connected to the correct database.
 * 
 * @author shill, cjmcentee
 */
public class Experiment
{
	private String experimentName;
	private String databaseName;
	
	public Experiment()
	{
	}
	
	/**
	 * Static method turns a list of experiments into a string describing each one.
	 */
	public static String toString(Collection<Experiment> experiments)
	{
		StringBuilder itemizedList = new StringBuilder();
		boolean notFirst = false;
		for (Experiment experiment : experiments) {

			if (notFirst)
				itemizedList.append(", ");

			itemizedList.append(experiment.toString());
		}

		return itemizedList.toString();
	}
	
	@Override
	public String toString()
	{
		return "Experiment [experimentName=" + experimentName + ", databaseName=" + databaseName + "]";
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof Experiment)
			return equals((Experiment) obj);
		else
			return false;
	}
	
	public boolean equals(Experiment experiment)
	{
		return this.experimentName.equals(experiment.experimentName);
	}
	
	@Override
	public int hashCode()
	{
		return (experimentName == null ? 0 : experimentName.hashCode())
			 + (databaseName == null   ? 0 : databaseName.hashCode() * 7);
	}
	
	public String getExperimentName()
	{
		return experimentName;
	}
	
	public void setExperimentName(String experimentName)
	{
		this.experimentName = experimentName;
	}
	
	public String getDatabaseName()
	{
		return databaseName;
	}
	
	public void setDatabaseName(String databaseName)
	{
		this.databaseName = databaseName;
	}
}
