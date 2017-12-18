package nari.SpatialIndex.geom;

public class DefaultCoordinate implements Coordinate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6724953103059493133L;

	private double x = 0;
	
	private double y = 0;
	
	public DefaultCoordinate(double x,double y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int compareTo(Coordinate o) {
		return 0;
	}

	@Override
	public double distance(Coordinate p) {
		return 0;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

}
