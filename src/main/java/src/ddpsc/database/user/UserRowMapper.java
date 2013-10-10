package src.ddpsc.database.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<DbUser> {

	@Override
	public DbUser mapRow(ResultSet resultSet, int line) throws SQLException {
		try{
			UserExtractor userExtractor = new UserExtractor();
			return userExtractor.extractData(resultSet);
		}catch (SQLException e){
			throw e;
		}
	}

}
/**
 * Inner class responsible for mapping table columns to fields in our database.
 * If a new field is added, this must be updated.
 * 
 * @author shill
 *
 */
class UserExtractor implements ResultSetExtractor<DbUser> {  
	public DbUser extractData(ResultSet resultSet) throws SQLException,
			DataAccessException {
		DbUser dbUser = new DbUser();
		DbGroup group = new DbGroup();
		//us.username, "
		//		   + "us.password, "
		//		   + "us.enabled, "
		//		   + "us.group_id, "
		//		   + "us.authority, "
		//		   + "u.username AS owner, "
		//		   + "gr.group_name "
		dbUser.setUsername(resultSet.getString(1));
		dbUser.setPassword(resultSet.getString(2));
		dbUser.setEnabled(resultSet.getBoolean(3));
		group.setGroupId(resultSet.getInt(4));
		dbUser.setAuthority(resultSet.getString(5));
		group.setOwner(new DbUser(resultSet.getString(6)));
		group.setGroupName(resultSet.getString(7));
		dbUser.setUserId(resultSet.getInt(8));
		dbUser.setGroup(group);
		return dbUser;
	}	
}  