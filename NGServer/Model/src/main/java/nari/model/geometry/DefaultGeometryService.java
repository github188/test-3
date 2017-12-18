package nari.model.geometry;

import java.util.concurrent.atomic.AtomicReference;

import nari.Geometry.Coordinate;
import nari.Geometry.CoordinateSequence;
import nari.Geometry.CoordinateSequenceFactory;
import nari.Geometry.DefaultCoordinate;
import nari.Geometry.DefaultCoordinateSequence;
import nari.Geometry.DefaultCoordinateSequenceFactory;
import nari.Geometry.DefaultEnvelope;
import nari.Geometry.DefaultMultiPoint;
import nari.Geometry.DefaultMultiPolygon;
import nari.Geometry.DefaultMultiPolyline;
import nari.Geometry.DefaultPoint;
import nari.Geometry.DefaultPolygon;
import nari.Geometry.DefaultPolyline;
import nari.Geometry.Envelope;
import nari.Geometry.MultiPoint;
import nari.Geometry.MultiPolygon;
import nari.Geometry.MultiPolyline;
import nari.Geometry.Point;
import nari.Geometry.Polygon;
import nari.Geometry.Polyline;

public class DefaultGeometryService implements GeometryService {

	private final AtomicReference<CoordinateSequenceFactory> ref = new AtomicReference<CoordinateSequenceFactory>();
	
	@Override
	public Point createPoint(Coordinate coord) {
		Point pt = new DefaultPoint(coord);
		return pt;
	}

	@Override
	public Point createPoint(double x, double y) {
		Coordinate cr = new DefaultCoordinate(x, y);
		Point pt = new DefaultPoint(cr);
		return pt;
	}

	@Override
	public Point createPoint(Point point) {
		Point pt = new DefaultPoint(point.getX(),point.getY());
		return pt;
	}

	@Override
	public Polyline createPolyline(Coordinate[] coords) {
		Polyline pl = new DefaultPolyline(coords);
		return pl;
	}

	@Override
	public Polyline createPolyline(double[] coords) {
		CoordinateSequence seq = new DefaultCoordinateSequence(coords);
		Polyline pl = new DefaultPolyline(seq);
		return pl;
	}

	@Override
	public Polyline createPolyline(Polyline polyline) {
		Polyline pl = new DefaultPolyline(polyline.getCoordinateSequence());
		return pl;
	}

	@Override
	public Polyline createPolyline(CoordinateSequence seq) {
		Polyline pl = new DefaultPolyline(seq);
		return pl;
	}

	@Override
	public Polygon createPolygon(Coordinate[] coords) {
		Polygon py = new DefaultPolygon(coords);
		return py;
	}

	@Override
	public Polygon createPolygon(double[] coords) {
		CoordinateSequence seq = new DefaultCoordinateSequence(coords);
		Polygon py = new DefaultPolygon(seq);
		return py;
	}

	@Override
	public Polygon createPolygon(Polygon polygon) {
		Polygon py = new DefaultPolygon(polygon.getCoordinateSequence());
		return py;
	}

	@Override
	public Polygon createPolygon(CoordinateSequence seq) {
		Polygon py = new DefaultPolygon(seq);
		return py;
	}

	@Override
	public MultiPoint createMultiPoint(Point[] points) {
		MultiPoint mpt = new DefaultMultiPoint(points);
		return mpt;
	}

	@Override
	public MultiPolyline createMultiPolyline(Polyline[] polylines) {
		MultiPolyline lines = new DefaultMultiPolyline(polylines);
		return lines;
	}

	@Override
	public MultiPolygon createMultiPolygon(Polygon[] polygons) {
		MultiPolygon pys = new DefaultMultiPolygon(polygons);
		return pys;
	}

	@Override
	public Envelope createEnvelope(double xmin, double ymin, double xmax, double ymax) {
		Envelope enve = new DefaultEnvelope(xmin, ymin, xmax, ymax);
		return enve;
	}

	@Override
	public Envelope createEnvelope(Coordinate minCoord, Coordinate maxCoord) {
		Envelope enve = new DefaultEnvelope(minCoord.getX(), minCoord.getY(), maxCoord.getX(), maxCoord.getY());
		return enve;
	}

	@Override
	public CoordinateSequenceFactory getSequenceFactory() {
		if(ref.get()==null){
			CoordinateSequenceFactory fac = new DefaultCoordinateSequenceFactory();
			ref.compareAndSet(null, fac);
		}
		return ref.get();
	}

}
