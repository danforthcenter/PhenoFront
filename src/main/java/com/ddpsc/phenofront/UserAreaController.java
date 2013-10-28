package com.ddpsc.phenofront;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import src.ddpsc.config.ExperimentConfig;
import src.ddpsc.database.experiment.Experiment;
import src.ddpsc.database.snapshot.Snapshot;
import src.ddpsc.database.snapshot.SnapshotDao;
import src.ddpsc.database.snapshot.SnapshotDaoImpl;
import src.ddpsc.database.user.DbUser;
import src.ddpsc.database.user.UserDao;
import src.ddpsc.exceptions.ExperimentNotAllowedException;

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
		
		@RequestMapping(value ="/selection", method=RequestMethod.POST)
		public @ResponseBody ResponseEntity<String> loadExperimentAction(
					@ModelAttribute("user") DbUser user,
					@RequestParam("experimentName") String experimentName){
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
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
			}
			return new ResponseEntity<String>("Experiment Loaded.", HttpStatus.OK);
		}
	
		@RequestMapping(value = "/userarea", method = RequestMethod.GET)
		public String homeAction(Locale locale, Model model, @ModelAttribute("user") DbUser user) {
			//sd should be wired up once we are in the userarea
			Snapshot snapper = sd.findWithTileBySnapshotId(32035);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    String name = auth.getName(); //get logged in username	 
		    model.addAttribute("username", name);
		    Date date = new Date();
			DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
			String formattedDate = dateFormat.format(date);
			model.addAttribute("date", date );
			model.addAttribute("snapper", snapper );

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
			return "results";
		}
		
		@RequestMapping(value = "/userarea/status", method = RequestMethod.GET)
		public String statusAction(Locale locale, Model model) {
			return "status";
		}
}
