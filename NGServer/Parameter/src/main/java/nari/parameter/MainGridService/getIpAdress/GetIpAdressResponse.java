package nari.parameter.MainGridService.getIpAdress;

import java.io.Serializable;

import nari.parameter.code.ReturnCode;
import net.sf.json.JSONArray;

public class GetIpAdressResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6106083533509730944L;

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
