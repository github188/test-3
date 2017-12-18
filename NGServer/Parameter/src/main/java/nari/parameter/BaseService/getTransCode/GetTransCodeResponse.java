package nari.parameter.BaseService.getTransCode;

import java.io.Serializable;

import nari.parameter.code.ReturnCode;
import net.sf.json.JSONArray;

public class GetTransCodeResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3097483462399270401L;


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
