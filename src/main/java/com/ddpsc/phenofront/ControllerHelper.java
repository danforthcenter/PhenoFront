package com.ddpsc.phenofront;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

class ControllerHelper
{
	private static final Logger log = Logger.getLogger(ControllerHelper.class);
	
	static final String ANONYMOUS_USER_MESSAGE = "User not logged in.";
	
	
	/**
	 * Returns the current username for this context
	 * 
	 * The username can be the anonymous username if the user isn't logged in.
	 * 
	 * @return			The current user's username
	 */
	static String currentUsername()
	{
		return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	/**
	 * Returns the current Authentication object for this context
	 * 
	 * @return			The current authentication object
	 */
	static Authentication currentAuthentication()
	{
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	/**
	 * Determines whether the username is that of the anonymous user
	 * 
	 * @param	username		Username to check for anonymity
	 * @return					Whether the username is anonymous
	 */
	static boolean isAnonymous(String username)
	{
		if (username.equals("anonymousUser")) {
			log.error(ANONYMOUS_USER_MESSAGE);
			return true;
		}
		return false;
	}
	
	/**
	 * Determines whether the current user is a registered user to the system and actively logged in.
	 * 
	 * Checks that a user is authenticated AND is not anonymous.
	 * 
	 * @return					Whether the user is actively logged in
	 */
	static boolean isActiveUser()
	{
		Authentication auth = currentAuthentication();
		String username = currentUsername();
		
		if (auth.isAuthenticated() && ! username.equals("anonymousUser"))
			return true;
		
		else
			return false;
	}
}
