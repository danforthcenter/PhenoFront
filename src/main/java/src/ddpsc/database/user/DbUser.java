package src.ddpsc.database.user;

import java.util.HashSet;
import java.util.Set;

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
 * 
 * @see {@link DbGroup }
 * 
 * @author shill, cjmcentee
 */
public class DbUser
{
	public static final String ADMIN = "ROLE_ADMIN";
	public static final String USER  = "ROLE_USER";
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Fields
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	private String	username;
	private String	password;
	private boolean enabled;
	private DbGroup group;
	private String	authority;
	private int		userId;
	
	private Set<Experiment> allowedExperiments;
	private Experiment activeExperiment;
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Constructors
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	/**
	 * Creates an empty and incomplete DbUser object
	 */
	public DbUser()
	{
		this.allowedExperiments = new HashSet<Experiment>();
	}
	
	/**
	 * Clones the DbUser object
	 * 
	 * @param user
	 */
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
	 * 
	 * @see {@link DbGroup }
	 */
	public DbUser(String username, String password, boolean enabled, String authority, DbGroup group)
	{
		this.allowedExperiments = new HashSet<Experiment>();
		
		this.username	= username;
		this.password 	= password;
		this.enabled 	= enabled;
		this.authority 	= authority;
	}
	
	/**
	 * Creates an incomplete DbUser object
	 * 
	 * @param String	username
	 */
	public DbUser(String username)
	{
		this.allowedExperiments = new HashSet<Experiment>();
		
		this.username = username;
	}
	
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Authority Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	/**
	 * Determines whether this user has admin privileges.
	 * 
	 * @return			Whether this user is admin
	 */
	public boolean isAdmin()
	{
		return authority != null && authority.equals(ADMIN);
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Completeness Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	/**
	 * Returns true if and only if all fields are non-null, group may be null.
	 * 
	 * @return		Whether this object is incomplete
	 */
	public boolean isComplete()
	{
		if (authority == null || username == null || password == null)
			return false;
		
		else
			return true;
	}
	
	/**
	 * Determines whether this object has any invalid fields.
	 * 
	 * Invalidity is a more restrictive than completeness as it requires certain fields to have
	 * the right input on top of being non-null (e.g,. no empty usernames)
	 * 
	 * @return		Whether this object is invalid
	 */
	public boolean isInvalid()
	{
		return (! isComplete()) || usernameInvalid(username) || authorityInvalid(authority); 
	}
	
	/**
	 * Returns a message indicating how this user is invalid, if it is at all.
	 * 
	 * @return			A natural message indicating whether and how the user is invalid or incomplete
	 */
	public String InvalidityMessage()
	{
		if ( ! isInvalid()) // is valid
			return "User not invalid.";
		
		boolean addedToMessage = false;
		StringBuilder message = new StringBuilder("");
		if (authority == null) {
			message.append((addedToMessage ? "," : "") + " authority null");
			addedToMessage = true;
		}
		
		if (authorityInvalid(authority)) {
			message.append((addedToMessage ? "," : "") + " authority not correct, but " + authority);
			addedToMessage = true;
		}
		
		if (usernameInvalid(username)) {
			message.append((addedToMessage ? "," : "") + " authoity ('" + authority + "') invalid");
			addedToMessage = true;
		}
		
		if (username == null) {
			message.append((addedToMessage ? "," : "") + " username null");
			addedToMessage = true;
		}
		
		if (usernameInvalid(username)) {
			message.append((addedToMessage ? "," : "") + " username ('" + username + "') invalid");
			addedToMessage = true;
		}
		
		if (password == null) {
			message.append((addedToMessage ? "," : "") + " password null");
			addedToMessage = true;
		}
		
		return message.toString();
	}
	
	/**
	 * Determines username validity
	 * 
	 * Usernames are valid if they are non-null and non-empty.
	 * 
	 * @param username		The username to check for validity
	 * @return				Whether a username is invalid
	 */
	public static boolean usernameInvalid(String username)
	{
		return username == null || username.equals("");
	}
	
	/**
	 * Determines authority validity
	 * 
	 * Authorities are valid if they are either "ROLE_USER" or "ROLE_ADMIN". Anything else is invalid.
	 * 
	 * @param authority		The authority to check for validity
	 * @return				Whether an authority is invalid
	 */
	public static boolean authorityInvalid(String authority)
	{
		boolean validAuthority = authority != null && (authority.equals(USER) || authority.equals(ADMIN));
		return ! validAuthority;
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Logging / Debug Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	/**
	 * Returns a full description of most fields of this user in a natural language.
	 * 
	 * The description includes the username, ID, whether the user is enabled, whether
	 * the user has a password (but not the password itself), and what the user's authority is.
	 * 
	 * Does not include experiment information.
	 * 
	 * The whole description is on a single line of text.
	 * 
	 * @return				A near-full description of the user
	 */
	public String describe()
	{
		StringBuilder userDescription = new StringBuilder();
		
		userDescription.append(username == null ? "<NO USERNAME>" : username);
		userDescription.append(" with ID " + userId);
		userDescription.append(enabled == true ? " is enabled " : " is disabled ");
		userDescription.append(password == null ? " and <NO SET PASSWORD> " : " with a valid password");
		userDescription.append(" and authority:" + (authority == null ? "<NO SET AUTHORITY>" : authority));
		
		return userDescription.toString();
	}
	
	/**
	 * Returns a string representation of this user. This is not written in a natural language,
	 * just a comma-delimited mapping of values.
	 * 
	 * Includes: username, password existence (but not the password itself), enabled, group, authority, and ID.
	 * 
	 * The whole description is on a single line of text.
	 * 
	 */
	@Override
	public String toString() {
		return "DbUser [username=" + username
				+ ", password=" + password==null ? "NULL PASSWORD" : "valid password"
				+ ", enabled=" + enabled
				+ ", group=" + group
				+ ", authority=" + authority
				+ ", userId=" + userId + "]";
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Accessor / Assignment Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	/**
	 * Returns an experiment allowed by this user matching the supplied name.
	 * 
	 * If no experiment is found, throws {@link ExperimentNotAllowedException}.
	 * 
	 * @param		experimentName		Name of returned experiment
	 * @return							Experiment allowed by the user, matching the supplied name
	 * 
	 * @throws ExperimentNotAllowedException			Thrown if no matching experiment is found.
	 */
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
	 * Returns the active experiment.
	 * 
	 * If no experiment is currently active, returns null. Experiments have to be set active by calling
	 * {@link setActiveExperiment}
	 * 
	 * @return					The active experiment
	 */
	public Experiment getActiveExperiment()
	{
		return activeExperiment;
	}
	
	/**
	 * Sets the active experiment from the set of allowed experiments
	 * 
	 * If the active experiment is not an allowed experiment, throws {@link ExperimentNotAllowedException}
	 * 
	 * @param	activeExperiment			The new active experiment
	 * 
	 * @throws	ExperimentNotAllowedException		Thrown if the supplied experiment is not in the allowed set of experiments
	 * 
	 * @see setAllowedExperiments
	 */
	public void setActiveExperiment(Experiment activeExperiment) throws ExperimentNotAllowedException
	{
		if (allowedExperiments.contains(activeExperiment))
			this.activeExperiment = activeExperiment;
		else
			throw new ExperimentNotAllowedException("Experiment is not allowed or does not exist.");
		
	}
	
	/**
	 * Returns the set of allowed experiments
	 * 
	 * @return		The allowed experiments
	 */
	public Set<Experiment> getAllowedExperiments()
	{
		return allowedExperiments;
	}
	
	/**
	 * Sets the collection of allowed experiments the the supplied collection of experiments
	 * 
	 * If a null value is provided, it clears the allowed experiment collection of all values.
	 * 
	 * @param	experiments		The new allowed set of experiments
	 */
	public void setAllowedExperiments(Set<Experiment> experiments)
	{
		// Don't let a null value sneak into the system
		if (experiments == null)
			allowedExperiments = new HashSet<Experiment>();
		else
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
