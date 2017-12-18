package nari.SpatialIndex.geom;

import java.io.Serializable;

public interface Coordinate extends Comparable<Coordinate>, Cloneable, Serializable{

	public double distance(Coordinate p);
	
	public double getX();
	
	public double getY();
	
	public void setX(double x);
	
	public void setY(double y);
	
}
