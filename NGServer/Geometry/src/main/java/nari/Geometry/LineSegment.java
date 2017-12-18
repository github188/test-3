package nari.Geometry;

public class LineSegment extends Segment {

	
	public LineSegment(double[] ordinatesArray) {
		super(ordinatesArray);
	}
	
	public LineSegment(Coordinate[] coordArray) {
		super(coordArray);
	}

	@Override
	public boolean isCurve() {
		return false;
	}

	@Override
	public boolean isLine() {
		return true;
	}

	@Override
	public Coordinate[] getCoordinates() {
		return super.getCoords();
	}

	@Override
	public boolean isCircle() {
		return false;
	}

	@Override
	public Polyline asPolyline() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Polygon asPolygon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Curve asCurve() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Circle asCircle() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
