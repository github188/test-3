package nari.parameter.BaseService.getTransCode;

import java.io.Serializable;

public class GetTransCodeRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7537742208624375055L;

	//码值
	private String[] codeDefId = null;
	
	//码值的类型
	private String[] codeId = null;

	private String[] returnField = null;
	
	public String[] getCodeDefId() {
		return codeDefId;
	}

	public void setCodeDefId(String[] codeDefId) {
		this.codeDefId = codeDefId;
	}

	public String[] getCodeId() {
		return codeId;
	}

	public void setCodeId(String[] codeId) {
		this.codeId = codeId;
	}

	public String[] getReturnField() {
		return returnField;
	}

	public void setReturnField(String[] returnField) {
		this.returnField = returnField;
	}
	
}
