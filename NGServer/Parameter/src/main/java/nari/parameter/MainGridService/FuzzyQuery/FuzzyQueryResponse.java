package nari.parameter.MainGridService.FuzzyQuery;

import java.io.Serializable;

import nari.parameter.bean.FuzzyQueryDevice;
import nari.parameter.code.ReturnCode;

public class FuzzyQueryResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5060054698614959502L;

	/**
	 * 查询结果
	 */
	private FuzzyQueryDevice[] fuzzyDevice = null;
	
	
	
	private ReturnCode code = null;


	public FuzzyQueryDevice[] getFuzzyDevice() {
		return fuzzyDevice;
	}

	public void setFuzzyDevice(FuzzyQueryDevice[] fuzzyDevice) {
		this.fuzzyDevice = fuzzyDevice;
	}


	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}
	
}
