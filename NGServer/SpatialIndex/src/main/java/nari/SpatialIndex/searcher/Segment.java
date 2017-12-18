package nari.SpatialIndex.searcher;

import nari.SpatialIndex.geom.Coordinate;
import nari.SpatialIndex.geom.DefaultCoordinate;
import nari.SpatialIndex.geom.DefaultEnvelope;
import nari.SpatialIndex.geom.Envelope;

public class Segment {

	private Coordinate[] seg;
	
	private Envelope env;
	
	private Coordinate maxCoord;
	
	private Coordinate minCoord;
	
	private double xmax = 0;
	
	private double xmin = 0;
	
	private double ymax = 0;
	
	private double ymin = 0;
	
	private Segment(Coordinate[] seg){
		this.seg = seg;
		cal();
	}
	
	public static Segment make(double[] coordSeg){
		Coordinate[] coords = new Coordinate[coordSeg.length/2];
		int len = coordSeg.length;
		for(int i=0;i<len;i=i+2){
			coords[i] = new DefaultCoordinate(coordSeg[i], coordSeg[i+1]);
		}
		
		return new Segment(coords);
	}
	
	public static Segment make(Coordinate[] coordSeg){
		return new Segment(coordSeg);
	}
	
	public Coordinate[] getSeg(){
		return seg;
	}
	
	public Coordinate getMax(){
		if(maxCoord==null){
			maxCoord = new DefaultCoordinate(xmax,ymax);
		}
		return maxCoord;
	}
	
	public Coordinate getMin(){
		if(minCoord==null){
			minCoord = new DefaultCoordinate(xmin,ymin);
		}
		return minCoord;
	}
	
	public Envelope getEnvelope(){
		if(env==null){
			env = new DefaultEnvelope(xmin, ymin, xmax, ymax);
		}
		
		return env;
	}
	
	private void cal(){
		Coordinate coord = seg[0];
		xmax = coord.getX();
		xmin = coord.getX();
		ymax = coord.getY();
		ymin = coord.getY();
		
		for(int i=1;i<seg.length;i++){
			if(seg[i].getX()<xmin){
				xmin = seg[i].getX();
			}
			if(seg[i].getX()>xmax){
				xmax = seg[i].getX();
			}
			
			if(seg[i].getY()<ymin){
				ymin = seg[i].getY();
			}
			if(seg[i].getY()>ymax){
				ymax = seg[i].getY();
			}
		}
	}
}
