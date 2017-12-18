package nari.parameter.bean;

import java.io.Serializable;

public class QueryField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7919891007629247869L;

	/**
	 * 字段名
	 */
	private String fieldName = "";
	
	/**
	 * 字段值
	 */
	private String fieldValue = "";
	
	//该字段别名
	private String fieldAlias = "";
	
	public String getFieldAlias() {
		return fieldAlias;
	}

	public void setFieldAlias(String fieldAlias) {
		this.fieldAlias = fieldAlias;
	}

	

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	
}
