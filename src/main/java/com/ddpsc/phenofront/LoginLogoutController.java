package com.ddpsc.phenofront;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles and retrieves the login or denied page depending on the URI template
 */
@Controller
@RequestMapping("/auth")
public class LoginLogoutController
{
	
	private static final Logger log = Logger.getLogger(LoginLogoutController.class);
	
	
	/**
	 * Handles and retrieves the login JSP page
	 * 
	 * @return			The name of the JSP page
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getLoginPage(
			@RequestParam(value = "error", required = false) boolean error,
			Model model)
	{
		log.info("Received request to show login page to user " + ControllerHelper.currentUsername());
		
		if (error == true) {
			// Assign an error message
			model.addAttribute("error", "You have entered an invalid username or password!");
			model.addAttribute("hidden", "");
		} else {
			model.addAttribute("error", "");
			model.addAttribute("hidden", "hidden");
		}
		
		return "login";
	}

	/**
	 * Handles and retrieves the denied JSP page.
	 * 
	 * This is shown whenever a regular user tries to access an admin only page.
	 * 
	 * @return			The name of the JSP page
	 */
	@RequestMapping(value = "/denied", method = RequestMethod.GET)
	public String getDeniedPage()
	{
		log.info("Received request to show denied page to user " + ControllerHelper.currentUsername());
		
		return "deniedpage";
	}
}