package com.narko.project.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.narko.project.configuration.ErrorMessageIndoConstants;
import com.narko.project.model.audit.AuditEntity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 
 * @author agus.wijanarko
 *
 */

@Entity
@Table(name = RoleTypeEntity.TABLE_NAME)
@JsonIgnoreProperties(value = { AuditEntity.COL_CREATEDBY, AuditEntity.COL_CREATEDAT, AuditEntity.COL_UPDATEDBY,
		AuditEntity.COL_UPDATEDAT}, allowGetters = true)

public class RoleTypeEntity extends AuditEntity {

	
	public static final String TABLE_NAME = "role_type";
	public static final String TABLE_ROLE_ACTION = "role_action";

	public static final String COL_NAME = "name";
	public static final String COL_ACTIONID = "m_action_id";
	public static final String COL_ROLETYPE = "role_type_id";
	
	
	@Schema(required = true, example = "ADMIN", description = "nama role type")
	@NotBlank(message =  ErrorMessageIndoConstants.ERROR + ErrorMessageIndoConstants.ROLE_CANT_EMPTY)
	@Size(max = 20, message = ErrorMessageIndoConstants.MAX_LEN + "20")
	@Column(name = COL_NAME, nullable = false)
	private String name;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = TABLE_ROLE_ACTION, joinColumns = @JoinColumn(name = COL_ROLETYPE), inverseJoinColumns = @JoinColumn(name = COL_ACTIONID))
	@JsonIgnore
	private Set<ApiEntity> action = new HashSet<>();

	public RoleTypeEntity() {

	}

	public RoleTypeEntity(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ApiEntity> getAction() {
		return action;
	}

	public void setAction(Set<ApiEntity> action) {
		this.action = action;
	}
	
	

}