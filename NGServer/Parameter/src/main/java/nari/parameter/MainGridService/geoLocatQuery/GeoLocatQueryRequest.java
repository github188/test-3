package nari.parameter.MainGridService.geoLocatQuery;

import java.io.Serializable;

public class GeoLocatQueryRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2639992292741153788L;
	
	private double xcoordnates = 0;
	
	private double ycoordnates = 0;
	
	private String[] classId = null;

	public double getXcoordnates() {
		return xcoordnates;
	}

	public void setXcoordnates(double xcoordnates) {
		this.xcoordnates = xcoordnates;
	}

	public double getYcoordnates() {
		return ycoordnates;
	}

	public void setYcoordnates(double ycoordnates) {
		this.ycoordnates = ycoordnates;
	}

	public String[] getClassId() {
		return classId;
	}

	public void setClassId(String[] classId) {
		this.classId = classId;
	}


	//x,y不能为0

}
