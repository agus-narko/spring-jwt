package com.narko.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.narko.project.configuration.ErrorMessageIndoConstants;
import com.narko.project.model.audit.AuditEntity;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = ApiEntity.TABLE_NAME, uniqueConstraints = { @UniqueConstraint(columnNames = ApiEntity.COL_APINAME) })
@JsonIgnoreProperties(value = { AuditEntity.COL_CREATEDBY, AuditEntity.COL_CREATEDAT, AuditEntity.COL_UPDATEDBY,
		AuditEntity.COL_UPDATEDAT, AuditEntity.COL_STATUS }, allowGetters = true)

public class ApiEntity extends AuditEntity {

	public static final String TABLE_NAME = "m_action";
	
	public static final String COL_GRUP = "grup";
	public static final String COL_APINAME = "api_name";
	public static final String COL_RESOURCENAME = "resource_name";
	public static final String COL_DRISKRIPSI = "deskripsi";
	public static final String COL_METHOD = "method";
	public static final String COL_PROCESS = "process";
	public static final String COL_APPROVAL = "approval";
	public static final String COL_ENTITYNAME = "entity_name";
	public static final String COL_INPUTSERVICE = "input_service";


	@Schema(required = true, example = "USER", description = "nama grup api/service")
	@NotBlank(message =  ErrorMessageIndoConstants.ERROR + "grup " + ErrorMessageIndoConstants.CANT_EMPTY)
	@Size(max = 50, message = ErrorMessageIndoConstants.MAX_LEN + "50")
	@Column(name = COL_GRUP, nullable = false)
	private String grup;

	@Schema(required = true, example = "getUserService", description = "nama api/service")
	@NotBlank(message =  ErrorMessageIndoConstants.ERROR + "apiName " + ErrorMessageIndoConstants.CANT_EMPTY)
	@Size(max = 50, message = ErrorMessageIndoConstants.MAX_LEN + "50")
	@Column(name = COL_APINAME, nullable = false)
	private String apiName;

	@Schema(required = true, example = "getUserResources", description = "nama resource dari service")
	@NotBlank(message =  ErrorMessageIndoConstants.ERROR + "resourceName " + ErrorMessageIndoConstants.CANT_EMPTY)
	@Size(max = 50, message = ErrorMessageIndoConstants.MAX_LEN + "50")
	@Column(name = COL_RESOURCENAME, nullable = false)
	private String resourceName;

	@Schema(required = false, example = "api untuk get user", description = "deskripsi api")
	@Column(name = COL_DRISKRIPSI, nullable = false)
	private String deskripsi;

	@Schema(required = true, example = "GET", description = "method dari service")
	@NotBlank(message =  ErrorMessageIndoConstants.ERROR + "method " + ErrorMessageIndoConstants.CANT_EMPTY)
	@Size(max = 10, message = ErrorMessageIndoConstants.MAX_LEN + "10")
	@Column(name = COL_METHOD, nullable = false)
	private String method;

	@Schema(required = true, example = "GET", description = "proses yang akan di kerjakan oleh service")
	@NotBlank(message =  ErrorMessageIndoConstants.ERROR + "process " + ErrorMessageIndoConstants.CANT_EMPTY)
	@Size(max = 10, message = ErrorMessageIndoConstants.MAX_LEN + "10")
	@Column(name = COL_PROCESS, nullable = false)
	private String process;

	@Schema(required = true, example = "N", description = "flag sebagai tanda service membutuhkan approval")
	@NotBlank(message =  ErrorMessageIndoConstants.ERROR + "approval " + ErrorMessageIndoConstants.CANT_EMPTY)
	@Size(max = 2, message = ErrorMessageIndoConstants.MAX_LEN + "2")
	@Column(name = COL_APPROVAL, nullable = false)
	private String approval;

	@Schema(required = false, example = "UserEntity", description = "nama entity yg digunakan oleh service")
	@Size(max = 50, message = ErrorMessageIndoConstants.MAX_LEN + "50")
	@Column(name = COL_ENTITYNAME, nullable = true)
	private String entityName;
	
	@Schema(required = false, example = "GetReqEntity", description = "nama inputan yg digunakan oleh service")
	@Size(max = 50, message = ErrorMessageIndoConstants.MAX_LEN + "50")
	@Column(name = COL_INPUTSERVICE, nullable = true)
	private String inputService;

	public ApiEntity() {
	}

	public ApiEntity(String apiName, String resourceName, String deskripsi, String method, String grup,
			String entityName, String approval, String inputService,String process) {
		super();
		this.apiName = apiName.substring(0, 1).toLowerCase() + apiName.substring(1);
		this.resourceName = resourceName.substring(0, 1).toLowerCase() + resourceName.substring(1);
		this.deskripsi = deskripsi;
		this.method = method.toUpperCase();
		this.grup = grup.toUpperCase();
		this.entityName = entityName;
		this.approval = approval.toUpperCase();
		this.inputService = inputService;
		this.process = process.toUpperCase();
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getDeskripsi() {
		return deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getGrup() {
		return grup;
	}

	public void setGrup(String grup) {
		this.grup = grup;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getApproval() {
		return approval;
	}

	public void setApproval(String approval) {
		this.approval = approval;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getInputService() {
		return inputService;
	}

	public void setInputService(String inputService) {
		this.inputService = inputService;
	}

	
}
