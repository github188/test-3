package nari.network.bean;

import java.io.Serializable;

import nari.Xml.bundle.annotation.XmlAttribute;

public class NetworkConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4184047607942826617L;
	
	@XmlAttribute(name="active")
	private boolean active = false;

	@XmlAttribute(name="containLowVoltage")
	private boolean containLowVoltage = false;

	private int threadNum = Runtime.getRuntime().availableProcessors() + 1;
	
	private int[] voltageLevelList = new int[] {22};
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isContainLowVoltage() {
		return containLowVoltage;
	}
	
	public void setContainLowVoltage(boolean containLowVoltage) {
		this.containLowVoltage = containLowVoltage;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	
	public int[] getVoltageLevelList() {
		return voltageLevelList;
	}
	
	public void setVoltageLevelList(int[] voltageLevelList) {
		this.voltageLevelList = voltageLevelList;
	}
}
