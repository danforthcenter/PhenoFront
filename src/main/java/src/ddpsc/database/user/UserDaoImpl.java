package src.ddpsc.database.user;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import src.ddpsc.database.user.DbUser;
import src.ddpsc.database.user.UserRowMapper;
import src.ddpsc.exceptions.UserException;
import src.ddpsc.exceptions.UserNotFoundException;

@Component
public class UserDaoImpl implements UserDao{
	protected static Logger logger = Logger.getLogger("service");
	@Autowired 
	DataSource authSource;
	@Override
	public DbUser findByUsername(String username) {
		String sql = "SELECT us.username, "
				   + "us.password, "
				   + "us.enabled, "
				   + "us.group_id, "
				   + "us.authority, "
				   + "u.username AS owner, "
				   + "gr.group_name, "
				   + "u.user_id "
				   + "FROM users AS us, groups AS gr "
				   + "JOIN users AS u ON u.user_id = gr.owner_id "
				   + "WHERE us.username =  '" + username + "' "
				   + "AND gr.group_id = us.group_id";

		List<DbUser> userList = new ArrayList<DbUser>();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(authSource);
		userList = jdbcTemplate.query(sql, new UserRowMapper());
		return ((ArrayList<DbUser>) userList).get(0);
	}
	
	@Override
	public void changePassword(DbUser user){
		String sql = "UPDATE users "
				+ "SET password='" + user.getPassword() + "' "
				+ "WHERE user_id='" + user.getUserId() + "'";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(authSource);
		try{
			int result = jdbcTemplate.update(sql);
			logger.info("Result of query:" + result);

		}
		catch ( RuntimeException e ){
			logger.error(e.getMessage().toString());
			throw e;
		}
	}
	/**
	 * Do not call this method unless the user is an admin.
	 * Method returns a list of all users. Can be used for user management, such as changing, deleting, updating groups
	 * 
	 */
	@Override
	public ArrayList<DbUser> findAllUsers(){
		String sql = "SELECT us.username, "
				   + "us.password, "
				   + "us.enabled, "
				   + "us.group_id, "
				   + "us.authority, "
				   + "u.username AS owner, "
				   + "gr.group_name, "
				   + "us.user_id "
				   + "FROM users AS us, groups AS gr "
				   + "JOIN users AS u ON u.user_id = gr.owner_id "
				   + "WHERE gr.group_id = us.group_id";

		List<DbUser> userList = new ArrayList<DbUser>();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(authSource);
		userList = jdbcTemplate.query(sql, new UserRowMapper());
		return (ArrayList<DbUser>) userList;
	}

	@Override
	public void addUser(DbUser user) throws UserException {
		if(user.getGroup() == null){
			this.addUserNoGroup(user);
			return;
		}
		if (! user.isComplete()){
			throw new UserException("User not complete.");
		}
		String sql = "INSERT INTO users (username, password, enabled, group_id, authority) "
				+ "VALUES ('" + user.getUsername()
				+ "', '" + user.getPassword()
				+ "', '" + ((user.getEnabled()) ? 1 : 0) //boolean to int
				+ "', '" + user.getGroup().getGroupId()
				+ "', '" + user.getAuthority() +"')";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(authSource);
		try{
			int result = jdbcTemplate.update(sql);
			logger.info("Result of query:" + result);

		}
		catch ( RuntimeException e ){
			logger.error(e.getMessage().toString());
			throw e;
		}
	}
	
	/**
	 * Private method specifically for adding users without group. Groups should only be null if 
	 * maintenance is in process or a new group is being added and assigned to this user.
	 * @param user
	 * @throws UserException
	 */
	private void addUserNoGroup(DbUser user) throws UserException {
		if (! user.isComplete() ){
			throw new UserException("User not complete.");
		}
		String sql = "INSERT INTO users (username, password, enabled, authority) "
				+ "VALUES ('" + user.getUsername()
				+ "', '" + user.getPassword()
				+ "', '" + ((user.getEnabled()) ? 1 : 0) //boolean to int
				+ "', '" + user.getAuthority() +"')";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(authSource);
		try{
			int result = jdbcTemplate.update(sql);
			logger.info("Result of query:" + result);

		}
		catch ( RuntimeException e ){
			logger.error(e.getMessage().toString());
			throw e;
		}
	}

	@Override
	public void addGroup(DbGroup group) {
		
	}

	@Override
	public void changePassword(DbUser user, String newPassword) {
		
	}
	
	@Override
	public void updateUsername(DbUser user){
		String sql = "UPDATE users "
				+ "SET username='" + user.getUsername()+ "' "
				+ "WHERE user_id='" + user.getUserId() + "'";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(authSource);
		try{
			int result = jdbcTemplate.update(sql);
			logger.info("Result of query:" + result);

		}
		catch ( RuntimeException e ){
			logger.error(e.getMessage().toString());
			throw e;
		}
	
	}

	/**
	 * General purpose update method designed to update all fields at once. (Slower, but easier).
	 */
	@Override
	public void updateUser(DbUser user) {
		String sql = "UPDATE users "
				+ "SET user_id='" + user.getUserId() + "', "
				+  "username='" + user.getUsername() + "', "
				+  "password='" + user.getPassword() + "', "
				+  "authority='" + user.getAuthority() + "', "
				+  "enabled='" + ((user.getEnabled()) ? 1 : 0) + "', "
				+  "group_id='" + user.getGroup().getGroupId() + "' "
				+ "WHERE user_id='" + user.getUserId() + "'";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(authSource);
		try{
			int result = jdbcTemplate.update(sql);
			logger.info("Result of query:" + result);

		}
		catch ( RuntimeException e ){
			logger.error(e.getMessage().toString());
			throw e;
		}
	}

	@Override
	public void removeUser(DbUser user) {
		String sql = "DELETE FROM users "
				+ "WHERE user_id='" + user.getUserId() + "'";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(authSource);
		try{
			int result = jdbcTemplate.update(sql);
			logger.info("Result of query:" + result);

		}
		catch ( RuntimeException e ){
			logger.error(e.getMessage().toString());
			throw e;
		}
	}

	@Override
	public void removeGroup(DbGroup user) {
		// TODO: Auto-generated method stub
		
	}

	@Override
	public DbUser findByUserId(int userId) throws UserNotFoundException {
		String sql = "SELECT us.username, "
				   + "us.password, "
				   + "us.enabled, "
				   + "us.group_id, "
				   + "us.authority, "
				   + "u.username AS owner, "
				   + "gr.group_name, "
				   + "us.user_id "
				   + "FROM users AS us, groups AS gr "
				   + "JOIN users AS u ON u.user_id = gr.owner_id "
				   + "WHERE us.user_id =  '" + userId + "' "
				   + "AND gr.group_id = us.group_id";

		List<DbUser> userList = new ArrayList<DbUser>();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(authSource);
		userList = jdbcTemplate.query(sql, new UserRowMapper());
		if (userList.size() < 1){
			throw new UserNotFoundException();
		}
		return ((ArrayList<DbUser>) userList).get(0);
		
	}

	@Override
	public void updateAuthority(DbUser user) {
		String sql = "UPDATE users "
				+ "SET authority='" + user.getAuthority() + "' "
				+ "WHERE user_id='" + user.getUserId() + "'";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(authSource);
		try{
			int result = jdbcTemplate.update(sql);
			logger.info("Result of query:" + result);

		}
		catch ( RuntimeException e ){
			logger.error(e.getMessage().toString());
			throw e;
		}
	}
	
	
	@Override
	public ArrayList<DbGroup> findAllGroups() {
		String sql = "SELECT u.username AS owner, u.user_id AS owner_id, gr.group_name, gr.group_id "
					+ "FROM groups AS gr "
					+ "JOIN users AS u ON u.user_id = gr.owner_id ";
		List<DbGroup> groupList = new ArrayList<DbGroup>();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(authSource);
		groupList =  jdbcTemplate.query(sql, new GroupRowMapper());
		return (ArrayList<DbGroup>) groupList;
	}

	@Override
	public void updateGroup(DbUser user) {
		String sql = "UPDATE users "
				+ "SET group_id='" + user.getGroup().getGroupId() + "' "
				+ "WHERE user_id='" + user.getUserId() + "'";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(authSource);
		try{
			int result = jdbcTemplate.update(sql);
			logger.info("Result of query:" + result);

		}
		catch ( RuntimeException e ){
			logger.error(e.getMessage().toString());
			throw e;
		}
	}

}
