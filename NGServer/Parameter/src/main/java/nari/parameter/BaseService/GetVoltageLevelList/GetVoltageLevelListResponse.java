package nari.parameter.BaseService.GetVoltageLevelList;

import java.io.Serializable;

import nari.parameter.bean.VoltageLevel;
import nari.parameter.code.ReturnCode;

public class GetVoltageLevelListResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2533698353975147766L;
	
	/**
	 * 返回参数
	 */
	private ReturnCode code = null;

	/**
	 * 电压等级列表
	 */
	private VoltageLevel[] voltageLevels = null;

	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}

	public VoltageLevel[] getVoltageLevels() {
		return voltageLevels;
	}

	public void setVoltageLevels(VoltageLevel[] voltageLevels) {
		this.voltageLevels = voltageLevels;
	}
}
