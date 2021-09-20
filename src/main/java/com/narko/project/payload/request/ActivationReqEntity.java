package com.narko.project.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.narko.project.configuration.ErrorMessageIndoConstants;

import io.swagger.v3.oas.annotations.media.Schema;

public class ActivationReqEntity {
	
	@Schema(required = true, example = "1", description = "id data")
	@NotNull(message = ErrorMessageIndoConstants.ID_CANT_EMPTY)
	protected Long id;
	
	@Schema(required = true, example = "ACTIVE", description = "status")
	@NotBlank(message =  ErrorMessageIndoConstants.ERROR + ErrorMessageIndoConstants.STATUS_CANT_EMPTY)
	protected String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	


}
