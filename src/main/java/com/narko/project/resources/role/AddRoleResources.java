package com.narko.project.resources.role;

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
import com.narko.project.model.RoleTypeEntity;
import com.narko.project.payload.request.AddRoleReqEntity;
import com.narko.project.payload.request.AuditReqEntity;
import com.narko.project.payload.response.FinalResEntity;
import com.narko.project.repository.RoleRepository;
import com.narko.project.repository.RoleTypeRepository;
import com.narko.project.resources.core.DataProcess;

/**
 * 
 * @author agus.wijanarko
 *
 */

@Service
public class AddRoleResources implements DataProcess<AddRoleReqEntity> {

	private static final Logger log = LogManager.getLogger(AddRoleResources.class);

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	RoleTypeRepository roleTypeRepository;

	@SuppressWarnings("unchecked")
	@Override
	public FinalResEntity<?> processBo(AddRoleReqEntity serviceInput, AuditReqEntity audit) {
		log.trace(AddRoleResources.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";
		Set<String> messageList = new HashSet<>();
		FinalResEntity<?> respon = null;
		RoleEntity approval = null;
		try {
			if (roleRepository.existsByName(serviceInput.getName())) {

				message = ErrorMessageIndoConstants.ERROR.concat(serviceInput.getName())
						.concat(ErrorMessageIndoConstants.ALREADY_USES);
				HttpStatus status = HttpStatus.BAD_REQUEST;
				return respon = new FinalResEntity(null, status.value(), status, message);
			}

			RoleTypeEntity roleType = roleTypeRepository.findById(serviceInput.getRoleMenu()).orElse(null);

			if (roleType == null || !roleType.getStatus().equals(MasterGeneralConstants.ACTIVATE)) {

				message = ErrorMessageIndoConstants.ERROR.concat(ErrorMessageIndoConstants.NOT_FOUND_ROLE_TYPE);
				HttpStatus status = HttpStatus.BAD_REQUEST;

				return respon = new FinalResEntity(null, status.value(), status, message);

			}

			if (serviceInput.getApprovalId() == null) {
				if (roleType.getName().contains(MasterGeneralConstants.USER)) {
					message = ErrorMessageIndoConstants.ERROR.concat(ErrorMessageIndoConstants.APPROVAL_CANT_EMPTY);
					HttpStatus status = HttpStatus.BAD_REQUEST;

					return respon = new FinalResEntity(null, status.value(), status, message);
				}
			} else if (serviceInput.getApprovalId() != null) {
				approval = roleRepository.findById(serviceInput.getApprovalId()).orElse(null);

				if (!approval.getRoleType().getName().contains(MasterGeneralConstants.APPROVAL)) {
					message = ErrorMessageIndoConstants.ERROR.concat(approval.getName())
							.concat(ErrorMessageIndoConstants.APPROVAL_NOT_FOUND);
					HttpStatus status = HttpStatus.BAD_REQUEST;

					return respon = new FinalResEntity(null, status.value(), status, message);
				} else if(!roleType.getName().contains(MasterGeneralConstants.USER)) {
					approval =  null;
				}

			}

			RoleEntity addRole = new RoleEntity(serviceInput.getName(), approval, roleType);

			roleRepository.save(addRole);

			HttpStatus status = HttpStatus.CREATED;
			message = SuccessMessageIndoConstants.CREATED.concat(serviceInput.getName())
					.concat(SuccessMessageIndoConstants.CREATE_SUCCESS);
			return respon = new FinalResEntity(addRole, status.value(), status, messageList);

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
