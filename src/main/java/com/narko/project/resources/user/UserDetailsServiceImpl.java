package com.narko.project.resources.user;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.narko.project.configuration.ErrorMessageIndoConstants;
import com.narko.project.configuration.MasterGeneralConstants;
import com.narko.project.model.ApiEntity;
import com.narko.project.model.UserDetailsImpl;
import com.narko.project.model.UserEntity;
import com.narko.project.repository.UserRepository;

@Service
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
	private static final Logger log = LogManager.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.trace(UserDetailsServiceImpl.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";

		try {

			UserEntity user = userRepository.findByUsernameAndStatus(username, MasterGeneralConstants.ACTIVATE)
					.orElseThrow(
							() -> new UsernameNotFoundException(username.concat(ErrorMessageIndoConstants.NOT_FOUND)));

			Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

			for (ApiEntity roleAction : user.getRoles().getRoleType().getAction()) {
				authorities.add(new SimpleGrantedAuthority(roleAction.getApiName()));
			}

			return new UserDetailsImpl(user.getId(), user.getUsername(), user.getRoles(), authorities);
		} catch (Exception e) {
			e.printStackTrace(ps);
			ps.close();
			message = e.getMessage();
			log.error(message);
			log.error("stackTrace : " + baos.toString());
			return null;

		}

	}

}
