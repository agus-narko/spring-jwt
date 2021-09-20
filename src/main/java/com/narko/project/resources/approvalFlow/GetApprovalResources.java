package com.narko.project.resources.approvalFlow;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.narko.project.configuration.MasterGeneralConstants;
import com.narko.project.model.ApiEntity;
import com.narko.project.model.ApprovalEntity;
import com.narko.project.model.DetailApprovalEntity;
import com.narko.project.payload.request.AuditReqEntity;
import com.narko.project.payload.request.GetReqEntity;
import com.narko.project.payload.response.FinalResEntity;
import com.narko.project.payload.response.GetApprovalRespEntity;
import com.narko.project.repository.ApiRepository;
import com.narko.project.repository.ApprovalRepository;
import com.narko.project.resources.core.DataProcess;
import com.narko.project.searchSpec.MappingSpesification;

/**
 * 
 * @author agus.wijanarko
 *
 */
@Service
public class GetApprovalResources implements DataProcess<GetReqEntity> {
	private static final Logger log = LogManager.getLogger(GetApprovalResources.class);

	@Autowired
	ApprovalRepository approvalRepository;

	@Autowired
	ApiRepository apiRepository;

	@Autowired
	MappingSpesification mappingSpesification;

	private final ApplicationContext applicationContext;

	@Autowired
	public GetApprovalResources(final ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public FinalResEntity<?> processBo(GetReqEntity serviceInput, AuditReqEntity audit) {
		log.trace(GetApprovalResources.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";
		FinalResEntity<?> respon = null;

		try {

			if (serviceInput.getSearch() != null) {
				if (serviceInput.getPage() != null) {
					Page<ApprovalEntity> result = approvalRepository.findAll(serviceInput.getSearch(),
							serviceInput.getPage());
					Set<GetApprovalRespEntity> dataApproval = getDataDetail(result.getContent(), audit);

					HttpStatus status = HttpStatus.OK;
					return respon = new FinalResEntity(dataApproval, status.value(), status, message);
				} else {
					List<ApprovalEntity> result = approvalRepository.findAll(serviceInput.getSearch(),
							Sort.by(serviceInput.getOrders()));
					Set<GetApprovalRespEntity> dataApproval = getDataDetail(result, audit);

					HttpStatus status = HttpStatus.OK;
					return respon = new FinalResEntity(dataApproval, status.value(), status, message);
				}

			} else if (serviceInput.getSearch() == null && serviceInput.getPage() != null) {
				Page<ApprovalEntity> result = approvalRepository.findAll(serviceInput.getPage());

				Set<GetApprovalRespEntity> dataApproval = getDataDetail(result.getContent(), audit);

				HttpStatus status = HttpStatus.OK;
				return respon = new FinalResEntity(dataApproval, status.value(), status, message);

			} else {

				List<ApprovalEntity> result = approvalRepository.findAll(Sort.by(serviceInput.getOrders()));

				Set<GetApprovalRespEntity> dataApproval = getDataDetail(result, audit);

				HttpStatus status = HttpStatus.OK;
				return respon = new FinalResEntity(dataApproval, status.value(), status, message);

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

	private Set<GetApprovalRespEntity> getDataDetail(List<ApprovalEntity> result, AuditReqEntity audit) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";
		Set<GetApprovalRespEntity> dataApproval = new HashSet<>();

		try {
			for (ApprovalEntity approvalEntity : result) {

				List<DetailApprovalEntity> detailApproval = new ArrayList<DetailApprovalEntity>(
						approvalEntity.getDetailApproval());

				ApiEntity dataApi = apiRepository.findByApiName(approvalEntity.getApi()).orElse(null);

				Class<?> entityClass = Class.forName(MasterGeneralConstants.PACK_MODEL + dataApi.getEntityName());

				Field field = entityClass.getDeclaredField(MasterGeneralConstants.CONST_GET_DATA);

				String search = "id:" + approvalEntity.getDataId() + ":;";
				String beanGetdata = field.get(entityClass).toString();

				System.out.println();

				GetReqEntity filterGet = mappingSpesification.toQuery(search, null, null);

				DataProcess callGetData = this.applicationContext.getBean(beanGetdata, DataProcess.class);
				FinalResEntity<?> resultGetData = callGetData.processBo(filterGet, audit);

				GetApprovalRespEntity approval = new GetApprovalRespEntity(approvalEntity, resultGetData.getData());
				dataApproval.add(approval);

			}
		} catch (Exception e) {
			e.printStackTrace(ps);
			ps.close();
			message = e.getMessage();
			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

		}
		return dataApproval;

	}
}
