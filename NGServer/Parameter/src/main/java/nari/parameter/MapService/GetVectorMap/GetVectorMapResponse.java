package nari.parameter.MapService.GetVectorMap;

import java.io.Serializable;

import nari.parameter.bean.QueryResult;
import nari.parameter.code.ReturnCode;

public class GetVectorMapResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8683826914805879258L;

	/**
	 * 查询结果
	 */
	private QueryResult[] result = null;
	
	private ReturnCode code = null;
	
	private String requestExtend = "";

	public QueryResult[] getResult() {
		return result;
	}

	public void setResult(QueryResult[] result) {
		this.result = result;
	}

	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}

	public String getRequestExtend() {
		return requestExtend;
	}

	public void setRequestExtend(String requestExtend) {
		this.requestExtend = requestExtend;
	}
	
	
}



