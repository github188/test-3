package nari.parameter.geojson;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import nari.Geometry.Coordinate;
import nari.Geometry.MultiPoint;
import nari.Geometry.MultiPolygon;
import nari.Geometry.MultiPolyline;
import nari.Geometry.Point;
import nari.Geometry.Polygon;
import nari.Geometry.Polyline;

public class GeoJsonTranslater {

	private static final AtomicReference<GeoJsonTranslater> ref = new AtomicReference<GeoJsonTranslater>(new GeoJsonTranslater());
	
	private final AtomicReference<GeoJsonCRS> crs = new AtomicReference<GeoJsonCRS>(null);
	
	private final Map<String,Object> defCRSProperty = new HashMap<String,Object>();
	
	private GeoJsonTranslater() {
		defCRSProperty.put("name","urn:ogc:def:crs:OGC:1.3:CRS84");
	}
	
	public Map<String,Object> getDefaultCRSProperty(){
		return defCRSProperty;
	}
	
	public GeoJsonCRS getDefaultCRS(){
		if(crs.get()==null){
			GeoJsonCRS geo = new GeoJsonCRS();
			crs.compareAndSet(null, geo);
		}
		return crs.get();
	}
	
	public GeoJsonFeature createFeature(Map<String,Object> properties,GeoJsonGeometry geometry){
		return new GeoJsonFeature(properties,geometry);
	}
	
	public GeoJsonGeometry createPoint(Object[] coordArr){
		return new GeoJsonGeometry("Point",coordArr);
	}
	
	public GeoJsonGeometry createLineString(Object[] coordArr){
		return new GeoJsonGeometry("LineString",coordArr);
	}
	
	public GeoJsonGeometry createPolygon(Object[] coordArr){
		return new GeoJsonGeometry("Polygon",coordArr);
	}
	
	public GeoJsonGeometry createMultiPoint(Object[] coordArr){
		return new GeoJsonGeometry("MultiPoint",coordArr);
	}
	
	public GeoJsonGeometry createMultiLineString(Object[] coordArr){
		return new GeoJsonGeometry("MultiLineString",coordArr);
	}
	
	public GeoJsonGeometry createMultiPolygon(Object[] coordArr){
		return new GeoJsonGeometry("MultiPolygon",coordArr);
	}
	
	public Object[] genPointCoordArray(Point point){
		Coordinate coord = point.getCoordinate();
		return new Object[]{coord.getX(),coord.getY()};
	}
	
	public Object[] genPointCoordArray(double x,double y){
		return new Object[]{x,y};
	}
	
	public Object[] genLineStringCoordArray(Polyline polyline){
		Object[] outerArr = new Object[1];
		Coordinate[] coords = polyline.getCoordinates();
		int k=0;
		double[] coordArr = new double[coords.length*2];
		for(Coordinate coord:coords){
			coordArr[k] = coord.getX();
			coordArr[k+1] = coord.getY();
			k = k+2;
		}
		
		outerArr[0] = coordArr;
		return outerArr;
	}
	
	public Object[] genPolygonCoordArray(Polygon polygon){
		Object[] outerArr = new Object[1];
		Object[] polyArr = new Object[1];
		Coordinate[] coords = polygon.getCoordinates();
		
		int k=0;
		double[] coordArr = new double[coords.length*2];
		for(Coordinate coord:coords){
			coordArr[k] = coord.getX();
			coordArr[k+1] = coord.getY();
			k = k+2;
		}
		
		polyArr[0] = coordArr;
		
		outerArr[0] = polyArr;
		
		return outerArr;
	}
	
	public Object[] genMultiPointCoordArray(MultiPoint point){
		int num = point.getNumGeometry();
		Object[] outerArr = new Object[num];
		for(int i=0;i<num;i++){
			Point p = (Point)point.getGeometry(i);
			Coordinate[] coords = p.getCoordinates();
			int k=0;
			double[] coordArr = new double[coords.length*2];
			for(Coordinate coord:coords){
				coordArr[k] = coord.getX();
				coordArr[k+1] = coord.getY();
				k = k+2;
			}
			outerArr[i] = coordArr;
		}
		
		return outerArr;
	}
	
	public Object[] genMultiLineStringCoordArray(MultiPolyline polyline){
		int num = polyline.getNumGeometry();
		Object[] outerArr = new Object[num];
		for(int i=0;i<num;i++){
			Polyline line = (Polyline)polyline.getGeometry(i);
			Coordinate[] coords = line.getCoordinates();
			int k=0;
			double[] coordArr = new double[coords.length*2];
			for(Coordinate coord:coords){
				coordArr[k] = coord.getX();
				coordArr[k+1] = coord.getY();
				k = k+2;
			}
			outerArr[i] = coordArr;
		}
		
		return outerArr;
	}
	
	public Object[] genMultiPolygonCoordArray(MultiPolygon polygon){
		return new Object[]{};
	}
	
	public static GeoJsonTranslater getTranslater(){
		return ref.get();
	}
}
