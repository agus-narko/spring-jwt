package com.narko.project.resources.user;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.stereotype.Service;

import com.narko.project.model.UserEntity;
import com.narko.project.payload.response.FinalResEntity;

/**
 * 
 * @author agus.wijanarko
 *
 */

@Service
public class GetUserLDAPResources {

	private static final Logger log = LogManager.getLogger(GetUserLDAPResources.class);

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

	public List<UserEntity> getLDAP(String uid) {
		log.trace(GetUserLDAPResources.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";
		FinalResEntity<?> respon = null;

		try {
//
//			contextSource.setUrl(url + '/' + base);
//			contextSource.setAnonymousReadOnly(anonymousReadOnly);
//			contextSource.setUserDn(userDnPattern);
//
//			contextSource.afterPropertiesSet();
//			ldapTemplate = new LdapTemplate(contextSource);
//
//			Filter filter = new EqualsFilter("uid", uid);
//
//			return ldapTemplate.search("", filter.encode(), new AttributesMapper() {
//				@Override
//				public UserEntity mapFromAttributes(Attributes attr) throws NamingException {
//
//					UserEntity user = new UserEntity();
//					String ldapUid = (String) attr.get("uid").get();

//
//					user.setUsername(ldapUid);
//
//					return user;
//				}
//			});

			// by pass ldap
			UserEntity user = new UserEntity();

			List<UserEntity> userList = new ArrayList<UserEntity>();
			user.setUsername(uid);
			userList.add(user);
			return userList;

		} catch (Exception e) {
			e.printStackTrace(ps);
			ps.close();
			message = e.getMessage();
			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
			log.error(message);
			log.error("stackTrace : " + baos.toString());
			return null;
		}

	}
}
