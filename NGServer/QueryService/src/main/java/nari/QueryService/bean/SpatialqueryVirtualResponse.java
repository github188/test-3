package nari.QueryService.bean;

import java.io.Serializable;

import nari.parameter.bean.QueryResult;
import nari.parameter.code.ReturnCode;

public class SpatialqueryVirtualResponse  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6654823994213808595L;

	/**
	 * 查询结果
	 */
	private QueryResult[] result = null;
	
	private ReturnCode code = null;

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
	
}


