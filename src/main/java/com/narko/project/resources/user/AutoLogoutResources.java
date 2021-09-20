package com.narko.project.resources.user;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.narko.project.configuration.MasterGeneralConstants;
import com.narko.project.repository.UserRepository;

/**
 * 
 * @author agus.wijanarko
 *
 */

@Component
public class AutoLogoutResources {

	private static final Logger log = LogManager.getLogger(AutoLogoutResources.class);

	@Autowired
	UserRepository userRepository;

	@Scheduled(cron = "0 0 0 * * ?")
	public void cronAutoLogout() {
		log.trace(AutoLogoutResources.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";

		try {

			userRepository.autoLogout(MasterGeneralConstants.NO);

		} catch (Exception e) {
			e.printStackTrace(ps);
			ps.close();
			message = e.getMessage();
			log.error(message);
			log.error("stackTrace : " + baos.toString());

		}

	}
}
