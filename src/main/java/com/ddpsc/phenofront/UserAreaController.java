package com.ddpsc.phenofront;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.security.core.context.SecurityContextHolder;
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
import src.ddpsc.database.snapshot.Snapshot;
import src.ddpsc.database.snapshot.SnapshotDao;
import src.ddpsc.database.snapshot.SnapshotDaoImpl;
import src.ddpsc.database.user.DbUser;
import src.ddpsc.database.user.UserDao;
import src.ddpsc.exceptions.ExperimentNotAllowedException;
import src.ddpsc.exceptions.MalformedConfigException;
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
	private static Logger log = Logger.getLogger("controller");
	
	private static final String ANONYMOUS_USER_MESSAGE = "User not logged in.";
	
	private static final PasswordEncoder encoder = new StandardPasswordEncoder();
	
	@Autowired
	UserDao userData;

	@Autowired
	ExperimentDao experimentData;
	
	SnapshotDao snapshotData = new SnapshotDaoImpl();
	
	@Autowired
	ServletContext servletContext;
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// User Operations
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	/**
	 * Selects which experiment databases to pull information from.
	 * 
	 * @param model			TODO: Fill in
	 * @return				TODO: Fill in
	 */
	@RequestMapping(value = "/selectexperiment", method = RequestMethod.GET)
	public String selectAction(Model model)
	{
		String username = CurrentUsername();
		
		if (IsAnonymous(username)) {
			model.addAttribute("message", "Error: " + ANONYMOUS_USER_MESSAGE);
			return "error";
		}
		
		DbUser user = null;
		
		try {
			user = userData.findByUsername(username);
		}
		catch (CannotGetJdbcConnectionException e) {
			String connectionFailedMessage = "Could not retrieve the user's data because this server could not connect to the user data server.";
			model.addAttribute("message", connectionFailedMessage);
			log.info(connectionFailedMessage);
			return "error";
		}
		catch (UserException e) {
			String userInvalidMessage = "Could not retrieve the user's data because the data is corrupt or invalid.";
			model.addAttribute("message", userInvalidMessage);
			log.info(userInvalidMessage);
			return "error";
		}
		catch (ObjectNotFoundException e) {
			String userNotFoundMessage = "Could not retrieve the user's data because the user could not be found.";
			model.addAttribute("message", userNotFoundMessage);
			log.info(userNotFoundMessage);
			return "error";
		}
		
		model.addAttribute("user", user);
		List<Experiment> allExperiments = new ArrayList<Experiment>();
		
		try {
			allExperiments = experimentData.findAll();
		}
		catch (CannotGetJdbcConnectionException e) {
			String connectionFailedMessage = "Could not retrieve the user's data because this server could not connect to the experiment data server.";
			model.addAttribute("message", connectionFailedMessage);
			log.info(connectionFailedMessage);
			return "error";
		}
		
		user.setAllowedExperiments(allExperiments);
		
		// Assume all databases are public and allowed.
		// However, filter out databases with 'system' in their name
		final String keywordFilter = "System";
		ArrayList<Experiment> filteredExperiments = new ArrayList<Experiment>();
		
		for (Experiment experiment : user.getAllowedExperiments()) {
			String experimentName = experiment.getExperimentName().toLowerCase();
			
			if ( ! experimentName.contains(keywordFilter.toLowerCase())) {
				filteredExperiments.add(experiment);
			}
		}
		
		model.addAttribute("allowed", filteredExperiments);
		return "select";
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
	 * @param user				The user logged loading the experiment
	 * @param experimentName	The experiment to load
	 * @return					Http response on whether the experiment will be loaded
	 */
	@RequestMapping(value = "/selection", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> loadExperimentAction(
			@ModelAttribute("user") DbUser user,
			@RequestParam("experimentName") String experimentName)
	{
		String username = CurrentUsername();
		
		if (IsAnonymous(username))
			return new ResponseEntity<String>("ERROR: " + ANONYMOUS_USER_MESSAGE, HttpStatus.FORBIDDEN);
		
		try {
			Experiment experiment = user.getExperimentByExperimentName(experimentName);
			user.setActiveExperiment(experiment);
			
			DataSource experimentDataSouce = Config.experimentDataSource(experimentName); 
			snapshotData.setDataSource(experimentDataSouce);
			
			log.info("The experiment " + experimentName  + " selected by user " + user.shortDescribe() + " loaded successfully.");
			return new ResponseEntity<String>("Experiment Loaded.", HttpStatus.OK);
		}
		
		catch (ExperimentNotAllowedException e) {
			log.info("The experiment " + experimentName + " selected by user " + user.shortDescribe() + " does not exist or is not allowed.");
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
	 * @return		New model and view indicating the redirect
	 */
	@RequestMapping(value = "/userarea", method = RequestMethod.GET)
	public ModelAndView homeAction()
	{
		return new ModelAndView("redirect:" + "/userarea/results");
	}
	
	/**
	 * TODO: Implement visualization tool.
	 */
	@RequestMapping(value = "/userarea/visualize", method = RequestMethod.GET)
	public String visualizeAction(Locale locale, Model model)
	{
		// Consider using jqplotter, open source plotting tool
		// also could call R/perl/python -> file, then load file (would be very
		// unresponsive)
		return "visualize";
	}
	
	/**
	 * TODO: Implement scheduling tool? Is this in the specification for the site?
	 */
	@RequestMapping(value = "/userarea/schedule", method = RequestMethod.GET)
	public String scheduleAction(Locale locale, Model model)
	{
		return "schedule";
	}
	
	/**
	 * Primary action of user area. Shows the most recent 50 entries.
	 * 
	 * @param locale
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/userarea/results", method = RequestMethod.GET)
	public String resultsAction(Locale locale, Model model)
	{
		final int numSnapshots = 50;
		try {
			DateMidnight todayMidnight = new DateMidnight();
			
			List<Snapshot> snapshots = snapshotData.findLastN_withTiles(numSnapshots);
			
			model.addAttribute("date", todayMidnight.toString("EEEE, MMMM dd, YYYY"));
			model.addAttribute("snapshots", snapshots);
			return "userarea-results";
		}
		
		catch (CannotGetJdbcConnectionException e) {
			String connectionFailedMessage = "Could not retrieve the last " + numSnapshots + " snapshots because this server could not connect to the snapshot data server.";
			model.addAttribute("message", connectionFailedMessage);
			log.info(connectionFailedMessage);
			return "error";
		}
	}

	/**
	 * Sends the user to the query builder page, where they build a custom snapshot query.
	 * Upon submission, a key is provided to the user which validates their download  (for use with wget
	 * and other command line tools)
	 * 
	 * These keys are stored in memory.
	 */
	@RequestMapping(value = "/userarea/querybuilder", method = RequestMethod.GET)
	public String queryBuilderAction(Locale locale, Model model,
			@ModelAttribute("user") DbUser user)
	{
		// this is for testing remove please shit works
		// TODO: Should I remove the shit that works? Which shit is it exactly that works and ought to be removed?
		
//		try {
			String downloadKey = DownloadManager.generateRandomKey(user);
			
			model.addAttribute("downloadKey", downloadKey);
			model.addAttribute("activeExperiment", user.getActiveExperiment().getExperimentName());
			
			return "userarea-querybuilder";
//		}
		
//		catch (ActiveKeyException e) {
//			String keyMessage = "Could not download as the user is already downloading another item.";
//			model.addAttribute("message", keyMessage);
//			log.info(keyMessage);
//			return "error";
//		}
	}

	/**
	 * This action handles mass downloading of images. Downloads expect a valid
	 * downloadKey which is stored in the system properties files. Manually
	 * sets up experiment.
	 * 
	 * @param	locale
	 * @param	model
	 * @return
	 * 
	 * @throws	IOException
	 * @throws	ExperimentNotAllowedException
	 */
	@RequestMapping(value = "/massdownload", method = RequestMethod.GET)
	public void massDownloadAction(
			HttpServletResponse response,
			Locale locale,
			Model model,
			@RequestParam(value = "before", required = false) String before,
			@RequestParam(value = "after", required = false) String after,
			@RequestParam(value = "activeExperiment", required = false) String activeExperiment,
			@RequestParam(value = "plantBarcode", required = false) String plantBarcode,
			@RequestParam(value = "measurementLabel", required = false) String measurementLabel,
			@RequestParam(value = "downloadKey", required = true) String downloadKey,
			@RequestParam(value = "vis", defaultValue = "false") boolean vis,
			@RequestParam(value = "nir", defaultValue = "false") boolean nir,
			@RequestParam(value = "fluo", defaultValue = "false") boolean fluo)
			throws IOException, ExperimentNotAllowedException
	{
		// TODO: Reimplement 1 download per user limit
		// TODO: Look through this mess and figure out what's going on

		DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
		// error handling
		if (downloadKey == null) {
			response.sendError(403, "Permission denied.");
			response.flushBuffer();
			return;
		}
		if (System.getProperty(downloadKey) == null) {

			// property is null so we can't do anything?
			response.sendError(400, "Invalid download key");
			response.flushBuffer();
			return;
		}
		
		
		try {
			DbUser user = this.userData.findByUsername(System.getProperty(downloadKey));
			
			// check permissions and setup experiment for anonymous users
			List<Experiment> experiments = user.getAllowedExperiments();
			for (Experiment experiment : experiments) {
				if (experiment.getExperimentName().equals(activeExperiment)) {
					user.setActiveExperiment(experiment);
					try {
						snapshotData.setDataSource(Config.experimentDataSource(experiment.getExperimentName()));
					} catch (MalformedConfigException e) {
						e.printStackTrace();
						response.sendError(400, "Bad experiment configuration");
						response.flushBuffer();
					}
					break;
				}
			}
			if (user.getActiveExperiment() == null) {
				response.sendError(403, "Invalid experiment selection");
				response.flushBuffer();
				return;
			}
			// setup query
			List<Snapshot> snapshots;
			Timestamp tsBefore = null;
			Timestamp tsAfter = null;
			if (!before.equals("")) {
				DateTime dBefore = formatter.parseDateTime(before);
				tsBefore = new Timestamp(dBefore.getMillis());
			}
			if (!after.equals("")) {
				DateTime dAfter = formatter.parseDateTime(after);
				tsAfter = new Timestamp(dAfter.getMillis());
			}
			plantBarcode = "^" + plantBarcode;
	
			response.setHeader("Transfer-Encoding", "chunked");
			response.setHeader("Content-type", "text/plain");
			// TODO: Add filename option
			response.setHeader("Content-Disposition", "attachment; filename=\"" + "Snapshots" + downloadKey + ".zip\"");
			response.flushBuffer();
			snapshots = snapshotData.findCustomQueryAnyTime_imageJobs_withTiles(tsAfter, tsBefore, plantBarcode, measurementLabel);
			ResultsBuilder results = new ResultsBuilder(response.getOutputStream(), snapshots, user.getActiveExperiment(), nir, vis, fluo);
			results.writeZipArchive();
			response.flushBuffer();
		}

		catch (CannotGetJdbcConnectionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (UserException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (ObjectNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Expects the date to be returned with the format of MM/dd/yyyy HH:mm. Only returns image snapshots.
	 *
	 * returns a list of new image snapshots to display to the page.
	 * 
	 * @param locale
	 * @param model
	 * @param before	return only snapshots before this date
	 * @param after		return only snapshots after this date
	 * @return
	 */
	@RequestMapping(value = "/userarea/filterresults", method = RequestMethod.GET)
	public String filterResultsAction(
			Locale locale,
			Model model,
			@RequestParam("before") String before,
			@RequestParam("after") String after)
	{
		// TODO: Look through this too
		DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
		// if they are not empty...
		ArrayList<Snapshot> snapshots;
		if (!before.equals("") && !after.equals("")) {
			DateTime dBefore = formatter.parseDateTime(before);
			Timestamp tsBefore = new Timestamp(dBefore.getMillis());

			DateTime dAfter = formatter.parseDateTime(after);
			Timestamp tsAfter = new Timestamp(dAfter.getMillis());
			snapshots = (ArrayList<Snapshot>) snapshotData.findBetweenTimes_imageJobs(tsBefore, tsAfter);
			model.addAttribute("date", "Before: " + before + " After: " + after);
		} else if (!after.equals("")) {
			DateTime dAfter = formatter.parseDateTime(after);
			Timestamp tsAfter = new Timestamp(dAfter.getMillis());
			// slow filter manually
			snapshots = (ArrayList<Snapshot>) snapshotData.findAfterTimestamp_imageJobs(tsAfter);
			model.addAttribute("date", "After: " + after);

		} else {
			// get here then we know after is empty and before is active
			DateTime dBefore = formatter.parseDateTime(before);
			Timestamp tsThreeBefore = new Timestamp(dBefore.minusDays(3).getMillis());
			Timestamp tsBefore = new Timestamp(dBefore.getMillis());
			// slow
			snapshots = (ArrayList<Snapshot>) snapshotData.findBetweenTimes_imageJobs(tsBefore, tsThreeBefore);
			model.addAttribute("date", "Before: " + before + " After: " + tsThreeBefore);
		}
		model.addAttribute("snapshots", snapshots);
		return "userarea-results";
	}

	/**
	 * Method for getting a snapshot and all associated images in a chunked
	 * manner. That is, a stream which will allow for a more responsive feeling
	 * download and for all image conversions to be done on the fly.
	 *
	 * @param response
	 * @param user
	 * @param snapshotId
	 * @throws IOException
	 */
	@RequestMapping(value = "/userarea/stream/{id}")
	public void streamSnapshot(HttpServletResponse response,
			@ModelAttribute("user") DbUser user,
			@PathVariable("id") int snapshotId) throws IOException
	{
		response.setHeader("Transfer-Encoding", "chunked");
		response.setHeader("Content-type", "text/plain");
		response.setHeader("Content-Disposition", "attachment; filename=\"Snapshot" + snapshotId + ".zip\"");
		ArrayList<Snapshot> snapshots = new ArrayList<Snapshot>(1);
		try {
			snapshots.add(snapshotData.findByID_withTiles(snapshotId));
		}
		catch (Exception e)
		{
			// TODO: Do this too
		}
		// no magic
		boolean vis = true;
		boolean nir = true;
		boolean fluo = true;
		try {
			ResultsBuilder results = new ResultsBuilder(
					response.getOutputStream(), snapshots,
					user.getActiveExperiment(), vis, nir, fluo);
			results.writeZipArchive();
		} catch (IOException e) {
			System.err.println("Download was probably cancelled.");
		}
		response.flushBuffer();
	}

	@RequestMapping(value = "/userarea/status", method = RequestMethod.GET)
	public String statusAction(Locale locale, Model model)
	{
		return "status";
	}

	/**
	 * Profile editing request. Interface for users changing their password,
	 * managing downloads, people in their group, metadata etc... So far only
	 * password is implemented.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/userarea/profile", method = RequestMethod.GET)
	public String profileAction(Model model, @ModelAttribute("user") DbUser user)
	{
		String username = CurrentUsername();
		
		if (IsAnonymous(username)) {
			model.addAttribute("message", "Error: " + ANONYMOUS_USER_MESSAGE);
			return "error";
		}
		
		model.addAttribute("group", user.getGroup());
		return "userarea-profile";
	}

	/**
	 * The user's method of changing their password.
	 *
	 * @param oldPassword			Old password, to overwrite
	 * @param newPassword			New password
	 * @param validationPassword	Second copy of the new password to ensure it was typed right
	 * @return						Http response of whether the password was changed
	 */
	@RequestMapping(value = "/userarea/profile/changepass", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> changePasswordAction(
			@RequestParam("oldpass") String oldPassword,
			@RequestParam("newpass") String newPassword,
			@RequestParam("validate") String validationPassword)
	{
		String username = CurrentUsername();
		
		if (IsAnonymous(username)) {
			log.error("Anonymous User attempted to change their password.");
			return new ResponseEntity<String>("ERROR: " + ANONYMOUS_USER_MESSAGE, HttpStatus.BAD_REQUEST);
		}
		
		if ( ! newPassword.equals(validationPassword)) {
			String nonmatchingMessage = username + " attempted to change passwords, but the two new passwords do not match.";
			log.trace(nonmatchingMessage);
			return new ResponseEntity<String>("Passwords do not match!", HttpStatus.BAD_REQUEST);
		}
		
		try {
			DbUser user = userData.findByUsername(username);
			
			if (CustomAuthenticationManager.validateCredentials(oldPassword, user) == false)
				return new ResponseEntity<String>("Current password is incorrect.", HttpStatus.BAD_REQUEST);
			
			this.userData.changePassword(user, encoder.encode(newPassword));
			return new ResponseEntity<String>("Success!", HttpStatus.OK);
		}
		
		catch (CannotGetJdbcConnectionException e) {
			log.info("Change password for user " + username + " failed because this server could not connect to the user data server.");
			return new ResponseEntity<String>("Cannot connect to authentication server.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (UserException e) {
			log.info("Change password for user " + username + " failed because the user data on the server is corrupt or incomplete.");
			return new ResponseEntity<String>("User data corrupted.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (ObjectNotFoundException e) {
			log.info("Change password for user " + username + " failed because the user could not be found.");
			return new ResponseEntity<String>("User not found.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Helper Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	private String CurrentUsername()
	{
		return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	private boolean IsAnonymous(String username)
	{
		if (username.equals("anonymousUser")) {
			log.error(ANONYMOUS_USER_MESSAGE);
			return true;
		}
		return false;
	}
}
