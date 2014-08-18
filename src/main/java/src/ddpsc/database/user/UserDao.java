package src.ddpsc.database.user;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.CannotGetJdbcConnectionException;

import src.ddpsc.exceptions.ObjectNotFoundException;
import src.ddpsc.exceptions.UserException;


/**
 * DAO interface for our database user table. The interface is necessary if we ever want to use a different backend.
 * All implementations should be in {@link UserDaoImpl}. Any time a change is being made, such as an update, insertion,
 * or deletion, an entire DbUser object should be passed.
 * 
 * @author shill, cjmcentee
 *
 */
public interface UserDao
{

	User findByUsername(String username)
			throws CannotGetJdbcConnectionException, UserException, ObjectNotFoundException;

	User findByID(int userID)
			throws CannotGetJdbcConnectionException, UserException, ObjectNotFoundException;

	void addUser(User user)
			throws UserException, CannotGetJdbcConnectionException;
	
	void removeUser(User user)
			throws CannotGetJdbcConnectionException;

	List<User> findAllUsers()
			throws CannotGetJdbcConnectionException;

	boolean userExists(User user)
			throws CannotGetJdbcConnectionException;
	
	boolean usernameExists(String username)
			throws CannotGetJdbcConnectionException;

	void updateUser(User oldUser, User newUser)
			throws UserException, ObjectNotFoundException, CannotGetJdbcConnectionException;
	
	void changePassword(User user, String password)
			throws UserException, ObjectNotFoundException, CannotGetJdbcConnectionException;

	void changeUsername(User user, String username)
			throws UserException, ObjectNotFoundException, CannotGetJdbcConnectionException;

	void changeAuthority(User user, String authority)
			throws UserException, ObjectNotFoundException, CannotGetJdbcConnectionException;

	void changeGroup(User user, Group group)
			throws ObjectNotFoundException, ObjectNotFoundException, CannotGetJdbcConnectionException;
	
	
	
	void addGroup(Group group);

	void removeGroup(Group group);

	List<Group> findAllGroups()
			throws CannotGetJdbcConnectionException;

	boolean groupNameExists(String groupName)
			throws CannotGetJdbcConnectionException;

	boolean groupExists(int groupID)
			throws CannotGetJdbcConnectionException;

	Group findGroupByName(String groupName)
			throws CannotGetJdbcConnectionException, ObjectNotFoundException;

	Group findGroupByID(int userID)
			throws CannotGetJdbcConnectionException, ObjectNotFoundException;

	
	
	void setUserDataSource(DataSource userDataSource);
}

