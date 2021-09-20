package com.narko.project.resources.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.narko.project.configuration.ErrorMessageIndoConstants;
import com.narko.project.configuration.HibernateProxyTypeAdapter;
import com.narko.project.configuration.MasterGeneralConstants;
import com.narko.project.configuration.ServiceConstants;
import com.narko.project.model.ApiEntity;
import com.narko.project.model.LogAccessEntity;
import com.narko.project.model.RoleEntity;
import com.narko.project.model.UserDetailsImpl;
import com.narko.project.payload.request.AddApprovalReqEntity;
import com.narko.project.payload.request.AuditReqEntity;
import com.narko.project.payload.response.FinalResEntity;
import com.narko.project.repository.ApiRepository;
import com.narko.project.repository.LogAccesRepository;
import com.narko.project.repository.RoleRepository;

/**
 * 
 * @author agus.wijanarko
 *
 * @param <T>
 */
@Service
public class BoInterceptor<T> {

	private static final Logger log = LogManager.getLogger(BoInterceptor.class);

	@Autowired
	ApiRepository apiRepository;

	@Autowired
	LogAccesRepository logAccesRepository;

	@Autowired
	RoleRepository roleRepository;

	private final ApplicationContext applicationContext;

	@Autowired
	public BoInterceptor(final ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public FinalResEntity<?> processAndLogCommand(T serviceInput, String service) {

		log.trace(BoInterceptor.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);

		String method = new String("-");
		FinalResEntity<?> response = null;
		Date requestDate = new Date();
		Set<String> messageList = new HashSet<>();
		String message = new String();
		AuditReqEntity audit = new AuditReqEntity();
		UserDetailsImpl userPrincipal = null;
		RoleEntity role = new RoleEntity();

		Class<?> logDataResult = null;
//		Object inputObj = input.newInstance();

		try {

			ApiEntity action = apiRepository.findByApiName(service).orElse(null);
			logDataResult = Class.forName(MasterGeneralConstants.PACK_MODEL + action.getEntityName());

			if (action == null) {

				message = ErrorMessageIndoConstants.ERROR + service + ErrorMessageIndoConstants.NOT_FOUND;
				HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

				response = new FinalResEntity(null, status.value(), status, message);
				return response;
			}

			else if (!action.getProcess().equals(MasterGeneralConstants.PROCESS_LOGIN)
					&& !action.getProcess().equals(MasterGeneralConstants.PROCESS_LOGOUT)) {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

				if (authentication != null || authentication.isAuthenticated()) {
					userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
					role = userPrincipal.getRole();
					audit.setUsername(userPrincipal.getUsername());
					audit.setRoleId(role.getId());
					if (role.getApproval() != null) {
						audit.setApproval(role.getApproval().getId());
					}
					
				}
			}

			method = action.getMethod();
			audit.setApi(action.getApiName());
			audit.setApi(action.getProcess());
			DataProcess bo = this.applicationContext.getBean(action.getResourceName(), DataProcess.class);

			if (action.getApproval().equals(MasterGeneralConstants.YES)) {

				if (audit.getApproval() != null && audit.getApproval() != null && audit.getApproval() != 0) {
					bo = this.applicationContext.getBean(MasterGeneralConstants.ADD_APPROVAL_RESOURCES,
							DataProcess.class);
					AddApprovalReqEntity addApproval = new AddApprovalReqEntity<>(serviceInput, action);
					response = bo.processBo(addApproval, audit);
					return response;
				}

				else {
					messageList
							.add(ErrorMessageIndoConstants.ERROR + service + ErrorMessageIndoConstants.NEED_APPROVER);
					messageList.add(ErrorMessageIndoConstants.ERROR + audit.getUsername()
							+ ErrorMessageIndoConstants.USER_NO_APPROVER);

					HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

					response = new FinalResEntity(null, status.value(), status, messageList);
					return response;
				}

			}

			else {

				response = bo.processBo(serviceInput, audit);
				return response;
			}
		} catch (NoSuchBeanDefinitionException e) {
			log.error(BoInterceptor.class);
			e.printStackTrace(ps);
			ps.close();
			messageList.add(e.getMessage());
			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

			response = new FinalResEntity(null, status.value(), status, messageList);
			return response;
		} catch (Exception e) {
			log.error(BoInterceptor.class);
			e.printStackTrace(ps);
			ps.close();
			messageList.add(e.getMessage());
			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

			response = new FinalResEntity(null, status.value(), status, messageList);
			return response;
		} finally {

			String userAkses = "anonymousUser";

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication != null) {

				userAkses = authentication.getName();
			}

			if (!method.equals(RequestMethod.GET.toString())) {

				Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY).create();
				String requestStr = gson.toJson(serviceInput);

				String responseStr = gson.toJson(response.getData());
//				String responseStr = null;
				LogAccessEntity logAccess = new LogAccessEntity(method, userAkses, service,
						ServiceConstants.BASE_REST_URL + service, requestStr, requestDate, responseStr,
						response.getStatus(), response.getError().name().toString());
				logAccesRepository.save(logAccess);

			}

			if (messageList.size() > 1 || baos.size() > 1) {
				log.error(messageList);
				log.error("stackTrace : " + baos.toString());
			}
		}

	}

}