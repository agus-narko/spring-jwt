package com.narko.project.payload.request;

import com.narko.project.model.ApiEntity;

public class AddApprovalReqEntity<T> {

	private T dataInput;

	private ApiEntity action;

	public AddApprovalReqEntity() {
		super();
	}

	public AddApprovalReqEntity(T dataInput, ApiEntity action) {
		super();
		this.dataInput = dataInput;
		this.action = action;
	}

	public ApiEntity getAction() {
		return action;
	}

	public void setAction(ApiEntity action) {
		this.action = action;
	}

	public T getDataInput() {
		return dataInput;
	}

	public void setDataInput(T dataInput) {
		this.dataInput = dataInput;
	}

}
