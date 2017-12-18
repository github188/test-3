package nari.Geometry;

import java.util.ArrayList;
import java.util.List;

public class DefaultPolyline extends AbstractGeometry implements Polyline {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2885583280370998008L;

	private Coordinate[] coordArray = null;
	
	private CoordinateSequence seq = null;
	
	private Segment[] segment = null;
	
	public DefaultPolyline(Coordinate[] coords){
		coordArray = coords;
	}
	
	public DefaultPolyline(CoordinateSequence seq){
		this.seq = seq;
		coordArray = seq.toCoordinateArray();
	}
	
	public DefaultPolyline(Segment[] segs){
		this.segment = segs;
		List<Coordinate> coordsList = new ArrayList<Coordinate>();
		for(Segment s:segs){
			Coordinate[] coord = s.getCoords();
			for(Coordinate c:coord){
				coordsList.add(c);
			}
		}
		Coordinate[] coords = new Coordinate[coordsList.size()];
		coords = coordsList.toArray(coords);
		this.coordArray = coords;
	}
	
	@Override
	public boolean isLineString(){
		if(segment == null || segment.length == 0){
			Segment[] segment = new Segment[1];
			segment[0] = new LineSegment(coordArray);
		}
		int num = segment.length;
		if(num==1 && (segment[0] instanceof LineSegment)){
			return true;
		}
		return false;
	}
	
	@Override
	public int getNumLineString(){
		return segment.length;
	}
	
	@Override
	public Segment getSegment(int num){
		return segment[num];
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

	@Override
	public boolean isCurve() {
		return false;
	}
	
}
