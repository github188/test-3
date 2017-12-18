package nari.SpatialIndex.geom;

public interface Point extends Geometry{

	public double getX();
	
	public double getY();
	
	public GeometryType getGeometryType();
	
	public boolean isEmpty();
	
	public boolean isSimple();
	
	public Coordinate getCoordinate();
	
	public Envelope getEnvelope();
}
