package nari.parameter.bean;

import java.io.Serializable;

public class SubPSRDef implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4187384470934590332L;

	/**
	 * 设备子类型
	 */
	private String subPSRType = "";
	
	/**
	 * 子类型名称
	 */
	private String subPSRName = "";

	public String getSubPSRType() {
		return subPSRType;
	}

	public void setSubPSRType(String subPSRType) {
		this.subPSRType = subPSRType;
	}

	public String getSubPSRName() {
		return subPSRName;
	}

	public void setSubPSRName(String subPSRName) {
		this.subPSRName = subPSRName;
	}
	
}
