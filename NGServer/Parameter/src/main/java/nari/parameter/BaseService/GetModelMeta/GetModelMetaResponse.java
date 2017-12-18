package nari.parameter.BaseService.GetModelMeta;

import java.io.Serializable;

import nari.parameter.code.ReturnCode;
import net.sf.json.JSONArray;

public class GetModelMetaResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4366359354575134508L;

	//返回结果状态信息
	private ReturnCode code = null;
	
	//返回结果
	private JSONArray resultJSONArray = null;

	
	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}

	public JSONArray getResultJSONArray() {
		return resultJSONArray;
	}

	public void setResultJSONArray(JSONArray resultJSONArray) {
		this.resultJSONArray = resultJSONArray;
	}


}
