package nari.parameter.MapService.BookMark;

import java.io.Serializable;

import nari.parameter.code.ReturnCode;

public class RemoveBookMarkResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5822980634697463610L;

	private ReturnCode code = null;

	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}
	
}
