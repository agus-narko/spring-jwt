package com.narko.project.resources.approvalFlow;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.narko.project.configuration.ErrorMessageIndoConstants;
import com.narko.project.configuration.MasterGeneralConstants;
import com.narko.project.model.ApiEntity;
import com.narko.project.model.ApprovalEntity;
import com.narko.project.model.DetailApprovalEntity;
import com.narko.project.payload.request.AuditReqEntity;
import com.narko.project.payload.request.IdReqEntity;
import com.narko.project.payload.response.FinalResEntity;
import com.narko.project.repository.ApiRepository;
import com.narko.project.repository.ApprovalRepository;
import com.narko.project.resources.core.DataProcess;

/**
 * 
 * @author agus.wijanarko
 *
 */

@Service
public class ApproveApprovalResources implements DataProcess<IdReqEntity> {
	private static final Logger log = LogManager.getLogger(ApproveApprovalResources.class);

	@Autowired
	ApprovalRepository approvalRepository;

	@Autowired
	ApiRepository apiRepository;

	private final ApplicationContext applicationContext;

	@Autowired
	public ApproveApprovalResources(final ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@SuppressWarnings("unchecked")
	@Override
	public FinalResEntity<?> processBo(IdReqEntity serviceInput, AuditReqEntity audit) {
		log.trace(ApproveApprovalResources.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";
		FinalResEntity<?> respon = null;
		ApprovalEntity approval = new ApprovalEntity();

		try {

			approval = approvalRepository.findById(serviceInput.getId()).orElse(null);

			if (approval == null) {
				message = ErrorMessageIndoConstants.ERROR.concat(ErrorMessageIndoConstants.NOT_FOUND_EMPTY);
				HttpStatus status = HttpStatus.BAD_REQUEST;
				return respon = new FinalResEntity(null, status.value(), status, message);
			}

			else if (approval.getRoleApprover() != audit.getRoleId()) {
				message = ErrorMessageIndoConstants.ERROR.concat(ErrorMessageIndoConstants.INVALID_APPROVAL);
				HttpStatus status = HttpStatus.BAD_REQUEST;
				return respon = new FinalResEntity(null, status.value(), status, message);
			}

			else if (!approval.getStatus().equals(MasterGeneralConstants.REQUEST)) {
				message = ErrorMessageIndoConstants.ERROR.concat(ErrorMessageIndoConstants.ALREADY_REJECTED);
				HttpStatus status = HttpStatus.BAD_REQUEST;
				return respon = new FinalResEntity(null, status.value(), status, message);
			} else {

				Set<DetailApprovalEntity> detailApproval = approval.getDetailApproval();

				ApiEntity action = apiRepository.findByApiName(approval.getApi()).orElse(null);

				DataProcess bo = this.applicationContext.getBean(action.getResourceName(), DataProcess.class);

				Class<?> input = Class.forName(MasterGeneralConstants.PACK_MODEL_REQ + action.getInputService());
				Object inputObj = input.newInstance();

				for (DetailApprovalEntity detailApprovalEntity : detailApproval) {

					Field field = input.getDeclaredField(detailApprovalEntity.getKeyName());
					field.setAccessible(true);

					Type dataType = field.getType();

					String keyName = detailApprovalEntity.getKeyName();
					String setterKeyName = "set" + keyName.substring(0, 1).toUpperCase() + keyName.substring(1);

					Method setter = input.getMethod(setterKeyName, field.getType());

					if (dataType.equals(Long.class)) {
						setter.invoke(inputObj, Long.valueOf(detailApprovalEntity.getValue()));

					} else if (dataType.equals(String.class)) {
						setter.invoke(inputObj, detailApprovalEntity.getValue());

					}

				}

				respon = bo.processBo(inputObj, audit);

				if (respon.getError().equals(HttpStatus.OK)) {
					approval.setStatus(MasterGeneralConstants.APPROVED);

					approval.setApproveBy(audit.getUsername());

					approvalRepository.save(approval);
				}

				return respon;

			}

		} catch (

		Exception e) {
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
