package src.ddpsc.config;

import org.apache.log4j.Logger;

import src.ddpsc.exceptions.MalformedConfigException;

/**
 * Reads the values out of a configuration file located one folder above the class file folder.
 * 
 * The configuration file has to contain values for username, password, and URL or creating this
 * object throws a MalformedConfigException.
 * 
 * If port or database are left out of the configuration file, they are set to the defaults of
 * 3306 and PhenoFront respectively.
 * 
 * @see DEFAULT_DATABASE
 * @see DEFAULT_PORT
 * 
 * @author cjmcentee
 */
public class UserDatabaseConfigReader extends ConfigReader
{
	private static final Logger log = Logger.getLogger(UserDatabaseConfigReader.class);
	
	//public static final String CONFIG_FILENAME = "userdatabase.conf";
	
	public static final String DEFAULT_DATABASE = "PhenoFront";
	public static final String DEFAULT_PORT = "3306";
	
	public String username;
	public String password;
	public String url;
	public String port;
	public String database;
	
	/**
	 * Creates a new UserDatabaseConfigReader object that contains all the values defined
	 * in its associated configuration file.
	 * 
	 * @see CONFIG_FILENAME
	 * 
	 * @throws MalformedConfigException			Thrown when the config file is incomplete
	 */
	public UserDatabaseConfigReader(String filename) throws MalformedConfigException
	{
		super(filename);
		
		SetDefaults();
		
		if (username == null || password == null || url == null)
			throw new MalformedConfigException("Required fields are missing. Check the file '" + filename + "' for completeness.");
		
		else {
			String configReadMessage = "Config file '" + filename + "' complete, and processed without error.";
			log.trace(configReadMessage);
		}
	}
	
	protected void ProcessLine(String name, String value) 
	{
		if (name.equals("username"))
			this.username = value;
		
		else if (name.equals("password"))
			this.password = value;
		
		else if (name.equals("url"))
			this.url = value;
		
		else if(name.equals("database"))
			this.database = value;
		
		else if (name.equals("port"))
			this.port = value;
	}
	
	// Defaults set at parameter initialization come after the call to super() and therefore overwrite
	// anything set in super. So put them in their own method, checking for null.
	private void SetDefaults()
	{
		if (database == null)
			database = DEFAULT_DATABASE;
		if (port == null)
			port = DEFAULT_PORT;
	}
}
