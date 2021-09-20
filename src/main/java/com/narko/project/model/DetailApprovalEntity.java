package com.narko.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.narko.project.model.audit.AuditEntity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 
 * @author agus.wijanarko
 *
 */

@Entity
@Table(name = DetailApprovalEntity.TABLE_NAME)
@JsonIgnoreProperties(value = { AuditEntity.COL_CREATEDBY, AuditEntity.COL_CREATEDAT, AuditEntity.COL_UPDATEDBY,
		AuditEntity.COL_UPDATEDAT, AuditEntity.COL_STATUS }, allowGetters = true)
public class DetailApprovalEntity {

	public static final String TABLE_NAME = "detail_approval";

	public static final String COL_TABLENAME = "table_name";
	public static final String COL_COLUMNNAME = "column_name";
	public static final String COL_KEYNAME = "key_name";
	public static final String COL_VALUE = "value";
	public static final String COL_ID = "id";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "serial")
	@Schema(required = true, example = "1", description = "id data")
	private Long id;

	@Column(name = COL_TABLENAME, nullable = false)
	private String tableName;

	@Column(name = COL_COLUMNNAME, nullable = false)
	private String columnName;

	@Column(name = COL_KEYNAME, nullable = false)
	private String keyName;

	@Column(name = COL_VALUE, nullable = false)
	private String value;

	public DetailApprovalEntity() {
		super();
	}

	public DetailApprovalEntity(String tableName, String columnName, String keyName, String value) {
		super();
		this.tableName = tableName;
		this.columnName = columnName;
		this.keyName = keyName;
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

}