package com.narko.project.resources.action;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.narko.project.configuration.ErrorMessageIndoConstants;
import com.narko.project.configuration.SuccessMessageIndoConstants;
import com.narko.project.model.ApiEntity;
import com.narko.project.payload.request.AddApiReqEntity;
import com.narko.project.payload.request.AuditReqEntity;
import com.narko.project.payload.response.FinalResEntity;
import com.narko.project.repository.ApiRepository;
import com.narko.project.resources.core.DataProcess;

/**
 * 
 * @author agus.wijanarko
 *
 */

@Service
public class AddApiResources implements DataProcess<AddApiReqEntity> {
	private static final Logger log = LogManager.getLogger(AddApiResources.class);

	@Autowired
	ApiRepository apiRepository;

	@SuppressWarnings("unchecked")
	@Override
	public FinalResEntity<?> processBo(AddApiReqEntity serviceInput, AuditReqEntity audit) {
		log.trace(AddApiResources.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";
		FinalResEntity<?> respon = null;
		try {
			if (apiRepository.existsByApiName(serviceInput.getApiName())) {

				message = ErrorMessageIndoConstants.ERROR.concat(serviceInput.getApiName())
						.concat(ErrorMessageIndoConstants.ALREADY_USES);
				HttpStatus status = HttpStatus.BAD_REQUEST;
				return respon = new FinalResEntity(null, status.value(), status, message);
			}

			ApiEntity addApi = new ApiEntity(serviceInput.getApiName(), serviceInput.getResourceName(),
					serviceInput.getDeskripsi(), serviceInput.getMethod(), serviceInput.getGrup(),
					serviceInput.getEntityName(), serviceInput.getApproval(), serviceInput.getInputService(),
					serviceInput.getProcess());

			apiRepository.save(addApi);

			HttpStatus status = HttpStatus.CREATED;
			message = SuccessMessageIndoConstants.CREATED.concat(addApi.getApiName())
					.concat(SuccessMessageIndoConstants.CREATE_SUCCESS);
			return respon = new FinalResEntity(addApi, status.value(), status, message);

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
