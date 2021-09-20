package com.narko.project.payload.response;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;

public class FinalResEntity<T> {

	private Date timestamp;

	private T data;

	private int status;

	private HttpStatus error;

	private Set<String> message;

	public FinalResEntity(T data, int status, HttpStatus error, Set<String> message) {
		super();
		this.data = data;
		this.status = status;
		this.error = error;
		this.message = message;

		this.timestamp = new Date();
	}

	public FinalResEntity(T data, int status, HttpStatus error, String message) {

		super();
		Set<String> messageList = new HashSet<>();
		messageList.add(message);
		this.data = data;
		this.status = status;
		this.error = error;
		this.message = messageList;
		this.timestamp = new Date();
	}

	public FinalResEntity() {
		super();
	}

	public Set<String> getmessage() {
		return message;
	}

	public void setmessage(Set<String> message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public HttpStatus getError() {
		return error;
	}

	public void setError(HttpStatus error) {
		this.error = error;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
