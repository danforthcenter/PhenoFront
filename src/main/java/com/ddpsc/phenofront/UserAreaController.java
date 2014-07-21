package com.ddpsc.phenofront;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
import src.ddpsc.config.ExperimentConfig;
import src.ddpsc.database.experiment.Experiment;
import src.ddpsc.database.experiment.ExperimentDao;
import src.ddpsc.database.snapshot.Snapshot;
import src.ddpsc.database.snapshot.SnapshotDao;
import src.ddpsc.database.snapshot.SnapshotDaoImpl;
import src.ddpsc.database.user.DbUser;
import src.ddpsc.database.user.UserDao;
import src.ddpsc.exceptions.ExperimentNotAllowedException;
import src.ddpsc.exceptions.MalformedConfigException;
import src.ddpsc.results.ResultsBuilder;

/**
 * Controller responsible for handling users.
 * 
 * @author shill
 *
 */
@SessionAttributes({"user", "experiment"})
@Controller
public class UserAreaController {
		@Autowired
		UserDao ud;

		@Autowired
		ExperimentDao ed;
		
		//added for filestreaming
		@Autowired
		ServletContext servletContext;
		
		SnapshotDao sd = new SnapshotDaoImpl();
		
		protected static Logger logger = Logger.getLogger("controller");
		/**
		 * 
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "/selectexperiment", method=RequestMethod.GET)
		public String selectAction(Model model){
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (username.equals("anonymousUser")){
				model.addAttribute("message", "Error: Not logged in.");
				return "error"; //lol suck it
			}
			
			DbUser user = ud.findByUsername(username);
			model.addAttribute("user", user);
			user.setAllowedExperiments(ed.findAll());
			model.addAttribute("allowed", user.getAllowedExperiments() );
			return "select";
		}
		
		/**
		 * Handles the experiment selection. Expects the user to be authentciated and a part of the SessionModel. It then
		 * will manually create a Snapshot datasource and assign it to the session.
		 * 
		 * @param user
		 * @param experimentName
		 * @return
		 */
		@RequestMapping(value ="/selection", method=RequestMethod.POST)
		public @ResponseBody ResponseEntity<String> loadExperimentAction(
					@ModelAttribute("user") DbUser user,
					@RequestParam("experimentName") String experimentName){
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (username.equals("anonymousUser")){
				return new ResponseEntity<String>("Error: Not allowed.", HttpStatus.FORBIDDEN);
			}
			try {
				Experiment experiment = user.getExperimentByExperimentName(experimentName);
				user.setActiveExperiment(experiment);
				//set our datasource bean
				sd.setDataSource(new ExperimentConfig().dataSource(experiment.getExperimentName()));
			} catch (ExperimentNotAllowedException e) {
				logger.warn("Experiment does not exist or is not allowed.");
				return new ResponseEntity<String>("Experiment is not allowed or does not exist.", HttpStatus.BAD_REQUEST);
			} catch (MalformedConfigException e) {				
				e.printStackTrace();
				return new ResponseEntity<String>("Database is not configured correctly.", HttpStatus.BAD_REQUEST);
			} 
			return new ResponseEntity<String>("Experiment Loaded.", HttpStatus.OK);
		}
		@RequestMapping(value = "/userarea", method = RequestMethod.GET)
	    public ModelAndView homeAction() {
	            return new ModelAndView("redirect:" + "/userarea/results");

	    }
		
		@RequestMapping(value = "/userarea/visualize", method = RequestMethod.GET)
		public String visualizeAction(Locale locale, Model model) {
			//Consider using jqplotter, open source plotting tool
			//also could call R/perl/python -> file, then load file (would be very unresponsive)
			return "visualize";
		}
		
		@RequestMapping(value = "/userarea/schedule", method = RequestMethod.GET)
		public String scheduleAction(Locale locale, Model model) {
			return "schedule";
		}
		
		/**
		 * Primary action of user area. Shows the most recent 50 entries.
		 * @param locale
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "/userarea/results", method = RequestMethod.GET)
		public String resultsAction(Locale locale, Model model) {
			DateMidnight todayMidnight = new DateMidnight();
			ArrayList<Snapshot> snapshots = (ArrayList<Snapshot>) sd.findWithTileLastNEntries(50);
			model.addAttribute("date", todayMidnight.toString("EEEE, MMMM dd, YYYY"));
			model.addAttribute("snapshots", snapshots );
			return "userarea-results";
		}
		/**
		 *  Sends the user to the query builder page, where they build a custom snapshot query.
		 *  Upon submission, a key is provided to the user which validates their download (for use with wget and other command line tools)
		 *  These keys are stored in memory.
		 */
		@RequestMapping(value = "/userarea/querybuilder", method = RequestMethod.GET)
		public String queryBuilderAction(Locale locale, Model model,  @ModelAttribute("user") DbUser user){
			// this is for testing remove please shit works
			String downloadKey;
			downloadKey = DownloadManager.generateRandomKey(user);
			model.addAttribute("downloadKey", downloadKey);
			System.out.println(user);
			System.out.println(user.getActiveExperiment().getExperimentName());
			model.addAttribute("activeExperiment", user.getActiveExperiment().getExperimentName());
			return "userarea-querybuilder";
		}
		/**
		 * This action handles mass downloading of images. Downloads expect a valid downloadKey which is stored in the
		 * system properties files. Manually setups experiment.
		 * @param locale
		 * @param model
		 * @return
		 * @throws IOException 
		 * @throws ExperimentNotAllowedException 
		 */
		@RequestMapping(value = "/massdownload", method = RequestMethod.GET)
		public void massDownloadAction(HttpServletResponse response, Locale locale, Model model,
																	 @RequestParam(value = "before", required = false) String before,
																	 @RequestParam(value = "after", required = false) String after,
																	 @RequestParam(value = "activeExperiment", required = false) String activeExperiment,
																	 @RequestParam(value = "plantBarcode", required = false) String plantBarcode,
																	 @RequestParam(value = "measurementLabel", required = false) String measurementLabel,
																	 @RequestParam(value = "downloadKey", required = true) String downloadKey,
																	 @RequestParam(value = "vis", defaultValue = "false") boolean vis,
																	 @RequestParam(value = "nir", defaultValue = "false") boolean nir,
																	 @RequestParam(value = "fluo", defaultValue = "false") boolean fluo) throws IOException, ExperimentNotAllowedException {
			//TODO: reimplement 1 download per user limit
			
			DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
			//error handling
			if (downloadKey == null){
				response.sendError(403, "Permission denied.");  
				response.flushBuffer();
		        return;
			}
			if (System.getProperty(downloadKey) == null){

				//property is null so we can't do anything?
				response.sendError(400, "Invalid download key");   
				response.flushBuffer();
				return;
			}
		
			DbUser user = ud.findByUsername(System.getProperty(downloadKey));
			//check permissions and setup experiment for anonymous users
			ArrayList<Experiment> experiments = user.getAllowedExperiments();
			for (Experiment experiment : experiments) {
				if (experiment.getExperimentName().equals(activeExperiment)){
					user.setActiveExperiment(experiment);
					try {
						sd.setDataSource(new ExperimentConfig().dataSource(experiment.getExperimentName()));
					} catch (MalformedConfigException e) {
						e.printStackTrace();
						response.sendError(400, "Bad experiment configuration");   
						response.flushBuffer();
					}
					break;
				}
			}
			if(user.getActiveExperiment() == null){
			    response.sendError(403, "Invalid experiment selection");   
				response.flushBuffer();
			    return;
			}
			//setup query
			ArrayList<Snapshot> snapshots;
			Timestamp tsBefore = null;
			Timestamp tsAfter = null;
			if (! before.equals("") ){
				DateTime dBefore = formatter.parseDateTime(before);	
				tsBefore = new Timestamp(dBefore.getMillis());
			}
			if (! after.equals("") ){
				DateTime dAfter = formatter.parseDateTime(after);	
				tsAfter = new Timestamp(dAfter.getMillis());
			}
			plantBarcode = "^" + plantBarcode;
						
	        response.setHeader("Transfer-Encoding", "chunked");     
	        response.setHeader("Content-type", "text/plain");
	        //TODO: Add filename option
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + "Snapshots" + downloadKey + ".zip\"");
			response.flushBuffer();
	        snapshots = (ArrayList<Snapshot>) sd.findWithTileCustomQueryImageJobs(tsAfter, tsBefore, plantBarcode, measurementLabel);
	        ResultsBuilder results = new ResultsBuilder(response.getOutputStream(), snapshots, user.getActiveExperiment(), nir, vis, fluo);
	        results.writeZipArchive();
			response.flushBuffer();			
			return;
		}
		
		
		/**
		 * Expects the date to be returned with the format of MM/dd/yyyy HH:mm. 
		 * Only returns image snapshots.
		 * 
		 * returns a list of new image snapshots to display to the page.
		 * @param locale
		 * @param model
		 * @param before return only snapshots before this date
		 * @param after return only snapshots after this date
		 * @return
		 */
		@RequestMapping(value = "/userarea/filterresults", method = RequestMethod.GET)
		public String filterResultsAction(Locale locale, Model model, @RequestParam("before") String before,
										   @RequestParam("after") String after) {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
			//if they are not empty...
			ArrayList<Snapshot> snapshots;
			if (! before.equals("") && ! after.equals("")){
				DateTime dBefore = formatter.parseDateTime(before);	
				Timestamp tsBefore = new Timestamp(dBefore.getMillis());

				DateTime dAfter = formatter.parseDateTime(after);
				Timestamp tsAfter = new Timestamp(dAfter.getMillis());
				snapshots = (ArrayList<Snapshot>) sd.findSnapshotBetweenTimesImageJobs(tsBefore, tsAfter);
				model.addAttribute("date", "Before: " + before +" After: " + after );
			}
			else if (! after.equals("") ){
				DateTime dAfter = formatter.parseDateTime(after);
				Timestamp tsAfter = new Timestamp(dAfter.getMillis());
				//slow filter manually
				snapshots = (ArrayList<Snapshot>) sd.findSnapshotAfterTimestampImageJobs(tsAfter);
				model.addAttribute("date", "After: " + after );

			}
			else{
				//get here then we know after is empty and before is active
				DateTime dBefore = formatter.parseDateTime(before);	
				Timestamp tsThreeBefore = new Timestamp(dBefore.minusDays(3).getMillis());
				Timestamp tsBefore = new Timestamp(dBefore.getMillis());
				//slow
				snapshots = (ArrayList<Snapshot>) sd.findSnapshotBetweenTimesImageJobs(tsBefore, tsThreeBefore);
				model.addAttribute("date", "Before: " + before +" After: " + tsThreeBefore );	
			}
			model.addAttribute("snapshots", snapshots );
			return "userarea-results";
		}
		
		/**
		 * Method for getting a snapshot and all associated images in a chunked manner. That is, a stream which will 
		 * allow for a more responsive feeling download and for all image conversions to be done on the fly.
		 * 
		 * @param response
		 * @param user
		 * @param snapshotId
		 * @throws IOException
		 */
		@RequestMapping(value = "/userarea/stream/{id}")
	    public void streamSnapshot(HttpServletResponse response, @ModelAttribute("user") DbUser user, @PathVariable("id") int snapshotId) throws IOException  {
	        response.setHeader("Transfer-Encoding", "chunked");     
	        response.setHeader("Content-type", "text/plain");
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + "Snapshot" + snapshotId + ".zip\"");
	        ArrayList<Snapshot> snapshots = new ArrayList<Snapshot>(1);
	        snapshots.add(sd.findWithTileBySnapshotId(snapshotId));
	        //no magic
	        boolean vis = true;
	        boolean nir = true;
	        boolean fluo = true;
	        try{
		        ResultsBuilder results = new ResultsBuilder(response.getOutputStream(), snapshots, user.getActiveExperiment(), vis, nir, fluo);
		        results.writeZipArchive();
	        } catch( IOException e){
	        	System.err.println("Download was probably cancelled.");
	        }
	        response.flushBuffer();
	    }

		@RequestMapping(value = "/userarea/status", method = RequestMethod.GET)
		public String statusAction(Locale locale, Model model) {
			return "status";
		}		
		/**
		 * Profile editing request. Interface for users changing their password, managing downloads, people in their group,
		 * metadata etc...
		 * So far only password is implemented.
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "/userarea/profile", method= RequestMethod.GET)
		public String profileAction(Model model, @ModelAttribute("user") DbUser user){
			System.out.println(user);
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (username.equals("anonymousUser")){
				model.addAttribute("message", "Error: Not logged in.");
				return "error";
			}
	        model.addAttribute("group", user.getGroup());
	        return "userarea-profile";	
		}
		/**
		 * This is the active user's method of changing the password. It does not require the old password. Encoding
		 * is done here with standard encoder. It is arguable that this functionality should not be at the controller layer.
		 * 
		 * @author shill
		 * @param oldpass
		 * @param newpass
		 * @return
		 */
		@RequestMapping(value = "/userarea/profile/changepass", method=RequestMethod.POST)
		public @ResponseBody ResponseEntity<String> changePasswordAction(@RequestParam("oldpass") String oldPass,
														 @RequestParam("newpass") String newPass,
														 @RequestParam("validate") String validate){
			if (!newPass.equals(validate)){
				return new ResponseEntity<String>("Passwords do not match!", HttpStatus.BAD_REQUEST);
			}
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (username.equals("anonymousUser")){
				return new ResponseEntity<String>("Not logged in.", HttpStatus.BAD_REQUEST);
			}
			DbUser user = ud.findByUsername(username);
			if (CustomAuthenticationManager.validateCredentials(user, oldPass) == false){
				return new ResponseEntity<String>("Current password is incorrect.", HttpStatus.BAD_REQUEST);
			}
			StandardPasswordEncoder se = new StandardPasswordEncoder();
			String pass = se.encode(newPass);
			user.setPassword(pass);
			ud.changePassword(user);
			return new ResponseEntity<String>("Success!", HttpStatus.OK);
		}
}
