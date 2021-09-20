package com.narko.project.configuration;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.Filter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.narko.project.model.UserEntity;
import com.narko.project.resources.user.UserDetailsServiceImpl;

@Component
//@EnableConfigurationProperties(LdapProperties.class)
public class OpenLdapAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private LdapContextSource contextSource;

	private LdapTemplate ldapTemplate;

	@Value("${ldap.userDnPattern}")
	private String userDnPattern;

	@Value("${ldap.urls}")
	private String url;

	@Value("${ldap.base}")
	private String base;

	@Value("${ldap.anonymousReadOnly}")
	private Boolean anonymousReadOnly;

	@PostConstruct
	private void initContext() {
		contextSource.setUrl(url + '/' + base);
		contextSource.setAnonymousReadOnly(anonymousReadOnly);
		contextSource.setUserDn(userDnPattern);

		contextSource.afterPropertiesSet();
		ldapTemplate = new LdapTemplate(contextSource);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

//		Filter filter = new EqualsFilter("uid", authentication.getName());
//
//		Boolean authenticate = ldapTemplate.authenticate(LdapUtils.emptyLdapName(), filter.encode(),
//				authentication.getCredentials().toString());
//
//		if (authenticate) {
//
//			UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
//			authentication = new UsernamePasswordAuthenticationToken(userDetails,
//					authentication.getCredentials().toString(), userDetails.getAuthorities());
//		}
//
//		return authentication;
		
		// bypass login
		UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
		authentication = new UsernamePasswordAuthenticationToken(userDetails,
				authentication.getCredentials().toString(), userDetails.getAuthorities());
		return authentication;
	}

	public List<UserEntity> getAllPerson(Filter filter) {
		return ldapTemplate.search("", filter.encode(), new AttributesMapper() {
			@Override
			public UserEntity mapFromAttributes(Attributes attr) throws NamingException {
				UserEntity person = new UserEntity();

				person.setUsername((String) attr.get("uid").get());
				return person;
			}
		});
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
