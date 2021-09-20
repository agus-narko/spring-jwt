package com.narko.project.resources.user;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.narko.project.configuration.ErrorMessageIndoConstants;
import com.narko.project.configuration.MasterGeneralConstants;
import com.narko.project.configuration.SuccessMessageIndoConstants;
import com.narko.project.configuration.jwt.JwtUtils;
import com.narko.project.model.UserDetailsImpl;
import com.narko.project.model.UserEntity;
import com.narko.project.payload.request.AuditReqEntity;
import com.narko.project.payload.request.SigninReqEntity;
import com.narko.project.payload.response.FinalResEntity;
import com.narko.project.payload.response.JwtResEntity;
import com.narko.project.repository.RoleRepository;
import com.narko.project.repository.UserRepository;
import com.narko.project.resources.core.AESUtil;
import com.narko.project.resources.core.DataProcess;

/**
 * 
 * @author agus.wijanarko
 *
 */

@Service
public class SigninResources implements DataProcess<SigninReqEntity> {

	private static final Logger log = LogManager.getLogger(SigninResources.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	AESUtil aesUtil;

	@SuppressWarnings("unchecked")
	@Override
	public FinalResEntity<?> processBo(SigninReqEntity serviceInput, AuditReqEntity audit) {
		log.trace(SigninResources.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";
		FinalResEntity<?> respon = null;
		try {

//			start decrypt user name dan password

//			String splitusername[] = serviceInput.getUsername().split(" ");
//			if(splitusername.length<2){
//				HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//				return respon = new FinalResEntity(null, status.value(),
//						status, message);
//			}
//			String usernameEncrypt = splitusername[0];
//			String passwordEncrypt = serviceInput.getPassword();
//			String keyAndsalt = splitusername[1];
//			String decryptedvalusername = aesUtil.decrypt(usernameEncrypt, keyAndsalt,
//					keyAndsalt);
//			String decryptedvalupassword = aesUtil.decrypt(passwordEncrypt, keyAndsalt,
//					keyAndsalt);
//			serviceInput.setUsername(decryptedvalusername);
//			serviceInput.setPassword(decryptedvalupassword);

//			end decrypt user name dan password

			if (!userRepository.existsByUsernameAndStatus(serviceInput.getUsername(),
					MasterGeneralConstants.ACTIVATE)) {

				HttpStatus status = HttpStatus.BAD_REQUEST;
				message = ErrorMessageIndoConstants.ERROR.concat(serviceInput.getUsername())
						.concat(ErrorMessageIndoConstants.WRONG_PASS_USER);

				return respon = new FinalResEntity(null, status.value(), status, message);
			}

			UserEntity user = userRepository
					.findByUsernameAndStatus(serviceInput.getUsername(), MasterGeneralConstants.ACTIVATE).orElse(null);
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(serviceInput.getUsername(), serviceInput.getPassword()));
			if (!user.getLogin().equals(MasterGeneralConstants.NO)) {
				if (!authentication.isAuthenticated()) {
					HttpStatus status = HttpStatus.UNAUTHORIZED;
					message = ErrorMessageIndoConstants.ERROR.concat(ErrorMessageIndoConstants.WRONG_PASS_USER);
					return respon = new FinalResEntity(null, status.value(), status, message);
				} else if (jwtUtils.validateJwtToken(user.getLogin())) {
					HttpStatus status = HttpStatus.UNAUTHORIZED;
					message = ErrorMessageIndoConstants.ERROR.concat(ErrorMessageIndoConstants.ALREADY_LOGGED_IN);
					return respon = new FinalResEntity(null, status.value(), status, message);
				} else {
					user.setLogin(MasterGeneralConstants.NO);
					userRepository.saveAndFlush(user);
				}

			}

			if (user.getRoles() == null) {

				HttpStatus status = HttpStatus.BAD_REQUEST;
				message = ErrorMessageIndoConstants.ERROR.concat(serviceInput.getUsername())
						.concat(ErrorMessageIndoConstants.USER_NO_ROLE);
				return respon = new FinalResEntity(null, status.value(), status, message);
			}
			SecurityContextHolder.getContext().setAuthentication(authentication);

			if (!authentication.isAuthenticated()) {
				HttpStatus status = HttpStatus.UNAUTHORIZED;
				message = ErrorMessageIndoConstants.ERROR.concat(ErrorMessageIndoConstants.WRONG_PASS_USER);
				return respon = new FinalResEntity(null, status.value(), status, message);
			}
			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> authority = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());

			user.setLogin(jwt);
			userRepository.saveAndFlush(user);

			// JwtResEntity token = new JwtResEntity(jwt,
			// userDetails.getUsername(), userDetails.getRole(), authority);
			JwtResEntity token = new JwtResEntity(userDetails.getId(), jwt, userDetails.getUsername(),
					userDetails.getRole(), authority);

			HttpStatus status = HttpStatus.OK;
			message = SuccessMessageIndoConstants.SUCCESS.concat(userDetails.getUsername())
					.concat(SuccessMessageIndoConstants.SIGNIN_SUCCESS);
			return respon = new FinalResEntity(token, status.value(), status, message);

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
