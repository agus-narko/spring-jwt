package com.narko.project.resources.role;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.narko.project.configuration.ErrorMessageIndoConstants;
import com.narko.project.configuration.MasterGeneralConstants;
import com.narko.project.configuration.SuccessMessageIndoConstants;
import com.narko.project.model.RoleEntity;
import com.narko.project.payload.request.ActivationReqEntity;
import com.narko.project.payload.request.AuditReqEntity;
import com.narko.project.payload.response.FinalResEntity;
import com.narko.project.repository.RoleRepository;
import com.narko.project.repository.UserRepository;
import com.narko.project.resources.core.DataProcess;

/**
 * 
 * @author agus.wijanarko
 *
 */

@Service
public class ActivationRoleResources implements DataProcess<ActivationReqEntity> {

	private static final Logger log = LogManager.getLogger(ActivationRoleResources.class);

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserRepository userRepository;

	@Override
	public FinalResEntity<?> processBo(ActivationReqEntity serviceInput, AuditReqEntity audit) {

		log.trace(ActivationRoleResources.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";
		FinalResEntity<?> respon = null;
		try {

			RoleEntity role = roleRepository.findById(serviceInput.getId()).orElse(null);

			if (role == null) {
				message = ErrorMessageIndoConstants.ERROR.concat(ErrorMessageIndoConstants.NOT_FOUND_EMPTY);
				HttpStatus status = HttpStatus.BAD_REQUEST;
				return respon = new FinalResEntity(null, status.value(), status, message);
			}

			else if (userRepository.existsByRoles(role)) {
				message = ErrorMessageIndoConstants.ERROR.concat(ErrorMessageIndoConstants.USER_ROLE_USED);
				HttpStatus status = HttpStatus.BAD_REQUEST;
				return respon = new FinalResEntity(null, status.value(), status, message);
			} else if (role.getRoleType().getName().contains(MasterGeneralConstants.APPROVAL)) {
				if (roleRepository.existsByApprovalAndStatus(role, MasterGeneralConstants.ACTIVATE)) {
					message = ErrorMessageIndoConstants.ERROR.concat(ErrorMessageIndoConstants.ROLE_APPROVER_USED);
					HttpStatus status = HttpStatus.BAD_REQUEST;
					return respon = new FinalResEntity(null, status.value(), status, message);
				}

			}

			role.setStatus(serviceInput.getStatus());
			role.setApproval(null);
			roleRepository.saveAndFlush(role);

			HttpStatus status = HttpStatus.OK;

			message = SuccessMessageIndoConstants.SUCCESS.concat(SuccessMessageIndoConstants.ACTIVATION_SUCCESS)
					.concat(serviceInput.getStatus());

			return respon = new FinalResEntity(role, status.value(), status, message);

		} catch (Exception e) {
			e.printStackTrace(ps);
			ps.close();
			message = e.getMessage();
			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error(message);
			log.error("stackTrace : " + baos.toString());
			return respon = new FinalResEntity(null, status.value(), status, message);
		
		}

	}
}
