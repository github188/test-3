package nari.SpatialIndex.geom;

public class DefaultEnvelope implements Envelope {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1089605250722852531L;

	private double xmin;
	
	private double xmax;
	
	private double ymin;
	
	private double ymax;
	
	public DefaultEnvelope(double xmin,double ymin,double xmax,double ymax){
		this.xmin = xmin;
		this.ymin = ymin;
		this.xmax = xmax;
		this.ymax = ymax;
	}
	
	public DefaultEnvelope(Envelope envelope){
		this.xmin = envelope.getMinX();
		this.ymin = envelope.getMinY();
		this.xmax = envelope.getMaxX();
		this.ymax = envelope.getMaxY();
	}
	
	@Override
	public boolean isNull() {
		return xmin==0 || xmax==0 || ymin==0 || ymax==0;
	}

	@Override
	public double getWidth() {
		return Math.abs(xmax-xmin);
	}

	@Override
	public double getHeight() {
		return Math.abs(ymax-ymin);
	}

	@Override
	public double getMinX() {
		return xmin;
	}

	@Override
	public double getMaxX() {
		return xmax;
	}

	@Override
	public double getMinY() {
		return ymin;
	}

	@Override
	public double getMaxY() {
		return ymax;
	}

	@Override
	public double getArea() {
		return 0;
	}

	@Override
	public void expandToInclude(Coordinate p) {
		
	}

	@Override
	public void expandBy(double distance) {
		
	}

	@Override
	public void expandBy(double deltaX, double deltaY) {
		
	}

	@Override
	public Coordinate centre() {
		return null;
	}

	@Override
	public Envelope intersection(Envelope env) {
		return null;
	}

	@Override
	public boolean intersects(Envelope other) {
		return false;
	}

	@Override
	public boolean intersects(Coordinate p) {
		return false;
	}

	@Override
	public boolean intersects(double x, double y) {
		return false;
	}

	@Override
	public boolean contains(Envelope other) {
		return false;
	}

	@Override
	public boolean contains(Coordinate p) {
		return false;
	}

	@Override
	public boolean contains(double x, double y) {
		return false;
	}

	@Override
	public boolean covers(double x, double y) {
		return false;
	}

	@Override
	public boolean covers(Coordinate p) {
		return false;
	}

	@Override
	public boolean covers(Envelope other) {
		return false;
	}

	@Override
	public double distance(Envelope env) {
		return 0;
	}

}
