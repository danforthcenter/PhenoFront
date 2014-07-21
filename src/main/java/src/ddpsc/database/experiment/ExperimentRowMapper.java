package src.ddpsc.database.experiment;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;


public class ExperimentRowMapper implements RowMapper<Experiment> {

	@Override
	public Experiment mapRow(ResultSet resultSet, int line) throws SQLException {
		try{
			ExperimentExtractor expExtractor = new ExperimentExtractor();
			return expExtractor.extractData(resultSet);
		}catch (SQLException e){
			throw e;
		}
	}

}

/**
 * Inner class responsible for mapping table columns to fields in our database.
 * If a new field is added, this must be updated.
 * 
 * 
 * @author shill
 *
 */
class ExperimentExtractor implements ResultSetExtractor<Experiment> {  
	public Experiment extractData(ResultSet resultSet) throws SQLException,
			DataAccessException {
		Experiment exp = new Experiment();		
		exp.setExperimentName(resultSet.getString(1));
		return exp;
	}	
}