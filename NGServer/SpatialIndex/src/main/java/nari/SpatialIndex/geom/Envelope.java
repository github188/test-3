package nari.SpatialIndex.geom;

import java.io.Serializable;

public interface Envelope extends Serializable{
	
	public boolean isNull();

	public double getWidth();

	public double getHeight();

	public double getMinX();

	public double getMaxX();

	public double getMinY();

	public double getMaxY();

	public double getArea();

	public void expandToInclude(Coordinate p);

	public void expandBy(double distance);

	public void expandBy(double deltaX, double deltaY);

	public Coordinate centre();

	public Envelope intersection(Envelope env);

	public boolean intersects(Envelope other);

	public boolean intersects(Coordinate p);
	
	public boolean intersects(double x, double y);

	public boolean contains(Envelope other);

	public boolean contains(Coordinate p);

	public boolean contains(double x, double y);

	public boolean covers(double x, double y);

	public boolean covers(Coordinate p);

	public boolean covers(Envelope other);

	public double distance(Envelope env);
	
	public boolean equals(Object other) ;

	public String toString();
}
