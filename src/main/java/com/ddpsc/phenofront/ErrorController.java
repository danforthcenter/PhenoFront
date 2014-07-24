package com.ddpsc.phenofront;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling general, unhandled server errors.
 * 
 * Ideally, the user will never interact with this controller, but just in case
 * an error slips past, this will be there to handle it and notify the user.
 * 
 * 
 * @author shill, cjmcentee
 */
@Controller
class ErrorController
{
	private static final Logger log = Logger.getLogger(ErrorController.class);
	
	
	@RequestMapping("/error")
	public String customError(HttpServletRequest request, HttpServletResponse response, Model model)
	{
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		Throwable exception = (Throwable) request.getAttribute("javax.servlet.error.exception");
		
		String exceptionMessage = getExceptionMessage(exception, statusCode);
		
		String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
		if (requestUri == null)
			requestUri = "Unknown";

		String message = MessageFormat.format("Error {0} returned for {1} with message {2}", statusCode, requestUri, exceptionMessage);
		
		log.error(message);
		message = "Error: " + statusCode + ". Contact an administrator for details.";
		model.addAttribute("message", message);
		
		return "error";
	}

	private String getExceptionMessage(Throwable exception, Integer statusCode)
	{
		if (exception != null) {
			return exception.getMessage();
		}
		else {
			HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
			return httpStatus.getReasonPhrase();
		}
	}
}





