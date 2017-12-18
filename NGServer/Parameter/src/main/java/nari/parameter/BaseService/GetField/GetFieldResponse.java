package nari.parameter.BaseService.GetField;

import java.io.Serializable;

import nari.parameter.bean.ClassField;
import nari.parameter.code.ReturnCode;

public class GetFieldResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1588386528644999475L;

	//返回该设备类型字段信息
	private ClassField[] classfields = null;
	
	public ClassField[] getClassfields() {
		return classfields;
	}

	public void setClassfields(ClassField[] classfields) {
		this.classfields = classfields;
	}

	/**
	 * 返回码值
	 */
	private ReturnCode code = null;

	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}
	

}
