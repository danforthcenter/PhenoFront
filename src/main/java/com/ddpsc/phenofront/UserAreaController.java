package com.ddpsc.phenofront;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import src.ddpsc.database.experiment.Experiment;
import src.ddpsc.database.user.DbUser;
import src.ddpsc.exceptions.ExperimentNotAllowedException;

/**
 * Controller responsible for handling users.
 * 
 * @author shill
 *
 */
@Controller
public class UserAreaController {
		protected static Logger logger = Logger.getLogger("controller");

		@RequestMapping(value = "/selectexperiment", method=RequestMethod.GET)
		public String selectAction(Model model){
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			DbUser user = (DbUser) auth.getCredentials();
			ArrayList<Experiment> experiments = user.getAllowedExperiments();
			return "select";
		}
		
		@RequestMapping(value ="/selection", method=RequestMethod.POST)
		public @ResponseBody ResponseEntity<String> loadExperimentAction(@RequestParam("experimentid")  int experimentId,  
										 @RequestParam("experimentName") String experimentName){
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			DbUser user = (DbUser) auth.getCredentials();
			try {
				//code in place
				Experiment experiment = user.getExperimentByExperimentName(experimentName);
				user.setActiveExperiment(experiment);
				user.getActiveExperiment();
			} catch (ExperimentNotAllowedException e) {
				//400
				logger.warn("Experiment does not exist or is not allowed.");
				return new ResponseEntity<String>("Experiment is not allowed or does not exist.", HttpStatus.BAD_REQUEST);
			}
			//really we wanna redirect if this happens, can be done in ajax
			return new ResponseEntity<String>("Experiment Loaded.", HttpStatus.OK);
		}
	
		@RequestMapping(value = "/userarea", method = RequestMethod.GET)
		public String homeAction(Locale locale, Model model) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    String name = auth.getName(); //get logged in username	 
		    model.addAttribute("username", name);
		    Date date = new Date();
			DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
			String formattedDate = dateFormat.format(date);
			model.addAttribute("date", formattedDate );
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
