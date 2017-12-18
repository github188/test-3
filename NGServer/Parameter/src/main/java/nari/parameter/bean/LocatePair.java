package nari.parameter.bean;

import java.io.Serializable;

public class LocatePair implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 893311369200010087L;

	/**
	 * 图形ID
	 */
	private String oid = "";
	
	/**
	 * 空间对象类型 1 点 2 线 3 面  
	 */
	private String geometryType = "";
	
	/**
	 * 坐标数组  [x1,y1,x2,y2,x3,y3]
	 */
	private double[] coords = null;

	/**
	 * 地图展示符号信息描述
	 */
	private SymbolPair symbol = null;

	public String getGeometryType() {
		return geometryType;
	}

	public void setGeometryType(String geometryType) {
		this.geometryType = geometryType;
	}

	public double[] getCoords() {
		return coords;
	}

	public void setCoords(double[] coords) {
		this.coords = coords;
	}

	public SymbolPair getSymbol() {
		return symbol;
	}

	public void setSymbol(SymbolPair symbol) {
		this.symbol = symbol;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}
}
