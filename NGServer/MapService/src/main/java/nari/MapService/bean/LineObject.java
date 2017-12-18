package nari.MapService.bean;

import oracle.spatial.geometry.JGeometry;

public class LineObject {

	private String sbmc;
	
	private int sbzlx;
	
	private int oid;
	
	private String vol;
	
	private JGeometry geometry;

	public String getSbmc() {
		return sbmc;
	}

	public void setSbmc(String sbmc) {
		this.sbmc = sbmc;
	}

	public int getSbzlx() {
		return sbzlx;
	}

	public void setSbzlx(int sbzlx) {
		this.sbzlx = sbzlx;
	}

	public int getOid() {
		return oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}

	
	public String getVol() {
		return vol;
	}

	public void setVol(String vol) {
		this.vol = vol;
	}

	public JGeometry getGeometry() {
		return geometry;
	}

	public void setGeometry(JGeometry geometry) {
		this.geometry = geometry;
	}
	
}
