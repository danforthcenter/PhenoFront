package src.ddpsc.database.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * Maps over each row in a returned query and transforms it into a {@link DbUser} object.
 * 
 * The values in the result set hash map are tied to the query from {@link UserDao.USER_QUERY_VARIABLES}.
 * Reference that static variable for determining what string to use to access data from the result set.
 * 
 * @author shill, cjmcentee
 *
 */
public class UserRowMapper implements RowMapper<DbUser>
{
	@Override
	public DbUser mapRow(ResultSet resultSet, int line) throws SQLException
	{
		// The order and names of queried variables 
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
		
		dbUser.setUsername(resultSet.getString("username"));
		dbUser.setPassword(resultSet.getString("password"));
		dbUser.setEnabled(resultSet.getBoolean("enabled"));
		dbUser.setAuthority(resultSet.getString("authority"));
		dbUser.setUserId(resultSet.getInt("user_id"));
		
		group.setGroupId(resultSet.getInt("group_id"));
		group.setOwner(new DbUser(resultSet.getString("owner")));
		group.setGroupName(resultSet.getString("group_name"));
		
		dbUser.setGroup(group);
		
		return dbUser;
	}
}