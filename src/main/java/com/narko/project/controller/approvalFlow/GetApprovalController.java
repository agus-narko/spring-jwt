package com.narko.project.controller.approvalFlow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.narko.project.configuration.ServiceConstants;
import com.narko.project.payload.request.GetReqEntity;
import com.narko.project.payload.response.FinalResEntity;
import com.narko.project.resources.core.BoInterceptor;
import com.narko.project.searchSpec.MappingSpesification;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@Api(tags = "approval flow")
public class GetApprovalController {

	@Autowired
	BoInterceptor commandProcessingService;

	@Autowired
	MappingSpesification mappingSpesification;

	public static final String serviceName = ServiceConstants.GET_APPROVAL_SERVICE;
	public static final String restUrlAddress = ServiceConstants.BASE_REST_URL + serviceName;

	@Operation(summary = "get list approval API", description = " API untuk get data approval", security = {
			@SecurityRequirement(name = "bearer-key") })
	@RequestMapping(value = restUrlAddress, method = RequestMethod.GET)
// di pakai jika service mempunyai hak akses,jika permit all dicomment saja
	@PreAuthorize("hasAuthority('" + serviceName + "')")

	public ResponseEntity<?> controller(@RequestParam(value = "", required = false) String search,
			@RequestParam(value = "", required = false) String page,
			@RequestParam(value = "", required = false) String sort) {

		GetReqEntity serviceInput = mappingSpesification.toQuery(search, page, sort);

		FinalResEntity<?> respon = commandProcessingService.processAndLogCommand(serviceInput, serviceName);
		return ResponseEntity.status(respon.getError()).body(respon);

	}

}
