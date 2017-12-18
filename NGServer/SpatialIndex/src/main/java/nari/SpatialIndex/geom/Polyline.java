package nari.SpatialIndex.geom;

public interface Polyline extends Geometry{

	public Coordinate[] getCoordinates() ;

	public CoordinateSequence getCoordinateSequence() ;

	public Coordinate getCoordinate(int n) ;

	public boolean isEmpty();

	public int getNumPoints();

	public Point getPointN(int n);

	public Point getStartPoint();

	public Point getEndPoint();

	public boolean isClosed();

	public boolean isRing();
	
	public GeometryType getGeometryType();

	public double getLength();

	public Envelope getEnvelope();

}
