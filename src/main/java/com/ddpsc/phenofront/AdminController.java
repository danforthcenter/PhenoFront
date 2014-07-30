package com.ddpsc.phenofront;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import src.ddpsc.database.user.DbGroup;
import src.ddpsc.database.user.DbUser;
import src.ddpsc.database.user.UserDao;
import src.ddpsc.exceptions.ObjectNotFoundException;
import src.ddpsc.exceptions.UserException;

/**
 * This class contains all the actions which provide functionality to the administrator page. Mainly just user modification,
 * add, remove, etc.
 * 
 * This controller is only allowed to be used by those with authority of ROLE_ADMIN.
 * 
 * @author shill, cjmcentee
 */

@Controller
@RequestMapping(value="/admin")
public class AdminController
{
	
	private static final Logger log = Logger.getLogger(AdminController.class);
	
	private static PasswordEncoder encoder = new StandardPasswordEncoder();
	
	@Autowired
	UserDao ud;
	
	
	/**
     * Exact same as user action. remove and point this action at the other
     * 
     * Side Effects: All users and groups are added to the model
     * 
     * @param	Model		And internal model for interacting with the view
     * @return				Takes the user to the users page, or an error page
     */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String homeAction(Model model)
	{
		List<DbGroup> groups = new ArrayList<DbGroup>();
		List<DbUser> users = new ArrayList<DbUser>();
		
		try {
			groups = ud.findAllGroups();
			users = ud.findAllUsers();
		}
		catch (CannotGetJdbcConnectionException e) {
			return ControllerHelper.handleJdbcException(model, log, "retrieve all user and group data");
		}
		
		model.addAttribute("users", users);
		model.addAttribute("groups", groups);
		
		return "users";
	}
	
	/**
	 * Base action which load a table of users.
	 * 
	 * @param 		model		Internal model for interacting with the view
	 * @return					Takes the user back to the home screen
	 */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String userManagementAction(Model model)
	{
		return homeAction(model);
	}
	
	
	/**
	 * Changes the user's password without requiring the current password.
	 * 
	 * To be used by administrators.
	 * 
	 * Encoding is done with the standard encoder.
	 * 
	 * @param	userId				The ID of the user whose password is going to change
	 * @param	newPassword			The first copy of the new password
	 * @param	validationPassword	The second copy of the new password
	 * 
	 * @return						Http response of whether the password change worked
	 */
	@RequestMapping(value = "/changepass", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> changePasswordAction(
			@RequestParam("userid") int userId,
			@RequestParam("newpass") String newPassword,
			@RequestParam("validate") String validationPassword)
	{
		// Validate input
		boolean passwordsMatch = newPassword.equals(validationPassword); 
		if ( ! passwordsMatch) {
			log.info("Change password for user ID=" + userId + " failed because the new and validation passwords don't match.");
			return new ResponseEntity<String>("Passwords do not match!", HttpStatus.BAD_REQUEST);
		}
		
		// Try to change password
		try {
			DbUser user = ud.findByID(userId);
			ud.changePassword(user, encoder.encode(newPassword));
			log.info("Change password for user ID=" + userId + " succeeded without errors.");
			return new ResponseEntity<String>("Success!", HttpStatus.OK);
		}
		
		catch (Exception e) {
			return ControllerHelper.handleUserDataPOSTExceptions(e, userId, log, "change the password of");
		}
	}
	
	
	/**
	 * Changes a given user's authority supplied authority.
	 * 
	 * If the supplied authority is not ROLE_USER or ROLE_ADMIN bad request is returned.
	 * 
	 * @param userId		ID of the user whose authority is changing
	 * @param newAuthority	New authority value for the user
	 * 
	 * @return				Http response of whether the authority change worked
	 */
	@RequestMapping(value = "/changeauthority", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> changeAuthorityAction(
			@RequestParam("userid")  int userId, 
			@RequestParam("authority") String newAuthority)
	{
		// Validate input	
		if (DbUser.authorityInvalid(newAuthority)) {
			log.info("Change authority for user ID=" + userId + " failed because the new authority isn't valid.");
			return new ResponseEntity<String>("Not a valid authority.", HttpStatus.BAD_REQUEST);
		}
		
		// Try to change authority
		try {
			DbUser user = ud.findByID(userId);
			ud.changeAuthority(user, newAuthority);
			log.info("Change authority for user ID=" + userId + " succeeded without errors.");
		    return new ResponseEntity<String>("Success!", HttpStatus.OK);
		}
		
		catch (Exception e) {
			return ControllerHelper.handleUserDataPOSTExceptions(e, userId, log, "change the authority of");
		}
		
	}
	
	/**
	 * Changes a user's username.
	 * 
	 * Requires that no one else has the same username, and that the username is valid (not empty).
	 * 
	 * @param userId		The ID of the user whose username will change
	 * @param newUsername	The new username of the user
	 * 
	 * @return				Http response of whether the username change worked
	 */
	@RequestMapping(value = "/changeusername", method=RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> changeUsernameAction(
			@RequestParam("userid") int userId,
			@RequestParam("username") String newUsername)
	{
		// Validate input	
		if (DbUser.usernameInvalid(newUsername)) {
			log.info("Change username for user ID=" + userId + " failed because the new username isn't valid.");
			return new ResponseEntity<String>("New username is not valid.", HttpStatus.BAD_REQUEST);
		}
		
		// Try to change the username
		try {
			DbUser user = ud.findByID(userId);
			
			if (user.getUsername().equals(newUsername)) {
				log.info("Change username for user ID=" + userId + " failed because the new username is the same as the current username.");
				return new ResponseEntity<String>("New username is the same as the current username.", HttpStatus.BAD_REQUEST);
			}
			
			else if (ud.usernameExists(newUsername)) {
				log.info("Change username for user ID=" + userId + " failed because the username already eixsts.");
				return new ResponseEntity<String>("New username is already taken.", HttpStatus.CONFLICT);
			}
			
			else {
				ud.changeUsername(user, newUsername);
				log.info("Change username for user ID=" + userId + " succeeded without errors.");
			    return new ResponseEntity<String>("Success!", HttpStatus.OK);
			}
		}
		
		catch (Exception e) {
			return ControllerHelper.handleUserDataPOSTExceptions(e, userId, log, "change the username of");
		}
	}
	
	
	/**
	 * Changes a user's group.
	 * 
	 * Expects the name of the new group. The group must exist.
	 * 
	 * 
	 * @param userId		The ID of the user whose group is changing
	 * @param newGroupName	The name of user's new group
	 * 
	 * @return				Http response of whether the group change worked
	 */
	@RequestMapping(value = "/changegroup", method=RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> changeGroupAction(
			@RequestParam("userid") int userId,
			@RequestParam("groupname") String newGroupName)
	{
		try {
			DbGroup group = null;
			try {
				group = ud.findGroupByName(newGroupName);
			}
			catch (ObjectNotFoundException e) {
				log.info("Change group for user ID=" + userId + " failed because the group could not be found.");
				return new ResponseEntity<String>("User data corrupted.", HttpStatus.BAD_REQUEST);
			}
			
			DbUser user = ud.findByID(userId);
			ud.changeGroup(user, group);
			return new ResponseEntity<String>("Success!", HttpStatus.OK);
		}
		
		catch (Exception e) {
			return ControllerHelper.handleUserDataPOSTExceptions(e, userId, log, "change the group of");
		}
	}
	
	/**
	 * Removes a user from the database.
	 * 
	 * 
	 * @param 	userId			ID of the user to remove
	 * @return					Http response of whether the user was removed
	 */
	@RequestMapping(value = "/removeuser", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> deleteUserAction(
			@RequestParam("userid") int userId)
	{
		try {
			DbUser user = ud.findByID(userId);
			ud.removeUser(user);
			return new ResponseEntity<String>("Success!", HttpStatus.OK);
		}
		
		catch (Exception e) {
			return ControllerHelper.handleUserDataPOSTExceptions(e, userId, log, "remove");
		}
	}
	
	/**
	 * Creates a new user and saves it into the database. Returns Conflict on an already existing username, Bad Request if the passwords 
	 * do not match or the authority is invalid.
	 * 
	 * Groups may be null.
	 * 
	 * @param 	username				User's name
	 * @param 	password				User's password
	 * @param 	authority				User's authority to make changes to the system
	 * @param 	groupName				User's group assignment
	 * @param 	validationPassword		Second password to ensure the password was typed correctly
	 * 
	 * @return							Http response indicating whether the user was added
	 */
	@RequestMapping(value = "/newuser", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addUserAction(
			@RequestParam("username") 	String username,
			@RequestParam("password") 	String password,
			@RequestParam("authority") 	String authority,
			@RequestParam("groupname") 	String groupName,
			@RequestParam("validate") 	String validationPassword)
	{
		// Validate input
		boolean passwordsMatch = password.equals(validationPassword);
		if( ! passwordsMatch) {
			log.info("Could not add user to system because the new and validation passwords do not match.");
			return new ResponseEntity<String>("Error, the new user's passwords do not match.", HttpStatus.BAD_REQUEST);
		}
		
		if (DbUser.usernameInvalid(username)) {
			log.info("Could not add user to system because users cannot have empty usernames.");
			return new ResponseEntity<String>("Error, the new user's username cannot be empty.", HttpStatus.BAD_REQUEST);
		}
		
		if (DbUser.authorityInvalid(authority)) {
			log.info("Could not add user to system because users must have a valid authority.");
			return new ResponseEntity<String>("Error, the new user's authority is invalid.", HttpStatus.BAD_REQUEST);
		}
		
		// Try to add the user
		try {
			DbUser newUser = new DbUser();
			newUser.setUsername(username);
			newUser.setPassword(encoder.encode(password));
			newUser.setEnabled(true);
			newUser.setAuthority(authority);
			if (ud.groupNameExists(groupName))
				try {
					newUser.setGroup(ud.findGroupByName(groupName));
				}
				catch (ObjectNotFoundException e) {
					log.error("Group existence in conflict. UserDao.groupNameExists says it exists, UserDao.findGroupByName says it doesn't.", e);
					return new ResponseEntity<String>("Error: Group existence in conflict.", HttpStatus.INTERNAL_SERVER_ERROR);
				}
			else
				newUser.setGroup(null);
			
			if (ud.usernameExists(username)) {
				log.info("Could not add user to system because that user already exists.");
				return new ResponseEntity<String>("Error: Username already exists.", HttpStatus.CONFLICT);
			}
			else {
				ud.addUser(newUser);
				log.info("User " + newUser.getUsername() + " successfully added to the system.");
				return new ResponseEntity<String>("Success!", HttpStatus.OK);
			}
		}
		
		catch (CannotGetJdbcConnectionException e) {
			log.info("Could not add the user, " + username + ", because this server could not connect to the user data server.");
			return new ResponseEntity<String>("Cannot connect to authentication server.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (UserException e) {
			log.info("Could not add the user " + username + " because the supplied user data is incomplete.");
			return new ResponseEntity<String>("User data corrupted.", HttpStatus.BAD_REQUEST);
		}
	}
}
