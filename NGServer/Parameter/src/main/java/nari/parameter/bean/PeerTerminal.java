package nari.parameter.bean;

import java.io.Serializable;

public class PeerTerminal implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2854231373884676398L;
	
	private TopoObjectResult[] peerObjectIds;
	
	private int[] peerTermIds;
	
	private int[] termIds;
	
	private long[] ConNodeids;

	public TopoObjectResult[] getPeerObjectIds() {
		return peerObjectIds;
	}

	public void setPeerObjectIds(TopoObjectResult[] peerObjectIds) {
		this.peerObjectIds = peerObjectIds;
	}

	public int[] getPeerTermIds() {
		return peerTermIds;
	}

	public void setPeerTermIds(int[] peerTermIds) {
		this.peerTermIds = peerTermIds;
	}

	public int[] getTermIds() {
		return termIds;
	}

	public void setTermIds(int[] termIds) {
		this.termIds = termIds;
	}

	public long[] getConNodeids() {
		return ConNodeids;
	}

	public void setConNodeids(long[] conNodeids) {
		ConNodeids = conNodeids;
	}
	
}
