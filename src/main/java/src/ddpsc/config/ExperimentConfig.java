package src.ddpsc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * LOAD CONFIG FROM CONFIG CONFIG CONFIG LOL
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
