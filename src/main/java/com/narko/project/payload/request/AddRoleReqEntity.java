package com.narko.project.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.narko.project.model.RoleEntity;
import com.narko.project.model.audit.AuditEntity;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(value = { AuditEntity.COL_CREATEDBY, AuditEntity.COL_CREATEDAT, AuditEntity.COL_UPDATEDBY,
		AuditEntity.COL_UPDATEDAT, AuditEntity.COL_ID, AuditEntity.COL_STATUS, RoleEntity.COL_APPROVAL,
		"roleType" }, allowGetters = false)
public class AddRoleReqEntity extends RoleEntity {

	@Schema(required = false, example = "1", description = "id role approval")
	private Long approvalId;

	@Schema(required = true, example = "1", description = "id role type")
	private Long roleMenu;

	public Long getRoleMenu() {
		return roleMenu;
	}

	public void setRoleMenu(Long roleMenu) {
		this.roleMenu = roleMenu;
	}

	public Long getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(Long approvalId) {
		this.approvalId = approvalId;
	}

}
