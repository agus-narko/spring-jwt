package com.narko.project.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.narko.project.model.ApiEntity;
import com.narko.project.model.audit.AuditEntity;

@JsonIgnoreProperties(value = { AuditEntity.COL_CREATEDBY, AuditEntity.COL_CREATEDAT, AuditEntity.COL_UPDATEDBY,
		AuditEntity.COL_UPDATEDAT,AuditEntity.COL_ID,AuditEntity.COL_STATUS}, allowGetters = true)
public class AddApiReqEntity extends ApiEntity {

	
}
