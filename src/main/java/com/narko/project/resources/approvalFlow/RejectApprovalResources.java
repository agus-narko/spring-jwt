package com.narko.project.resources.approvalFlow;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.narko.project.configuration.ErrorMessageIndoConstants;
import com.narko.project.configuration.MasterGeneralConstants;
import com.narko.project.configuration.SuccessMessageIndoConstants;
import com.narko.project.model.ApprovalEntity;
import com.narko.project.payload.request.AuditReqEntity;
import com.narko.project.payload.request.RejectApprovalReqEntity;
import com.narko.project.payload.response.FinalResEntity;
import com.narko.project.repository.ApprovalRepository;
import com.narko.project.resources.core.DataProcess;

/**
 * 
 * @author agus.wijanarko
 *
 */

@Service
public class RejectApprovalResources implements DataProcess<RejectApprovalReqEntity> {
	private static final Logger log = LogManager.getLogger(RejectApprovalResources.class);

	@Autowired
	ApprovalRepository approvalRepository;

	@SuppressWarnings("unchecked")
	@Override
	public FinalResEntity<?> processBo(RejectApprovalReqEntity serviceInput, AuditReqEntity audit) {
		log.trace(RejectApprovalResources.class);

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

			else if (!approval.getStatus().equals(MasterGeneralConstants.REQUEST)) {
				message = ErrorMessageIndoConstants.ERROR.concat(ErrorMessageIndoConstants.ALREADY_REJECTED);
				HttpStatus status = HttpStatus.BAD_REQUEST;
				return respon = new FinalResEntity(null, status.value(), status, message);
			} else {
				approval.setStatus(MasterGeneralConstants.REJECTED);

				approval.setNote(serviceInput.getNote());
				approval.setApproveBy(audit.getUsername());

				approvalRepository.save(approval);
				HttpStatus status = HttpStatus.OK;
				message = SuccessMessageIndoConstants.APPROVAL.concat(approval.getApi())
						.concat(SuccessMessageIndoConstants.REJECT_PROCESS);
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
