package nari.Geometry;

public class DefaultCoordinateSequence implements CoordinateSequence {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7687041760231274460L;

	private double[] coords = null;
	
	private Coordinate[] coordArray = null;
	
	public DefaultCoordinateSequence(double[] coords){
		this.coords = coords;
		
		if(coordArray==null){
			coordArray = new Coordinate[coords.length/2];
			int k = 0;
			for(int i=0;i<coords.length;i+=2){
				coordArray[k++] = new DefaultCoordinate(coords[i],coords[i+1]); 
			}
		}
	}
	
	public DefaultCoordinateSequence(Coordinate[] coordArray){
		this.coordArray = coordArray;
	}
	
	@Override
	public Coordinate getCoordinate(int index) {
		return coordArray[index];
	}

	@Override
	public Coordinate getCoordinateCopy(int index) {
		Coordinate coord = new DefaultCoordinate(coordArray[index].getX(), coordArray[index].getY());
		return coord;
	}

	@Override
	public void getCoordinate(int index, Coordinate coord) {
		coord.setX(coordArray[index].getX());
		coord.setY(coordArray[index].getY());
	}

	@Override
	public double getX(int index) {
		return coordArray[index].getX();
	}

	@Override
	public double getY(int index) {
		return coordArray[index].getY();
	}

	@Override
	public int size() {
		return coords.length/2;
	}

	@Override
	public Coordinate[] toCoordinateArray() {
		return coordArray;
	}
	
}
