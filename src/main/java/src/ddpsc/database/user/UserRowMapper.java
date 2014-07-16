package src.ddpsc.database.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<DbUser> {
	
	@Override
	public DbUser mapRow(ResultSet resultSet, int line) throws SQLException {
		
		UserExtractor userExtractor = new UserExtractor();
		return userExtractor.extractData(resultSet);
	}
}

/**
* Private class responsible for mapping table columns to fields in our database.
* If a new field is added, this must be updated.
*
* @author shill
*
*/
class UserExtractor implements ResultSetExtractor<DbUser> {
	
	private static Logger log = Logger.getLogger("service");
	
	public DbUser extractData(ResultSet resultSet) throws SQLException {
		
		// The order of returned objects from the SQL query 
		//		username
		//		password
		//		enabled 
		//		group_id
		//		authority
		//		owner
		//		group_name
		//		user_id
		
		DbUser dbUser = new DbUser();
		DbGroup group = new DbGroup();
		
		dbUser.setUsername(resultSet.getString(1));
		dbUser.setPassword(resultSet.getString(2));
		dbUser.setEnabled(resultSet.getBoolean(3));
		dbUser.setAuthority(resultSet.getString(5));
		dbUser.setUserId(resultSet.getInt(8));
		
		group.setGroupId(resultSet.getInt(4));
		group.setOwner(new DbUser(resultSet.getString(6)));
		group.setGroupName(resultSet.getString(7));
		
		dbUser.setGroup(group);
		
		if ( ! resultSet.isLast())
			log.warn("(UserRowMapper.mapRow()): MySQL database containing user credentials not returning unique users.");
		
		return dbUser;
	}
}