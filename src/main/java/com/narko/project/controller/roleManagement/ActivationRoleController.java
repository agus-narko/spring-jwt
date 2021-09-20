package com.narko.project.controller.roleManagement;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narko.project.configuration.ServiceConstants;
import com.narko.project.payload.request.ActivationReqEntity;
import com.narko.project.payload.response.FinalResEntity;
import com.narko.project.resources.core.BoInterceptor;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@Api(tags = "role management")
public class ActivationRoleController {

	@Autowired
	BoInterceptor commandProcessingService;

	public static final String serviceName = ServiceConstants.ACTIVATION_ROLE_SERVICE;
	public static final String restUrlAddress = ServiceConstants.BASE_REST_URL + serviceName;

	@Operation(summary = "activation role API", description = " API untuk activation role ", security = {
			@SecurityRequirement(name = "bearer-key") })
	@RequestMapping(value = restUrlAddress, method = RequestMethod.PUT)
	@PreAuthorize("hasAuthority('" + serviceName + "')")
// di pakai jika service mempunyai hak akses,jika permit all dicomment saja

	public ResponseEntity<?> controller(@Valid @RequestBody ActivationReqEntity serviceInput) {

//		
		FinalResEntity<?> respon = commandProcessingService.processAndLogCommand(serviceInput, serviceName);

		return ResponseEntity.status(respon.getError()).body(respon);

	}

}
