package src.ddpsc.database.user;

import java.util.ArrayList;

import src.ddpsc.exceptions.UserException;
import src.ddpsc.exceptions.UserNotFoundException;

/**
 * DAO interface for our database user table. The interface is necessary if we ever want to use a different backend.
 * All implementations should be in {@link UserDaoImpl}. Anytime a change is being made, such as an update, insertion,
 * or deletion, an entire DbUser object should be passed.
 * 
 * @author shill
 *
 */
public interface UserDao {
	public DbUser findByUsername(String username);
	public ArrayList<DbUser> findAllUsers();
	public void addUser(DbUser user) throws UserException;
	
	//maybe user too
	public void changePassword(DbUser user, String newPassword);
	
	/**
	 * General purpose function for updating the user. Expects every field in user to be instantiated.
	 * 
	 * @param user
	 */
	public void updateUser(DbUser user);
	public void removeUser(DbUser user);

	public DbUser findByUserId(int userId) throws UserNotFoundException;
	public void changePassword(DbUser user);
	public void updateAuthority(DbUser user);
	
	//how bad of a practice is this? yolo they should be coupled
	public void removeGroup(DbGroup user);
	public void updateGroup(DbUser user);
	public void addGroup(DbGroup group);
	public ArrayList<DbGroup> findAllGroups();
	public void updateUsername(DbUser user);
}


