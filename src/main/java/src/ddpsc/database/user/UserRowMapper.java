package src.ddpsc.database.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * Maps over each row in a returned query and transforms it into a {@link User} object.
 * 
 * The values in the result set hash map are tied to the query from {@link UserDao.USER_QUERY_VARIABLES}.
 * Reference that static variable for determining what string to use to access data from the result set.
 * 
 * @author shill, cjmcentee
 *
 */
public class UserRowMapper implements RowMapper<User>
{
	@Override
	public User mapRow(ResultSet resultSet, int line) throws SQLException
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
		
		User dbUser = new User();
		Group group = new Group();
		
		dbUser.setUsername(resultSet.getString(User.USERNAME));
		dbUser.setPassword(resultSet.getString(User.PASSWORD));
		dbUser.setEnabled(resultSet.getBoolean(User.ENABLED));
		dbUser.setAuthority(resultSet.getString(User.AUTHORITY));
		dbUser.setUserId(resultSet.getInt(User.USER_ID));
		
		group.setGroupId(resultSet.getInt(User.GROUP_ID));
		group.setOwner(new User(resultSet.getString("owner")));
		group.setGroupName(resultSet.getString("group_name"));
		
		dbUser.setGroup(group);
		
		return dbUser;
	}
}