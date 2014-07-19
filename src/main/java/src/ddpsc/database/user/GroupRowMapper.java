package src.ddpsc.database.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * Maps over each row in a returned query and transforms it into a {@link DbGroup} object.
 * 
 * The values in the result set hash map are tied to the query from {@link UserDao.GROUP_QUERY_VARIABLES}.
 * Reference that static variable for determining what string to use to access data from the result set.
 * 
 * @author shill, cjmcentee
 *
 */
public class GroupRowMapper implements RowMapper<DbGroup>
{
	@Override
	public DbGroup mapRow(ResultSet resultSet, int line) throws SQLException
	{
		// The order and name of the queried variables
		// 		owner
		//		owner_id
		//		group_name
		//		group_id
		
		DbUser owner = new DbUser();
		DbGroup group = new DbGroup();
		
		owner.setUsername(resultSet.getString("owner"));
		owner.setUserId(resultSet.getInt("owner_id"));
		
		group.setGroupName(resultSet.getString("group_name"));
		group.setGroupId(resultSet.getInt("group_id"));
		
		group.setOwner(owner);
		return group;
	}
}
