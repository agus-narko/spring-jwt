package com.narko.project.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.narko.project.configuration.ErrorMessageIndoConstants;
import com.narko.project.configuration.MasterGeneralConstants;
import com.narko.project.model.audit.AuditEntity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 
 * @author agus.wijanarko
 *
 */
@Entity
@Table(name = UserEntity.TABLE_NAME, uniqueConstraints = { @UniqueConstraint(columnNames = UserEntity.COL_USERNAME) })
@JsonIgnoreProperties(value = { AuditEntity.COL_CREATEDBY, AuditEntity.COL_CREATEDAT, AuditEntity.COL_UPDATEDBY,
		AuditEntity.COL_UPDATEDAT, AuditEntity.COL_ID }, allowGetters = true)
public class UserEntity extends AuditEntity {

	public static final String TABLE_NAME = "m_user";
	public static final String TABLE_USER_ROLE = "user_role";

	public static final String COL_USERNAME = "username";
	public static final String COL_ROLEID = "role_id";
	public static final String COL_LOGIN = "login";

	@Schema(required = true, example = "admin", description = "username")
	@NotBlank(message = ErrorMessageIndoConstants.ERROR + ErrorMessageIndoConstants.USER_CANT_EMPTY)
	@Size(max = 50, message = ErrorMessageIndoConstants.MAX_LEN + "50")
	@Column(name = COL_USERNAME, nullable = false)
	private String username;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = COL_ROLEID, referencedColumnName = AuditEntity.COL_ID, nullable = true)
	private RoleEntity roles;

	@Column(name = COL_LOGIN, nullable = false)
	@JsonIgnore
	private String login = MasterGeneralConstants.NO;

	public UserEntity() {
	}

	public UserEntity(String username, RoleEntity roles) {
		this.username = username;
		this.roles = roles;
	}

	public UserEntity(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public RoleEntity getRoles() {
		return roles;
	}

	public void setRoles(RoleEntity roles) {
		this.roles = roles;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

}
