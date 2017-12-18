package nari.distribution.TopoAnalysis.bean.MockOutageRangeAnalyze;

import java.io.Serializable;

import nari.distribution.TopoAnalysis.bean.DeviceInfo;
import nari.parameter.code.ReturnCode;

public class MockOutageRangeAnalyzeResponse 
	implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6151902303651090070L;
	
	/**
	 * 返回码
	 */
	private ReturnCode code = null;
	
	/**
	 * 停电的配电变压器
	 */
	private DeviceInfo[] outageTransformer = null;

	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}

	public DeviceInfo[] getOutageTransformer() {
		return outageTransformer;
	}

	public void setOutageTransformer(DeviceInfo[] outageTransformer) {
		this.outageTransformer = outageTransformer;
	}
}
