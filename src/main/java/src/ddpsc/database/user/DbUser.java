package src.ddpsc.database.user;

import java.util.ArrayList;
import java.util.List;

import src.ddpsc.database.experiment.Experiment;
import src.ddpsc.exceptions.ExperimentNotAllowedException;

/**
 * Our User class. Used for storing user information from the database. Users may access the userarea and
 * are expected (but not required) to have a group. The user may then view different experiments belonging
 * to that group.
 * 
 * If the group does not exist, mark it as empty. There is a group in the database titled empty owned by admin.
 * 
 * @field String	username
 * @field String	password
 * @field boolean	enabled
 * @field String	authority Can only be ROLE_ADMIN and ROLE_USER
 * @field DbGroup	group Group object which this user belongs to.
 * @see {@link DbGroup }
 * 
 * @author shill, cjmcentee
 */
public class DbUser {
	
	private String	username;
	private String	password;
	private boolean enabled;
	private DbGroup group;
	private String	authority;
	private int		userId;
	
	private List<Experiment> allowedExperiments;
	private Experiment activeExperiment;

	public DbUser()
	{
		this.allowedExperiments = new ArrayList<Experiment>();
	}
	
	public DbUser(DbUser user)
	{
		this.allowedExperiments = user.allowedExperiments;
		this.activeExperiment	= user.activeExperiment;
		
		this.username 	= user.username;
		this.password 	= user.password;
		this.enabled 	= user.enabled;
		this.group		= user.group;
		this.authority 	= user.authority;
		this.userId		= user.userId;
		
	}

	/**
	 * @param String	username
	 * @param String	password
	 * @param boolean	enabled
	 * @param String	authority 	Can only be ROLE_ADMIN and ROLE_USER
	 * @param DbGroup	group 		Group object which this user belongs to.
	 * @see {@link DbGroup }
	 */
	public DbUser(String username, String password, boolean enabled, String authority, DbGroup group)
	{
		this.allowedExperiments = new ArrayList<Experiment>();
		
		this.username	= username;
		this.password 	= password;
		this.enabled 	= enabled;
		this.authority 	= authority;
	}

	public DbUser(String username)
	{
		this.username = username;
	}
	
	/**
	 * Returns true if and only if all fields are non-empty non-null, group may be null.
	 * @return boolean
	 */
	public boolean isComplete()
	{
		if (authority == null || username == null || username == "" || password == null)
			return false;
		
		else
			return true;
	}
	
	public boolean isInvalid()
	{
		return isComplete() || usernameInvalid(username) || authorityInvalid(authority); 
	}
	
	public static boolean usernameInvalid(String username)
	{
		return username == null || username.equals("");
	}
	
	public static boolean authorityInvalid(String authority)
	{
		return ! (authority == null || authority.equals("ROLE_USER") || authority.equals("ROLE_ADMIN"));
	}
	
	public String shortDescribe()
	{
		return username + " with ID " + userId;
	}
	
	public String describe()
	{
		StringBuilder userDescription = new StringBuilder();
		
		userDescription.append(username == null ? "<NO USERNAME>" : username);
		userDescription.append(" with ID " + userId);
		userDescription.append(enabled == true ? " is enabled " : " is disabled ");
		userDescription.append(password == null ? " and <NO SET PASSWORD> " : " with a valid password ");
		userDescription.append(" and authority:" + authority == null ? "<NO SET AUTHORITY>" : authority);
		userDescription.append(" in " + group == null ? " <NULL GROUP> " : group.describe());
		
		return userDescription.toString();
	}
	
	@Override
	public String toString() {
		return "DbUser [username=" + username
				+ ", password=" + password==null ? "NULL PASSWORD" : "valid password"
				+ ", enabled=" + enabled
				+ ", group=" + group
				+ ", authority=" + authority
				+ ", userId=" + userId + "]";
	}
	
	
	public Experiment getExperimentByExperimentName(String experimentName) throws ExperimentNotAllowedException
	{
		for (Experiment experiment : getAllowedExperiments()) {
			if (experiment.getExperimentName().equals(experimentName)) {
				return experiment;
			}
		}
		
		throw new ExperimentNotAllowedException("Experiment is not allowed or does not exist.");
	}
	
	/**
	 * Will probably be null unless you call setActiveExperiment first
	 * 
	 * @return
	 */
	public Experiment getActiveExperiment()
	{
		return activeExperiment;
	}

	public void setActiveExperiment(Experiment active) throws ExperimentNotAllowedException
	{
		if (allowedExperiments.contains(active)) {
			activeExperiment = active;
		} else {
			throw new ExperimentNotAllowedException("Experiment is not allowed or does not exist.");
		}
	}

	public List<Experiment> getAllowedExperiments()
	{
		if (allowedExperiments == null) {
			allowedExperiments = new ArrayList<Experiment>();
		}
		System.err.println("Warning: using a statically set experiment list");
		return allowedExperiments;
	}

	public void setAllowedExperiments(List<Experiment> experiments)
	{
		allowedExperiments = experiments;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public DbGroup getGroup()
	{
		return this.group;
	}

	public void setGroup(DbGroup group)
	{
		this.group = group;
	}

	public void setAuthority(String authority)
	{
		this.authority = authority;
	}

	public String getAuthority()
	{
		return this.authority;
	}

	public boolean getEnabled()
	{
		return this.enabled;
	}

	public void setUserId(int id)
	{
		this.userId = id;
	}

	public int getUserId()
	{
		return this.userId;
	}
}
