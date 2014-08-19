package com.ddpsc.phenofront;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import src.ddpsc.exceptions.MalformedConfigException;
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
	 * @param	action		The action the controller is handling
	 * @param	log			The logger from the source class (otherwise the wrong logger would register this event)
	 * @return				The "error" view
	 */
	public static String handleJdbcException(Model model, String action, Logger log)
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
	 * @param	action		A string representing the action being modified taking the place of a predicate in a sentence
	 * @param	log			The logger of the source class
	 * @return				A response string indicating what form of failure has taken place
	 */
	public static String handleUserDataGETExceptions(Exception e, Model model, String username, String action, Logger log)
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
	 * @param	action		A string representing the action being modified taking the place of a predicate in a sentence
	 * @param	log			The logger of the source class
	 * @return				A response string indicating what form of failure has taken place
	 * @throws IOException	Thrown when servlet has IO access error
	 */
	public static void handleUserDataGETExceptions(Exception e, HttpServletResponse response, String username, String action, Logger log)
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
	 * @param	action		A string representing the action being modified taking the place of a predicate in a sentence
	 * @param	log			The logger of the source class
	 * @return				A response string indicating what form of failure has taken place
	 */
	public static ResponseEntity<String> handleUserDataPOSTExceptions(Exception e, int userId, String action, Logger log)
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
	 * @param	action		A string representing the action being modified taking the place of a predicate in a sentence
	 * @param	log			The logger of the source class
	 * @return				A response string indicating what form of failure has taken place
	 */
	public static ResponseEntity<String> handleUserDataPOSTExceptions(Exception e, String username, String action, Logger log)
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
	
	/**
	 * Handles a common set of exceptions involved in handling POST custom query requests
	 * 
	 * @param e					The exception thrown
	 * @param username			The username of the user logged in making the query
	 * @param experimentName	The user's current experiment
	 * @param measurementLabel	The measurement label regex of the custom query attempted
	 * @param plantBarcode		The plant barcode regex of the custom query attempted
	 * @param log				The logger of the source class
	 * @return					A response entity designating the kind of failure encountered based on the exception thrown
	 */
	public static ResponseEntity<String> handleCustomQueryPOSTExceptions(Exception e, String username, String experimentName, String measurementLabel, String plantBarcode, Logger log)
	{
		if (e instanceof CannotGetJdbcConnectionException) {
			log.error("Could not access the experiments server in search of experiments under the name " + experimentName + " for user " + username + ". Terminating mass download.", e);
			return new ResponseEntity<String>("Cannot connect to authentication server.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else if (e instanceof MalformedConfigException) {
			log.fatal(e.getMessage(), e);
			return new ResponseEntity<String>("Experiment database access misconfigured.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else if (e instanceof ObjectNotFoundException) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<String>("Experiment " + experimentName + " not found.", HttpStatus.BAD_REQUEST);
		}
		else { // (e instanceof DataIntegrityViolationException) {
			if (e.getCause() instanceof PSQLException) {
				log.error("Could not return a preview of the query because the user's regex was invalid. "
						+ "Barcode='" + plantBarcode  + "' and Measurement Label='" + measurementLabel + "'.", e);
				return new ResponseEntity<String>("Invalid regex, barcode='" + plantBarcode + "', measurement label='" + measurementLabel +"'.", HttpStatus.BAD_REQUEST);
			}
			log.fatal("Unhandled exception: " + e.getMessage(), e);
			return new ResponseEntity<String>("Fatal exception.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Handles a common set of exceptions involved in handling GET custom query requests
	 * 
	 * @param e					The exception thrown
	 * @param response			The servlet response
	 * @param experimentName	The user's current experiment
	 * @param measurementLabel	The measurement label regex of the custom query attempted
	 * @param plantBarcode		The plant barcode regex of the custom query attempted
	 * @param log				The source class's logger
	 * @throws IOException		Thrown if server access illegally interrupted
	 */
	public static void handleCustomQueryPOSTExceptions(Exception e, HttpServletResponse response, String action, String experimentName, String measurementLabel, String plantBarcode, Logger log)
			throws IOException
	{
		if (e instanceof CannotGetJdbcConnectionException) {
			log.info("Could not access the experiments server in search of experiments under the name " + experimentName +". Terminating " + action + ".");
			response.sendError(500, "Internal error: Could not access server. Could not " + action + ".");
			response.flushBuffer();
		}
		else if (e instanceof MalformedConfigException) {
			log.fatal(e.getMessage(), e);
			response.sendError(400, "Improperly formed database configuration. Could not " + action + ".");
			response.flushBuffer();
		}
		else if (e instanceof ObjectNotFoundException) {
			log.error(e.getMessage(), e);
			response.sendError(400, "Experiment not found. Could not " + action + ".");
			response.flushBuffer();
		}
		else { // (e instanceof DataIntegrityViolationException) {
			if (e.getCause() instanceof PSQLException) {
				log.error("Could not " + action + " because the user's regex was invalid. "
						+ "Barcode='" + plantBarcode  + "' and Measurement Label='" + measurementLabel + "'.", e);
				response.sendError(403, "Invalid regex, barcode='" + plantBarcode + "', measurement label='" + measurementLabel +"'.");
				response.flushBuffer();
			}
			else {
				log.fatal("Unhandled exception: " + e.getMessage(), e);
				response.sendError(500, "Unknown error.");
				response.flushBuffer();
			}
		}
	}
}
