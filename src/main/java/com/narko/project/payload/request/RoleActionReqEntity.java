package com.narko.project.payload.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;


public class RoleActionReqEntity  {

	@Schema(required = true, example = "1", description = "id role")
	private Long id;
	
	@Schema(required = true, example = "ADD", description = "fungsi/operasi terhadap role action,(ADD,REPLACE,DALETE)")
	private String func;
	
	@Schema(required = true, description = "list action/api")
	private List<String> actionList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFunc() {
		return func;
	}

	public void setFunc(String func) {
		this.func = func;
	}

	public List<String> getActionList() {
		return actionList;
	}

	public void setActionList(List<String> actionList) {
		this.actionList = actionList;
	}

	
	


}
