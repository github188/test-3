package nari.parameter.BaseService.JudgeFeildName;

import java.io.Serializable;

public class JudgeFeildNameRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4660190524542208610L;
	
	String [] fieldName = null;

	public String[] getFieldName() {
		return fieldName;
	}

	public void setFieldName(String[] fieldName) {
		this.fieldName = fieldName;
	}
	
	
	
}
