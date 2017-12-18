package nari.parameter.bean;

import java.io.Serializable;

public class FuzzyQueryDevice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1823655649099885780L;

	
	private String sbmc = "";
	
	private String oId = "";
	
	private String searchClassid = "";
	
	private YXDWMessage yxdwMessage = null;

	public String getSbmc() {
		return sbmc;
	}

	public void setSbmc(String sbmc) {
		this.sbmc = sbmc;
	}

	public String getoId() {
		return oId;
	}

	public void setoId(String oId) {
		this.oId = oId;
	}

	public String getSearchClassid() {
		return searchClassid;
	}

	public void setSearchClassid(String searchClassid) {
		this.searchClassid = searchClassid;
	}

	public YXDWMessage getYxdwMessage() {
		return yxdwMessage;
	}

	public void setYxdwMessage(YXDWMessage yxdwMessage) {
		this.yxdwMessage = yxdwMessage;
	}
	
	
}
