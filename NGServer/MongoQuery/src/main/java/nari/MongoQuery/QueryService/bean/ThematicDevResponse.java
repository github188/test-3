package nari.MongoQuery.QueryService.bean;

import java.io.Serializable;

import nari.parameter.bean.QueryResult;
import nari.parameter.code.ReturnCode;

public class ThematicDevResponse  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1354636084178614521L;

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
