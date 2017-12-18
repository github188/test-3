package nari.Geometry;

public class CurveSegment extends Segment {

	public CurveSegment(double[] ordinatesArray) {
		super(ordinatesArray);
	}
	
	public CurveSegment(Coordinate[] coordArray) {
		super(coordArray);
	}

	
	@Override
	public boolean isCurve() {
		return true;
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
