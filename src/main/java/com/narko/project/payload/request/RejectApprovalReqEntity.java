package com.narko.project.payload.request;

public class RejectApprovalReqEntity{

	private Long id;

	private String note;

	public RejectApprovalReqEntity() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}


}
