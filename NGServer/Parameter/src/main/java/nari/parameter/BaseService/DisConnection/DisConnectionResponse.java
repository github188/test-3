package nari.parameter.BaseService.DisConnection;

import java.io.Serializable;

import nari.parameter.code.ReturnCode;

public class DisConnectionResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8512205964341580749L;

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
