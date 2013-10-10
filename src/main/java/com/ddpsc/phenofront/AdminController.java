package com.ddpsc.phenofront;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import src.ddpsc.exceptions.UserException;
import src.ddpsc.exceptions.UserNotFoundException;

/**
 * This class contains all the actions which provide functionality to the administrator page. This controller is secured to
 * ROLE_ADMIN only.
 * 
 * @author shill
 *
 */
@Controller
@RequestMapping(value="/admin")
public class AdminController {
	@Autowired
	UserDao ud;
	@RequestMapping(value = "", method= RequestMethod.GET)
	public String homeAction(){
		//displays dumbhome page
		return "jobrequests";
		
	}
	/**
	 * This is the admin's method of changing the password. It does not require the old password. Encoding
	 * is done here with standard encoder. It is arguable that this funcitonality should not be at the controller layer.
	 * Since the user and admin change password functions are different (this is essentially setting the password), I have
	 * chosen to do it here.
	 * 
	 * @author shill
	 * @param userid
	 * @param oldpass
	 * @param newpass
	 * @return
	 */
	@RequestMapping(value = "/changepass", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> changePasswordAction(@RequestParam("userid")  int userId, 
													 @RequestParam("newpass") String newPass,
													 @RequestParam("validate") String validate){
		if (!newPass.equals(validate)){
			return new ResponseEntity<String>("Passwords do not match!", HttpStatus.BAD_REQUEST);
		}
		DbUser user;
		try {
			user = ud.findByUserId(userId);
			StandardPasswordEncoder se = new StandardPasswordEncoder();
			String pass = se.encode(newPass);
			user.setPassword(pass);
			ud.changePassword(user);
		    return new ResponseEntity<String>("Success!", HttpStatus.OK);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<String>("User not found.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	/**
	 * Changes a given user's authority to the passed role. If the role is not ROLE_USER or ROLE_ADMIN bad request is returned.
	 * 
	 * 
	 * @param userId
	 * @param newAuth
	 * @return
	 * @author shill
	 */
	@RequestMapping(value = "/changeauthority", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> changeAuthorityAction(@RequestParam("userid")  int userId, 
													 @RequestParam("authority") String newAuth){
		if (!(newAuth.equals("ROLE_USER") || newAuth.equals("ROLE_ADMIN"))){
			return new ResponseEntity<String>("Not a valid authority.", HttpStatus.BAD_REQUEST);
		}
		DbUser user;
		try {
			user = ud.findByUserId(userId);
			user.setAuthority(newAuth);
			ud.updateAuthority(user);
		    return new ResponseEntity<String>("Success!", HttpStatus.OK);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<String>("User not found.", HttpStatus.BAD_REQUEST);
		}
		
	}
	/**
	 * Creates a new user and saves it into the database. Returns Conflict on an already existing username, Bad Request if the passwords 
	 * do not match or the authority is invalid.
	 * 
	 * Groups may be null.
	 * 
	 * @param username
	 * @param password
	 * @param authority
	 * @param group
	 * @param validate
	 * @return
	 */
	@RequestMapping(value = "/newuser", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addUserAction(@RequestParam("username") String username, @RequestParam("password") String password,
															  @RequestParam("authority") String authority, @RequestParam("groupname") String group,
															  @RequestParam("validate") String validate){

		if(!password.equals(validate)){
			return new ResponseEntity<String>("Error, passwords do not match.", HttpStatus.BAD_REQUEST);
		}
		ArrayList<DbUser> userList = ud.findAllUsers();
		boolean exists = false;
		for (DbUser user : userList) {
			if (user.getUsername().equals(username)){
				exists = true;
				break;
			}
		}
		if (exists){
			return new ResponseEntity<String>("Error, username already exists.", HttpStatus.CONFLICT);
		}
		if (!(authority.equals("ROLE_USER") || authority.equals("ROLE_ADMIN"))){
			return new ResponseEntity<String>("Not a valid authority.", HttpStatus.BAD_REQUEST);
		}
		
		ArrayList<DbGroup> groupList = ud.findAllGroups();
		DbGroup dbGroup = null;
		for (DbGroup egroup : groupList) {
			if (egroup.getGroupName().equals(group)){
				dbGroup = egroup;
				break;
			}
		}
		
		DbUser newuser = new DbUser();
		StandardPasswordEncoder pe = new StandardPasswordEncoder();
		newuser.setGroup(dbGroup); //may be null
		newuser.setUsername(username);
		newuser.setPassword(pe.encode(password));
		newuser.setEnabled(true);
		newuser.setAuthority(authority);
		try {
			ud.addUser(newuser);
			return new ResponseEntity<String>("Success!", HttpStatus.OK);
		} catch (UserException e) {
			return new ResponseEntity<String>("Error!", HttpStatus.BAD_REQUEST);
		}
		
	}
	/**
	 * Changes the group for a user from one to another. Expects the name of the new group. The group must exist or
	 * a BadRequest is returned.
	 * 
	 * @param userId
	 * @param newGroup
	 * @return
	 */
	@RequestMapping(value = "/changegroup", method=RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> changeGroupAction( @RequestParam("userid") int userId, @RequestParam("groupname") String newGroup) {
		//make sure group exists
		ArrayList<DbGroup> groupList = ud.findAllGroups();
		DbGroup dbGroup = null;
		for (DbGroup group : groupList) {
			if (group.getGroupName().equals(newGroup)){
				dbGroup = group;
				break;
			}
		}
		if (dbGroup == null){
			return new ResponseEntity<String>("Not a valid group.",
					HttpStatus.BAD_REQUEST);
		}
		DbUser user;
		try {
			user = ud.findByUserId(userId);
			user.setGroup(dbGroup);
			ud.updateGroup(user);
			return new ResponseEntity<String>("Success!", HttpStatus.OK);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<String>("User not found.", HttpStatus.BAD_REQUEST);
		}
	
	}
	
	/**
	 * Method responsible for changing a user's username. It also queries the list of active users to make sure the name
	 * does not already exist. For an error to be thrown, the existing username ALSO must not be this user's username.
	 * 
	 * @param userId
	 * @param newUsername
	 * @return
	 * @author shill
	 */
	@RequestMapping(value = "/changeusername", method=RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> changeUsernameAction( @RequestParam("userid") int userId, @RequestParam("username") String newUsername) {
		//make sure group exists
		ArrayList<DbUser> userList = ud.findAllUsers();
		boolean exists = false;
		for (DbUser user : userList) {
			if (user.getUsername().equals(newUsername) && user.getUserId() != userId){
				exists = true;
				break;
			}
		}
		if (exists){
			return new ResponseEntity<String>("Error, username already exists.", HttpStatus.CONFLICT);
		}
		DbUser user;
		try {
			user = ud.findByUserId(userId);
			user.setUsername(newUsername);
			ud.updateUsername(user);
			return new ResponseEntity<String>("Success!", HttpStatus.OK);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<String>("User not found.", HttpStatus.BAD_REQUEST);
		} //could be optimized to just find one in our already found list of users
		
	}
	
	/**
	 * Removes a user from the database.
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/removeuser", method=RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> deleteUserAction( @RequestParam("userid") int userId) {
		//make sure group exists
		DbUser user;
		try {
			user = ud.findByUserId(userId);
			ud.removeUser(user);
			return new ResponseEntity<String>("Success!", HttpStatus.OK);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<String>("User not found.", HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Base action which loads a table of users.
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/users", method=RequestMethod.GET)
	public String userManagementAction(Model model){
		ArrayList<DbGroup> groups = ud.findAllGroups();
		ArrayList<DbUser> users = ud.findAllUsers();
		model.addAttribute("users", users);
		model.addAttribute("groups", groups);
		return "users";
	}
}
