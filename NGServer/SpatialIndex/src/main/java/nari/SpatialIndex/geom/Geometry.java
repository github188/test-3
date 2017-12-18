package nari.SpatialIndex.geom;

import java.io.Serializable;

public interface Geometry extends Comparable<Geometry>,Cloneable,Serializable{

	public GeometryFactory getFactory();

	public abstract Coordinate[] getCoordinates();

	public abstract int getNumPoints();

	public boolean isSimple();

	public boolean isValid();
	
	public abstract boolean isEmpty();

	public double distance(Geometry g);

	public boolean isWithinDistance(Geometry geom, double distance);

	public boolean isRectangle();

	public double getArea();

	public double getLength();

	public Point getCentroid();

	public Point getInteriorPoint();

	public Envelope getEnvelope();

	public boolean disjoint(Geometry g);

	public boolean touches(Geometry g);

	public boolean intersects(Geometry g);

	public boolean crosses(Geometry g);

	public boolean within(Geometry g);

	public boolean contains(Geometry g);

	public boolean overlaps(Geometry g);
	
	public boolean covers(Geometry g);

	public boolean coveredBy(Geometry g);
	
	public boolean relate(Geometry g, String intersectionPattern);

	public GeometryType getGeometryType();
	
	public boolean equals(Geometry g);

	public String toString();

	public String toText();

	public Geometry buffer(double distance);

	public Geometry convexHull();

	public Geometry intersection(Geometry other);

	public Geometry union(Geometry other);

	public Geometry difference(Geometry other);

	public Geometry symDifference(Geometry other);

	public Geometry union();
	
	public boolean isGeometryCollection(Geometry g);
	
}
