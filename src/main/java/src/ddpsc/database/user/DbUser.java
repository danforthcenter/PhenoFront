package src.ddpsc.database.user;

import java.util.ArrayList;

import src.ddpsc.database.experiment.Experiment;
import src.ddpsc.exceptions.ExperimentNotAllowedException;

/**
 * Our User class. Used for storing user information from the database. Users may access the userarea and
 * are expected (but not required) to have a group. The user may then view different experiments belonging
 * to that group.
 * 
 * If the group does not exist, mark it as empty. There is a group in the database titled empty owned by admin.
 * 
 * @field String
 *            username
 * @field String
 *            password
 * @field boolean enabled
 * @field String
 *            authority Can only be ROLE_ADMIN and ROLE_USER
 * @field DbGroup
 *            group Group object which this user belongs to.
 * @see {@link DbGroup }
 * @author shill
 */
public class DbUser {
	private String username;
	private String password;
	private boolean enabled;
	private DbGroup group;
	private String authority;
	private int userId;
	private ArrayList<Experiment> allowedExperiments;
	private Experiment activeExperiment;
	private static Experiment TEST_EXPERIMENT;

	public DbUser() {
		TEST_EXPERIMENT = new Experiment();
		TEST_EXPERIMENT.setDatabaseName("Phenotyping1");
		TEST_EXPERIMENT.setExperimentId(1); 
		TEST_EXPERIMENT.setExperimentName("Phenotyping1");
	}

	/**
	 * @param String
	 *            username
	 * @param String
	 *            password
	 * @param boolean enabled
	 * @param String
	 *            authority Can only be ROLE_ADMIN and ROLE_USER
	 * @param DbGroup
	 *            group Group object which this user belongs to.
	 * @see {@link DbGroup }
	 */
	public DbUser(String username, String password, boolean enabled,
			String authority, DbGroup group) {
		super();
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.authority = authority;
	}

	public DbUser(String username) {
		super();
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public DbGroup getGroup() {
		return this.group;
	}

	public void setGroup(DbGroup group) {
		this.group = group;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getAuthority() {
		return this.authority;
	}

	public boolean getEnabled() {
		return this.enabled;
	}
	
	/**
	 * Returns true if and only if all fields are non-empty non-null, group may be null.
	 * @return boolean
	 */
	public boolean isComplete(){
		if (this.authority == null || this.username == null || this.password == null){
			return false;
		}
		else return true;
	}

	public void setUserId(int id) {
		this.userId = id;
	}
	
	public int getUserId(){
		return this.userId;
	}

	@Override
	public String toString() {
		return "DbUser [username=" + username + ", password=" + password
				+ ", enabled=" + enabled + ", group=" + group + ", authority="
				+ authority + ", userId=" + userId + "]";
	}
	
	public ArrayList<Experiment> getAllowedExperiments() {
		// TODO PLEASE ACTUALLY SETUP THE EXPERIMENTS CLASSES
		if (this.allowedExperiments == null){
			this.allowedExperiments = new ArrayList<Experiment>();
			this.allowedExperiments.add(TEST_EXPERIMENT);
			
		}
		System.err.println("Warning: using a statically set experiment list: " + TEST_EXPERIMENT );
		return this.allowedExperiments;
	}
	public void setActiveExperiment(Experiment active) throws ExperimentNotAllowedException{
		if (this.allowedExperiments.contains(active)){
			this.activeExperiment = active;
		} else{
			throw new ExperimentNotAllowedException("Experiment is not allowed or does not exist.");
		}
	}
	/**
	 * Will probably be null unless you call setActiveExperiment first
	 * 10-17-13 -- Experiments unimplemented
	 * @return
	 */
	public Experiment getActiveExperiment(){
		return this.activeExperiment;
	}

	public void setAllowedExperiments(ArrayList<Experiment> experiments){
		this.allowedExperiments = experiments;
	}
	
	public Experiment getExperimentByExperimentName(String experimentName) throws ExperimentNotAllowedException{
		for (Experiment experiment : this.getAllowedExperiments()) {
			if (experiment.getExperimentName().equals(experimentName)){
				return experiment;
			}
		}
		throw new ExperimentNotAllowedException("Experiment is not allowed or does not exist.");
	}

	

}
