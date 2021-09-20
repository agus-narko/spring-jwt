package com.narko.project.resources.user;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.narko.project.configuration.ErrorMessageIndoConstants;
import com.narko.project.configuration.MasterGeneralConstants;
import com.narko.project.configuration.SuccessMessageIndoConstants;
import com.narko.project.model.RoleEntity;
import com.narko.project.model.UserEntity;
import com.narko.project.payload.request.ActivationReqEntity;
import com.narko.project.payload.request.AuditReqEntity;
import com.narko.project.payload.response.FinalResEntity;
import com.narko.project.repository.UserRepository;
import com.narko.project.resources.core.DataProcess;

/**
 * 
 * @author agus.wijanarko
 *
 */

@Service
public class ActivationUserResources implements DataProcess<ActivationReqEntity> {

	private static final Logger log = LogManager.getLogger(ActivationUserResources.class);

	@Autowired
	UserRepository userRepository;

	@Override
	public FinalResEntity<?> processBo(ActivationReqEntity serviceInput, AuditReqEntity audit) {
		log.trace(ActivationUserResources.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";
		FinalResEntity<?> respon = null;
		Set<RoleEntity> roles = new HashSet<>();
		try {

			UserEntity user = userRepository.findById(serviceInput.getId()).orElse(null);

			if (user == null) {
				message = ErrorMessageIndoConstants.ERROR.concat(ErrorMessageIndoConstants.NOT_FOUND_EMPTY);
				HttpStatus status = HttpStatus.BAD_REQUEST;
				return respon = new FinalResEntity(null, status.value(), status, message);
			}

			if (!serviceInput.getStatus().equals(MasterGeneralConstants.ACTIVATE)) {
				user.setRoles(null);
			}

			user.setStatus(serviceInput.getStatus());

			userRepository.saveAndFlush(user);

			HttpStatus status = HttpStatus.OK;
			message = SuccessMessageIndoConstants.SUCCESS.concat(SuccessMessageIndoConstants.ACTIVATION_SUCCESS)
					.concat(serviceInput.getStatus());
			return respon = new FinalResEntity(user, status.value(), status, message);

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
