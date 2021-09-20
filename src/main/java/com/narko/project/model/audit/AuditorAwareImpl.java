package com.narko.project.model.audit;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.narko.project.model.UserDetailsImpl;
import com.narko.project.repository.UserRepository;

/**
 * 
 * @author agus.wijanarko
 * @param <T>
 *
 */

//public class AuditorAwareImpl implements AuditorAware<String> {
//
//	@Override
//	public Optional<String> getCurrentAuditor() {
//		return Optional.of("Ramesh");
//		// Use below commented code when will use Spring Security.
//	}
//
//}

// ------------------ Use below code for spring security --------------------------

public class AuditorAwareImpl<T> implements AuditorAware<T> {

	@Autowired
	UserRepository userRepository;

	@Override
	public Optional<T> getCurrentAuditor() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}

		if (!authentication.getName().equals("anonymousUser")) {

			UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
			return (Optional<T>) Optional.of(userPrincipal.getId());
		}
		else {
			return (Optional<T>) Optional.of(-99l);

		}

		
	}
}