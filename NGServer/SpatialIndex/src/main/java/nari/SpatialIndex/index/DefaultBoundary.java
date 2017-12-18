package nari.SpatialIndex.index;

public class DefaultBoundary implements Boundary {

	private double xmin;
	
	private double xmax;
	
	private double ymin;
	
	private double ymax;
	
	public DefaultBoundary(double xmin,double ymin,double xmax,double ymax){
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
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

}
