package nari.Geometry;

public abstract class Segment  {

	private Coordinate[] coords = null;
	
	public Coordinate[] getCoords() {
		return coords;
	}

	public void setCoords(Coordinate[] coords) {
		this.coords = coords;
	}

	public Segment(){
		
	}
	
	public Segment(Coordinate[] coords) {
		super();
		this.coords = coords;
	}
	
	public Segment(double[] ordinatesArray) {
		super();
		Coordinate[] coords = new Coordinate[ordinatesArray.length/2];
		for(int i=0;i<ordinatesArray.length/2;i++){
			coords[i] = new DefaultCoordinate(ordinatesArray[2*i],ordinatesArray[2*i+1]);
		}
		this.coords = coords;
	}
	

	public abstract boolean isCurve();
	
	public abstract boolean isLine();
	
	public abstract boolean isCircle();
	
	public abstract Coordinate[] getCoordinates();
	
	public Geometry getGeometry(){
		return null;
	}
	
	public abstract Polyline asPolyline();
	
	public abstract Polygon asPolygon();
	
	public abstract Curve asCurve();
	
	public abstract Circle asCircle();
}
