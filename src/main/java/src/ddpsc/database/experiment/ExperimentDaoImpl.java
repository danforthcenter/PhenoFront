package src.ddpsc.database.experiment;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class ExperimentDaoImpl implements ExperimentDao{
	@Autowired 
	DataSource experimentSource;
	protected static Logger logger = Logger.getLogger("service");

	@Override
	public ArrayList<Experiment> findAll() {
		// TODO Fill in SQL 
		String sql = "SELECT lt.name, "
				   + "lt.id, "
				   + "u.user_id "
				   + "FROM ltdbs AS db ";

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
