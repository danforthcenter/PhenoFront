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

	DbUser findByUsername(String username)
			throws CannotGetJdbcConnectionException, UserException, ObjectNotFoundException;

	DbUser findByID(int userID)
			throws CannotGetJdbcConnectionException, UserException, ObjectNotFoundException;

	void addUser(DbUser user)
			throws UserException, CannotGetJdbcConnectionException;
	
	void removeUser(DbUser user)
			throws CannotGetJdbcConnectionException;

	List<DbUser> findAllUsers()
			throws CannotGetJdbcConnectionException;

	boolean userExists(DbUser user)
			throws CannotGetJdbcConnectionException;
	
	boolean usernameExists(String username)
			throws CannotGetJdbcConnectionException;

	void updateUser(DbUser oldUser, DbUser newUser)
			throws UserException, ObjectNotFoundException, CannotGetJdbcConnectionException;
	
	void changePassword(DbUser user, String password)
			throws UserException, ObjectNotFoundException, CannotGetJdbcConnectionException;

	void changeUsername(DbUser user, String username)
			throws UserException, ObjectNotFoundException, CannotGetJdbcConnectionException;

	void changeAuthority(DbUser user, String authority)
			throws UserException, ObjectNotFoundException, CannotGetJdbcConnectionException;

	void changeGroup(DbUser user, DbGroup group)
			throws ObjectNotFoundException, ObjectNotFoundException, CannotGetJdbcConnectionException;
	
	
	
	void addGroup(DbGroup group);

	void removeGroup(DbGroup group);

	List<DbGroup> findAllGroups()
			throws CannotGetJdbcConnectionException;

	boolean groupNameExists(String groupName)
			throws CannotGetJdbcConnectionException;

	boolean groupExists(int groupID)
			throws CannotGetJdbcConnectionException;

	DbGroup findGroupByName(String groupName)
			throws CannotGetJdbcConnectionException, ObjectNotFoundException;

	DbGroup findGroupByID(int userID)
			throws CannotGetJdbcConnectionException, ObjectNotFoundException;

	
	
	void setUserDataSource(DataSource userDataSource);
}

