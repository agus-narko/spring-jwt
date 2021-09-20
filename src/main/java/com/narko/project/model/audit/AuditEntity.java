package com.narko.project.model.audit;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.narko.project.configuration.MasterGeneralConstants;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 
 * @author agus.wijanarko
 *
 */

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { AuditEntity.COL_CREATEDBY, AuditEntity.COL_CREATEDAT, AuditEntity.COL_UPDATEDBY,
		AuditEntity.COL_UPDATEDAT, AuditEntity.COL_STATUS }, allowGetters = true)
public abstract class AuditEntity implements Serializable {

	public static final String COL_CREATEDBY = "created_by";
	public static final String COL_CREATEDAT = "created_at";
	public static final String COL_UPDATEDBY = "updated_by";
	public static final String COL_UPDATEDAT = "updated_at";
	public static final String COL_STATUS = "status";
	public static final String COL_ID = "id";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "serial")
	@Schema(required = true, example = "1", description = "id data")
	protected Long id;

	@CreatedBy
	@Column(name = COL_CREATEDBY, nullable = false, updatable = false)
	@JsonIgnore
	protected Long createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = COL_CREATEDAT, nullable = false, updatable = false)
	@CreatedDate
	@JsonIgnore
	protected Date createdAt;

	@LastModifiedBy
	@Column(name = COL_UPDATEDBY, nullable = false)
	@JsonIgnore
	protected Long updateBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = COL_UPDATEDAT, nullable = false)
	@LastModifiedDate
	@JsonIgnore
	protected Date updatedAt;

	@Column(name = COL_STATUS, nullable = false)
	protected String status = MasterGeneralConstants.ACTIVATE;

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNameId() {
		return "id";
	}

}
