package nari.SpatialIndex.geom;

public interface Polygon extends Geometry{

	public Coordinate[] getCoordinates();
	
	public int getNumPoints();
		
	public boolean isEmpty();
		
	public boolean isSimple();
		
	public boolean isRectangle();
		
	public int getNumInteriorRing();
		
	public Polyline getInteriorRing(int n);
		
	public GeometryType getGeometryType();
		
	public double getArea();
		
	public double getLength();
		
	public Geometry getBoundary();
		
	public Envelope getEnvelope();
	
	public Geometry convexHull();
	
	public CoordinateSequence getCoordinateSequence();
	  
}
