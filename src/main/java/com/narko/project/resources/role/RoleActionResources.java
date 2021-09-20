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
import com.narko.project.model.ApiEntity;
import com.narko.project.model.RoleTypeEntity;
import com.narko.project.payload.request.AuditReqEntity;
import com.narko.project.payload.request.RoleActionReqEntity;
import com.narko.project.payload.response.FinalResEntity;
import com.narko.project.repository.ApiRepository;
import com.narko.project.repository.RoleTypeRepository;
import com.narko.project.resources.core.DataProcess;

/**
 * 
 * @author agus.wijanarko
 *
 */

@Service
public class RoleActionResources implements DataProcess<RoleActionReqEntity> {
	private static final Logger log = LogManager.getLogger(RoleActionResources.class);

	@Autowired
	ApiRepository apiRepository;

	@Autowired
	RoleTypeRepository roleTypeRepository;

	@Override
	public FinalResEntity<?> processBo(RoleActionReqEntity serviceInput,AuditReqEntity audit) {
		log.trace(RoleActionResources.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";
		Set<String> messageList = new HashSet<>();
		FinalResEntity<?> respon = null;
		Set<String> actionListOld = new HashSet<>();
		try {
			RoleTypeEntity roleType = roleTypeRepository.findById(serviceInput.getId()).orElse(null);
			Set<ApiEntity> actionList = roleType.getAction();

			if (roleType == null) {
				message = ErrorMessageIndoConstants.ERROR.concat(ErrorMessageIndoConstants.NOT_FOUND_EMPTY);
				HttpStatus status = HttpStatus.BAD_REQUEST;
				return respon = new FinalResEntity(null, status.value(), status, message);
			}

			for (ApiEntity api : actionList) {
				actionListOld.add(api.getApiName());
			}

			switch (serviceInput.getFunc()) {
			case MasterGeneralConstants.ADD:

				for (String apiStr : serviceInput.getActionList()) {
					ApiEntity api = apiRepository.findByApiName(apiStr).orElse(null);
					if (api != null) {
						if (!actionListOld.contains(api.getApiName())) {
							actionList.add(api);
						}

					} else {
						messageList.add(apiStr.concat(ErrorMessageIndoConstants.NOT_FOUND));
					}
				}
				break;
			case MasterGeneralConstants.REPLACE:
				actionList = new HashSet<>();
				for (String apiStr : serviceInput.getActionList()) {
					ApiEntity api = apiRepository.findByApiName(apiStr).orElse(null);
					if (api != null) {

						actionList.add(api);
					} else {
						messageList.add(apiStr.concat(ErrorMessageIndoConstants.NOT_FOUND));
					}
				}
				break;
			case MasterGeneralConstants.DELETE:

				for (String apiStr : serviceInput.getActionList()) {
					Set<ApiEntity> actionListTemp = new HashSet<>();
					for (ApiEntity api : actionList) {
						if (!api.getApiName().equals(apiStr)) {
							actionListTemp.add(api);
						}

					}
					actionList = actionListTemp;
				}

				break;
			default:
				break;
			}

			roleTypeRepository.saveAndFlush(roleType);

			HttpStatus status = HttpStatus.OK;
			message = SuccessMessageIndoConstants.SUCCESS.concat(SuccessMessageIndoConstants.UPDATE_SUCCESS);
			return respon = new FinalResEntity(roleType, status.value(), status, messageList);

		} catch (Exception e) {
			e.printStackTrace(ps);
			ps.close();
			message = e.getMessage();
			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

			return respon = new FinalResEntity(null, status.value(), status, message);
		} finally {
			if(!message.isEmpty() || !message.equals("") || baos.size() > 1) {
				log.error( message);
				log.error("stackTrace : " + baos.toString());
			}
		}

	}
}
