package src.ddpsc.database.experiment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class which holds the database Experiment object.
 * 
 * This class should be used to load a list of experiments that exist within the LemnaTec system.
 * That is, it loads rows directly from the LTSystem database. Once loaded, authenticated users
 * should be connected to the correct database.
 * 
 * @see ExperimentDaoImpl
 * 
 * @author shill, cjmcentee
 */
public class Experiment
{
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Fields
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public final int id;
	public final String name;
	
	public int numberSnapshots = -1;
	public int numberTiles = -1;
	public Timestamp lastUpdated;
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Constructor
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public Experiment(int experimentId, String experimentName)
	{
		id = experimentId;
		name = experimentName;
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Static List Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public static Map<Integer, Experiment> getExperimentIdMap(Collection<Experiment> experiments)
	{
		Map<Integer, Experiment> experimentsMap = new HashMap<Integer, Experiment>();
		for (Experiment experiment : experiments)
			experimentsMap.put(experiment.id, experiment);
		
		return experimentsMap;
	}
	
	public static List<Integer> getIds(Collection<Experiment> experiments)
	{
		List<Integer> ids = new ArrayList<Integer>();
		for (Experiment experiment : experiments)
			ids.add(experiment.id);
		
		return ids;
	}
	
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
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Object Overrides
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	@Override
	public String toString()
	{
		return name;
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
		return name.equals(experiment.name);
	}
	
	@Override
	public int hashCode()
	{
		return name.hashCode();
	}

	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Get / Set Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public int getNumberSnapshots()
	{
		return numberSnapshots;
	}


	public void setNumberSnapshots(int numberSnapshots)
	{
		this.numberSnapshots = numberSnapshots;
	}


	public int getNumberTiles()
	{
		return numberTiles;
	}


	public void setNumberTiles(int numberTiles)
	{
		this.numberTiles = numberTiles;
	}


	public Timestamp getLastUpdated()
	{
		return lastUpdated;
	}


	public void setLastUpdated(Timestamp lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}


	public int getId()
	{
		return id;
	}


	public String getName()
	{
		return name;
	}
}
