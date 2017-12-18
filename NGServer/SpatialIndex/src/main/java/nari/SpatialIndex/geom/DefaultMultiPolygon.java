package nari.SpatialIndex.geom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultMultiPolygon extends AbstractGeometryCollection implements MultiPolygon {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7721461249591398010L;

	private Polygon[] polygons = null;
	
	private Coordinate[] arr = null;
	
	public DefaultMultiPolygon(Polygon[] polygons) {
		this.polygons = polygons;
	}
	
	@Override
	public GeometryType getGeometryType() {
		return GeometryType.MULTIPOLYGON;
	}

	@Override
	public Coordinate[] getCoordinates() {
		if(arr==null){
			List<Coordinate> coords = new ArrayList<Coordinate>();
			for(Polygon py:polygons){
				Collections.addAll(coords, py.getCoordinates());
			}
			arr = new Coordinate[coords.size()];
			arr = coords.toArray(arr);
		}
		return arr;
	}

	@Override
	public Geometry getGeometry(int n) {
		if(n<0 || n>polygons.length-1){
			throw new ArrayIndexOutOfBoundsException(n);
		}
		return polygons[n];
	}

	@Override
	public int getNumGeometry() {
		return polygons.length;
	}

	@Override
	public Envelope getEnvelope() {
		Coordinate[] coordArray = getCoordinates();
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
	public int getNumPoints() {
		return getCoordinates().length;
	}
}
