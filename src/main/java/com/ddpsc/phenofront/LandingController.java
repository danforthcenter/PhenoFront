package com.ddpsc.phenofront;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * Handles an anonymous user. Controls the default 3 views and provides a gateway to the Login controller.
 * 
 * I almost feel although the mapping requests should be handled in a master file... not in this one.
 * @author shill
 *
 */
@Controller
public class LandingController {
	
	/**
	 * Authentication.isAuthenticated will return true virtually always because users may be anonymous. This checks
	 * that a user is authenticated AND is not anonymous.
	 *  
	 * @return boolean
	 */
	private boolean isActiveUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.isAuthenticated() && !auth.getName().equals("anonymousUser")){
			return true;
		}
		else return false;
	}

	/**
	 * Landing action. Takes an anonymous user to the home page
	 * @param locale
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String homeAction(Locale locale, Model model) {
	
		if (this.isActiveUser()){
			return "userarea";
		}
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		//name of the jsp
		return "home";
	}
	/**
	 * Page shows the status of running jobs, the queue, or jobs in process. Probably going to change this
	 * to news or something.
	 * 
	 * @param locale
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public String statusAction(Locale locale, Model model){
		if (this.isActiveUser()){
			return "userarea";
		}
		//doesn't do anything cool yet.
		return "status";
	}
}
