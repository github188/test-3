package nari.Geometry;

public interface GeometryFactory {

	public Geometry toGeometry(Envelope envelope);
	
	public Point createPoint(Coordinate coordinate);

	public Point createPoint(CoordinateSequence coordinates);

	public MultiPolyline createMultiLineString(MultiPolyline[] lineStrings);

	public GeometryCollection createGeometryCollection(Geometry[] geometries);

	public MultiPolygon createMultiPolygon(Polygon[] polygons);

	public LinearRing createLinearRing(Coordinate[] coordinates);

	public LinearRing createLinearRing(CoordinateSequence coordinates);

	public MultiPoint createMultiPoint(Point[] point);

	public MultiPoint createMultiPoint(Coordinate[] coordinates);

	public MultiPoint createMultiPoint(CoordinateSequence coordinates);

	public Polygon createPolygon(LinearRing shell, LinearRing[] holes);

	public Polyline createLineString(Coordinate[] coordinates);

	public Polyline createLineString(CoordinateSequence coordinates);

	public Geometry createGeometry(Geometry g);

	public CoordinateSequenceFactory getCoordinateSequenceFactory();
}
