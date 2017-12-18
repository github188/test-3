package nari.parameter.bean;

import java.io.Serializable;

public class SubPSRCondition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3555209591427262579L;
	
	
	private String sbzlx = "";
	
	private String[] DYDJ = null;

	public String getSbzlx() {
		return sbzlx;
	}

	public void setSbzlx(String sbzlx) {
		this.sbzlx = sbzlx;
	}

	public String[] getDYDJ() {
		return DYDJ;
	}

	public void setDYDJ(String[] dYDJ) {
		DYDJ = dYDJ;
	}

	
}
