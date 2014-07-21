package com.ddpsc.phenofront;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Handles an anonymous user. Controls the default 3 views and provides a gateway to the Login controller.
 * 
 * I almost feel although the mapping requests should be handled in a master file... not in this one.
 * 
 * @author shill
 *
 */
@Controller
public class LandingController
{
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Controller Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	/**
	 * Landing action. Takes an anonymous user to the home page
	 * 
	 * @param	locale		Geographical region the user is from
	 * @param	model		TODO: What is this Model object, how is it passed around
	 * @return				The string indicating the next target view 
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String homeAction(Locale locale, Model model)
	{
		StandardPasswordEncoder encoder = new StandardPasswordEncoder();
		System.out.println(encoder.encode("password"));
		
		if (ControllerHelper.isActiveUser())
			return "userarea";
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);
		return "home";
	}

	/**
	 * Page shows the status of running jobs, the queue, or jobs in process. Probably going to change this to news or
	 * something.
	 * 
	 * 
	 * @param	locale		Geographical region the user is from
	 * @param	model		TODO: Still have no clue what Model is or what manages it, some spring thing
	 * @return				TODO: Determine if status is still a page that exists, currently this whole method appears useless
	 */
	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public String statusAction(Locale locale, Model model)
	{
		if (ControllerHelper.isActiveUser())
			return "userarea";
		
		return "status";
	}
}
