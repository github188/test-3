package nari.parameter.bean;

import java.io.Serializable;

public class YXDWMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4101465497503768493L;
	
	private String bmmc = "";
	
	//网省公司
	private String wsgs = "";

	public String getBmmc() {
		return bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	public String getWsgs() {
		return wsgs;
	}

	public void setWsgs(String wsgs) {
		this.wsgs = wsgs;
	}

	
}
