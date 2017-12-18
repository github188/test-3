package nari.SpatialIndex.geom;

public interface GeometryCollection extends Geometry{

	public Geometry getGeometry(int n);

	public int getNumGeometry();
	
	public Envelope getEnvelope();
}
