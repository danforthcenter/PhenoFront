package src.ddpsc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import src.ddpsc.exceptions.MalformedConfigException;

/**
 * This class wraps the DBConfig flat file by reading the information and loading it into a Java Bean.
 * The only configuration handled here is the LTDatabase Configuration. 
 * 
 * Makes the assumption (true assumption) that the LTDatabase is postgres database operating on port
 * 5432.
 * 
 * @author shill
 *
 */
@Configuration
public class ExperimentConfig extends DriverManagerDataSource{
	
	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() throws MalformedConfigException {
		DBConfigReader conf = new DBConfigReader();
	    DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
	    driverManagerDataSource.setDriverClassName("org.postgresql.Driver");
	    driverManagerDataSource.setUrl("jdbc:postgresql://"+ conf.dbURL +":5432/" + conf.dbDefault); // + X
	    driverManagerDataSource.setUsername(conf.dbUsername);
	    driverManagerDataSource.setPassword(conf.dbPass);
	    return driverManagerDataSource;
	}
	
	/**
	 * Same builder as the previous, except appends the argument database to the host path (sets it as database).
	 * 
	 * Expects no preceeding '/'.
	 * 
	 * @param database
	 * @return
	 * @throws MalformedConfigException 
	 */
	public DriverManagerDataSource dataSource(String database) throws MalformedConfigException {
		DBConfigReader conf = new DBConfigReader();
	    DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
	    //database = "LemnaTest";
	    driverManagerDataSource.setDriverClassName("org.postgresql.Driver");
	    driverManagerDataSource.setUrl("jdbc:postgresql://"+ conf.dbURL +":5432/" + database); // + X
	    driverManagerDataSource.setUsername(conf.dbUsername);
	    driverManagerDataSource.setPassword(conf.dbPass);
	    return driverManagerDataSource;
	}

}
