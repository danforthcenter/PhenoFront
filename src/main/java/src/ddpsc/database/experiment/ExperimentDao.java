package src.ddpsc.database.experiment;

import java.util.ArrayList;

/**
 * Data abstraction object for Experiments. These are represented in the LemnaTec database as lt_dbs.
 * @author shill
 *
 */
public interface ExperimentDao {
	public ArrayList<Experiment> findAll();
	public Experiment findById(); //arguably useful

}
