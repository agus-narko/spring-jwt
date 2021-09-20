package com.narko.project.payload.response;

import java.util.List;

import com.narko.project.model.RoleEntity;

/**
 * @author agus.wijanarko
 *
 */
public class JwtResEntity {
	private Long id;
	private String token;
	private String type = "Bearer";
	private String username;
	private RoleEntity role;
	private List<String> authorities;

	public JwtResEntity(String token, String username, RoleEntity role, List<String> authorities) {
		this.token = token;
		this.username = username;
		this.role = role;
		this.authorities = authorities;
	}

	public JwtResEntity(Long id, String token, String username, RoleEntity role, List<String> authorities) {
		super();
		this.id = id;
		this.token = token;
		this.username = username;
		this.role = role;
		this.authorities = authorities;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

	public List<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

}
