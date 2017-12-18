package nari.Geometry;

public class CircleSegment extends Segment {

	public CircleSegment(double[] ordinatesArray) {
		super(ordinatesArray);
	}
	
	public CircleSegment(Coordinate[] coordArray) {
		super(coordArray);
	}
	
	@Override
	public boolean isCurve() {
		return false;
	}

	@Override
	public boolean isLine() {
		return false;
	}

	@Override
	public Coordinate[] getCoordinates() {
		return super.getCoords();
	}

	@Override
	public boolean isCircle() {
		return true;
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
