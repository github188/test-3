package nari.parameter.BaseService.GetMapClassList;

import java.io.Serializable;

import nari.parameter.bean.PsrTypeInfoList;
import nari.parameter.code.ReturnCode;

public class GetMapClassListResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3468381050196477215L;
	
	/**
	 * 返回参数
	 */
	private ReturnCode code = null;
	
	/**
	 * 图类型对应的设备子类型的列表
	 */
	private PsrTypeInfoList psrTypeInfoList = null;

	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}

	public PsrTypeInfoList getPsrTypeInfoList() {
		return psrTypeInfoList;
	}

	public void setPsrTypeInfoList(PsrTypeInfoList psrTypeInfoList) {
		this.psrTypeInfoList = psrTypeInfoList;
	}

}
