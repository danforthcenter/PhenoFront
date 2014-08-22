package com.ddpsc.phenofront;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import src.ddpsc.authentication.CustomAuthenticationManager;
import src.ddpsc.config.Config;
import src.ddpsc.database.experiment.Experiment;
import src.ddpsc.database.experiment.ExperimentDao;
import src.ddpsc.database.queries.QueryFilter;
import src.ddpsc.database.queries.Query;
import src.ddpsc.database.queries.QueryDao;
import src.ddpsc.database.queries.QueryMetadata;
import src.ddpsc.database.snapshot.Snapshot;
import src.ddpsc.database.snapshot.SnapshotDao;
import src.ddpsc.database.tagging.MetadataFileReader;
import src.ddpsc.database.tagging.TaggingDao;
import src.ddpsc.database.user.User;
import src.ddpsc.database.user.UserDao;
import src.ddpsc.exceptions.ExperimentNotAllowedException;
import src.ddpsc.exceptions.MalformedConfigException;
import src.ddpsc.exceptions.NotImplementedException;
import src.ddpsc.exceptions.ObjectNotFoundException;
import src.ddpsc.exceptions.UserException;
import src.ddpsc.results.DownloadZipResult;
import src.ddpsc.results.ResultsBuilder;
import src.ddpsc.utility.Tuple;

import com.google.gson.Gson;

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
	
	private static final int NUMBER_QUERIES = 25;
	
	private static final String METADATA_INSTRUCTIONS_FILEPATH = "metadata modification instructions.txt";
	private static final String RESUME_DOWNLOAD_APPLICATION_FILEPATH = "ResumeDownloadApplication.jar";
	
	@Autowired
	UserDao userData;
	
	@Autowired
	ExperimentDao experimentData;
	
	@Autowired
	QueryDao queryData;
	
	@Autowired
	SnapshotDao snapshotData;
	
	@Autowired
	TaggingDao taggingData;
	
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

		User user = null;
		try {
			user = userData.findByUsername(username);
			model.addAttribute("user", user);
		}
		catch (Exception e) {
			return ControllerHelper.handleUserDataGETExceptions(e, model, username, "retrieve the user's data", log);
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
	 * @param	experiment			The experiment to load
	 * @return 						Http response on whether the experiment will be loaded
	 * @throws IOException 
	 */
	@RequestMapping(value = "/selection", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> loadExperimentAction(
			@ModelAttribute("user")		User user,
			@RequestParam("experiment") String experiment) throws IOException
	{
		String username = user.getUsername();
		log.info("Attempting to load the experiment " + experiment + " for the user " + user.getUsername());
		
		if (ControllerHelper.isAnonymous(username))
			return new ResponseEntity<String>("ERROR: " + ControllerHelper.ANONYMOUS_USER_MESSAGE, HttpStatus.FORBIDDEN);
		
		try {
			Experiment experimentObject = user.getExperimentByExperimentName(experiment);
			user.setActiveExperiment(experimentObject);
			
			snapshotData.setSnapshotExperiment(experiment);
			
			log.info("The experiment " + experiment + " selected by user " + user.getUsername() + " loaded successfully.");
			return new ResponseEntity<String>("Experiment Loaded.", HttpStatus.OK);
		}
		catch (ExperimentNotAllowedException e) {
			log.info("The experiment " + experiment + " selected by user " + user.getUsername() + " does not exist or is not allowed.");
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
	
	/**
	 * Gives the user a preview of the query that can be made in the query builder.
	 * 
	 * Responds to the POST request with the CSV file of the user defined query.
	 * 
	 * Many parameters are optional. If they're excluded, it is assumed they take on
	 * the broadest possible value (e.g, if startTime is null, then snapshots can go as far back
	 * in history as need be). The booleans default to false.
	 * 
	 * Required parameters:
	 * 		experiment
	 * 
	 * @param locale					Timezone of user
	 * @param model						Active model for user instance
	 * 
	 * @param experiment		Name of the current experiment being queried
	 * 
	 * @param tags						Metadata tags used to either include or exclude snapshots from the result
	 * @param tagRequirements			How many of the tags the query results will have
	 * 
	 * @param barcode				Returned snapshots have a barcode matching this regex pattern
	 * @param measurementLabel			Returned snapshots have a measurement label matchign this regex pattern
	 * @param startTime					No snapshots occur before this time
	 * @param endTime					No snapshots occur after this time
	 * 
	 * @param includeWatering			Whether to include snapshots that are only watering data
	 * 
	 * @param visibleListImages			Whether to include visible light images in the returned snapshots
	 * @param includeFluorescentImages	Whether to include fluorescent images in the returned snapshots
	 * @param includeNearInfraredImages	Whether to include near IR images in the returned snapshots
	 * 
	 * @return							An HTTP response containing the query as a CSV
	 * 
	 * @throws IOException						Thrown if the user disconnects from the server
	 */
	@RequestMapping(value = "/userarea/querypreview", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> queryPreview(
			Locale locale,
			Model model,
			@RequestParam(value = "experiment",			required = true) String experiment,
			
			@RequestParam(value = "barcode",			required = false,	defaultValue = "") String barcode,
			@RequestParam(value = "measurementLabel",	required = false,	defaultValue = "") String measurementLabel,
			@RequestParam(value = "startTime",			required = false,	defaultValue = "") String startTime,
			@RequestParam(value = "endTime",			required = false,	defaultValue = "") String endTime,
			
			@RequestParam(value = "includeWatering",	required = false, 	defaultValue = "false")	boolean includeWatering,
			
			@RequestParam(value = "includeVisible",		defaultValue = "false")	boolean includeVisibleLightImages,
			@RequestParam(value = "includeFluorescent",	defaultValue = "false")	boolean includeFluorescentImages,
			@RequestParam(value = "includeInfrared",	defaultValue = "false")	boolean includeNearInfraredImages )
					throws IOException
	{
		String username = ControllerHelper.currentUsername();
		log.info("Requesting a custom query preview for user " + username
				+ "\nExperiment: " + experiment
				+ "\nPlant Barcode: " + barcode
				+ "\nMeasurement Label: " + measurementLabel
				+ "\nStart Time: " + startTime
				+ "\nEnd Time: " + endTime
				+ "\nInclude Watering?: " + includeWatering
				+ "\nInclude Visible?: " + includeVisibleLightImages
				+ "\nInclude Fluorescent?: " + includeFluorescentImages
				+ "\nInclude Infrared?: " + includeNearInfraredImages );
		
		try {
			User user = userData.findByUsername(username);
			
			Experiment activeExperiment = experimentData.getByName(experiment);
			snapshotData.setSnapshotExperiment(experiment);
			
			// If the experiment isn't valid
			if (activeExperiment == null) {
				log.info("The active experiment for the user " + username + " was found to not be set."
						+ "The system doesn't know where to look. Terminating custom query preview.");
				return new ResponseEntity<String>("Invalid experiment name.", HttpStatus.BAD_REQUEST);
			}
			else {
				
				log.info("Querying database for snaphot preview.");
				Query query = new Query(
						experiment,
						barcode,
						measurementLabel,
						startTime,
						endTime,
						includeWatering,
						includeVisibleLightImages,
						includeFluorescentImages,
						includeNearInfraredImages );
				
				Timestamp timeOfQuery = new Timestamp(DateTime.now().getMillis());
				List<Snapshot> snapshots = snapshotData.executeCustomQuery(query);
				
				
				// Gather information on the query
				QueryMetadata metadata = new QueryMetadata(
						user.getUserId(),
						user.getUsername(),
						timeOfQuery,
						snapshots.size(),
						Snapshot.getTiles(snapshots).size(),
						"");
				query.metadata = metadata;
				
				log.info("The custom query preview for user " + username + " with active experiment " + experiment + " is successful.");
				
				Map<String, Object> queryStructure = new HashMap<String, Object>();
				queryStructure.put("query", query);
				queryStructure.put("snapshots", snapshots);
				String json = (new Gson()).toJson(queryStructure);
				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				return new ResponseEntity<String>(json, headers, HttpStatus.CREATED);
			}
		}
		
		
		catch (Exception e) {
			return ControllerHelper.handleCustomQueryPOSTExceptions(e, username, experiment, measurementLabel, barcode, log);
		}
	}
	
	/**
	 * Sends the user a download of all files found by the supplied query
	 * 
	 * Does not require the user to be logged in. Instead, it requires a valid download key.
	 * New download keys are issued every time the user enters the query builder or resume download pages
	 * 
	 * Many parameters are optional. If they're excluded, it is assumed they take on
	 * the broadest possible value (e.g, if startTime is null, then snapshots can go as far back
	 * in history as need be).
	 * 
	 * @param locale					Timezone of user
	 * @param model						Active model for user instance
	 * 
	 * @param downloadKey				A number that maps to a user generating a download link from the query builder
	 * 
	 * @param experiment				Name of the current experiment being queried
	 * 
	 * @param tags						Metadata tags used to either include or exclude snapshots from the result
	 * @param tagRequirements				Whether the tags act as inclusion or exclusion
	 * 
	 * @param plantBarcode				Returned snapshots have a barcode matching this regex pattern
	 * @param measurementLabel			Returned snapshots have a measurement label matchign this regex pattern
	 * @param startTime					No snapshots occur before this time
	 * @param endTime					No snapshots occur after this time
	 * 
	 * @param includeWatering			Whether to include snapshots that are only watering data
	 * 
	 * @param visibleListImages			Whether to include visible light images in the returned snapshots
	 * @param includeFluorescentImages	Whether to include fluorescent images in the returned snapshots
	 * @param includeNearInfraredImages	Whether to include near IR images in the returned snapshots
	 * 
	 * @return							A TCP download of the queried files
	 * 
	 * @throws IOException						Thrown if the user disconnects from the server
	 */
	@RequestMapping(value = "/massdownload", method = RequestMethod.GET)
	public void massDownloadAction(
			HttpServletResponse response,
			Locale locale,
			Model model,
			
			@RequestParam(value = "downloadKey",		required = true) String downloadKey,
			
			@RequestParam(value = "experiment",			required = false) String experiment,
			
			@RequestParam(value = "barcode",			required = false,	defaultValue = "") String plantBarcode,
			@RequestParam(value = "measurementLabel",	required = false,	defaultValue = "") String measurementLabel,
			@RequestParam(value = "startTime",			required = false,	defaultValue = "") String startTime,
			@RequestParam(value = "endTime",			required = false,	defaultValue = "") String endTime,
			
			@RequestParam(value = "includeWatering",	required = false,	defaultValue = "false")	boolean includeWatering,
			
			@RequestParam(value = "includeVisible",		required = true,	defaultValue = "false")	boolean includeVisibleLightImages,
			@RequestParam(value = "includeFluorescent",	required = true,	defaultValue = "false")	boolean includeFluorescentImages,
			@RequestParam(value = "includeInfrared",	required = true,	defaultValue = "false")	boolean includeNearInfraredImages,
			
			@RequestParam(value = "logQuery",			required = false,	defaultValue = "false")	boolean logQuery,
			
			@RequestParam(value = "convertJPEG",		required = false,	defaultValue = "false")	boolean convertJPEG )
					throws IOException
	{
		log.info("Requesting a mass download for download key ='" + downloadKey + "': "
				+ "\nExperiment: " + experiment
				+ "\nPlant Barcode: " + plantBarcode
				+ "\nMeasurement Label: " + measurementLabel
				+ "\nStart Time: " + startTime
				+ "\nEnd Time: " + endTime
				+ "\nInclude Watering?: " + includeWatering
				+ "\nInclude Visible?: " + includeVisibleLightImages
				+ "\nInclude Fluorescent?: " + includeFluorescentImages
				+ "\nInclude Infrared?: " + includeNearInfraredImages );
		
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
		
		User user = null;
		try {
			user = userData.findByUsername(System.getProperty(downloadKey));
		}
		catch (Exception e) {
			ControllerHelper.handleUserDataGETExceptions(e, response, "unknown", "execute a mass download", log);
			return;
		}
		
		try {
			// Setup the snapshot data to pull from the appropriate experiment
		    String username = user.getUsername();
			Experiment activeExperiment = experimentData.getByName(experiment);
			snapshotData.setSnapshotExperiment(experiment);
			
			// If the experiment isn't valid
			if (activeExperiment == null) {
				log.info("The active experiment for the user " + username + " was found to not be set."
						+ "The system doesn't know where to look. Terminating mass download.");
				response.sendError(403, "Invalid experiment selection");
				response.flushBuffer();
				return;
			}
			
			// Begin download response
			Timestamp timeOfQuery = new Timestamp(DateTime.now().getMillis());
			
			response.setHeader("Transfer-Encoding", "chunked");
			response.setHeader("Content-type", "text/plain");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + "Snapshots " + timeOfQuery + ".zip\"");
			response.flushBuffer();
			
		    log.info("Querying database for snapshots and tiles.");
		    Query query = new Query(
					experiment,
					plantBarcode,
					measurementLabel,
					startTime,
					endTime,
					includeWatering,
					includeVisibleLightImages,
					includeFluorescentImages,
					includeNearInfraredImages);
		    
			List<Snapshot> snapshots = snapshotData.executeCustomQuery(query);
			
			
			// Gather information on the query
			QueryMetadata metadata = new QueryMetadata(
					user.getUserId(),
					user.getUsername(),
					timeOfQuery,
					snapshots.size(),
					Snapshot.getTiles(snapshots).size(),
					"");
			query.metadata = metadata;
			
			int queryId = -1;
			if (logQuery) {
				queryId = queryData.addQuery(query);
				query.id = queryId;
			}
			
			Timestamp timeOfDownloadStart = new Timestamp(DateTime.now().getMillis());
			DownloadZipResult downloadedZip = new DownloadZipResult(-1, new ArrayList<Integer>(), false);
			
			try {
			    log.info("Got snapshots and tiles. Building results.");
				ResultsBuilder results = new ResultsBuilder(
						response.getOutputStream(),
						snapshots,
						activeExperiment,
						convertJPEG);
				
			    log.info("Writing zip archive.");
				downloadedZip = results.writeZipArchive();
				
				response.flushBuffer();
			}
			
			catch (IOException e) {
				log.info("The mass download for user " + username + " was interrupted because the connection was lost.");
			}
			
			Timestamp timeOfDownloadEnd = new Timestamp(DateTime.now().getMillis());
			
			// Permanently log the query
			if (logQuery) {
				queryData.setQuerySize(queryId, downloadedZip.size);
				queryData.setDownloadStart(queryId, timeOfDownloadStart);
				queryData.setDownloadEnd(queryId, timeOfDownloadEnd);
				queryData.setInterrupted(queryId, ! downloadedZip.succeeded);
				queryData.setMissedSnapshots(queryId, downloadedZip.missedSnapshots);
			}
			
			log.info("The mass download for user " + username + " with active experiment " + experiment + " is successful.");
			
		}
		
		catch (Exception e) {
			ControllerHelper.handleCustomQueryPOSTExceptions(
					e,
					response,
					"mass download snapshots", 
					experiment,
					measurementLabel,
					plantBarcode,
					log);
		}
	}
	
	/**
	 * Gives the user a preview of the query that can be made in the query builder.
	 * 
	 * Responds to the POST request with the CSV file of the user defined query.
	 * 
	 * Many parameters are optional. If they're excluded, it is assumed they take on
	 * the broadest possible value (e.g, if startTime is null, then snapshots can go as far back
	 * in history as need be).
	 * 
	 * @param locale					Timezone of user
	 * @param model						Active model for user instance
	 * 
	 * @param downloadKey				A number that maps to a user generating a download link from the query builder
	 * @param experiment				Name of the current experiment being queried
	 * @param snapshotIds				A CSV list of the snapshot IDs to download
	 * 
	 * @return							A TCP download of the requested snapshots
	 * 
	 * @throws IOException						Thrown if the user disconnects from the server
	 */
	@RequestMapping(value = "/snapshots", method = RequestMethod.GET)
	public void downloadSnapshotsAction(
			HttpServletResponse response,
			Locale locale,
			Model model,
			@RequestParam(value = "downloadKey",		required = true)	String downloadKey,
			@RequestParam(value = "experiment",			required = true)	String experiment,
			@RequestParam(value = "snapshotIds",		required = true)	List<Integer> snapshotIds,
			@RequestParam(value = "convertJPEG",	required = false,	defaultValue = "false")	boolean convertJPEG )
				throws IOException
	{
		log.info("Attempting to execute snapshots defined download for download key ='" + downloadKey + "'.");
		
		if (downloadKey == null) {
			log.info("The download key was null. Terminating snapshots defined download.");
			response.sendError(403, "Permission denied.");
			response.flushBuffer();
			return;
		}
		
		if (System.getProperty(downloadKey) == null) {
			log.info("The download key for the user was found to be null. Terminating snapshots defined download.");
			response.sendError(400, "Invalid download key");
			response.flushBuffer();
			return;
		}
		
		User user = null;
		try {
			user = userData.findByUsername(System.getProperty(downloadKey));
		}
		catch (Exception e) {
			ControllerHelper.handleUserDataGETExceptions(e, response, "unknown", "execute snapshots defined download", log);
			return;
		}
		
		try {
			// Setup the snapshot data to pull from the appropriate experiment
		    String username = user.getUsername();
			Experiment activeExperiment = experimentData.getByName(experiment);
			snapshotData.setSnapshotExperiment(experiment);
			
			// If the experiment isn't valid
			if (activeExperiment == null) {
				log.info("The active experiment for the user " + username + " was found to not be set."
						+ "The system doesn't know where to look. Terminating snapshots defined download.");
				response.sendError(403, "Invalid experiment selection");
				response.flushBuffer();
				return;
			}
			
			Timestamp timeOfQuery = new Timestamp(DateTime.now().getMillis());
			
			// Begin download response
			response.setHeader("Transfer-Encoding", "chunked");
			response.setHeader("Content-type", "text/plain");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + "Snapshots " + timeOfQuery + ".zip\"");
			response.flushBuffer();
			
		    log.info("Querying database for snapshots and tiles.");
			
			List<Snapshot> snapshots = snapshotData.findById(snapshotIds);
			
		    log.info("Got snapshots and tiles. Building results.");
			ResultsBuilder results = new ResultsBuilder(
					response.getOutputStream(),
					snapshots,
					activeExperiment,
					convertJPEG);
			
		    log.info("Writing zip archive.");
			results.writeZipArchive();
			response.flushBuffer();
			
			log.info("The snapshots defined download for user " + username + " with active experiment " + experiment + " is successful.");
		}
		
		catch (Exception e) {
			ControllerHelper.handleCustomQueryPOSTExceptions(
					e,
					response,
					"snapshots defined download", 
					experiment,
					"<NOT SPECIFIED BY USER>",
					"<NOT SPECIFIED BY USER>",
					log);
		}
	}
	
	/**
	 * Method for getting a snapshot and all associated images in a chunked manner. That is, a stream which will allow for a
	 * more responsive feeling download and for all image conversions to be done on the fly.
	 *
	 * @param response			The HTTP response to this action
	 * @param user				The user doing the downloading
	 * @param snapshotId		The ID of the snapshot to download
	 * 
	 * @throws IOException		Thrown if the client times out
	 */
	@RequestMapping(value = "/userarea/stream/{id}")
	public void streamSnapshot(
									HttpServletResponse	response,
			@ModelAttribute("user")	User				user,
			@PathVariable("id")		int					snapshotId)
					throws IOException
	{
		String username = user.getUsername();
		String experiment = user.getActiveExperiment().getExperimentName();
		log.info("Attempting to retrieve snapshot with id='" + snapshotId + "' for user " + username);
		
		// Begin response
		response.setHeader("Transfer-Encoding", "chunked");
		response.setHeader("Content-type", "text/plain");
		response.setHeader("Content-Disposition", "attachment; filename=\"Snapshot " + snapshotId + ".zip\"");
		response.flushBuffer();
		
		try {
			snapshotData.setSnapshotExperiment(experiment);
			Snapshot snapshot = snapshotData.findById(snapshotId);
			
			ResultsBuilder results = new ResultsBuilder(
					response.getOutputStream(),
					Arrays.asList(new Snapshot[]{snapshot}),
					user.getActiveExperiment(),
					false);
			
			results.writeZipArchive();
			
			log.info("Streaming download of snapshot with ID='" + snapshotId + "' for user + " + user.getUsername() + " succeeded.");
			response.flushBuffer();
		}
		
		catch (Exception e) {
			ControllerHelper.handleCustomQueryPOSTExceptions(
					e,
					response,
					"download the snapshot with id='" + snapshotId + "'",
					experiment,
					"<NOT SPECIFIED BY USER>",
					"<NOT SPECIFIED BY USER>",
					log);
		}
	}
	
	/**
	 * Sends the user to the query builder page, where they build a custom snapshot query. Upon submission, a key is provided
	 * to the user which validates their download (for use with wget and other command line tools)
	 * 
	 * These keys are stored in memory.
	 */
	@RequestMapping(value = "/userarea/querybuilder", method = RequestMethod.GET)
	public String queryBuilderAction(
									Locale	locale,
									Model	model,
			@ModelAttribute("user") User	user)
	{
		String username = user.getUsername();
		log.info("Loading query buidler page for user " + username);
		
		String downloadKey = DownloadManager.generateRandomKey(user);
		model.addAttribute("downloadKey", downloadKey);
		
		Experiment activeExperiment = user.getActiveExperiment();
		model.addAttribute("experiment", activeExperiment.getExperimentName());
		
		return "userarea-querybuilder";
	}
	
	@RequestMapping(value = "/userarea/queryexplorer", method = RequestMethod.GET)
	public String viewQueryHistory(Model model, @ModelAttribute("user") User user)
	{
		String username = user.getUsername();
		log.info("Accessing the query history page for user " + username);
		
		String downloadKey = DownloadManager.generateRandomKey(user);
		model.addAttribute("downloadKey", downloadKey);
		
		List<User> users = userData.findAllUsers();
		model.addAttribute("allUsers", users);
		
		model.addAttribute("experiment", user.getActiveExperiment().getExperimentName());
		
		log.info("Retrieved queries for query history page for user " + username);
		return "userarea-queryexplorer";
	}
	

	@RequestMapping(value = "/userarea/downloadresumeapplet")
	public void retrieveDownloadResumeJar(HttpServletResponse response) throws IOException
	{
		String username = ControllerHelper.currentUsername();
		log.info("Attempting to retrieve download resuming application for the user " + username);
		
		// Begin response
		response.setHeader("Transfer-Encoding", "chunked");
		response.setHeader("Content-type", "application/java-archive");
		response.setHeader("Content-Disposition", "attachment; filename=\"ResumeDownloadApplication.jar\"");
		response.flushBuffer();
		
		ClassPathResource c = new ClassPathResource("/");
		File f = c.getFile();
		System.out.println("DOWNLOAD FILE: " + f.getAbsolutePath() + "/" + RESUME_DOWNLOAD_APPLICATION_FILEPATH);
		InputStream input = new FileInputStream(f.getAbsolutePath() + "/" + RESUME_DOWNLOAD_APPLICATION_FILEPATH);
		FileCopyUtils.copy(input, response.getOutputStream());
		response.flushBuffer();
		
		log.info("Successfully retrieved download resuming application for the user " + username);
	}
	
	@RequestMapping(value = "/userarea/changemetadatainstructions")
	public void retrieveInstructions(HttpServletResponse response) throws IOException
	{
		String username = ControllerHelper.currentUsername();
		log.info("Attempting to retrieve metadata file modification structure instructions for the user " + username);
		
		// Begin response
		response.setHeader("Transfer-Encoding", "chunked");
		response.setHeader("Content-type", "text/plain");
		response.setHeader("Content-Disposition", "attachment; filename=\"metadata modification instructions.text\"");
		response.flushBuffer();
		
		ClassPathResource c = new ClassPathResource("/");
		File f = c.getFile();
		System.out.println("DOWNLOAD FILE " + f.getAbsolutePath() + "/" + METADATA_INSTRUCTIONS_FILEPATH);
		InputStream input = new FileInputStream(f.getAbsolutePath() + "/" + METADATA_INSTRUCTIONS_FILEPATH);
		FileCopyUtils.copy(input, response.getOutputStream());
		response.flushBuffer();
		
		log.info("Successfully retrieved metadata file modification structure instructions for the user " + username);
	}
	
	@RequestMapping(value = "/userarea/changemetadatabyfile", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> changeMetadataByFile(
			@RequestParam("file") MultipartFile file) throws IOException
	{
		log.info("Attempting to change metadata as specified by an uploaded file.");
		try {
			
			InputStream input = file.getInputStream();
			BufferedReader fileReader = new BufferedReader(new InputStreamReader(input));
			
			MetadataFileReader changes = new MetadataFileReader(fileReader);
			
			for (Integer id : changes.queryCommentChanges.keySet())
				queryData.setQueryComment(id, changes.queryCommentChanges.get(id));
			
			for (Integer id : changes.snapshotTagChanges.keySet())
				taggingData.changeSnapshotTag(id, changes.experiment, changes.snapshotTagChanges.get(id));
			
			for (Integer id : changes.tileTagChanges.keySet())
				taggingData.changeTileTag(id, changes.experiment, changes.tileTagChanges.get(id));
			
			log.info("Successfully changed metadata as specified by an uploaded file.");
			return new ResponseEntity<String>("Metadata changed.", HttpStatus.OK);
		}
		
		
		catch (MalformedConfigException e) {
			log.error("Could not read the metdata change file because it is not configured correctly. " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@RequestMapping(value = "/userarea/changemetadata", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> changeMetadata(
			Model model,
			@RequestParam(value = "experiment",		required = true)	String experiment,
			@RequestParam(value = "metadataType",	required = true)	String metadataType,
			@RequestParam(value = "ids",			required = true)	List<Integer> ids,
			@RequestParam(value = "metadata",		required = false)	String metadata,
			@RequestParam(value = "deleteMetadata",	required = false)	boolean deleteMetadata )
	{
		String username = ControllerHelper.currentUsername();
		log.info("Request to modify metadata from " + username + ": "
				+ "\nExperiment: " + experiment
				+ "\nMetadata Type: " + metadataType
				+ "\nIDs: " + ids
				+ "\nNew Metadata: " + metadata
				+ "\nDelete Action?: " + deleteMetadata);
		
		if (metadataType.equals("query")) {
			if (deleteMetadata == true)
				queryData.setQueryComments(ids, "");
			else
				queryData.setQueryComments(ids, metadata);
			
			return new ResponseEntity<String>("Query comments changed.", HttpStatus.OK);
		}
		
		if (metadataType.equals("snapshot")) {
			if (deleteMetadata == true)
				taggingData.removeSnapshotTags(ids, experiment);
			else
				taggingData.changeSnapshotTags(ids, experiment, metadata);
			
			return new ResponseEntity<String>("Snapshot tags changed.", HttpStatus.OK);
		}
		
		if (metadataType.equals("tile")) {
			if (deleteMetadata == true)
				taggingData.removeTileTags(ids, experiment);
			else
				taggingData.changeTileTags(ids, experiment, metadata);
			
			return new ResponseEntity<String>("Tile tag changed.", HttpStatus.OK);
		}
		
		log.fatal("Could not change metadata type " + metadataType + " for " + username + " because it is not recognized.");
		return new ResponseEntity<String>("Failure. Invalid type of metadata.", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@RequestMapping(value = "/userarea/queries", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> getQueries(
			Model model,
			@RequestParam(value = "experiment",			required = true)							String experiment,
			@RequestParam(value = "currentQueries",		required = false)							List<Integer> currentIds,
			@RequestParam(value = "queryUsername",		required = false, defaultValue = "")		String filterByUsername,
			@RequestParam(value = "onlyCommented",		required = false, defaultValue = "false")	boolean onlyCommented)
	{
		String username = ControllerHelper.currentUsername();
		log.info("Request for queries from " + username + ": "
				+ "\nExperiment: " + experiment
				+ "\nCurrentQueries: " + currentIds
				+ "\nOnly From Username: " + filterByUsername
				+ "\nOnly Commented?: " + onlyCommented);
		
		QueryFilter queryFilter = new QueryFilter(experiment);
		queryFilter.excludedIds		= currentIds;
		queryFilter.username		= filterByUsername;
		queryFilter.onlyCommented	= onlyCommented;
		queryFilter.limit			= NUMBER_QUERIES;
		
		List<Query> queries = queryData.getQueries(queryFilter);
		String jsonQueries = Query.toJSON(queries);
		
		log.info("Queries successfully retrieved for " + username);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(jsonQueries, headers, HttpStatus.CREATED);
	}
	
	/**
	 * Directs to a page with an applet to allow users to resume incomplete downloads
	 * 
	 * @param model			Internal system model to interact with the view
	 * @return 				Resume download page with applet
	 */
	@RequestMapping(value = "/userarea/resumedownload", method = RequestMethod.GET)
	public String resumeDownloadAction(
									Model	model,
			@ModelAttribute("user") User	user)
	{
		String username = ControllerHelper.currentUsername();
		log.info("Loading resume download page for user " + username);
		
		String downloadKey = DownloadManager.generateRandomKey(user);
		model.addAttribute("downloadKey", downloadKey);
		
		Experiment activeExperiment = user.getActiveExperiment();
		model.addAttribute("experiment", activeExperiment.getExperimentName());
		
		return "userarea-resumedownload";
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
			@ModelAttribute("user") User	user)
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
			User user = userData.findByUsername(username);

			if (CustomAuthenticationManager.validateCredentials(oldPassword, user) == false) {
				log.info("Password change for user " + username + " failed because the old password that was input was incorrect.");
				return new ResponseEntity<String>("Current password is incorrect.", HttpStatus.BAD_REQUEST);
			}
			
			this.userData.changePassword(user, encoder.encode(newPassword));
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
