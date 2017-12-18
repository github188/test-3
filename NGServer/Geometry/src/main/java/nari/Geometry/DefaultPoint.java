package nari.Geometry;

public class DefaultPoint extends AbstractGeometry implements Point {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1265649684851330238L;

	private Coordinate coord = null;
	
	public DefaultPoint(double x,double y){
		this.coord = new DefaultCoordinate(x,y);
	}
	
	public DefaultPoint(Coordinate coord){
		this.coord = coord;
	}
	
	@Override
	public double getX() {
		return coord.getX();
	}

	@Override
	public double getY() {
		return coord.getY();
	}

	@Override
	public GeometryType getGeometryType() {
		return GeometryType.POINT;
	}

	@Override
	public Coordinate getCoordinate() {
		return coord;
	}

	@Override
	public Coordinate[] getCoordinates() {
		return new Coordinate[]{coord};
	}

	@Override
	public int getNumPoints() {
		return 1;
	}

	@Override
	public Envelope getEnvelope() {
		Envelope env = new DefaultEnvelope(getX(), getY(), getX(), getY());
		return env;
	}
}
