package nari.parameter.QueryService.VirtualConditionQuery;

import java.io.Serializable;

import nari.parameter.bean.SimpleQueryResult;
import nari.parameter.code.ReturnCode;

public class VirtualConditionQueryResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5632049803699633096L;

	/**
	 * 查询结果
	 */
	private SimpleQueryResult[] result = null;
	
	private ReturnCode code = null;

	public SimpleQueryResult[] getResult() {
		return result;
	}

	public void setResult(SimpleQueryResult[] result) {
		this.result = result;
	}

	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}

}
