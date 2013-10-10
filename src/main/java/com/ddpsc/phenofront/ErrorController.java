package com.ddpsc.phenofront;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
class CustomErrorController {
	protected static Logger logger = Logger.getLogger("general-error");

	@RequestMapping("/error")
	public String customError(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		// retrieve some useful information from the request
		Integer statusCode = (Integer) request
				.getAttribute("javax.servlet.error.status_code");
		Throwable throwable = (Throwable) request
				.getAttribute("javax.servlet.error.exception");
		// String servletName = (String)
		// request.getAttribute("javax.servlet.error.servlet_name");
		String exceptionMessage = getExceptionMessage(throwable, statusCode);

		String requestUri = (String) request
				.getAttribute("javax.servlet.error.request_uri");
		if (requestUri == null) {
			requestUri = "Unknown";
		}

		String message = MessageFormat.format(
				"Error {0} returned for {1} with message {2}", statusCode,
				requestUri, exceptionMessage);
		logger.error(message);
		message = "Error: " + statusCode + ". Contact an administrator for details."; 
		model.addAttribute("message", message);
		return "error";
	}

	private String getExceptionMessage(Throwable throwable, Integer statusCode) {
		if (throwable != null) {
			return throwable.getMessage();
		}
		HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
		return httpStatus.getReasonPhrase();
	}
}