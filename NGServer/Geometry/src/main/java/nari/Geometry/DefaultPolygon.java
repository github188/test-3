package nari.Geometry;

public class DefaultPolygon extends AbstractGeometry implements Polygon {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1278157471504490206L;

	private Coordinate[] coordArray = null;
	
	private CoordinateSequence seq = null;
	
	public DefaultPolygon(CoordinateSequence seq){
		this.seq = seq;
		coordArray = seq.toCoordinateArray();
	}
	
	public DefaultPolygon(Coordinate[] coordArray) {
		this.coordArray = coordArray;
	}
	
	@Override
	public int getNumInteriorRing() {
		return 1;
	}

	@Override
	public Polyline getInteriorRing(int n) {
		return null;
	}

	@Override
	public GeometryType getGeometryType() {
		return GeometryType.POLYGON;
	}

	@Override
	public Geometry getBoundary() {
		return null;
	}

	@Override
	public Coordinate[] getCoordinates() {
		return coordArray;
	}

	@Override
	public CoordinateSequence getCoordinateSequence() {
		return seq;
	}

	@Override
	public int getNumPoints() {
		return coordArray.length;
	}

	@Override
	public Envelope getEnvelope() {
		double xmin = coordArray[0].getX();
		double xmax = coordArray[0].getX();
		double ymin = coordArray[0].getY();
		double ymax = coordArray[0].getY(); 
		
		for(int i=1;i<coordArray.length;i++){
			if(coordArray[i].getX()<xmin){
				xmin = coordArray[i].getX();
			}
			if(coordArray[i].getX()>xmax){
				xmax = coordArray[i].getX();
			}
			
			if(coordArray[i].getY()<ymin){
				ymin = coordArray[i].getY();
			}
			if(coordArray[i].getY()>ymax){
				ymax = coordArray[i].getY();
			}
		}
		Envelope env = new DefaultEnvelope(xmin, ymin, xmax, ymax);
		return env;
	}

	@Override
	public boolean isCircle() {
		return false;
	}
}
