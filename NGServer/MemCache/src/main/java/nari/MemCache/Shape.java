package nari.MemCache;

import java.io.Serializable;

public class Shape implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3659708422356978355L;

	private int type;
	
	private int[] elementInfo;
	
	private double[] coordinates;
	
	private double xmax;
	
	private double ymax;
	
	private double xmin;
	
	private double ymin;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int[] getElementInfo() {
		return elementInfo;
	}

	public void setElementInfo(int[] elementInfo) {
		if(elementInfo==null){
			elementInfo = new int[0];
		}
		this.elementInfo = elementInfo;
	}

	public double[] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(double[] coordinates) {
		this.coordinates = coordinates;
		
		xmin = coordinates[0];
		xmax = coordinates[0];
		ymin = coordinates[1];
		ymax = coordinates[1];
		
		for(int i=2;i<coordinates.length;i=i+2){
			if(coordinates[i]<xmin){
				xmin = coordinates[i];
			}
			if(coordinates[i]>xmax){
				xmax = coordinates[i];
			}
			
			if(coordinates[i+1]<ymin){
				ymin = coordinates[i+1];
			}
			if(coordinates[i+1]>ymax){
				ymax = coordinates[i+1];
			}
		}
	}

	public double getMinX(){
		return xmin;
	}
	
	public double getMinY(){
		return ymin;
	}
	
	public double getMaxX(){
		return xmax;
	}
	
	public double getMaxY(){
		return ymax;
	}
}
