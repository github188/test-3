package nari.model.geometry;

import nari.Geometry.Coordinate;
import nari.Geometry.CoordinateSequence;
import nari.Geometry.CoordinateSequenceFactory;
import nari.Geometry.Envelope;
import nari.Geometry.MultiPoint;
import nari.Geometry.MultiPolygon;
import nari.Geometry.MultiPolyline;
import nari.Geometry.Point;
import nari.Geometry.Polygon;
import nari.Geometry.Polyline;

public interface GeometryService {
	
	public static final GeometryService NONE = new GeometryService(){

		@Override
		public Point createPoint(Coordinate coord) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Point createPoint(double x, double y) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Point createPoint(Point point) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Polyline createPolyline(Coordinate[] coords) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Polyline createPolyline(double[] coords) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Polyline createPolyline(Polyline polyline) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Polyline createPolyline(CoordinateSequence seq) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Polygon createPolygon(Coordinate[] coords) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Polygon createPolygon(double[] coords) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Polygon createPolygon(Polygon polygon) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Polygon createPolygon(CoordinateSequence seq) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MultiPoint createMultiPoint(Point[] points) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MultiPolyline createMultiPolyline(Polyline[] polylines) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MultiPolygon createMultiPolygon(Polygon[] polygons) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Envelope createEnvelope(double xmin, double ymin, double xmax,double ymax) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Envelope createEnvelope(Coordinate minCoord, Coordinate maxCoord) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CoordinateSequenceFactory getSequenceFactory() {
			// TODO Auto-generated method stub
			return null;
		}
		
	};
	
	public Point createPoint(Coordinate coord);
	
	public Point createPoint(double x,double y);
	
	public Point createPoint(Point point);
	
	public Polyline createPolyline(Coordinate[] coords);
	
	public Polyline createPolyline(double[] coords);
	
	public Polyline createPolyline(Polyline polyline);
	
	public Polyline createPolyline(CoordinateSequence seq);
	
	public Polygon createPolygon(Coordinate[] coords);
	
	public Polygon createPolygon(double[] coords);
	
	public Polygon createPolygon(Polygon polygon);
	
	public Polygon createPolygon(CoordinateSequence seq);
	
	public MultiPoint createMultiPoint(Point[] points);
	
	public MultiPolyline createMultiPolyline(Polyline[] polylines);
	
	public MultiPolygon createMultiPolygon(Polygon[] polygons);
	
	public Envelope createEnvelope(double xmin,double ymin,double xmax,double ymax);
	
	public Envelope createEnvelope(Coordinate minCoord,Coordinate maxCoord);
	
	public CoordinateSequenceFactory getSequenceFactory();
	
}
