package com.ddpsc.phenofront;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import src.ddpsc.authentication.CustomAuthenticationManager;
import src.ddpsc.config.Config;
import src.ddpsc.database.experiment.Experiment;
import src.ddpsc.database.experiment.ExperimentDao;
import src.ddpsc.database.snapshot.CustomQuerySettings;
import src.ddpsc.database.snapshot.Snapshot;
import src.ddpsc.database.snapshot.SnapshotDao;
import src.ddpsc.database.snapshot.SnapshotDaoImpl;
import src.ddpsc.database.user.DbUser;
import src.ddpsc.database.user.UserDao;
import src.ddpsc.exceptions.ExperimentNotAllowedException;
import src.ddpsc.exceptions.MalformedConfigException;
import src.ddpsc.exceptions.NotImplementedException;
import src.ddpsc.exceptions.UserException;
import src.ddpsc.exceptions.ObjectNotFoundException;
import src.ddpsc.results.ResultsBuilder;

/**
 * Controller responsible for handling users actions such as requesting experiments.
 *
 * @author shill, cjmcentee
 *
 */
@SessionAttributes({ "user", "experiment" })
@Controller
public class UserAreaController
{
	private static final Logger log = Logger.getLogger(UserAreaController.class);
	
	private static final PasswordEncoder encoder = new StandardPasswordEncoder();
	
	@Autowired
	UserDao userDataSource;
	
	@Autowired
	ExperimentDao experimentData;
	
	SnapshotDao snapshotData = new SnapshotDaoImpl();
	
	@Autowired
	ServletContext servletContext;
	
	/**
	 * Selects which experiment databases to pull information from.
	 * 
	 * @param model			Internal system model to interact with the view
	 * @return 				An experiment selection page, or error page
	 */
	@RequestMapping(value = "/selectexperiment", method = RequestMethod.GET)
	public String selectAction(Model model)
	{
		String username = ControllerHelper.currentUsername();
		log.info("Selecting experiments for user " + username);

		if (ControllerHelper.isAnonymous(username)) {
			model.addAttribute("message", "Error: " + ControllerHelper.ANONYMOUS_USER_MESSAGE);
			return "error";
		}

		DbUser user = null;
		try {
			user = userDataSource.findByUsername(username);
			model.addAttribute("user", user);
		}
		catch (Exception e) {
			return ControllerHelper.handleUserDataGETExceptions(e, model, username, log, "retrieve the user's data");
		}
		
		try {
			Set<Experiment> allExperiments = experimentData.findAll();
			
			user.setAllowedExperiments(allExperiments);
			Set<Experiment> allowedExperiments = user.getAllowedExperiments();

			// Assume all databases are public and allowed.
			model.addAttribute("allowed", allowedExperiments);
			log.info("Experiments " + Experiment.toString(allowedExperiments) + " have been added to user " + username + " as allowed experiments.");
			return "select";
		}
		catch (CannotGetJdbcConnectionException e) {
			String connectionFailedMessage = "Could not retrieve the user's data because this server could not connect to the experiment data server.";
			model.addAttribute("message", connectionFailedMessage);
			log.info(connectionFailedMessage);
			return "error";
		}
	}

	/**
	 * Handles the experiment selection.
	 * 
	 * Expects the user to be authenticated and a part of the SessionModel.
	 * 
	 * Reads the experiment data source from a configuration file.
	 * 
	 * @see Config
	 * 
	 * @param	user				The user logged loading the experiment
	 * @param	experimentName		The experiment to load
	 * @return 						Http response on whether the experiment will be loaded
	 */
	@RequestMapping(value = "/selection", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> loadExperimentAction(
			@ModelAttribute("user")			DbUser user,
			@RequestParam("experimentName") String experimentName)
	{
		String username = user.getUsername();
		log.info("Attempting to load the experiment " + experimentName + " for the user " + user.getUsername());
		
		if (ControllerHelper.isAnonymous(username))
			return new ResponseEntity<String>("ERROR: " + ControllerHelper.ANONYMOUS_USER_MESSAGE, HttpStatus.FORBIDDEN);
		
		try {
			Experiment experiment = user.getExperimentByExperimentName(experimentName);
			user.setActiveExperiment(experiment);
			
			DataSource experimentDataSouce = Config.experimentDataSource(experimentName);
			snapshotData.setDataSource(experimentDataSouce);
			
			log.info("The experiment " + experimentName + " selected by user " + user.getUsername() + " loaded successfully.");
			return new ResponseEntity<String>("Experiment Loaded.", HttpStatus.OK);
		}
		catch (ExperimentNotAllowedException e) {
			log.info("The experiment " + experimentName + " selected by user " + user.getUsername() + " does not exist or is not allowed.");
			return new ResponseEntity<String>("Experiment does not exist or is not allowed.", HttpStatus.BAD_REQUEST);
		}
		catch (MalformedConfigException e) {
			log.fatal("Database connection configuration file is not written correctly (probably ltdatabase.config).", e);
			return new ResponseEntity<String>("Could not access experiment server.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Redirects to results view as the default userarea page.
	 * 
	 * @return New model and view indicating the redirect
	 */
	@RequestMapping(value = "/userarea", method = RequestMethod.GET)
	public ModelAndView homeAction()
	{
		log.info("Redirecting user " + ControllerHelper.currentUsername() + " to results view");
		return new ModelAndView("redirect:" + "/userarea/querybuilder");
	}

	/**
	 * @throws NotImplementedException 
	 */
	@RequestMapping(value = "/userarea/visualize", method = RequestMethod.GET)
	public String visualizeAction(Locale locale, Model model) throws NotImplementedException
	{
		// Consider using jqplotter, open source plotting tool
		// also could call R/perl/python -> file, then load file (would be very
		// unresponsive)
		log.info("Accessing the visualization page. This is an unimplemented feature.");
		throw new NotImplementedException();
	}

	/**
	 * @throws NotImplementedException 
	 */
	@RequestMapping(value = "/userarea/schedule", method = RequestMethod.GET)
	public String scheduleAction(Locale locale, Model model) throws NotImplementedException
	{
		log.info("Accessing the schedule page. This is an unimplemented feature.");
		throw new NotImplementedException();
	}
	
//	/**
//	 * Displays the results of the last search.
//	 * 
//	 * Currently shows the most recent 50 entries of the selected experiment.
//	 * 
//	 * @param locale	Geographical area the user is from
//	 * @param model		The internal system model to talk with the view
//	 * @return 			The results page, or error
//	 */
//	@RequestMapping(value = "/userarea/results", method = RequestMethod.GET)
//	public String resultsAction(Locale locale, Model model)
//	{
//		String username = ControllerHelper.currentUsername();
//		log.info("Attempting to display the results of the last search for user " + username);
//
//		final int numSnapshots = 50;
//		try {
//			DateMidnight todayMidnight = new DateMidnight();
//			model.addAttribute("date", todayMidnight.toString("EEEE, MMMM dd, YYYY"));
//
//			List<Snapshot> snapshots = snapshotData.findLastN_withTiles(numSnapshots);
//			model.addAttribute("snapshots", snapshots);
//
//			log.info("The results of the last search found correctly for user " + username);
//			return "userarea-results";
//		}
//		catch (CannotGetJdbcConnectionException e) {
//			String connectionFailedMessage = "Could not retrieve the last " + numSnapshots + " snapshots for user "
//					+ username + " because this server could not connect to the snapshot data server.";
//			model.addAttribute("message", connectionFailedMessage);
//			log.info(connectionFailedMessage);
//			return "error";
//		}
//	}
	
	/**
	 * 
	 */
	@RequestMapping(value = "/userarea/querypreview", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> previewAction(
			Locale locale,
			Model model,
			@RequestParam(value = "activeExperiment",	required = false) String activeExperimentName,
			
			@RequestParam(value = "plantBarcode",		required = false,	defaultValue = "") String plantBarcode,
			@RequestParam(value = "measurementLabel",	required = false,	defaultValue = "") String measurementLabel,
			@RequestParam(value = "startTime",			required = false,	defaultValue = "") String startTime,
			@RequestParam(value = "endTime",			required = false,	defaultValue = "") String endTime,
			@RequestParam(value = "userRestriction",	required = false,	defaultValue = "") String restrictedUsers,
			
			@RequestParam(value = "watering",	defaultValue = "false")	boolean includeWatering,
			@RequestParam(value = "vis",		defaultValue = "false")	boolean visibileLightImages,
			@RequestParam(value = "nir",		defaultValue = "false")	boolean nearInfraredImages,
			@RequestParam(value = "fluo",		defaultValue = "false")	boolean fluorescentImages
			)
					throws IOException, ExperimentNotAllowedException
	{
		String username = ControllerHelper.currentUsername();
		log.info("Attempting to return the preview results of the custom query for user " + username);
		
		DbUser user = null;
		try {
			user = userDataSource.findByUsername(username);
		}
		catch (Exception e) {
			return ControllerHelper.handleUserDataPOSTExceptions(e, username, log, "to return the preview a custom query");
		}
		
		try {
			// Setup the snapshot data to pull from the appropriate experiment
			Set<Experiment> allExperiments = experimentData.findAll();
			user.setAllowedExperiments(allExperiments);
			
			Experiment activeExperiment = experimentData.getByName(activeExperimentName);
			user.setActiveExperiment(activeExperiment);
			
			snapshotData.setDataSource(Config.experimentDataSource(activeExperimentName));
			
			// If none of the user's experiments match the active experiment
			if (activeExperiment == null) {
				log.info("The active experiment for the user " + username + " was found to not be set."
						+ "The system doesn't know where to look. Terminating custom query preview.");
			}
			
			CustomQuerySettings querySettings = new CustomQuerySettings(plantBarcode, measurementLabel, startTime, endTime, restrictedUsers, includeWatering);
			List<Snapshot> snapshots = snapshotData.findCustomQueryAnyTime_imageJobs(querySettings);
			log.info("The custom query preview for user " + username + " with active experiment " + activeExperimentName + " is successful.");
			return new ResponseEntity<String>(Snapshot.toCSV(snapshots, true), HttpStatus.OK);
		}
		
		
		catch (CannotGetJdbcConnectionException e) {
			log.error("Could not access the experiments server in search of experiments under the name " + activeExperimentName + " for user " + username + ". Terminating mass download.", e);
			return new ResponseEntity<String>("Cannot connect to authentication server.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (MalformedConfigException e) {
			log.fatal(e.getMessage(), e);
			return new ResponseEntity<String>("Experiment database access misconfigured.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (ObjectNotFoundException e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<String>("Experiment " + activeExperimentName + " not found.", HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Sends the user to the query builder page, where they build a custom snapshot query. Upon submission, a key is provided
	 * to the user which validates their download (for use with wget and other command line tools)
	 * 
	 * These keys are stored in memory.
	 * 
	 * TODO: Determine if the one unique download feature is to be implemented
	 */
	@RequestMapping(value = "/userarea/querybuilder", method = RequestMethod.GET)
	public String queryBuilderAction(
									Locale	locale,
									Model	model,
			@ModelAttribute("user") DbUser	user)
	{
		String username = user.getUsername();
		log.info("Attempting to retrieve download key for user " + username);
		
		String downloadKey = DownloadManager.generateRandomKey(user);
		model.addAttribute("downloadKey", downloadKey);
		
		Experiment activeExperiment = user.getActiveExperiment();
		model.addAttribute("activeExperiment", activeExperiment.getExperimentName());
		
		log.info("Retrieved download key for user " + username + " and queried the active experiment " + activeExperiment.getExperimentName());
		return "userarea-querybuilder";
	}

	/**
	 * This action handles mass downloading of images. Manually sets up experiment.
	 * 
	 * Requires a valid download key.
	 * 
	 * @param locale			Geographical location of the user
	 * @param model
	 * 
	 * @throws IOException						Thrown if the client times out
	 * @throws ExperimentNotAllowedException	Thrown if the experiment is not allowed by the user, or does not exist
	 */
	@RequestMapping(value = "/massdownload", method = RequestMethod.GET)
	public void massDownloadAction(
			HttpServletResponse response,
			Locale locale,
			Model model,
			@RequestParam(value = "activeExperiment",	required = false) String activeExperimentName,
			
			@RequestParam(value = "plantBarcode",		required = false,	defaultValue = "") String plantBarcode,
			@RequestParam(value = "measurementLabel",	required = false,	defaultValue = "") String measurementLabel,
			@RequestParam(value = "startTime",			required = false,	defaultValue = "") String startTime,
			@RequestParam(value = "endTime",			required = false,	defaultValue = "") String endTime,
			@RequestParam(value = "userRestriction",	required = false,	defaultValue = "") String restrictedUsers,
			
			@RequestParam(value = "downloadKey",		required = true) String downloadKey,
			
			@RequestParam(value = "vis",		defaultValue = "false")	boolean visibileLightImages,
			@RequestParam(value = "nir",		defaultValue = "false")	boolean nearInfraredImages,
			@RequestParam(value = "fluo",		defaultValue = "false")	boolean fluorescentImages,
			@RequestParam(value = "watering",	defaultValue = "false")	boolean includeWatering)
					throws IOException, ExperimentNotAllowedException
	{
		log.info("Attempting to execute a mass download.");
		
		// TODO: Reimplement 1 download per user limit? Yes
		if (downloadKey == null) {
			log.info("The download key was null. Terminating mass download.");
			response.sendError(403, "Permission denied.");
			response.flushBuffer();
			return;
		}
		
		if (System.getProperty(downloadKey) == null) {
			log.info("The download key for the user was found to be null. Terminating mass download.");
			response.sendError(400, "Invalid download key");
			response.flushBuffer();
			return;
		}
		
		DbUser user = null;
		try {
			user = userDataSource.findByUsername(System.getProperty(downloadKey));
		}
		catch (Exception e) {
			ControllerHelper.handleUserDataGETExceptions(e, response, username, log, "execute a mass download");
			return;
		}
		
		try {
			// Setup the snapshot data to pull from the appropriate experiment
			Set<Experiment> allExperiments = experimentData.findAll();
			user.setAllowedExperiments(allExperiments);
			
			Experiment activeExperiment = experimentData.getByName(activeExperimentName);
			user.setActiveExperiment(activeExperiment);	// TODO All these lines are because of a deprecated, but not yet removed, "allowed experiments" functionality, booo
			
			snapshotData.setDataSource(Config.experimentDataSource(activeExperimentName));
			
			// If none of the user's experiments match the active experiment
			if (activeExperiment == null) {
				log.info("The active experiment for the user " + username + " was found to not be set."
						+ "The system doesn't know where to look. Terminating mass download.");
				response.sendError(403, "Invalid experiment selection");
				response.flushBuffer();
				return;
			}
			
			response.setHeader("Transfer-Encoding", "chunked");
			response.setHeader("Content-type", "text/plain");
			// TODO: Add filename option
			response.setHeader("Content-Disposition", "attachment; filename=\"" + "Snapshots" + downloadKey + ".zip\"");
			response.flushBuffer();
			
		    log.info("Querying database for snapshots and tiles.");
			CustomQuerySettings querySettings = new CustomQuerySettings(plantBarcode, measurementLabel, startTime, endTime, restrictedUsers, includeWatering);
			List<Snapshot> snapshots = snapshotData.findCustomQueryAnyTime_imageJobs(querySettings);
		    log.info("Got snapshots and tiles. Building results.");
			ResultsBuilder results = new ResultsBuilder(
					response.getOutputStream(),
					snapshots,
					activeExperiment,
					nearInfraredImages,
					visibileLightImages,
		    log.info("Writing zip archive.");
			results.writeZipArchive();
			response.flushBuffer();
			log.info("The mass download for user " + username + " with active experiment " + activeExperimentName + " is successful.");
		}
		catch (CannotGetJdbcConnectionException e) {
			log.info("Could not access the experiments server in search of experiments under the name " + activeExperimentName + " for user " + username + ". Terminating mass download.");
			response.sendError(500, "Internal error: Could not access server.");
			response.flushBuffer();
			return;
		}
		catch (MalformedConfigException e) {
			log.fatal(e.getMessage(), e);
			response.sendError(400, "Improperly formed database configuration");
			response.flushBuffer();
		}
		catch (ObjectNotFoundException e) {
			log.error(e.getMessage(), e);
			response.sendError(400, "Experiment not found.");
			response.flushBuffer();
		}
	}

	/**
	 * Expects the date to be returned with the format of MM/dd/yyyy HH:mm. Only returns image snapshots.
	 * 
	 * Returns a list of new image snapshots to display to the page.
	 * 
	 * @param locale
	 * @param model
	 * @param startTime			return only snapshots after this date
	 * @param endTime			return only snapshots before this date
	 * @return
	 */
//	@RequestMapping(value = "/userarea/filterresults", method = RequestMethod.GET)
//	public String filterResultsAction(
//										Locale	locale,
//										Model	model,
//			@RequestParam("startTime")	String	startTime,
//			@RequestParam("endTime")	String	endTime)
//	{
//		
//		String username = ControllerHelper.currentUsername();
//		log.info("Attempting to filter snapshots by date for user " + username);
//		String filterMessage = "Filtered snapshots by dates: ";
//		List<Snapshot> snapshots;
//		
//		if (validTime(endTime) && validTime(startTime)) {
//			
//			Timestamp endTimestamp = timestampFromString(endTime);
//			Timestamp startTimestamp = timestampFromString(startTime);
//			
//			snapshots = snapshotData.findBetweenTimes_imageJobs(endTimestamp, startTimestamp);
//			
//			String dateMessage = "Start time: " + startTime + " End time: " + endTime;
//			log.info(filterMessage + dateMessage + " for user " + username);
//			model.addAttribute("date", dateMessage);
//		}
//		
//		else if (validTime(startTime)) {
//			Timestamp startTimestamp = timestampFromString(startTime);
//			
//			snapshots = snapshotData.findAfterTimestamp_imageJobs(startTimestamp);
//			
//			String dateMessage = "Start time: " + startTime;
//			log.info(filterMessage + dateMessage + " for user " + username);
//			model.addAttribute("date", dateMessage);
//		}
//		
//		else {
//			// TODO: Determine why we're subtracting 3 days here
//			Timestamp endTimestamp = timestampFromString(endTime);
//			
//			DateTime endDate = formatter.parseDateTime(endTime);
//			Timestamp startTimestamp_3daysBefore = new Timestamp(endDate.minusDays(3).getMillis());
//			
//			snapshots = snapshotData.findBetweenTimes_imageJobs(endTimestamp, startTimestamp_3daysBefore);
//			
//			String dateMessage = "Start time: " + startTimestamp_3daysBefore + " End time: " + endTime;
//			log.info(filterMessage + dateMessage + " for user " + username);
//			model.addAttribute("date", dateMessage);
//		}
//		
//		model.addAttribute("snapshots", snapshots);
//		return "userarea-results";
//	}
	
	/**
	 * Method for getting a snapshot and all associated images in a chunked manner. That is, a stream which will allow for a
	 * more responsive feeling download and for all image conversions to be done on the fly.
	 *
	 * @param response			The HTTP response to this action
	 * @param user				The user doing the downloading
	 * @param snapshotID		The ID of the snapshot to download
	 * 
	 * @throws IOException		Thrown if the client times out
	 */
	@RequestMapping(value = "/userarea/stream/{id}")
	public void streamSnapshot(
									HttpServletResponse	response,
			@ModelAttribute("user")	DbUser				user,
			@PathVariable("id")		int					snapshotID)
					throws IOException
	{
		// Download header
		response.setHeader("Transfer-Encoding", "chunked");
		response.setHeader("Content-type", "text/plain");
		response.setHeader("Content-Disposition", "attachment; filename=\"Snapshot" + snapshotID + ".zip\"");
		
		
		List<Snapshot> snapshots = new ArrayList<Snapshot>();
		try {
			snapshots.add(snapshotData.findByID_withTiles(snapshotID));
		}
		catch (CannotGetJdbcConnectionException e) {
			log.info("Streaming download of snapshot with ID='" + snapshotID + "' for user + " + user.getUsername()
					+ " failed because this server could not connect to the user data server.");
			response.flushBuffer();
			return;
		}
		catch (ObjectNotFoundException e) {
			log.info("Streaming download of snapshot with ID='" + snapshotID + "' for user + " + user.getUsername()
					+ " failed because the user could not be found.");
			response.flushBuffer();
			return;
		}
		
		// TODO: Read image inclusion from model
		boolean vis = true;
		boolean nir = true;
		boolean fluo = true;
		try {
			ResultsBuilder results = new ResultsBuilder(response.getOutputStream(), snapshots, user.getActiveExperiment(), vis, nir, fluo);
			results.writeZipArchive();
		}
		catch (IOException e) {
			log.info("Streaming download of snapshot with ID='" + snapshotID + "' for user + " + user.getUsername()
					+ " failed because the download was cancelled.");
			response.flushBuffer();
			return;
		}
		
		log.info("Streaming download of snapshot with ID='" + snapshotID + "' for user + " + user.getUsername() + " succeeded.");
		response.flushBuffer();
	}
	
	@RequestMapping(value = "/userarea/status", method = RequestMethod.GET)
	public String statusAction(
			Locale locale,
			Model model)
	{
		log.info("Accessing the status page. This is an unimplemented feature.");
		return "status";
	}

	/**
	 * Profile editing request. Interface for users changing their password, managing downloads, people in their group,
	 * metadata etc... So far only password is implemented.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/userarea/profile", method = RequestMethod.GET)
	public String profileAction(
									Model	model,
			@ModelAttribute("user") DbUser	user)
	{
		String username = ControllerHelper.currentUsername();
		log.info("Attempting to access the profile of user " + username);

		if (ControllerHelper.isAnonymous(username)) {
			model.addAttribute("message", "Error: " + ControllerHelper.ANONYMOUS_USER_MESSAGE);
			log.info("Could not access the profile of the user because they aren't logged in.");
			return "error";
		}

		model.addAttribute("group", user.getGroup());
		log.info("Successfully access the profile of the user " + username);
		return "userarea-profile";
	}

	/**
	 * The user's method of changing their password.
	 *
	 * @param oldPassword			Old password, to overwrite
	 * @param newPassword			New password
	 * @param validationPassword	Second copy of the new password to ensure it was typed right
	 * @return 						Http response of whether the password was changed
	 */
	@RequestMapping(value = "/userarea/profile/changepass", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> changePasswordAction(
			@RequestParam("oldpass")  String oldPassword,
			@RequestParam("newpass")  String newPassword,
			@RequestParam("validate") String validationPassword)
	{
		String username = ControllerHelper.currentUsername();
		log.info("Attempting to change the password of the user " + username);
		
		if (ControllerHelper.isAnonymous(username)) {
			log.info("Password change failed because the user was not logged in.");
			return new ResponseEntity<String>("ERROR: " + ControllerHelper.ANONYMOUS_USER_MESSAGE, HttpStatus.BAD_REQUEST);
		}

		if (!newPassword.equals(validationPassword)) {
			log.info("Password change for user " + username + " failed because the two new passwords do not match.");
			return new ResponseEntity<String>("Passwords do not match!", HttpStatus.BAD_REQUEST);
		}

		try {
			DbUser user = userDataSource.findByUsername(username);

			if (CustomAuthenticationManager.validateCredentials(oldPassword, user) == false) {
				log.info("Password change for user " + username + " failed because the old password that was input was incorrect.");
				return new ResponseEntity<String>("Current password is incorrect.", HttpStatus.BAD_REQUEST);
			}
			
			this.userDataSource.changePassword(user, encoder.encode(newPassword));
			log.info("Password change for user " + username + " successful.");
			return new ResponseEntity<String>("Success!", HttpStatus.OK);
		}
		
		catch (CannotGetJdbcConnectionException e) {
			log.info("Password change for user " + username + " failed because this server could not connect to the user data server.");
			return new ResponseEntity<String>("Cannot connect to authentication server.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (UserException e) {
			log.info("Password change for user " + username + " failed because the user data on the server is corrupt or incomplete.");
			return new ResponseEntity<String>("User data corrupted.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (ObjectNotFoundException e) {
			log.info("Password change for user " + username + " failed because the user could not be found.");
			return new ResponseEntity<String>("User not found.", HttpStatus.BAD_REQUEST);
		}
	}
}
