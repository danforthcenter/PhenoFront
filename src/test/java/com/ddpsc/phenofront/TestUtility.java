package com.ddpsc.phenofront;

import java.util.ArrayList;

import org.springframework.security.crypto.password.StandardPasswordEncoder;

import src.ddpsc.database.user.Group;
import src.ddpsc.database.user.User;
/**
 * Utility abstract class that contains various data instantiation.
 * 
 * Test suites should extend this class to get the general test data.
 * 
 * @author shill
 */

public abstract class TestUtility {
	/**
	 * user 1 is called testuser1 and belongs to group one, also owns group 1
	 * user 2 is called testuser2 and belongs to group one, does not own a group.
	 * user 3 is called testuser3 and belongs to group two, owns group 2.
	 * 
	 * all passwords are null, testuser1 is the only ROLE_ADMIN authority
	 * 
	 * @param version
	 * @return
	 */
	
	public User FIRST_USER;
	public User SECOND_USER;
	public User THIRD_USER;
	
	public Group FIRST_USER_GROUP;
	public Group SECOND_USER_GROUP;
	
	public ArrayList<User> USERS;
	public ArrayList<Group> GROUPS;
	
	/**
	 * Returns all possible ROLE_USER paths that should be accessed via GET
	 * @return
	 */
	public ArrayList<String> getAllGetUserPaths()
	{
		ArrayList<String> paths = new ArrayList<String>();
		
		paths.add("/userarea");
		paths.add("/userarea/status");
		paths.add("/userarea/visualize");
		paths.add("/userarea/schedule");
		paths.add("/userarea/results");
		
		return paths;
	}
	
	/**
	 * Returns all possible ROLE_USER paths that should be accessed via POST.
	 * @return
	 */
	public ArrayList<String> getAllPostUserPaths()
	{
		ArrayList<String> paths = new ArrayList<String>();
		return paths;
	}
	
	/**
	 * Returns all possible ROLE_ADMIN paths that should be accessed via GET.
	 * Does not return ROLE_USER paths. Check both when executing tests.
	 * @return
	 */
	public ArrayList<String> getAllGetAdminPaths()
	{
		ArrayList<String> paths = new ArrayList<String>();
		
		paths.add("/admin");
		paths.add("/admin/users");
		
		return paths;
	}
	
	/**
	 * Returns all possible ROLE_ADMIN paths that should be acessed via POST.
	 * Does not also include ROLE_USER paths. Be sure to check both when executing tests.
	 * @return
	 */
	public ArrayList<String> getAllPostAdminPaths()
	{
		ArrayList<String> paths = new ArrayList<String>();
		
		paths.add("/admin/changepass");
		paths.add("/admin/changeauthority");
		paths.add("/admin/newuser");
		paths.add("/admin/changegroup");
		paths.add("/admin/changeusername");
		paths.add("/admin/removeuser");
		
		return paths;
	}
	/**
	 * All passwords are Standard Password Encoded password
	 * @return 
	 */
	public void UserTestUtility()
	{
		StandardPasswordEncoder se = new StandardPasswordEncoder();
		
		FIRST_USER = new User();
		FIRST_USER.setUserId(1);
        FIRST_USER.setUsername("testuser1");
        FIRST_USER.setAuthority("ROLE_ADMIN");
        FIRST_USER.setEnabled(true);
        FIRST_USER.setPassword(se.encode("password"));
        FIRST_USER_GROUP = buildGroupMock(1, FIRST_USER);
        FIRST_USER.setGroup(FIRST_USER_GROUP);
        
        SECOND_USER = new User();
		SECOND_USER.setUserId(2);
		SECOND_USER.setUsername("testuser2");
		SECOND_USER.setAuthority("ROLE_USER");
		SECOND_USER.setEnabled(true);
		SECOND_USER.setPassword(se.encode("password"));
		SECOND_USER.setGroup(FIRST_USER_GROUP);
		
		THIRD_USER = new User();
		THIRD_USER.setUserId(3);
		THIRD_USER.setUsername("testuser3");
		THIRD_USER.setAuthority("ROLE_USER");
		THIRD_USER.setEnabled(true);
		THIRD_USER.setPassword(se.encode("password"));
		SECOND_USER_GROUP = buildGroupMock(2, THIRD_USER);
		THIRD_USER.setGroup(SECOND_USER_GROUP);
		
		USERS = new ArrayList<User>();
        USERS.add(FIRST_USER);
        USERS.add(SECOND_USER);
        USERS.add(THIRD_USER);
        
        GROUPS = new ArrayList<Group>();
        GROUPS.add(FIRST_USER_GROUP);
        GROUPS.add(SECOND_USER_GROUP);
	}
	
	/**
	 * Group1 is owned by testuser1, group2 is owned by testuser3
	 * @param version
	 * @return
	 */
	public static Group buildGroupMock(int version, User owner)
	{
		if (version == 1) {
			Group FIRST_USER = new Group();
			FIRST_USER.setGroupId(1);
			FIRST_USER.setGroupName("group1");
			FIRST_USER.setOwner(owner);
			return FIRST_USER;
		}
		if (version == 2) {
			Group SECOND_USER = new Group();
			SECOND_USER.setGroupId(2);
			SECOND_USER.setGroupName("group2");
			SECOND_USER.setOwner(owner);
			return SECOND_USER;
		}
		return null;
	}
}
