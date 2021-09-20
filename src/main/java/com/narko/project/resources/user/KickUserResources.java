package com.narko.project.resources.user;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
import com.narko.project.configuration.jwt.JwtUtils;
import com.narko.project.model.UserDetailsImpl;
import com.narko.project.model.UserEntity;
import com.narko.project.payload.request.AuditReqEntity;
import com.narko.project.payload.request.SigninReqEntity;
import com.narko.project.payload.response.FinalResEntity;
import com.narko.project.repository.UserRepository;
import com.narko.project.resources.core.AESUtil;
import com.narko.project.resources.core.DataProcess;

/**
 * 
 * @author surra arjun utomo
 *
 */

@Service
public class KickUserResources implements DataProcess<SigninReqEntity> {

	private static final Logger log = LogManager.getLogger(KickUserResources.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	SigninResources signIn;

	@Autowired
	AESUtil aesUtil;

	@SuppressWarnings("unchecked")
	@Override
	public FinalResEntity<?> processBo(SigninReqEntity serviceInput, AuditReqEntity audit) {
		log.trace(KickUserResources.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";
		FinalResEntity<?> respon = null;
		FinalResEntity<?> response = null;
		try {
//			start decrypt user name dan password
//			String splitusername[] = serviceInput.getUsername().split(" ");
//			String usernameEncrypt = splitusername[0];
//			String passwordEncrypt = serviceInput.getPassword();
//			String keyAndsalt = splitusername[1];
//			String decryptedvalusername = aesUtil.decrypt(usernameEncrypt, keyAndsalt,
//					keyAndsalt);
//			String decryptedvalupassword = aesUtil.decrypt(passwordEncrypt, keyAndsalt,
//					keyAndsalt);
//			serviceInput.setUsername(decryptedvalusername);
//			serviceInput.setPassword(decryptedvalupassword);
//			start decrypt user name dan password
			if (!userRepository.existsByUsernameAndStatusAndLogin(serviceInput.getUsername(),
					MasterGeneralConstants.ACTIVATE, MasterGeneralConstants.NO)) {
				Authentication authentication = authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(serviceInput.getUsername(),
								serviceInput.getPassword()));

				if (!authentication.isAuthenticated()) {
					HttpStatus status = HttpStatus.UNAUTHORIZED;
					message = ErrorMessageIndoConstants.ERROR.concat(ErrorMessageIndoConstants.WRONG_PASS_USER);
					return respon = new FinalResEntity(null, status.value(), status, message);
				} else {
					SecurityContextHolder.getContext().setAuthentication(authentication);
					UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
					UserEntity user = userRepository
							.findByUsernameAndStatus(serviceInput.getUsername(), MasterGeneralConstants.ACTIVATE)
							.orElse(null);
					user.setLogin(MasterGeneralConstants.NO);
					userRepository.saveAndFlush(user);
					respon = signIn.processBo(serviceInput, audit);
					return respon = respon;
				}
			} else {
				HttpStatus status = HttpStatus.UNAUTHORIZED;
				message = ErrorMessageIndoConstants.ERROR.concat(ErrorMessageIndoConstants.USER_LOGOUT);
				
			}

		

			HttpStatus status = HttpStatus.OK;
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
