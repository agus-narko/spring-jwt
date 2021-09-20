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
@Table(name = ApprovalEntity.TABLE_NAME)
@JsonIgnoreProperties(value = { AuditEntity.COL_CREATEDBY, AuditEntity.COL_CREATEDAT, AuditEntity.COL_UPDATEDBY,
		AuditEntity.COL_UPDATEDAT, AuditEntity.COL_STATUS }, allowGetters = true)
public class ApprovalEntity extends AuditEntity {

	public static final String TABLE_NAME = "m_approval";
	public static final String TABLE_DETAIL_APPROVAL_JOIN = "detail_approval_join";

	public static final String COL_API = "api";
	public static final String COL_REQUESTPROCESS = "request_process";
	public static final String COL_REQUESTBY = "request_by";
	public static final String COL_APPROVEBY = "approve_by";
	public static final String COL_ROLEAPPROVER = "role_approver";
	public static final String COL_APPROVALID = "m_approval_id";
	public static final String COL_DETAILAPPROVALID = "detail_approval_id";
	public static final String COL_NOTE = "note";
	public static final String COL_DATAID = "data_id";

	@Schema(required = true, example = "stopCashbackService", description = "nama api")
	@NotBlank(message =  ErrorMessageIndoConstants.ERROR + ErrorMessageIndoConstants.API_CANT_EMPTY)
	@Size(max = 50, message = ErrorMessageIndoConstants.MAX_LEN + " 50 char")
	@Column(name = COL_API, nullable = false)
	private String api;

	@Schema(required = true, example = "user-a", description = "username req approval")
	@NotBlank(message =  ErrorMessageIndoConstants.ERROR + "requestProcess " + ErrorMessageIndoConstants.CANT_EMPTY)
	@Size(max = 20, message = ErrorMessageIndoConstants.MAX_LEN + " 20 char")
	@Column(name = COL_REQUESTPROCESS, nullable = false)
	private String requestProcess;

	@Schema(required = true, example = "user-a", description = "username req approval")
	@NotBlank(message =  ErrorMessageIndoConstants.ERROR + ErrorMessageIndoConstants.NAME_CANT_EMPTY)
	@Size(max = 50, message = ErrorMessageIndoConstants.MAX_LEN + " 50 char")
	@Column(name = COL_REQUESTBY, nullable = false)
	private String requestBy;

	@Schema(required = false, example = "approver-a", description = "username approver")
	@Size(max = 50, message = ErrorMessageIndoConstants.MAX_LEN + " 50 char")
	@Column(name = COL_APPROVEBY, nullable = true)
	private String approveBy;

	@Schema(required = true, example = "2", description = "id role approver")
	@Column(name = COL_ROLEAPPROVER, nullable = true)
	private Long roleApprover;

	@Schema(required = false, example = "2", description = "id data")
	@Column(name = COL_DATAID, nullable = true)
	private Long dataId;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = TABLE_DETAIL_APPROVAL_JOIN, joinColumns = @JoinColumn(name = COL_APPROVALID), inverseJoinColumns = @JoinColumn(name = COL_DETAILAPPROVALID))
	private Set<DetailApprovalEntity> detailApproval = new HashSet<>();

	@Schema(required = false, description = "note dari approver")
	@Column(name = COL_NOTE, nullable = true)
	private String note;

	public ApprovalEntity() {
		super();
	}

	public ApprovalEntity(
			@NotBlank(message =  ErrorMessageIndoConstants.ERROR + "Api tidak boleh kosong") @Size(max = 50, message = "maksimal inputan  50 char") String api,
			@NotBlank(message =  ErrorMessageIndoConstants.ERROR + "name tidak boleh kosong") @Size(max = 20, message = "maksimal inputan  20 char") String requestProcess,
			@NotBlank(message =  ErrorMessageIndoConstants.ERROR + "name tidak boleh kosong") @Size(max = 50, message = "maksimal inputan  50 char") String requestBy,
			Long roleApprover, Long dataId) {
		super();
		this.api = api;
		this.requestProcess = requestProcess;
		this.requestBy = requestBy;
		this.roleApprover = roleApprover;
		this.dataId = dataId;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public String getRequestBy() {
		return requestBy;
	}

	public void setRequestBy(String requestBy) {
		this.requestBy = requestBy;
	}

	public String getApproveBy() {
		return approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

	public Long getRoleApprover() {
		return roleApprover;
	}

	public void setRoleApprover(Long roleApprover) {
		this.roleApprover = roleApprover;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getRequestProcess() {
		return requestProcess;
	}

	public void setRequestProcess(String requestProcess) {
		this.requestProcess = requestProcess;
	}

	public Long getDataId() {
		return dataId;
	}

	public void setDataId(Long dataId) {
		this.dataId = dataId;
	}

	public Set<DetailApprovalEntity> getDetailApproval() {
		return detailApproval;
	}

	public void setDetailApproval(Set<DetailApprovalEntity> detailApproval) {
		this.detailApproval = detailApproval;
	}
	
	
	

}