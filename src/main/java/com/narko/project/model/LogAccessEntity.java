package com.narko.project.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = LogAccessEntity.TABLE_NAME)
public class LogAccessEntity {
	
	
	public static final String TABLE_NAME = "log_access";
	
	public static final String COL_METHOD = "method";
	public static final String COL_USERNAME = "username";
	public static final String COL_SERVICE = "service";
	public static final String COL_URL = "url";
	public static final String COL_REQUEST = "request";
	public static final String COL_REQUESTDATE = "request_date";
	public static final String COL_RESPONSE = "response";
	public static final String COL_RESPONSEDATE = "response_date";
	public static final String COL_STATUS = "status";
	public static final String COL_ERROR = "error";
	public static final String COL_ID = "id";
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "serial")
	private Long id;

	@Column(name = COL_METHOD)
	@Size(max = 10)
	private String method;

	@Column(name = COL_USERNAME)
	@Size(max = 50)
	private String username;

	@Column(name = COL_SERVICE )
	@Size(max = 50)
	private String service;

	@Column(name = COL_URL)
	@Size(max = 120)
	private String url;

	@Column(name = COL_REQUEST)
	private String request;

	@Column(name = COL_REQUESTDATE)
	private Date requestDate;

	@Column(name = COL_RESPONSE)
	private String response;

	@Column(name = COL_RESPONSEDATE)
	private Date responseDate;

	@Column(name = COL_STATUS)
	private int status;

	@Column(name = COL_ERROR)
	private String error;

	public LogAccessEntity(@Size(max = 5) String method, @Size(max = 50) String username,
			@Size(max = 120) String service, @Size(max = 50) String url, String request, Date requestDate,
			String response, int status, String error) {
		super();
		this.method = method;
		this.username = username;
		this.service = service;
		this.url = url;
		this.request = request;
		this.requestDate = requestDate;
		this.response = response;
		this.responseDate = new Date();
		this.status = status;
		this.error = error;
	}

	public LogAccessEntity() {
		super();
	}

}
