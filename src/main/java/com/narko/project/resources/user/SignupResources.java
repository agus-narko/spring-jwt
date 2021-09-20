package com.narko.project.resources.user;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.narko.project.configuration.ErrorMessageIndoConstants;
import com.narko.project.configuration.MasterGeneralConstants;
import com.narko.project.configuration.SuccessMessageIndoConstants;
import com.narko.project.model.RoleEntity;
import com.narko.project.model.UserEntity;
import com.narko.project.payload.request.AuditReqEntity;
import com.narko.project.payload.request.SignupReqEntity;
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
public class SignupResources implements DataProcess<SignupReqEntity> {

	private static final Logger log = LogManager.getLogger(SignupResources.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	EntityManager entityManager;

	@Autowired
	GetUserLDAPResources getUserLDAPResources;

	@Override
	public FinalResEntity<?> processBo(SignupReqEntity serviceInput, AuditReqEntity audit)
			throws UnsupportedOperationException {
		log.trace(SignupResources.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";
		FinalResEntity<?> respon = null;
		List<UserEntity> ldapUser = new ArrayList<UserEntity>();

		ObjectMapper mapper = new ObjectMapper();

		try {

			if (userRepository.existsByUsernameAndStatus(serviceInput.getUsername(), MasterGeneralConstants.ACTIVATE)) {

				message = ErrorMessageIndoConstants.ERROR.concat(serviceInput.getUsername())
						.concat(ErrorMessageIndoConstants.ALREADY_USES);
				HttpStatus status = HttpStatus.BAD_REQUEST;
				return respon = new FinalResEntity(null, status.value(), status, message);
			}

			ldapUser = getUserLDAPResources.getLDAP(serviceInput.getUsername());

			if (ldapUser.size() < 1 || !ldapUser.get(0).getUsername().equals(serviceInput.getUsername())) {
				message = ErrorMessageIndoConstants.ERROR.concat(serviceInput.getUsername())
						.concat(ErrorMessageIndoConstants.UID_NOT_FOUND);
				HttpStatus status = HttpStatus.BAD_REQUEST;
				return respon = new FinalResEntity(null, status.value(), status, message);
			}

			if (!roleRepository.existsByIdAndStatus(serviceInput.getRoleId(), MasterGeneralConstants.ACTIVATE)) {

				message = ErrorMessageIndoConstants.ERROR.concat(serviceInput.getRoleId().toString())
						.concat(ErrorMessageIndoConstants.NOT_FOUND_ROLE);
				HttpStatus status = HttpStatus.BAD_REQUEST;

				return respon = new FinalResEntity(null, status.value(), status, message);

			}

//			// Create new user's account
//
			RoleEntity role = roleRepository.findById(serviceInput.getRoleId()).orElse(null);

			UserEntity user = new UserEntity();
			user.setUsername(serviceInput.getUsername());
			user.setRoles(role);
			userRepository.saveAndFlush(user);

			HttpStatus status = HttpStatus.CREATED;
			message = SuccessMessageIndoConstants.CREATED.concat(serviceInput.getUsername())
					.concat(SuccessMessageIndoConstants.CREATE_SUCCESS);
			respon = new FinalResEntity(user, status.value(), status, message);
			return respon;

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
