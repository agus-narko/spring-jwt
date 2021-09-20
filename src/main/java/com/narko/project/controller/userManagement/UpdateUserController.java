package com.narko.project.controller.userManagement;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narko.project.configuration.ServiceConstants;
import com.narko.project.payload.request.UpdateUserReqEntity;
import com.narko.project.payload.response.FinalResEntity;
import com.narko.project.resources.core.BoInterceptor;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@Api(tags = "user management")
public class UpdateUserController {

	@Autowired
	BoInterceptor commandProcessingService;

	public static final String serviceName = ServiceConstants.UPATE_USER_SERVICE;
	public static final String restUrlAddress = ServiceConstants.BASE_REST_URL + serviceName;

	@Operation(summary = "update user API", description = " API untuk update user ", security = {
			@SecurityRequirement(name = "bearer-key") })
	@RequestMapping(value = restUrlAddress, method = RequestMethod.PUT)
//	@PreAuthorize("hasAuthority({serviceName})") // di pakai jika service mempunyai hak akses,jika permit all dicomment saja

	public ResponseEntity<?> controller(@Valid @RequestBody UpdateUserReqEntity serviceInput) {

//		
		FinalResEntity<?> respon = commandProcessingService.processAndLogCommand(serviceInput, serviceName);

//		
		return ResponseEntity.status(respon.getError()).body(respon);

	}

}
