package nari.Geometry;

public abstract class AbstractGeometryCollection extends AbstractGeometry implements GeometryCollection {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6813862912385254160L;

	public abstract Geometry getGeometry(int n);

	public abstract int getNumGeometry();
	
	public abstract Envelope getEnvelope();

}
