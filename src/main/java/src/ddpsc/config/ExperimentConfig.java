package src.ddpsc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import src.ddpsc.exceptions.MalformedConfigException;

/**
 * This class wraps the DBConfig flat file by reading the information and loading it into a Java Bean.
 * The only configuration handled here is the LTDatabase Configuration used by different experiments.
 * 
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
	    driverManagerDataSource.setDriverClassName(conf.properties.get("pg.driverClassName"));
	    driverManagerDataSource.setUrl(conf.properties.get("pg.url"));
	    driverManagerDataSource.setUsername(conf.properties.get("pg.username"));
	    driverManagerDataSource.setPassword(conf.properties.get("pg.password"));
	    return driverManagerDataSource;
	}
	/**
	 * Same builder as the previous, except appends the argument database to the host path (sets it as database).
	 * 
	 * Expects no preceeding '/'
	 * Expects the pg.url property to contain no database
	 * 
	 * @param database
	 * @return
	 * @throws MalformedConfigException 
	 */
	public DriverManagerDataSource dataSource(String database) throws MalformedConfigException {
	    DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		DBConfigReader conf = new DBConfigReader();
	    driverManagerDataSource.setDriverClassName(conf.properties.get("pg.driverClassName"));
	    driverManagerDataSource.setUrl(conf.properties.get("pg.url") + "/" + database);
	    driverManagerDataSource.setUsername(conf.properties.get("pg.username"));
	    driverManagerDataSource.setPassword(conf.properties.get("pg.password"));
	    return driverManagerDataSource;
	}

}
