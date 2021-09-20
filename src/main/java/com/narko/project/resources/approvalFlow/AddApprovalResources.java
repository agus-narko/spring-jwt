package com.narko.project.resources.approvalFlow;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
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
import com.narko.project.model.ApprovalEntity;
import com.narko.project.model.DetailApprovalEntity;
import com.narko.project.payload.request.AddApprovalReqEntity;
import com.narko.project.payload.request.AuditReqEntity;
import com.narko.project.payload.response.FinalResEntity;
import com.narko.project.repository.ApprovalRepository;
import com.narko.project.repository.DetailApprovalRepository;
import com.narko.project.resources.core.DataProcess;

/**
 * 
 * @author agus.wijanarko
 *
 */

@Service
public class AddApprovalResources implements DataProcess<AddApprovalReqEntity> {
	private static final Logger log = LogManager.getLogger(AddApprovalResources.class);

	@Autowired
	ApprovalRepository approvalRepository;

	@Autowired
	DetailApprovalRepository detailApprovalRepository;

	@SuppressWarnings("unchecked")
	@Override
	public FinalResEntity<?> processBo(AddApprovalReqEntity serviceInput, AuditReqEntity audit) {
		log.trace(AddApprovalResources.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";
		FinalResEntity<?> respon = null;
		Set<DetailApprovalEntity> detailApprovalList = new HashSet<>();
		ApprovalEntity approval = new ApprovalEntity();

		try {

			ApiEntity api = serviceInput.getAction();
			Field[] dataField = serviceInput.getDataInput().getClass().getDeclaredFields();

			Class<?> act = Class.forName(MasterGeneralConstants.PACK_MODEL + api.getEntityName());
			Field[] entityField = act.getFields();

			for (Field field : dataField) {
				DetailApprovalEntity detailApproval = new DetailApprovalEntity();
				field.setAccessible(true);

				if (field.getName().equals(MasterGeneralConstants.CONST_ID)) {
					approval.setDataId(Long.valueOf(field.get(serviceInput.getDataInput()).toString()));

				}

				detailApproval.setKeyName(field.getName());
				detailApproval.setValue(field.get(serviceInput.getDataInput()).toString());

				for (Field field2 : entityField) {

					String colName = MasterGeneralConstants.CONST_COL + field.getName().toUpperCase();
					if (field2.getName().equals(colName)) {

						detailApproval.setColumnName(field2.get(act).toString());
					} else if (field2.getName().equals(MasterGeneralConstants.CONST_TABLE)) {
						detailApproval.setTableName(field2.get(act).toString());

					}

				}

				detailApprovalList.add(detailApproval);

			}

			if (approvalRepository.existsByDataIdAndStatus(approval.getDataId(), MasterGeneralConstants.REQUEST)) {
				message = ErrorMessageIndoConstants.ERROR.concat(ErrorMessageIndoConstants.REQUESTING)
						.concat(ErrorMessageIndoConstants.FINISH_PREVIOUS);
				HttpStatus status = HttpStatus.BAD_REQUEST;
				return respon = new FinalResEntity(null, status.value(), status, message);
			} else {
				approval.setStatus(MasterGeneralConstants.REQUEST);

				approval.setApi(api.getApiName());
				approval.setRequestProcess(api.getProcess());
				approval.setRoleApprover(audit.getApproval());
				approval.setRequestBy(audit.getUsername());

				detailApprovalRepository.saveAll(detailApprovalList);
				detailApprovalRepository.flush();
				approval.setDetailApproval(detailApprovalList);

				approvalRepository.save(approval);
				approvalRepository.flush();
				HttpStatus status = HttpStatus.MULTI_STATUS;
				message = SuccessMessageIndoConstants.APPROVAL.concat(api.getProcess())
						.concat(SuccessMessageIndoConstants.NEED_APPROVE);
				return respon = new FinalResEntity(approval, status.value(), status, message);
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
