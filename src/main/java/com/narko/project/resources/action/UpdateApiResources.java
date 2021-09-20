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
import com.narko.project.payload.request.AuditReqEntity;
import com.narko.project.payload.response.FinalResEntity;
import com.narko.project.repository.ApiRepository;
import com.narko.project.resources.core.DataProcess;

/**
 */

@Service
public class UpdateApiResources implements DataProcess<ApiEntity> {
	private static final Logger log = LogManager.getLogger(UpdateApiResources.class);

	@Autowired
	ApiRepository apiRepository;

	@Override
	public FinalResEntity<?> processBo(ApiEntity serviceInput, AuditReqEntity audit) {
		log.trace(UpdateApiResources.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";
		FinalResEntity<?> respon = null;

		try {

			ApiEntity dataOld = apiRepository.findById(serviceInput.getId()).orElse(null);

			if (dataOld == null) {
				message = ErrorMessageIndoConstants.ERROR.concat(ErrorMessageIndoConstants.NOT_FOUND_EMPTY);
				HttpStatus status = HttpStatus.BAD_REQUEST;
				return respon = new FinalResEntity(null, status.value(), status, message);
			}

			if (apiRepository.existsByApiName(serviceInput.getApiName())
					&& !serviceInput.getApiName().equals(dataOld.getApiName())) {

				message = ErrorMessageIndoConstants.ERROR.concat(serviceInput.getApiName())
						.concat(ErrorMessageIndoConstants.ALREADY_USES);
				HttpStatus status = HttpStatus.BAD_REQUEST;
				return respon = new FinalResEntity(null, status.value(), status, message);
			}

			apiRepository.saveAndFlush(serviceInput);
			message = SuccessMessageIndoConstants.SUCCESS.concat(SuccessMessageIndoConstants.UPDATE_SUCCESS);
			HttpStatus status = HttpStatus.OK;

			return respon = new FinalResEntity(serviceInput, status.value(), status, message);
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
