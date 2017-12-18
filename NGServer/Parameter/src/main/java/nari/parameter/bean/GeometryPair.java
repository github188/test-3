package nari.parameter.bean;

import java.io.Serializable;

public class GeometryPair implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3349651571440049121L;

	/**
	 * 空间对象类型1 点 2 线 3 面
	 */
	private String geometryType = "";
	
	/**
	 * 坐标数组  [x1,y1,x2,y2,x3,y3]
	 */
	private double[] coords = null;
	
	/*
	 *表示形状其他所需条件 (如每组坐标包含的坐标点数)
	 */
	private double other = 0;
	
	/*
	 * 该地理图形的详细信息
	 */
	private int[] startDouble = null; 
	
	public int[] getStartDouble() {
		return startDouble;
	}

	public void setStartDouble(int[] startDouble) {
		this.startDouble = startDouble;
	}

	public double getOther() {
		return other;
	}

	public void setOther(double other) {
		this.other = other;
	}

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
	
}
