package nari.Geometry.generator;

import nari.Geometry.Geometry;
import oracle.spatial.geometry.JGeometry;

public class GeometryGenerator {

	private int gtype;
	
//	private int[] elementInfo;
//	
//	private double[] coordinates;
	
	public GeometryGenerator(int gtype,int[] elementInfo,double[] coordinates){
		this.gtype = gtype;
//		this.elementInfo = elementInfo;
//		this.coordinates = coordinates;
	}
	
	public GeometryGenerator(JGeometry geom) {
		this(geom.getType(),geom.getElemInfo(),geom.getOrdinatesArray());
	}
	
	public Geometry toGeometry(){
		
		
		switch (gtype) {
			
			case JGeometry.GTYPE_POINT:
				
				break;
			case JGeometry.GTYPE_CURVE:
						
				break;
			case JGeometry.GTYPE_POLYGON:
				
				break;
			case JGeometry.GTYPE_COLLECTION:
				
				break;
			case JGeometry.GTYPE_MULTIPOINT:
				
				break;
			case JGeometry.GTYPE_MULTICURVE:
				
				break;
			case JGeometry.GTYPE_MULTIPOLYGON:
				
				break;
	
			default:
				break;
				
		}
		
		
		
		return null;
	}
	
//	private Point createPoint(double[] coordinate){
//		Point geometry = new DefaultPoint(coordinate[0],coordinate[1]);
//		return geometry;
//	}
	
}
