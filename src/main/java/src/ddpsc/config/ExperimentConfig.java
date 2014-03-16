package src.ddpsc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * This class doesn't really do anything yet so we probably shouldn't use it.
 * 
 * Purpose of this class is to create an interface for configurating the experiment selection.
 * 
 * @author shill
 *
 */
@Configuration
public class ExperimentConfig extends DriverManagerDataSource{
	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() {
	    DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
	    driverManagerDataSource.setDriverClassName("org.postgresql.Driver");
	    driverManagerDataSource.setUrl("jdbc:postgresql://localhost:5432/LemnaTest"); // + X
	    driverManagerDataSource.setUsername("shill");
	    driverManagerDataSource.setPassword("");
	    return driverManagerDataSource;
	}
	
	/**
	 * Same builder as the previous, except appends the argument database to the host path (sets it as database).
	 * 
	 * Expects no preceeding '/'.
	 * 
	 * @param database
	 * @return
	 */
	public DriverManagerDataSource dataSource(String database) {
	    DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
	    driverManagerDataSource.setDriverClassName("org.postgresql.Driver");
	    driverManagerDataSource.setUrl("jdbc:postgresql://localhost:5432/" + database); // + X
	    driverManagerDataSource.setUsername("shill");
	    driverManagerDataSource.setPassword("");
	    return driverManagerDataSource;
	}

}
