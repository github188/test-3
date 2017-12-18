package nari.parameter.BaseService.JudgeFeildName;

import java.io.Serializable;

import nari.parameter.code.ReturnCode;

public class JudgeFeildNameResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8582160916672140056L;
	
	private ReturnCode code = null;
	
	private String[] containClassIds = null;
	
	private String[] notContainClassIds = null;
	
	private String[] noDataClassIds = null;
	
	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}

	public String[] getContainClassIds() {
		return containClassIds;
	}

	public void setContainClassIds(String[] containClassIds) {
		this.containClassIds = containClassIds;
	}

	public String[] getNotContainClassIds() {
		return notContainClassIds;
	}

	public void setNotContainClassIds(String[] notContainClassIds) {
		this.notContainClassIds = notContainClassIds;
	}

	public String[] getNoDataClassIds() {
		return noDataClassIds;
	}

	public void setNoDataClassIds(String[] noDataClassIds) {
		this.noDataClassIds = noDataClassIds;
	}


}
