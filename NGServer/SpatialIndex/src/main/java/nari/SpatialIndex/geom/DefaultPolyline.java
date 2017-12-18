package nari.SpatialIndex.geom;

public class DefaultPolyline extends AbstractGeometry implements Polyline {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2885583280370998008L;

	private Coordinate[] coordArray = null;
	
	private CoordinateSequence seq = null;
	
	public DefaultPolyline(Coordinate[] coords){
		coordArray = coords;
	}
	
	public DefaultPolyline(CoordinateSequence seq){
		this.seq = seq;
		coordArray = seq.toCoordinateArray();
	}
	
	@Override
	public CoordinateSequence getCoordinateSequence() {
		return seq;
	}

	@Override
	public Coordinate getCoordinate(int n) {
		return coordArray[n];
	}

	@Override
	public Point getPointN(int n) {
		Coordinate coord = coordArray[n];
		Point pt = new DefaultPoint(coord);
		return pt;
	}

	@Override
	public Point getStartPoint() {
		Coordinate coord = coordArray[0];
		Point pt = new DefaultPoint(coord);
		return pt;
	}

	@Override
	public Point getEndPoint() {
		Coordinate coord = coordArray[coordArray.length-1];
		Point pt = new DefaultPoint(coord);
		return pt;
	}

	@Override
	public boolean isClosed() {
		return getStartPoint().getX() == getEndPoint().getX() && getStartPoint().getY() == getEndPoint().getY();
	}

	@Override
	public boolean isRing() {
		return getStartPoint().getX() == getEndPoint().getX() && getStartPoint().getY() == getEndPoint().getY();
	}

	@Override
	public GeometryType getGeometryType() {
		return GeometryType.POLYLINE;
	}

	@Override
	public Coordinate[] getCoordinates() {
		return coordArray;
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
}
