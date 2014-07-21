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
 *
 */
public class ExperimentDaoImpl implements ExperimentDao{
	@Autowired 
	DataSource experimentSource;
	protected static Logger logger = Logger.getLogger("service");
	protected static String systemFilter = "System"; 

	@Override
	public ArrayList<Experiment> findAll() {
		String sql = "SELECT name from ltdbs where removed = FALSE";

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
