package nari.parameter.bean;

import java.io.Serializable;

public class VoltageLevel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4624780244608089450L;
	
	/**
	 * 电压等级名
	 */
	private String voltageLevelName = "";

	/**
	 * 电压等级ID
	 */
	private String voltageLevelID = "";
	
	public String getVoltageLevelName() {
		return voltageLevelName;
	}

	public void setVoltageLevelName(String voltageLevelName) {
		this.voltageLevelName = voltageLevelName;
	}

	public String getVoltageLevelID() {
		return voltageLevelID;
	}

	public void setVoltageLevelID(String voltageLevelID) {
		this.voltageLevelID = voltageLevelID;
	}
}
