package com.narko.project.payload.request;

/**
 * 
 * @author agus.wijanarko
 *
 */
public class AuditReqEntity {

	protected String api;
	protected String process;
	protected String username;
	protected Long approval;
	protected Long roleId;


	public AuditReqEntity(String api, String process, String username, Long approval, Long roleId) {
		super();
		this.api = api;
		this.process = process;
		this.username = username;
		this.approval = approval;
		this.roleId = roleId;
	}

	public AuditReqEntity() {
		super();
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getApproval() {
		return approval;
	}

	public void setApproval(Long approval) {
		this.approval = approval;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	

}
