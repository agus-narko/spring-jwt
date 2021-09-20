package com.narko.project.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.narko.project.configuration.ErrorMessageIndoConstants;

import io.swagger.v3.oas.annotations.media.Schema;

public class SigninReqEntity {

	@Schema(required = true, example = "admin", description = "username")
	@NotBlank(message =  ErrorMessageIndoConstants.ERROR + ErrorMessageIndoConstants.USER_CANT_EMPTY)
	@Size(max = 50, message = ErrorMessageIndoConstants.MAX_LEN + "50")
	private String username;
	
	@Schema(required = true, example = "password", description = "password")
	@NotBlank(message =  ErrorMessageIndoConstants.ERROR + ErrorMessageIndoConstants.PASSWORD_CANT_EMPTY)
	@Size(max = 50, message = ErrorMessageIndoConstants.MAX_LEN + "50")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
