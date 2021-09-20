package com.narko.project.controller.userManagement;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narko.project.configuration.ServiceConstants;
import com.narko.project.payload.request.SigninReqEntity;
import com.narko.project.payload.response.FinalResEntity;
import com.narko.project.resources.core.BoInterceptor;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@Api(tags = "user management")
public class SigninController {

	@Autowired
	BoInterceptor commandProcessingService;

	public static final String serviceName = ServiceConstants.SIGN_IN_SERVICE;
	public static final String restUrlAddress = ServiceConstants.BASE_REST_URL + serviceName;
	

	@Operation(summary = "Sign in API", description = " API untuk sign in ")
	@RequestMapping(value = restUrlAddress, method = RequestMethod.POST)
//	@PreAuthorize("hasAuthority($serviceName)") // di pakai jika service mempunyai hak akses,jika permit all dicomment saja
	public ResponseEntity<?> controller(@Valid @RequestBody SigninReqEntity serviceInput) {

		FinalResEntity<?> respon = commandProcessingService.processAndLogCommand(serviceInput, serviceName);

		return ResponseEntity.status(respon.getError()).body(respon);

	}

}
