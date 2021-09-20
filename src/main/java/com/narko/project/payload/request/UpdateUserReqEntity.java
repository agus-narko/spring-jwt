package com.narko.project.payload.request;

import javax.validation.constraints.NotNull;

import com.narko.project.configuration.ErrorMessageIndoConstants;

import io.swagger.v3.oas.annotations.media.Schema;

public class UpdateUserReqEntity extends SignupReqEntity {

	@Schema(required = true, example = "1", description = "id data user")
	@NotNull(message = ErrorMessageIndoConstants.ID_CANT_EMPTY)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
