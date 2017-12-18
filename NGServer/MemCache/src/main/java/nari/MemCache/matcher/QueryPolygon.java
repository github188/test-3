package nari.MemCache.matcher;

public class QueryPolygon {

	private double maxX;
	
	private double maxY;
	
	private double minX;
	
	private double minY;
	
	private double[] coordinates;
	
	public QueryPolygon(double minX,double minY,double maxX,double maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}
	
	public QueryPolygon(double[] coordinates) {
		this.coordinates = coordinates;
		
		maxX = coordinates[0];
		maxY = coordinates[1];
		minX = coordinates[0];
		minY = coordinates[1];
		
		for(int i=2;i<coordinates.length;i=i+2){
			double x = coordinates[i];
			double y = coordinates[i+1];
			if(maxX<x){
				maxX = x;
			}
			if(maxY<y){
				maxY = y;
			}
			if(minX>x){
				minX = x;
			}
			if(minY>y){
				minY = y;
			}
		}
	}

	public double getMaxX() {
		return maxX;
	}

	public double getMaxY() {
		return maxY;
	}

	public double getMinX() {
		return minX;
	}

	public double getMinY() {
		return minY;
	}
	
	public double[] getCoordinates(){
		return coordinates;
	}
}
