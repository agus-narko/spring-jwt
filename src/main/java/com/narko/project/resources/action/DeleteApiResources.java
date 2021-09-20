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
import com.narko.project.payload.request.IdReqEntity;
import com.narko.project.payload.response.FinalResEntity;
import com.narko.project.repository.ApiRepository;
import com.narko.project.resources.core.DataProcess;

/**
 * 
 * @author agus.wijanarko
 *
 */

@Service
public class DeleteApiResources implements DataProcess<IdReqEntity> {
	private static final Logger log = LogManager.getLogger(DeleteApiResources.class);

	@Autowired
	ApiRepository apiRepository;

	@Override
	public FinalResEntity<?> processBo(IdReqEntity serviceInput, AuditReqEntity audit) {
		log.trace(DeleteApiResources.class);

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

			apiRepository.delete(dataOld);

			HttpStatus status = HttpStatus.OK;
			message = SuccessMessageIndoConstants.SUCCESS
					.concat(SuccessMessageIndoConstants.DELETE_SUCCESS);
			return respon = new FinalResEntity(null, status.value(), status, message);

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
