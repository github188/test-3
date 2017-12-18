package nari.parameter.BaseService.GetPSRDef;

import java.io.Serializable;

import nari.parameter.bean.PSRDef;
import nari.parameter.code.ReturnCode;

public class GetPSRDefResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6314995824124128645L;

	/**
	 * 设备类型集合
	 */
	private PSRDef[] psrDefs = null;
	
	private ReturnCode code = null;

	public PSRDef[] getPsrDefs() {
		return psrDefs;
	}

	public void setPsrDefs(PSRDef[] psrDefs) {
		this.psrDefs = psrDefs;
	}

	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}
	
}
