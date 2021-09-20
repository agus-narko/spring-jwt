package com.narko.project.configuration.jwt;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.narko.project.model.UserDetailsImpl;
import com.narko.project.model.UserEntity;
import com.narko.project.repository.UserRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;


@Component
public class JwtUtils {
	
	private static final Logger log = LogManager.getLogger(JwtUtils.class);


	@Value("${authservice.app.jwtSecret}")
	private String jwtSecret;

	@Value("${authservice.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	@Autowired
	UserRepository userRepository;

	public String generateJwtToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		return Jwts.builder().setSubject((userPrincipal.getUsername())) // perlu tambahan encript
				.setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		UserEntity user = new UserEntity();
		try {
			user = userRepository.findByLogin(authToken)
					.orElseThrow(() -> new UsernameNotFoundException("token invalid"));

			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
//		} catch (SignatureException e) {
//
//			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			e.printStackTrace(ps);
			ps.close();
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			
			user.setLogin("N");
			userRepository.saveAndFlush(user);
			e.printStackTrace(ps);
			ps.close();
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			e.printStackTrace(ps);
			ps.close();
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace(ps);
			ps.close();
			log.error("JWT claims string is empty: {}", e.getMessage());
		}
		finally {
			log.error("stackTrace : " + baos.toString());
		}

		return false;
	}
}
