package com.ddpsc.phenofront;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import src.ddpsc.exceptions.ObjectNotFoundException;
import src.ddpsc.exceptions.UserException;

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
	public static String currentUsername()
	{
		return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	/**
	 * Returns the current Authentication object for this context
	 * 
	 * @return			The current authentication object
	 */
	public static Authentication currentAuthentication()
	{
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	/**
	 * Determines whether the username is that of the anonymous user
	 * 
	 * @param	username		Username to check for anonymity
	 * @return					Whether the username is anonymous
	 */
	public static boolean isAnonymous(String username)
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
	public static boolean isActiveUser()
	{
		Authentication auth = currentAuthentication();
		String username = currentUsername();
		
		if (auth.isAuthenticated() && ! username.equals("anonymousUser"))
			return true;
		
		else
			return false;
	}
	
	/**
	 * Handles the very common CannotGetJdbcConnectionException for controller
	 * handlers that work with a Model
	 * 
	 * @param	model		The current model of the system
	 * @param	log			The logger from the source class (otherwise the wrong logger would register this event)
	 * @param	action		The action the controller is handling
	 * @return				The "error" view
	 */
	public static String handleJdbcException(Model model, Logger log, String action)
	{
		String connectionFailedMessage = "Could not " + action + " because this server could not connect to the user data server.";
		model.addAttribute("message", connectionFailedMessage);
		log.info(connectionFailedMessage);
		return "error";
	}
	
	/**
	 * Handles the three most common errors associated with GET requests to the usera area page.
	 * 
	 * @param	e			The currently thrown exception
	 * @param	model		DOM model
	 * @param	username	The username of the relevant user
	 * @param	log			The logger of the source class
	 * @param	action		A string representing the action being modified taking the place of a predicate in a sentence
	 * @return				A response string indicating what form of failure has taken place
	 */
	public static String handleUserDataGETExceptions(Exception e, Model model, String username, Logger log, String action)
	{
		if (e instanceof CannotGetJdbcConnectionException) {
			String connectionFailedMessage = "Could not " + action + " for " + username + " because this server could not connect to the user data server.";
			model.addAttribute("message", connectionFailedMessage);
			log.error(connectionFailedMessage, e);
			return "error";
		}
		else if (e instanceof UserException) {
			String userInvalidMessage = "Could not " + action + " for " + username + " because the data is corrupt or invalid.";
			model.addAttribute("message", userInvalidMessage);
			log.error(userInvalidMessage, e);
			return "error";
		}
		else if (e instanceof ObjectNotFoundException) {
			String userNotFoundMessage = "Could not " + action + " for " + username + " because the user could not be found.";
			model.addAttribute("message", userNotFoundMessage);
			log.error(userNotFoundMessage, e);
			return "error";
		}
		else {
			String unhandledMessage = "Unhandled exception on action: '" + action + "' for " + username + ".";
			model.addAttribute("message", unhandledMessage);
			log.fatal(unhandledMessage, e);
			return "error";
		}
	}
	
	/**
	 * Handles the three most common errors associated with GET requests to the usera area page.
	 * 
	 * @param	e			The currently thrown exception
	 * @param	response	Serlvet response to the request
	 * @param	username	The username of the relevant user
	 * @param	log			The logger of the source class
	 * @param	action		A string representing the action being modified taking the place of a predicate in a sentence
	 * @return				A response string indicating what form of failure has taken place
	 * @throws IOException	Thrown when servlet has IO access error
	 */
	public static void handleUserDataGETExceptions(Exception e, HttpServletResponse response, String username, Logger log, String action)
			throws IOException
	{
		if (e instanceof CannotGetJdbcConnectionException) {
			log.error("Could not " + action + " for " + username + " because this server could not connect to the user data server.", e);
			response.sendError(500, "Internal error: Could not access server.");
		}
		else if (e instanceof UserException) {
			log.error("Could not " + action + " for " + username + " because the data is corrupt or invalid.", e);
			response.sendError(500, "User data corrupt.");
		}
		else if (e instanceof ObjectNotFoundException) {
			log.error("Could not " + action + " for " + username + " because the user could not be found.", e);
			response.sendError(403, "Invalid download key.");
		}
		else {
			log.fatal("Unhandled exception on action: '" + action + "' for " + username + ".", e);
			response.sendError(500, "Unhandled fatal exception,");
		}
		
		response.flushBuffer();
	}
	
	/**
	 * Handles the three most common errors associated with modifying a user's data (e.g., change username, authority, etc.).
	 * 
	 * @param	e			The currently thrown exception
	 * @param	userId		The ID of the user being modified
	 * @param	log			The logger of the source class
	 * @param	action		A string representing the action being modified taking the place of a predicate in a sentence
	 * @return				A response string indicating what form of failure has taken place
	 */
	public static ResponseEntity<String> handleUserDataPOSTExceptions(Exception e, int userId, Logger log, String action)
	{
		if (e instanceof CannotGetJdbcConnectionException) {
			log.error("Could not " + action + " the user with ID='" + userId + "' because this server could not connect to the user data server.", e);
			return new ResponseEntity<String>("Cannot connect to authentication server.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else if (e instanceof UserException) {
			log.error("Could not " + action + " the user with ID='" + userId + "' because the user data on the server is corrupt or incomplete.", e);
			return new ResponseEntity<String>("User data corrupted.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else if (e instanceof ObjectNotFoundException) {
			log.error("Could not " + action + " the user with ID='" + userId + "' because that user could not be found.", e);
			return new ResponseEntity<String>("User not found.", HttpStatus.BAD_REQUEST);
		}
		else {
			log.fatal("Unhandled exception on action: '" + action + "' with the user with ID='" + userId + "'.", e);
			return new ResponseEntity<String>("Unknown error.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Handles the three most common errors associated with modifying a user's data (e.g., change username, authority, etc.).
	 * 
	 * @param	e			The currently thrown exception
	 * @param	username	The username of the current user
	 * @param	log			The logger of the source class
	 * @param	action		A string representing the action being modified taking the place of a predicate in a sentence
	 * @return				A response string indicating what form of failure has taken place
	 */
	public static ResponseEntity<String> handleUserDataPOSTExceptions(Exception e, String username, Logger log, String action)
	{
		if (e instanceof CannotGetJdbcConnectionException) {
			log.error("Could not " + action + " for " + username + " because this server could not connect to the user data server.", e);
			return new ResponseEntity<String>("Cannot connect to authentication server.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else if (e instanceof UserException) {
			log.error("Could not " + action + " for " + username + " because the data is corrupt or invalid.", e);
			return new ResponseEntity<String>("User data corrupted.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else if (e instanceof ObjectNotFoundException) {
			log.error("Could not " + action + " for " + username + " because the user could not be found.", e);
			return new ResponseEntity<String>("User not found.", HttpStatus.BAD_REQUEST);
		}
		else {
			log.fatal("Unhandled exception on action: '" + action + "' for " + username + ".", e);
			return new ResponseEntity<String>("Unknown error.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
