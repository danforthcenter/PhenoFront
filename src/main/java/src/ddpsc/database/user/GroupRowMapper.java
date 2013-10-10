package src.ddpsc.database.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class GroupRowMapper implements RowMapper<DbGroup> {
	@Override
	public DbGroup mapRow(ResultSet resultSet, int line) throws SQLException {
		try{
			GroupExtractor groupExtractor = new GroupExtractor();
			return groupExtractor.extractData(resultSet);
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
class GroupExtractor implements ResultSetExtractor<DbGroup> {  
	public DbGroup extractData(ResultSet resultSet) throws SQLException,
			DataAccessException {
		DbUser owner = new DbUser();
		DbGroup group = new DbGroup();
		//owner owner_id group_name group_id
		owner.setUsername(resultSet.getString(1));
		owner.setUserId(resultSet.getInt(2));
		group.setGroupName(resultSet.getString(3));
		group.setGroupId(resultSet.getInt(4));
		group.setOwner(owner);
		return group;
	}	
}
