package src.ddpsc.utility;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


/**
 * This class maps a query containing a single string column into a list of strings.
 * 
 * @author cjmcentee
 */
public class StringRowMapper implements RowMapper<String> {

	@Override
	public String mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		String str = rs.getString(1); // only 1 row
		return str;
	}

}