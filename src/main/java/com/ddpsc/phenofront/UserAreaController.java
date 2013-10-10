package com.ddpsc.phenofront;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller responsible for handling users.
 * 
 * @author shill
 *
 */
@Controller
public class UserAreaController {
		@RequestMapping(value = "/userarea", method = RequestMethod.GET)
		public String homeAction(Locale locale, Model model) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    String name = auth.getName(); //get logged in username	 
		    model.addAttribute("username", name);
			
		    Date date = new Date();
			DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
			
			String formattedDate = dateFormat.format(date);
			
			model.addAttribute("date", formattedDate );
			//path of the jsp?
			//aka render?
			return "userarea";
		}
		//ahghghhhg only one ctonroller per i guess. idk
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
