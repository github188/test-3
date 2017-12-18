package nari.Geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultMultiPolyline extends AbstractGeometryCollection implements MultiPolyline {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2049114499142478490L;

	@SuppressWarnings("unused")
	private Polyline[] polyline = null;
	
	private Coordinate[] arr = null;
	
	private Segment[] segments = null;
	
	public DefaultMultiPolyline(Polyline[] polyline){
		this.polyline = polyline;
	}
	
	
	public DefaultMultiPolyline(Segment[] segments) {
		super();
		this.segments = segments;
	}

	
	public Segment[] getSegs() {
		return segments;
	}


	public void setSegs(Segment[] segments) {
		this.segments = segments;
	}


	@Override
	public GeometryType getGeometryType() {
		return GeometryType.MULTIPOLYLINE;
	}
	
//	@Override
//	public Geometry getGeometry(int num) {
//		return polyline[num];
//	}
	
	@Override
	public Geometry getGeometry(int num) {
		Segment[] seg = new Segment[1];
		seg[0] = segments[num];
		Geometry geom = new DefaultPolyline(seg);
		return geom;
	}
	
	@Override
	public int getNumGeometry() {
		return segments.length;
	}
	
	@Override
	public Coordinate[] getCoordinates() {
		
//		if(arr==null){
//			List<Coordinate> list = new ArrayList<Coordinate>();
//			
//			for(Polyline pl:polyline){
//				Collections.addAll(list, pl.getCoordinates());
//			}
//			arr = new Coordinate[list.size()];
//			arr = list.toArray(arr);
//		}
		
		if(arr==null){
			List<Coordinate> list = new ArrayList<Coordinate>();
			
			for(Segment seg:segments){
				Collections.addAll(list, seg.getCoordinates());
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
