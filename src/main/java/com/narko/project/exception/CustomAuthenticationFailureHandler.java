package com.narko.project.exception;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException ex) throws IOException, ServletException {

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", Calendar.getInstance().getTime());
		body.put("exception", ex.getMessage());
		
//		 List<String> errors = ex.getBindingResult()
//	                .getFieldErrors()
//	                .stream()
//	                .map(x -> x.getDefaultMessage())
//	                .collect(Collectors.toList());
//
//	        body.put("message", errors);


		response.getOutputStream().println(objectMapper.writeValueAsString(body));
	}
}
