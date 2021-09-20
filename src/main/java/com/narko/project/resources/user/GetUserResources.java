package com.narko.project.resources.user;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.narko.project.model.UserEntity;
import com.narko.project.payload.request.AuditReqEntity;
import com.narko.project.payload.request.GetReqEntity;
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
public class GetUserResources implements DataProcess<GetReqEntity> {

	private static final Logger log = LogManager.getLogger(GetUserResources.class);

	@Autowired
	UserRepository getUserRepository;

	@Autowired
	RoleRepository roleRepository;

	@Override
	public FinalResEntity<?> processBo(GetReqEntity serviceInput, AuditReqEntity audit) {
		log.trace(GetUserResources.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";
		FinalResEntity<?> respon = null;

		try {

			if (serviceInput.getSearch() != null) {
				if (serviceInput.getPage() != null) {
					Page<UserEntity> result = getUserRepository.findAll(serviceInput.getSearch(),
							serviceInput.getPage());
					HttpStatus status = HttpStatus.OK;
					return respon = new FinalResEntity(result, status.value(), status, message);
				} else {
					List<UserEntity> result = getUserRepository.findAll(serviceInput.getSearch(),
							Sort.by(serviceInput.getOrders()));
					HttpStatus status = HttpStatus.OK;
					return respon = new FinalResEntity(result, status.value(), status, message);
				}

			} else if (serviceInput.getSearch() == null && serviceInput.getPage() != null) {
				Page<UserEntity> result = getUserRepository.findAll(serviceInput.getPage());
				HttpStatus status = HttpStatus.OK;
				return respon = new FinalResEntity(result, status.value(), status, message);

			} else {
				List<UserEntity> result = getUserRepository.findAll(Sort.by(serviceInput.getOrders()));
				HttpStatus status = HttpStatus.OK;
				respon = new FinalResEntity(result, status.value(), status, message);
				return respon;

			}

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
