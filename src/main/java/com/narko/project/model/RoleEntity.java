package com.narko.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
@Table(name = RoleEntity.TABLE_NAME)
@JsonIgnoreProperties(value = { AuditEntity.COL_CREATEDBY, AuditEntity.COL_CREATEDAT, AuditEntity.COL_UPDATEDBY,
		AuditEntity.COL_UPDATEDAT }, allowGetters = true
//		,ignoreUnknown = true
)
public class RoleEntity extends AuditEntity {

	public static final String TABLE_NAME = "m_role";
	public static final String TABLE_ROLE_TYPE = "role_type_join";

	public static final String COL_NAME = "name";
	public static final String COL_APPROVAL = "approval";
	public static final String COL_ROLEID = "m_role_id";
	public static final String COL_ROLETYPE = "role_type_id";
	public static final String COL_MENU = "menu";

	@Schema(required = true, example = "ADMIN", description = "nama role")
	@NotBlank(message =  ErrorMessageIndoConstants.ERROR + ErrorMessageIndoConstants.ROLE_NAME_CANT_EMPTY)
	@Size(max = 20, message = ErrorMessageIndoConstants.MAX_LEN + "20")
	@Column(name = COL_NAME, nullable = false)
	private String name;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = COL_APPROVAL, referencedColumnName = AuditEntity.COL_ID, nullable = true)
	private RoleEntity approval;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = COL_MENU, referencedColumnName = AuditEntity.COL_ID, nullable = true)
	private RoleTypeEntity roleType;

	public RoleEntity(String name, RoleEntity approval, RoleTypeEntity roleType) {
		super();
		this.name = name;
		this.roleType = roleType;
		this.approval = approval;
	}

	public RoleEntity(String name, Long id) {
		super();
		this.name = name;
		this.id = id;
	}

	public RoleEntity() {

	}

	public RoleEntity(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RoleTypeEntity getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleTypeEntity roleType) {
		this.roleType = roleType;
	}

	public RoleEntity getApproval() {
		return approval;
	}

	public void setApproval(RoleEntity approval) {
		this.approval = approval;
	}

}