package nari.parameter.bean;

import java.io.Serializable;

public class MapView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2008239658970130231L;

	/**
	 * x坐标最小值
	 */
	private double xmin = 0;
	
	/**
	 * y坐标最小值
	 */
	private double ymin = 0;
	
	/**
	 * x坐标最大值
	 */
	private double xmax = 0;
	
	/**
	 * y坐标最大值
	 */
	private double ymax = 0;

	public double getXmin() {
		return xmin;
	}

	public void setXmin(double xmin) {
		this.xmin = xmin;
	}

	public double getYmin() {
		return ymin;
	}

	public void setYmin(double ymin) {
		this.ymin = ymin;
	}

	public double getXmax() {
		return xmax;
	}

	public void setXmax(double xmax) {
		this.xmax = xmax;
	}

	public double getYmax() {
		return ymax;
	}

	public void setYmax(double ymax) {
		this.ymax = ymax;
	}
	
}
