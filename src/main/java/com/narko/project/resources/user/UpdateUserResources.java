package com.narko.project.resources.user;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import com.narko.project.payload.request.AuditReqEntity;
import com.narko.project.payload.request.UpdateUserReqEntity;
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
public class UpdateUserResources implements DataProcess<UpdateUserReqEntity> {

	private static final Logger log = LogManager.getLogger(UpdateUserResources.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	GetUserLDAPResources getUserLDAPResources;

	@Override
	public FinalResEntity<?> processBo(UpdateUserReqEntity serviceInput, AuditReqEntity audit) {
		log.trace(UpdateUserResources.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";
		FinalResEntity<?> respon = null;
		List<UserEntity> ldapUser = new ArrayList<UserEntity>();

		try {

			String password = new String();
			UserEntity userOld = userRepository.findById(serviceInput.getId()).orElse(null);

			if (userOld == null) {
				message = ErrorMessageIndoConstants.ERROR.concat(serviceInput.getUsername())
						.concat(ErrorMessageIndoConstants.NOT_FOUND);
				HttpStatus status = HttpStatus.BAD_REQUEST;
				return respon = new FinalResEntity(null, status.value(), status, message);
			}

			if (userRepository.existsByUsernameAndStatus(serviceInput.getUsername(), MasterGeneralConstants.ACTIVATE)
					&& !serviceInput.getUsername().equals(userOld.getUsername())) {

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

			// update user's account

			RoleEntity roleNew = roleRepository.findById(serviceInput.getRoleId()).orElse(null); // jika data tidak
			// ketemu akan // di
			// isi null

			userOld.setUsername(serviceInput.getUsername());
			userOld.setRoles(roleNew);
			userRepository.saveAndFlush(userOld);

			HttpStatus status = HttpStatus.OK;
			message = SuccessMessageIndoConstants.SUCCESS.concat(SuccessMessageIndoConstants.UPDATE_SUCCESS);

			return respon = new FinalResEntity(userOld, status.value(), status, message);
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
