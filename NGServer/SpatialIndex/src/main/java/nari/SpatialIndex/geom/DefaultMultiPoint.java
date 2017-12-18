package nari.SpatialIndex.geom;

public class DefaultMultiPoint extends AbstractGeometryCollection implements MultiPoint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5700187627251238410L;

	private Point[] points = null;
	
	public DefaultMultiPoint(Point[] points){
		if(points==null){
			throw new IllegalArgumentException("null point argument");
		}
		this.points = points;
	}
	
	@Override
	public GeometryType getGeometryType() {
		return GeometryType.MULTIPOINT;
	}

	@Override
	public Coordinate[] getCoordinates() {
		Coordinate[] coords = new Coordinate[points.length];
		int i=0;
		for(Point point:points){
			coords[i++] = point.getCoordinate();
		}
		return coords;
	}

	@Override
	public int getNumGeometry() {
		return points.length;
	}
	
	@Override
	public Geometry getGeometry(int num) {
		if(num<0 || num>points.length-1){
			throw new ArrayIndexOutOfBoundsException(num);
		}
		return points[num];
	}

	@Override
	public Envelope getEnvelope() {
		double xmin = points[0].getX();
		double xmax = points[0].getX();
		double ymin = points[0].getY();
		double ymax = points[0].getY(); 
		
		for(int i=1;i<points.length;i++){
			if(points[i].getX()<xmin){
				xmin = points[i].getX();
			}
			if(points[i].getX()>xmax){
				xmax = points[i].getX();
			}
			
			if(points[i].getY()<ymin){
				ymin = points[i].getY();
			}
			if(points[i].getY()>ymax){
				ymax = points[i].getY();
			}
		}
		Envelope env = new DefaultEnvelope(xmin, ymin, xmax, ymax);
		return env;
	}

	@Override
	public int getNumPoints() {
		return points.length;
	}

}
