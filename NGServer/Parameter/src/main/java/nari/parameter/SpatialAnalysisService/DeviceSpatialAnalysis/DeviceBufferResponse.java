package nari.parameter.SpatialAnalysisService.DeviceSpatialAnalysis;

import java.io.Serializable;

import nari.parameter.bean.QueryResult;
import nari.parameter.code.ReturnCode;

public class DeviceBufferResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5521225298436309817L;

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
