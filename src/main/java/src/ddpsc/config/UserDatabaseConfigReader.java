package src.ddpsc.config;

import org.apache.log4j.Logger;

import src.ddpsc.exceptions.MalformedConfigException;

public class UserDatabaseConfigReader extends ConfigReader
{
	private static Logger log = Logger.getLogger("service");
	
	public static final String CONFIG_FILENAME = "userdatabase.conf";
	
	public static final String DEFAULT_DATABASE = "PhenoFront";
	public static final String DEFAULT_PORT = "3306";
	
	public String username;
	public String password;
	public String url;
	public String port;
	public String database;
	
	
	public UserDatabaseConfigReader() throws MalformedConfigException
	{
		super(CONFIG_FILENAME);
		
		SetDefaults();
		
		if (username == null || password == null || url == null)
			throw new MalformedConfigException("Required fields are missing. Check the file '" + CONFIG_FILENAME + "' for completeness.");
		
		else {
			String configReadMessage = "Config file '" + CONFIG_FILENAME + "' complete, and processed without error.";
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
