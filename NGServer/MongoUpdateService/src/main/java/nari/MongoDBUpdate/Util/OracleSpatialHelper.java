package nari.MongoDBUpdate.Util;

import java.sql.SQLException;

import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class OracleSpatialHelper {
	
	static private GeometryFactory geometryFactory = new GeometryFactory();
	
	static public double getLengthFromOracleSpatial(Object shapeObject) {
		
		if (!(shapeObject instanceof STRUCT)) {
			return 0;
		}
		STRUCT struct = (STRUCT) shapeObject;
		
		JGeometry jGeometry = null;
		try {
			jGeometry = JGeometry.load(struct);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
		Geometry geometry = getGeometry(jGeometry);
		return getLength(geometry);
		
	}

	static public Geometry getGeometry(JGeometry jGeometry) {
		switch(jGeometry.getType()) {
		case JGeometry.GTYPE_POINT:
			return getPoint(geometryFactory, jGeometry);
		case JGeometry.GTYPE_CURVE:
			return getLineString(geometryFactory, jGeometry);
		case JGeometry.GTYPE_MULTICURVE:
			return getMultiLineString(geometryFactory, jGeometry);
		case JGeometry.GTYPE_POLYGON:
			return getPolygon(geometryFactory, jGeometry);
		}
		return null;
	}
	
	static private Point getPoint(GeometryFactory geometryFactory, 
			JGeometry jGeometry) {
		
		double[] ordinatesArray = jGeometry.getMBR();
		if (ordinatesArray == null || ordinatesArray.length <= 2) {
			ordinatesArray = jGeometry.getOrdinatesArray();
			if (ordinatesArray == null || ordinatesArray.length <= 2) {
				return null;
			}
			
		}
		Coordinate coordinate = new Coordinate(ordinatesArray[0], ordinatesArray[1]);
		return geometryFactory.createPoint(coordinate);
	}
	
	static private LineString getLineString(GeometryFactory geometryFactory, 
			JGeometry jGeometry) {
		
		double[] ordinatesArray = jGeometry.getOrdinatesArray();
		if (ordinatesArray == null || ordinatesArray.length <= 0) {
			return null;
		}
		Coordinate[] coordinates = new Coordinate[ordinatesArray.length / 2];
		for (int i = 0; i < coordinates.length; i++) {
			coordinates[i] = new Coordinate(
					ordinatesArray[2 * i], ordinatesArray[2 * i + 1]);
		}
		return geometryFactory.createLineString(coordinates);
	}
	
	static private MultiLineString getMultiLineString(GeometryFactory geometryFactory, 
			JGeometry jGeometry) {
		
		double[] ordinatesArray = jGeometry.getOrdinatesArray();
		int[] elemInfo = jGeometry.getElemInfo();
		int elementNum = elemInfo[2]; // 元素的数量

		LineString[] lineStrings = new LineString[elementNum];
		for (int i = 0; i < elementNum - 1; i++) {
			int firstValueIndex = elemInfo[3 + 3 * i] - 1;
			int lastValueIndex = elemInfo[3 + 3 * (i + 1)] - 1;
			Coordinate[] coordinates = new Coordinate[(lastValueIndex - firstValueIndex) / 2];
			for (int j = 0; j < coordinates.length; j++) {
				coordinates[j] = new Coordinate(
						ordinatesArray[firstValueIndex + 2 * j], ordinatesArray[firstValueIndex + 2 * j + 1]);
			}
			lineStrings[i] = geometryFactory.createLineString(coordinates);
		}
		// 最后一个点
		int firstValueIndex = elemInfo[3 + 3 * (elementNum - 1)] - 1;
		int lastValueIndex = ordinatesArray.length;
		Coordinate[] coordinates = new Coordinate[(lastValueIndex - firstValueIndex) / 2];
		for (int i = 0; i < coordinates.length; i++) {
			coordinates[i] = new Coordinate(
					ordinatesArray[firstValueIndex + 2 * i], ordinatesArray[firstValueIndex + 2 * i + 1]);
		}
		lineStrings[elementNum - 1] = geometryFactory.createLineString(coordinates);
		return geometryFactory.createMultiLineString(lineStrings);
	}
	
	static private Polygon getPolygon(GeometryFactory geometryFactory, 
			JGeometry jGeometry) {
		
		double[] ordinatesArray = jGeometry.getOrdinatesArray();
		if (ordinatesArray == null || ordinatesArray.length <= 0) {
			return null;
		}
		Coordinate[] coordinates = new Coordinate[ordinatesArray.length / 2];
		for (int i = 0; i < coordinates.length; i++) {
			coordinates[i] = new Coordinate(
					ordinatesArray[2 * i], ordinatesArray[2 * i + 1]);
		}
		
		LinearRing linearRing = geometryFactory.createLinearRing(coordinates);
		return geometryFactory.createPolygon(linearRing, null);
	}
	
	static private double Rad(double d) {
		return d * Math.PI / 180.0;
	}
	
	static private double getLength(Geometry geometry) {
		if (geometry instanceof LineString) {
			
			LineString lineString = (LineString) geometry;
			int pointsNum = lineString.getNumPoints();
			double length = 0;
			for (int i = 0; i < pointsNum - 1; i++) {
				Point p1 = lineString.getPointN(i);
				Point p2 = lineString.getPointN(i + 1);
				length += getLength(p1, p2);
			}
			return length;
			
		} else if (geometry instanceof MultiLineString) {
			MultiLineString multiLineString = (MultiLineString) geometry;
			int lineStringsNum = multiLineString.getNumGeometries();
			double length = 0;
			for (int i = 0; i < lineStringsNum; i++) {
				length += getLength(multiLineString.getGeometryN(i));
			}
			return length;
		}
		return 0;
	}
	
	static private double getLength(Point p1, Point p2) {
		double radLat1 = Rad(p1.getY());
		double radLat2 = Rad(p2.getY());
		double a = radLat1 - radLat2;
		double b = Rad(p1.getX()) - Rad(p2.getX());
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
		         Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b/2),2)));
		s = s * 6378137.0;
        //s = Math.round(s * 10000) / 10000;
		return s;
	}
}