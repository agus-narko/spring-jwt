package com.narko.project.resources.user;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.narko.project.configuration.ErrorMessageIndoConstants;
import com.narko.project.configuration.MasterGeneralConstants;
import com.narko.project.configuration.SuccessMessageIndoConstants;
import com.narko.project.model.UserEntity;
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
public class SignoutResources implements DataProcess<Authentication> {

	private static final Logger log = LogManager.getLogger(SignoutResources.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Override
	public FinalResEntity<?> processBo(Authentication serviceInput, AuditReqEntity audit) {
		log.trace(SignoutResources.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";
		FinalResEntity<?> respon = null;
		try {

			if (!userRepository.existsByUsernameAndStatus(serviceInput.getName(), MasterGeneralConstants.ACTIVATE)) {

				HttpStatus status = HttpStatus.BAD_REQUEST;
				message = ErrorMessageIndoConstants.ERROR.concat(serviceInput.getName())
						.concat(ErrorMessageIndoConstants.NOT_FOUND);
				return respon = new FinalResEntity(null, status.value(), status, message);
			}

			UserEntity user = userRepository
					.findByUsernameAndStatus(serviceInput.getName(), MasterGeneralConstants.ACTIVATE).orElse(null);

			user.setLogin(MasterGeneralConstants.NO);
			userRepository.save(user);

			HttpStatus status = HttpStatus.OK;
			message = SuccessMessageIndoConstants.SUCCESS.concat(serviceInput.getName())
					.concat(SuccessMessageIndoConstants.SIGNOUT_SUCCESS);

			return respon = new FinalResEntity(null, status.value(), status, message);

		} catch (Exception e) {
			e.printStackTrace(ps);
			ps.close();
			message = e.getMessage();
			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

			return respon = new FinalResEntity(null, status.value(), status, message);
		} finally {
			if (!message.isEmpty() || !message.equals("") || baos.size() > 1) {
				log.error(message);
				log.error("stackTrace : " + baos.toString());
			}
		}

	}
}
