package src.ddpsc.config;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import src.ddpsc.exceptions.MalformedConfigException;

/**
 * Config is a singleton class containing a series of factory methods for accessing important
 * configuration settings for the PhenoFront web service.
 * 
 * Currently implemented are:
 * 		Configuration for accessing the LemnaTech database
 * 		Configuration for accessing the database containing user login information (usernames, passwords, etc.)
 * 
 * @see LemnaTechDatabaseConfigReader
 * @see UserDatabaseConfigReader
 * 
 * @author shill, cjmcentee
 */
public class Config extends DriverManagerDataSource // Access to protected methods
{
	// Singleton class
	private Config()
	{
	}
	
	/**
	 * Returns a DataSource pointing to the default experiment defined by the LemnaTech database configuration file
	 * 
	 * @see LemnaTechDatabaseConfigReader
	 * 
	 * @return	DataSource object pointing to the default experiment database
	 * 
	 * @throws 	MalformedConfigException
	 */
	public static DriverManagerDataSource experimentDataSource() throws MalformedConfigException
	{
		LemnaTechDatabaseConfigReader config = new LemnaTechDatabaseConfigReader("ltdatabase.conf");
		
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		
		driverManagerDataSource.setDriverClassName("org.postgresql.Driver");
		driverManagerDataSource.setUrl("jdbc:postgresql://" + config.url + ":" + config.port + "/" + config.database);
		driverManagerDataSource.setUsername(config.username);
		driverManagerDataSource.setPassword(config.password);
		
		return driverManagerDataSource;
	}

	/**
	 * Returns a DataSource pointing to the supplied experiment on the server defined by the LemnaTech database configuration file
	 * 
	 * @see LemnaTechDatabaseConfigReader
	 * 
	 * @param	database	name of the database on the LemnaTech server to access
	 * @return	DataSource	object pointing to given database on the LemnaTech server
	 * 
	 * @throws 	MalformedConfigException
	 */
	public static DriverManagerDataSource experimentDataSource(String database) throws MalformedConfigException
	{
		database = database.replace("\\", "").replace("/", ""); // Removes any leading slashes
		LemnaTechDatabaseConfigReader config = new LemnaTechDatabaseConfigReader("ltdatabase.conf");
		
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		
		driverManagerDataSource.setDriverClassName("org.postgresql.Driver");
		driverManagerDataSource.setUrl("jdbc:postgresql://" + config.url + ":" + config.port + "/" + database);
		driverManagerDataSource.setUsername(config.username);
		driverManagerDataSource.setPassword(config.password);

		return driverManagerDataSource;
	}
	
	/**
	 * Returns a DataSource pointing to the user data database on the server defined by the user data database configuration file
	 * 
	 * @see UserDatabaseConfigReader
	 * 
	 * @return	DataSource object pointing to the database containing all the user data (usernames, password, etc)
	 * 
	 * @throws 	MalformedConfigException
	 */
	public static DriverManagerDataSource userDatabaseDataSource() throws MalformedConfigException
	{
		UserDatabaseConfigReader config = new UserDatabaseConfigReader("userdatabase.conf");
		
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		
		driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
		driverManagerDataSource.setUrl("jdbc:mysql://" + config.url + ":" + config.port + "/" + config.database);
		driverManagerDataSource.setUsername(config.username);
		driverManagerDataSource.setPassword(config.password);
		
		return driverManagerDataSource;
	}
}
