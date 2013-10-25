package src.ddpsc.database.experiment;

/**
 * Class which holds the database Experiment object. Has associated Dao, DaoImpl, and RowMapper classes.
 * Users can access experiments, and the objects will be accessed mostly from that point.
 * 
 * This class should be used to load a list of experiments that exist within the lemnatec system. That is, it loads rows directly from
 * the LTSystem database. Once loaded, authenticated users should be connected to the correct database.
 * @author shill
 *
 */
public class Experiment {
	private int experimentId;
	private String experimentName;
	private String databaseName;
	
	public Experiment(){}

	public int getExperimentId() {
		return experimentId;
	}

	public void setExperimentId(int experimentId) {
		this.experimentId = experimentId;
	}

	public String getExperimentName() {
		return experimentName;
	}

	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	@Override
	public String toString() {
		return "Experiment [experimentId=" + experimentId
				+ ", experimentName=" + experimentName + ", databaseName="
				+ databaseName + "]";
	}
	
}
