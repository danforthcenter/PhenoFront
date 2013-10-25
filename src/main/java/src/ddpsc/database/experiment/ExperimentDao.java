package src.ddpsc.database.experiment;

import java.util.ArrayList;

/**
 * TODO: research how to go from experiment name => datasource
 * @author shill
 *
 */
public interface ExperimentDao {
	public ArrayList<Experiment> findAll();
	public Experiment findById(); //arguably useful

}
