package com.ddpsc.phenofront;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import src.ddpsc.config.ExperimentConfig;
import src.ddpsc.database.experiment.Experiment;
import src.ddpsc.database.snapshot.Snapshot;
import src.ddpsc.database.snapshot.SnapshotDao;
import src.ddpsc.database.snapshot.SnapshotDaoImpl;
import src.ddpsc.database.user.DbUser;
import src.ddpsc.database.user.UserDao;
import src.ddpsc.exceptions.ExperimentNotAllowedException;
import src.ddpsc.results.Tester;
import src.ddpsc.results.ZippedResultsUtil;

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
		
		//added for filestreaming
		@Autowired
		ServletContext servletContext;
		
		//important lol
		SnapshotDao sd = new SnapshotDaoImpl();
		
		protected static Logger logger = Logger.getLogger("controller");

		@RequestMapping(value = "/selectexperiment", method=RequestMethod.GET)
		public String selectAction(Model model){
	
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (username.equals("anonymousUser")){
				model.addAttribute("message", "Error: Not logged in.");
				return "error"; //lol suck it
			}
			
			DbUser user = ud.findByUsername(username);
			model.addAttribute("user", user);
			ArrayList<Experiment> experiments = user.getAllowedExperiments();
			model.addAttribute("allowed", experiments );
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
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (username.equals("anonymousUser")){
				return new ResponseEntity<String>("Error: Not allowed.", HttpStatus.FORBIDDEN);
			}
			/*
			try {
				Experiment experiment = user.getExperimentByExperimentName(experimentName);
				user.setActiveExperiment(experiment);
				//set our datasource bean
				sd.setDataSource(new ExperimentConfig().dataSource(experiment.getExperimentName()));
			} catch (ExperimentNotAllowedException e) {
				logger.warn("Experiment does not exist or is not allowed.");
				return new ResponseEntity<String>("Experiment is not allowed or does not exist.", HttpStatus.BAD_REQUEST);
			} 
			*/
			System.out.println("Connected to Phenotyper1");
			return new ResponseEntity<String>("Experiment Loaded.", HttpStatus.OK);
		}
	
		@RequestMapping(value = "/userarea", method = RequestMethod.GET)
		public String homeAction(Locale locale, Model model, @ModelAttribute("user") DbUser user) {
			//sd should be wired up once we are in the userarea
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    String name = auth.getName(); //get logged in username	 
		    model.addAttribute("username", name);
		    DateTime date = new DateTime();
			model.addAttribute("date", date.toString("YYYY-mm-dd HH:MM:SS") );

			return "userarea";
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
		
		@RequestMapping(value = "/userarea/results", method = RequestMethod.GET)
		public String resultsAction(Locale locale, Model model) {
			DateMidnight todayMidnight = new DateMidnight();
			ArrayList<Snapshot> snapshots = (ArrayList<Snapshot>) sd.findWithTileLastNEntries(50);
			model.addAttribute("date", todayMidnight.toString("EEEE, MMMM dd, YYYY"));
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
	        //TODO: Investigate saving snapshot queries in memory for one iteration.
	        ArrayList<Snapshot> snapshots = new ArrayList<Snapshot>(1);
	        snapshots.add(sd.findWithTileBySnapshotId(snapshotId));
	      
	        ZippedResultsUtil.ZipSnapshots(response.getOutputStream(), snapshots, user.getActiveExperiment());	    
	        response.flushBuffer();
	    }
		/**
		 * Another dumb proof of concept method.
		 * @param snapshotId
		 * @return
		 * @throws IOException
		 */
		@RequestMapping(value ="/userarea/fetchimage/{id}", method = RequestMethod.GET)
		public @ResponseBody byte[]  fetchImageAction(@PathVariable("id") int snapshotId) throws IOException{
			
			System.out.println(snapshotId);
			//before this is done we need to actually build the resource
			//good test
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			InputStream in = servletContext.getResourceAsStream("/resources/image_sets/A.zip");
		    return IOUtils.toByteArray(in);
		//	return new ResponseEntity<String>("urigoeshere", HttpStatus.OK);
		}
		@RequestMapping(value = "/userarea/status", method = RequestMethod.GET)
		public String statusAction(Locale locale, Model model) {
			return "status";
		}
}
