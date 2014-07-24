package src.ddpsc.database.user;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;

import src.ddpsc.database.user.DbUser;
import src.ddpsc.database.user.UserRowMapper;
import src.ddpsc.exceptions.ObjectNotFoundException;
import src.ddpsc.exceptions.UserException;

/**
 * UserDaoImpl is a general, functionally stateless manager for accessing and modifying 
 * user data from the appropriate database (as determined by how userDataSource is linked as a bean).
 * 
 * UserDaoImpl is designed to work with the Spring framework.
 * 
 * Implements the interface {@link UserDao}.
 * 
 * @see CustomAuthenticationManager
 * @see UserRowMapper
 * @see UserExtractor
 * @see GroupRowMapper
 * 
 * @author shill, cjmcentee
 *
 */
public class UserDaoImpl implements UserDao
{
	private static final Logger log = Logger.getLogger(UserDaoImpl.class);
	
	@Autowired
	private DataSource userDataSource; // Should we make this static and transition this class into being a singleton?
									   // What is gained by having instances of this class?
	
	private static final String USER_QUERY_VARIABLES = "SELECT "
			+ "us.username, "
			+ "us.password, "
			+ "us.enabled, "
			+ "us.group_id, "
			+ "us.authority, "
			+ "u.username AS owner, "
			+ "gr.group_name, "
			+ "us.user_id "
			+ "FROM users AS us, groups AS gr ";
	
	private static final String GROUP_QUERY_VARIABLES = "SELECT "
			+ "u.username AS owner, "
			+ "u.user_id AS owner_id, "
			+ "gr.group_name, "
			+ "gr.group_id ";
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// User Operations
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	/**
	 * Returns a DbUser from the user database that matches the supplied name
	 * 
	 * @param		username		The name of the returned user
	 * @return						The user with the name matching the supplied username
	 * 
	 * @throws		CannotGetJdbcConnectionException	Thrown if the database isn't available to be queried
	 * @throws 		UserException						Thrown if the retrieved user returns incomplete data
	 * @throws		ObjectNotFoundException				Thrown if the username is not in the database 
	 */
	@Override
	public DbUser findByUsername(String username) 
			throws ObjectNotFoundException, CannotGetJdbcConnectionException, UserException
	{
		log.info("Attempting to find user with name " + username);
		
		String getUser = USER_QUERY_VARIABLES
				+ "JOIN users AS u ON u.user_id = gr.owner_id "
				+ "WHERE us.username =  '" + username + "' "
				+ "AND gr.group_id = us.group_id";
		
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(userDataSource);
		List<DbUser> users = jdbcTemplate.query(getUser, new UserRowMapper());
		
		
		if (users.size() == 0)
			throw new ObjectNotFoundException("User with name '" + username + "' not found.");
		
		if (users.size() > 1)
		{
			String multiplicityMessage = "More than one user matches the name '" + username + "':";
			for (int i = 0; i < users.size(); i++) {
				multiplicityMessage += users.get(i).getUsername();
				if (i != users.size() - 1)
					multiplicityMessage += ", ";
			}
			log.error(multiplicityMessage);
		}
		
		DbUser user = users.get(0);
		
		ensureValidity(user); // Throws UserException
		
		log.info("User with username "+ username + " found.");
		
		return user;
	}
	
	
	/**
	 * Returns a DbUser from the user database that matches the supplied user ID
	 * 
	 * @param		userID			The ID of the returned user
	 * @return						The user with the name matching the supplied ID
	 * 
	 * @throws		ObjectNotFoundException				Thrown if the ID is not in the database
	 * @throws		CannotGetJdbcConnectionException	Thrown if the database isn't available to be queried
	 * @throws 		UserException						Thrown if the retrieved user returns incomplete data 
	 */
	@Override
	public DbUser findByID(int userID)
			throws ObjectNotFoundException, CannotGetJdbcConnectionException, UserException
	{
		log.info("Attempting to find user with ID='" + userID +"'");
		
		String getUser = USER_QUERY_VARIABLES
				+ "JOIN users AS u ON u.user_id = gr.owner_id "
				+ "WHERE us.user_id =  '" + userID + "' "
				+ "AND gr.group_id = us.group_id";
		
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(userDataSource);
		List<DbUser> users = jdbcTemplate.query(getUser, new UserRowMapper());
		
		if (users.size() == 0)
			throw new ObjectNotFoundException("User with ID='" + userID + "' not found.");
		
		DbUser user = users.get(0);
		
		ensureValidity(user); // Throws UserException
		
		log.info("User with ID='" + userID + "' found.");
		
		return user;
	}
	
	
	/**
	 * Adds a user to the user database with the data defined by the supplied {@link DbUser} object.
	 * 
	 * To be used for administrative purposes.
	 * 
	 * @param	user		The user to add to the database
	 * @throws	UserException						Thrown if the supplied user is incomplete
	 * @throws	CannotGetJdbcConnectionException	Thrown if the database is inaccessible
	 */
	@Override
	public void addUser(DbUser user) throws UserException, CannotGetJdbcConnectionException
	{
		log.info("Attempting to add user " + user.getUsername());
		
		ensureValidity(user); // Throws UserException
		
		if (user.getGroup() == null)
			addUserNoGroup(user);
		
		String addUser = "INSERT INTO users (username, password, enabled, group_id, authority) VALUES "
				+ "("
					+ "'" + user.getUsername() + "', "
					+ "'" + user.getPassword() + "', "
					+ "'" + (user.getEnabled() ? 1 : 0) + "', "
					+ "'" + user.getGroup().getGroupID() + "', "
					+ "'" + user.getAuthority() + "'"
				+ ")";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(userDataSource);
		int result = jdbcTemplate.update(addUser);
		
		boolean userAdded = (result != 0);
		if (userAdded)
			log.info("Add user operation for " + user.getUsername() + " successful.");
		else
			log.error("Add user operation for " + user.getUsername() + " failed. Reason unknown.");
	}
	
	
	/**
	 * Adds a user that doesn't have a group.
	 * 
	 * Primarily a maintenance method. Could also be used in the event that a group will soon be added
	 * to contain the user.
	 * 
	 * @param 	user		The user to add to the database
	 * @throws 	UserException						Errors if the user to add to the database is incomplete
	 * @throws	CannotGetJdbcConnectionException	Thrown if the program has failed to access the user database
	 * 
	 * @see addUser
	 */
	private void addUserNoGroup(DbUser user) throws UserException, CannotGetJdbcConnectionException
	{
		log.info("Attempting to add user " + user.getUsername() + " without a group.");
		
		ensureValidity(user); // Throws UserException
		
		String addUser = "INSERT INTO users (username, password, enabled, authority) VALUES "
				+ "(" 
					+ "'" + user.getUsername() + "', "
					+ "'" + user.getPassword() + "', "
					+ "'" + (user.getEnabled() ? 1 : 0) + "', "
					+ "'" + user.getAuthority() + "'"
				+ ")";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(userDataSource);
		int result = jdbcTemplate.update(addUser);
		
		boolean userAdded = (result > 0);
		if (userAdded)
			log.info("Add user (no group) operation for " + user.getUsername() + " successful.");
		else
			log.error("Add user (no group) operation for " + user.getUsername() + " failed. Reason unknown.");
	}
	
	
	/**
	 * Check if the supplied user is in the database
	 * 
	 * @param	user		The users to look for
	 * @return				Whether the user is in the database
	 * 
	 * @see		{@link ExistenceResultSet}
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the program has failed to access the user database
	 */
	@Override
	public boolean userExists(DbUser user) throws CannotGetJdbcConnectionException
	{
		log.info("Checking if user " + user.getUsername() + " exists in the database.");
		
		String getByID = "SELECT * FROM users WHERE user_id='" + user.getUserId() + "' " + " OR username='" + user.getUsername() + "'";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(userDataSource);
		boolean userExists = jdbcTemplate.query(getByID, new ExistenceResultSet());
		
		if (userExists)
			log.info("User " + user.getUsername() + " does exist in the database.");
		else
			log.info("User " + user.getUsername() + " could not be found in the database.");
		
		return userExists;
	}
	
	/**
	 * Check if the supplied username is in the database
	 * 
	 * @param	username	The username to look for
	 * @return				Whether the username is in the database
	 * 
	 * @see		{@link ExistenceResultSet}
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the program has failed to access the user database
	 */
	@Override
	public boolean usernameExists(String username) throws CannotGetJdbcConnectionException
	{
		log.info("Checking if the username '" + username + "' exists in the database.");
		
		String getByName = "SELECT * FROM users WHERE username='" + username + "'";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(userDataSource);
		boolean usernameExists = jdbcTemplate.query(getByName, new ExistenceResultSet());
		
		if (usernameExists)
			log.info("The username '" + username + "' does exist in the database.");
		else
			log.info("The username '" + username + "' could not be found in the database.");
		
		return usernameExists;
	}
	
	/**
	 * Replaces the data of oldUser in the database to the data from newUser.
	 * 
	 * It doesn't matter if the old user is complete, we only need an ID for the
	 * update operation. The new user, however, does need to be complete. Otherwise
	 * this throws a UserException.
	 * 
	 * If the old user can't be found in the database by its ID, throws UserNotFoundException
	 * 
	 * @param	oldUser		The old user data to write over in the database
	 * @param	newUser		The new user data to write with
	 * @throws	UserException						Thrown if newUser is incomplete
	 * @throws 	ObjectNotFoundException 				Thrown if the user could not be found in the database
	 * @throws	CannotGetJdbcConnectionException	Thrown if the server cannot be accessed
	 * 
	 * @see DbUser
	 */
	@Override
	public void updateUser(DbUser oldUser, DbUser newUser)
			throws UserException, ObjectNotFoundException, CannotGetJdbcConnectionException
	{
		log.info("Attempting update user operation for user with ID=" + oldUser.getUserId() + " adding new user " + newUser.describe());
		
		ensureUserExistence(oldUser); // Throws UserNotFoundException
		ensureValidity(newUser); // Throws UserException
		
		String updateUser = "UPDATE users "
				+ "SET user_id='" + newUser.getUserId() + "', "
				+ "username='" + newUser.getUsername() + "', "
				+ "password='" + newUser.getPassword() + "', "
				+ "authority='" + newUser.getAuthority() + "', "
				+ "enabled='" + (newUser.getEnabled() ? 1 : 0) + "', "
				+ "group_id='" + newUser.getGroup().getGroupID() + "' "
				+ "WHERE user_id='" + oldUser.getUserId() + "'";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(userDataSource);
		int result = jdbcTemplate.update(updateUser);
		
		boolean userUpdated = (result > 0);
		if (userUpdated)
			log.info("User update for " + newUser.getUsername() + " successful.");
		else
			log.error("User update for " + newUser.getUsername() + " unsuccessful.");
	}
	
	
	/**
	 * Sets the password in the database to the password of the supplied {@link DbUser} object.
	 * 
	 * Matches the supplied user against the database using the user's ID number, and not username.
	 * 
	 * @param	user		The user to change the password for
	 * @param	password	The new password
	 * @throws 	ObjectNotFoundException 				Thrown if the user could not be found in the database
	 * @throws	CannotGetJdbcConnectionException	Thrown if the server cannot be accessed
	 */
	@Override
	public void changePassword(DbUser user, String password)
			throws UserException, ObjectNotFoundException, CannotGetJdbcConnectionException
	{
		log.info("Attempting to change password for user " + user.getUsername());
		
		ensureUserExistence(user); // Throws UserNotFoundException
		
		String changePassword = "UPDATE users "
				+ "SET password='" + password + "' "
				+ "WHERE user_id='" + user.getUserId() + "'";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(userDataSource);
		int result = jdbcTemplate.update(changePassword);
		
		boolean passwordChanged = (result > 0);
		if (passwordChanged) {
			log.info("Password changed for user " + user.getUsername());
			user.setPassword(password);
		}
		else
			log.error("Failed to change password for user " + user.getUsername());
	}
	
	/**
	 * Sets the username in the database to the username of the supplied {@link DbUser} object.
	 * 
	 * Matches the supplied user against the database using the user's ID number, and not username.
	 * 
	 * @param	user		The user to change the username for
	 * @param	username	The new username
	 * @throws 	ObjectNotFoundException 				Thrown if the user could not be found in the database
	 * @throws	CannotGetJdbcConnectionException	Thrown if the server cannot be accessed
	 */
	@Override
	public void changeUsername(DbUser user, String username)
			throws UserException, ObjectNotFoundException, CannotGetJdbcConnectionException
	{
		log.info("Attempting to change username for user " + user.getUsername());
		
		ensureUserExistence(user); // Throws UserNotFoundException
		
		String changeUsername = "UPDATE users "
				+ "SET username='" + username + "' "
				+ "WHERE user_id='" + user.getUserId() + "'";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(userDataSource);
		int result = jdbcTemplate.update(changeUsername);
		
		boolean usernameChanged = (result > 0);
		if (usernameChanged) {
			log.info("Username changed for user " + user.getUsername());
			user.setUsername(username);
		}
		else
			log.error("Failed to change username for user " + user.getUsername());
	}
	
	/**
	 * Sets the authority in the database to the authority of the supplied {@link DbUser} object.
	 * 
	 * Matches the supplied user against the database using the user's ID number, and not username.
	 * 
	 * @param	user		The user to change the authority for
	 * @param	authority	The new authority
	 * @throws 	ObjectNotFoundException 				Thrown if the user could not be found in the database
	 * @throws	CannotGetJdbcConnectionException	Thrown if the server cannot be accessed
	 */
	@Override
	public void changeAuthority(DbUser user, String authority)
			throws UserException, ObjectNotFoundException, CannotGetJdbcConnectionException
	{
		log.info("Attempting to change authority for user " + user.getUsername());
		
		ensureUserExistence(user); // Throws UserNotFoundException
		
		String changeAuthority = "UPDATE users "
				+ "SET authority='" + authority + "' "
				+ "WHERE user_id='" + user.getUserId() + "'";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(userDataSource);
		int result = jdbcTemplate.update(changeAuthority);
		
		boolean authorityChanged = (result > 0);
		if (authorityChanged) {
			log.info("Authority changed for user " + user.getUsername());
			user.setAuthority(authority);
		}
		else
			log.error("Failed to change authority for user " + user.getUsername());
		
	}
	
	/**
	 * Sets the group in the database to the group of the supplied group
	 * 
	 * Matches the supplied user against the database using the user's ID number, and not username.
	 * 
	 * @param	user		The user to change the authority for
	 * @param	group		The new group
	 * 
	 * @throws	ObjectNotFoundException				Thrown if the group was not in the database
	 * @throws 	ObjectNotFoundException 				Thrown if the user could not be found in the database
	 * @throws	CannotGetJdbcConnectionException	Thrown if the server cannot be accessed
	 */
	@Override
	public void changeGroup(DbUser user, DbGroup group)
			throws ObjectNotFoundException, CannotGetJdbcConnectionException, ObjectNotFoundException
	{
		log.info("Attempting to change group assignment for user " + user.getUsername());
		
		ensureUserExistence(user); // Throws UserNotFoundException
		
		String changeGroup = "UPDATE users "
				+ "SET group_id='" + group.getGroupID() + "' "
				+ "WHERE user_id='" + user.getUserId() + "'";
		
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(userDataSource);
		group = this.findGroupByID(group.getGroupID()); // Ensure the group exists by getting it
		int result = jdbcTemplate.update(changeGroup);
		
		boolean groupChanged = (result > 0);
		if (groupChanged) {
			log.info("Group changed for user " + user.getUsername());
			user.setGroup(group);
		}
		else
			log.error("Failed to change group for user " + user.getUsername());
	}
	
	
	/**
	 * Deletes the user from the database
	 * 
	 * @param user		The user to delete from the database
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the server cannot be accessed
	 */
	@Override
	public void removeUser(DbUser user) throws CannotGetJdbcConnectionException
	{
		log.info("Attempting to delete user " + user.getUsername());
		
		String deleteUser = "DELETE FROM users WHERE user_id='" + user.getUserId() + "'";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(userDataSource);
		int result = jdbcTemplate.update(deleteUser);
		
		boolean deleteSuccessful = (result > 0);
		if (deleteSuccessful)
			log.info("User " + user.getUsername() + " deleted");
		else
			log.error("Failed to delete user " + user.getUsername());
	}
	
	/**
	 * Returns a list of all users.
	 * 
	 * To be used for administrative purposes.
	 * 
	 * @return 	All the users in the database
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the program has failed to access the user database
	 */
	@Override
	public List<DbUser> findAllUsers() throws CannotGetJdbcConnectionException
	{
		log.info("Attempting to find all users in the database.");
		
		String getAllUsers = USER_QUERY_VARIABLES
				+ "JOIN users AS u ON u.user_id = gr.owner_id "
				+ "WHERE gr.group_id = us.group_id";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(userDataSource);
		List<DbUser> users = jdbcTemplate.query(getAllUsers, new UserRowMapper());
		
		log.info("All users in the database found.");
		
		return users;
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Group Operations
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	/**
	 * Returns a DbUser from the user database that matches the supplied name
	 * 
	 * @param		groupName		The name of the returned user
	 * @return						The user with the name matching the supplied username
	 * 
	 * @throws		CannotGetJdbcConnectionException	Thrown if the database isn't available to be queried
	 * @throws		ObjectNotFoundException				Thrown if the group is not in the database 
	 */
	@Override
	public DbGroup findGroupByName(String groupName) 
			throws CannotGetJdbcConnectionException, ObjectNotFoundException
	{
		log.info("Attempting to find group with name " + groupName + ".");
		
		String getGroup = GROUP_QUERY_VARIABLES
				+ "FROM groups AS gr "
				+ "JOIN users AS u ON u.user_id = gr.owner_id "
				+ "WHERE gr.group_name='" + groupName + "'";
		
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(userDataSource);
		List<DbGroup> groups = jdbcTemplate.query(getGroup, new GroupRowMapper());
		
		
		if (groups.size() == 0)
			throw new ObjectNotFoundException("Group with name '" + groupName + "' not found.");
		
		if (groups.size() > 1)
		{
			String multiplicityMessage = "More than one group matches the name '" + groupName + "':";
			for (int i = 0; i < groups.size(); i++) {
				multiplicityMessage += groups.get(i).getGroupName();
				if (i != groups.size() - 1)
					multiplicityMessage += ", ";
			}
			log.error(multiplicityMessage);
		}
		
		DbGroup group = groups.get(0);
		
		log.info("Group with name " + groupName + " found.");
		
		return group;
	}
	
	
	/**
	 * Returns a DbUser from the user database that matches the supplied user ID
	 * 
	 * @param		groupID			The ID of the returned user
	 * @return						The user with the name matching the supplied ID
	 * 
	 * @throws		CannotGetJdbcConnectionException	Thrown if the database isn't available to be queried
	 * @throws		ObjectNotFoundException				Thrown if the ID is not in the database 
	 */
	@Override
	public DbGroup findGroupByID(int groupID)
			throws CannotGetJdbcConnectionException, ObjectNotFoundException
	{
		log.info("Attempting to find group with ID='" + groupID + "'.");
		
		String getGroup = GROUP_QUERY_VARIABLES
				+ "FROM groups AS gr "
				+ "JOIN users AS u ON u.user_id = gr.owner_id "
				+ "WHERE gr.group_id='" + groupID + "'";
		
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(userDataSource);
		List<DbGroup> groups = jdbcTemplate.query(getGroup, new GroupRowMapper());
		
		if (groups.size() == 0)
			throw new ObjectNotFoundException("Group with ID='" + groupID + "' not found.");
		
		if (groups.size() > 1)
		{
			String multiplicityMessage = "More than one group matches the ID='" + groupID + "':";
			for (int i = 0; i < groups.size(); i++) {
				multiplicityMessage += groups.get(i).getGroupName();
				if (i != groups.size() - 1)
					multiplicityMessage += ", ";
			}
			log.error(multiplicityMessage);
		}
		
		DbGroup group = groups.get(0);
		
		log.info("Group with ID='" + groupID + "' found.");
		
		return group;
	}
	
	/**
	 * TODO: Implement, but only if groups will be a real feature. As it stands,
	 * they don't seem to have much of a future in this web service.
	 */
	@Override
	public void addGroup(DbGroup group)
	{
		
	}

	/**
	 * TODO: Implement, but only if groups will be a real feature. As it stands,
	 * they don't seem to have much of a future in this web service.
	 */
	@Override
	public void removeGroup(DbGroup group)
	{
		
	}
	
	/**
	 * Returns all the groups from the user database
	 * 
	 * @return		All the groups in the database
	 * 
	 * @throws	CannotGetJdbcConnectionException	Thrown if the program has failed to access the user database
	 */
	@Override
	public List<DbGroup> findAllGroups() throws CannotGetJdbcConnectionException
	{
		log.info("Attempting to get all groups from the user database.");
		
		String getGroups = GROUP_QUERY_VARIABLES
				+ "FROM groups AS gr "
				+ "JOIN users AS u ON u.user_id = gr.owner_id";
		
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(userDataSource);
		List<DbGroup> groupList = jdbcTemplate.query(getGroups, new GroupRowMapper());
		
		log.info("All groups in the database have been found.");
		
		return groupList;
	}
	
	/**
	 * Determines whether a group with the supplied name exists.
	 * 
	 * @params	groupName	The group name to check for
	 * @return				Whether a group with the supplied name exists.
	 * 
	 * @throws	CannotGetJdbcConnectionException		Thrown if the program has failed to access the user database
	 */
	@Override
	public boolean groupNameExists(String groupName) throws CannotGetJdbcConnectionException
	{
		log.info("Checking if the group with the name '" + groupName + "' exists in the database.");
		
		String getByName = "SELECT * FROM groups WHERE group_name='" + groupName + "'";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(userDataSource);
		boolean groupNameExists = jdbcTemplate.query(getByName, new ExistenceResultSet());
		
		if (groupNameExists)
			log.info("The group with the name '" + groupName + "' does exist in the database.");
		else
			log.info("The group with the name '" + groupName + "' could not be found in the database.");
		
		return groupNameExists;
	}
	
	
	/**
	 * Determines whether a group by the supplied ID exists
	 * 
	 * @params	groupID		The group to check for
	 * @return				Whether a group with the supplied name exists.
	 * 
	 * @throws	CannotGetJdbcConnectionException		Thrown if the program has failed to access the user database
	 */
	@Override
	public boolean groupExists(int groupID) throws CannotGetJdbcConnectionException
	{
		log.info("Checking if the group with the ID '" + groupID + "' exists in the database.");
		
		String getByID = "SELECT * FROM groups WHERE group_id='" + groupID + "'";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(userDataSource);
		boolean groupExists = jdbcTemplate.query(getByID, new ExistenceResultSet());
		
		if (groupExists)
			log.info("The group with the ID '" + groupID + "' does exist in the database.");
		else
			log.info("The group with the ID '" + groupID + "' could not be found in the database.");
		
		return groupExists;
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Getters / Setters
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	@Override
	public void setUserDataSource(DataSource userDataSource)
	{
		this.userDataSource = userDataSource;
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Helper methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	protected void ensureValidity(DbUser user) throws UserException
	{
		if (user.isInvalid()) {
			String incompleteMessage = "The user " + user.describe() + " is incomplete or invalid because " + user.InvalidityMessage();
			UserException exception = new UserException(incompleteMessage);
			log.error(incompleteMessage, exception);
			throw exception;
		}	
	}
	
	protected void ensureUserExistence(DbUser user) throws ObjectNotFoundException, CannotGetJdbcConnectionException
	{
		if ( ! userExists(user)) {
			log.error("Operation for user " + user.getUsername() + " failed because no such user is in the database.");
			throw new ObjectNotFoundException();
		}
	}
	
}
