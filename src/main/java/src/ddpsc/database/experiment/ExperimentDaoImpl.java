package src.ddpsc.database.experiment;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Implements query to fetch experiment tables from database
 * 
 * @author shill
 */
public class ExperimentDaoImpl implements ExperimentDao{
	
	private static Logger log = Logger.getLogger("service");
	
	protected static String systemFilter = "System"; 
	
	
	@Autowired 
	DataSource experimentSource;
	
	
	@Override
	public ArrayList<Experiment> findAll() {
		String sql = "SELECT datname FROM pg_database " +
					 "WHERE datistemplate = false AND datname != 'postgres' AND datname !='bacula'";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(experimentSource);
		List<Experiment> experimentList = jdbcTemplate.query(sql, new ExperimentRowMapper());
		return (ArrayList<Experiment>) experimentList;
	}

	@Override
	public Experiment findById() {
		// TODO Fill in SQL
		return null;
	}
}
