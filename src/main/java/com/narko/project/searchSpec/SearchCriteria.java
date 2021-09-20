package com.narko.project.searchSpec;

/**
 * 
 * @author agus.wijanarko
 *
 * @param <T>
 */

public class SearchCriteria {

	private String key;
	private Object value;
	private SearchOperation operation;
	private String logical;
	private String dataType;

	public SearchCriteria(String key, Object value, SearchOperation operation, String logical) {
		super();
		this.key = key;
		this.value = value;
		this.operation = operation;
		this.logical = logical;
	}

	public SearchCriteria(String key, Object value, SearchOperation operation, String logical, String dataType) {
		super();
		this.key = key;
		this.value = value;
		this.operation = operation;
		this.logical = logical;
		this.dataType = dataType;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public SearchOperation getOperation() {
		return operation;
	}

	public void setOperation(SearchOperation operation) {
		this.operation = operation;
	}

	public String getLogical() {
		return logical;
	}

	public void setLogical(String logical) {
		this.logical = logical;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

}