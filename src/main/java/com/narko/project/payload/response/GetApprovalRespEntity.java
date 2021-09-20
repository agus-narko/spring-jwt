package com.narko.project.payload.response;

import com.narko.project.model.ApprovalEntity;

public class GetApprovalRespEntity<T> {

	private ApprovalEntity approvalEntity;
	private T detailData;

	public ApprovalEntity getApprovalEntity() {
		return approvalEntity;
	}

	public void setApprovalEntity(ApprovalEntity approvalEntity) {
		this.approvalEntity = approvalEntity;
	}

	public T getDetailData() {
		return detailData;
	}

	public void setDetailData(T detailData) {
		this.detailData = detailData;
	}

	public GetApprovalRespEntity(ApprovalEntity approvalEntity, T detailData) {
		super();
		this.approvalEntity = approvalEntity;
		this.detailData = detailData;
	}

	public GetApprovalRespEntity() {
		super();
	}
	
	

}
