package com.narko.project.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.narko.project.configuration.ErrorMessageIndoConstants;

import io.swagger.v3.oas.annotations.media.Schema;

public class SignupReqEntity {

	@Schema(required = true, example = "admin", description = "username")
	@NotBlank(message =  ErrorMessageIndoConstants.ERROR + ErrorMessageIndoConstants.USER_CANT_EMPTY)
	@Size(max = 50, message = ErrorMessageIndoConstants.MAX_LEN + "50")
	private String username;

	@Schema(required = true, example = "1", description = "id role")
	@NotNull(message = ErrorMessageIndoConstants.ROLE_CANT_EMPTY)
	private Long roleId;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}
