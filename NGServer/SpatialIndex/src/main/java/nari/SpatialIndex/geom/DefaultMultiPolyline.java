package nari.SpatialIndex.geom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultMultiPolyline extends AbstractGeometryCollection implements MultiPolyline {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2049114499142478490L;

	private Polyline[] polyline = null;
	
	private Coordinate[] arr = null;
	
	public DefaultMultiPolyline(Polyline[] polyline){
		this.polyline = polyline;
	}
	
	@Override
	public GeometryType getGeometryType() {
		return GeometryType.MULTIPOLYLINE;
	}
	
	@Override
	public Geometry getGeometry(int num) {
		return polyline[num];
	}
	
	@Override
	public int getNumGeometry() {
		return polyline.length;
	}
	
	@Override
	public Coordinate[] getCoordinates() {
		if(arr==null){
			List<Coordinate> list = new ArrayList<Coordinate>();
			
			for(Polyline pl:polyline){
				Collections.addAll(list, pl.getCoordinates());
			}
			arr = new Coordinate[list.size()];
			arr = list.toArray(arr);
		}
		return arr;
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
