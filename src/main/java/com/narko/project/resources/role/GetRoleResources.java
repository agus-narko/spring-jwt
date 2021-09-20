package com.narko.project.resources.role;

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

import com.narko.project.model.RoleEntity;
import com.narko.project.payload.request.AuditReqEntity;
import com.narko.project.payload.request.GetReqEntity;
import com.narko.project.payload.response.FinalResEntity;
import com.narko.project.repository.RoleRepository;
import com.narko.project.resources.core.DataProcess;

/**
 * 
 * @author agus.wijanarko
 *
 */

@Service
public class GetRoleResources implements DataProcess<GetReqEntity> {

	private static final Logger log = LogManager.getLogger(GetRoleResources.class);

	@Autowired
	RoleRepository roleRepository;

	@Override
	public FinalResEntity<?> processBo(GetReqEntity serviceInput,AuditReqEntity audit) {
		log.trace(GetRoleResources.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";
		FinalResEntity<?> respon = null;

		try {

			if (serviceInput.getSearch() != null) {
				if (serviceInput.getPage() != null) {
					Page<RoleEntity> result = roleRepository.findAll(serviceInput.getSearch(), serviceInput.getPage());
					HttpStatus status = HttpStatus.OK;
					return respon = new FinalResEntity(result, status.value(), status, message);
				} else {
					List<RoleEntity> result = roleRepository.findAll(serviceInput.getSearch(),
							Sort.by(serviceInput.getOrders()));
					HttpStatus status = HttpStatus.OK;
					return respon = new FinalResEntity(result, status.value(), status, message);
				}

			} else if (serviceInput.getSearch() == null && serviceInput.getPage() != null) {
				Page<RoleEntity> result = roleRepository.findAll(serviceInput.getPage());
				HttpStatus status = HttpStatus.OK;
				return respon = new FinalResEntity(result, status.value(), status, message);

			} else {
				List<RoleEntity> result = roleRepository.findAll(Sort.by(serviceInput.getOrders()));
				HttpStatus status = HttpStatus.OK;
				return respon = new FinalResEntity(result, status.value(), status, message);

			}

		} catch (Exception e) {
			e.printStackTrace(ps);
			ps.close();
			message = e.getMessage();
			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

			return respon = new FinalResEntity(null, status.value(), status, message);
		}
		finally {
			if(!message.isEmpty() || !message.equals("") || baos.size() > 1) {
				log.error( message);
				log.error("stackTrace : " + baos.toString());
			}
		}

	}
}
