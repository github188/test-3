package nari.SpatialIndex.geom;

import com.vividsolutions.jts.operation.buffer.BufferOp;

public abstract class AbstractGeometry implements Geometry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4078304736239278498L;

	private final com.vividsolutions.jts.geom.GeometryFactory factory = new com.vividsolutions.jts.geom.GeometryFactory();
	
	@Override
	public int compareTo(Geometry o) {
		return 0;
	}

	@Override
	public GeometryFactory getFactory() {
		return null;
	}

	@Override
	public abstract int getNumPoints() ;

	@Override
	public boolean isSimple() {
		return false;
	}

	@Override
	public boolean isValid() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public double distance(Geometry g) {
		return 0;
	}

	@Override
	public boolean isWithinDistance(Geometry geom, double distance) {
		return false;
	}

	@Override
	public boolean isRectangle() {
		return false;
	}

	@Override
	public double getArea() {
		return 0;
	}

	@Override
	public double getLength() {
		return 0;
	}

	@Override
	public Point getCentroid() {
		return null;
	}

	@Override
	public Point getInteriorPoint() {
		return null;
	}

	@Override
	public Envelope getEnvelope() {
		return null;
	}

	@Override
	public boolean disjoint(Geometry g) {
		return false;
	}

	@Override
	public boolean touches(Geometry g) {
		return false;
	}

	@Override
	public boolean intersects(Geometry g) {
		com.vividsolutions.jts.geom.Geometry geom = null;
		Coordinate[] coords = null;
		GeometryType type = getGeometryType();
		switch (type) {
			case POINT:
				coords = getCoordinates();
				com.vividsolutions.jts.geom.Coordinate coord = new com.vividsolutions.jts.geom.Coordinate(coords[0].getX(),coords[0].getY());
				geom = factory.createPoint(coord);
				break;
			case POLYLINE:
				coords = getCoordinates();
				
				com.vividsolutions.jts.geom.Coordinate[] jtsLine = new com.vividsolutions.jts.geom.Coordinate[coords.length];
				int i=0;
				for(Coordinate c:coords){
					com.vividsolutions.jts.geom.Coordinate cc = new com.vividsolutions.jts.geom.Coordinate(c.getX(),c.getY());
					jtsLine[i++] = cc;
				}
				
				geom = factory.createLineString(jtsLine);
				break;
			case POLYGON:
				coords = getCoordinates();
				
				com.vividsolutions.jts.geom.Coordinate[] jtsPoly = new com.vividsolutions.jts.geom.Coordinate[coords.length];
				int j=0;
				for(Coordinate c:coords){
					com.vividsolutions.jts.geom.Coordinate cc = new com.vividsolutions.jts.geom.Coordinate(c.getX(),c.getY());
					jtsPoly[j++] = cc;
				}
				
				com.vividsolutions.jts.geom.LinearRing shell = factory.createLinearRing(jtsPoly);
				geom = factory.createPolygon(shell, null);
				break;
			case MULTIPOINT:
				AbstractGeometryCollection pointColl = (AbstractGeometryCollection)this;
				int pointNums = pointColl.getNumGeometry();
				com.vividsolutions.jts.geom.Point[] pts = new com.vividsolutions.jts.geom.Point[pointNums];
				
				for(int k=0;k<pointNums;k++){
					Point pt = (Point)pointColl.getGeometry(k);
					com.vividsolutions.jts.geom.Coordinate mp = new com.vividsolutions.jts.geom.Coordinate(pt.getX(),pt.getY());
					com.vividsolutions.jts.geom.Point jtsPoint = factory.createPoint(mp);
					pts[k] = jtsPoint;
				}
				
				geom = factory.createMultiPoint(pts);
				break;
			case MULTIPOLYLINE:
				AbstractGeometryCollection lineColl = (AbstractGeometryCollection)this;
				int lineNums = lineColl.getNumGeometry();
				com.vividsolutions.jts.geom.LineString[] lineStrings = new com.vividsolutions.jts.geom.LineString[lineNums];
				
				for(int k=0;k<lineNums;k++){
					Polyline pt = (Polyline)lineColl.getGeometry(k);
					
					int m=0;
					com.vividsolutions.jts.geom.Coordinate[] jtsMCoord = new com.vividsolutions.jts.geom.Coordinate[pt.getNumPoints()];
					for(Coordinate c:pt.getCoordinates()){
						com.vividsolutions.jts.geom.Coordinate cc = new com.vividsolutions.jts.geom.Coordinate(c.getX(),c.getY());
						jtsMCoord[m++] = cc;
					}
					
					
					com.vividsolutions.jts.geom.LineString jtsMLine = factory.createLineString(jtsMCoord);
					lineStrings[k] = jtsMLine;
				}
				factory.createMultiLineString(lineStrings);
				break;
			case MULTIPOLYGON:
				AbstractGeometryCollection polyColl = (AbstractGeometryCollection)this;
				int polyNums = polyColl.getNumGeometry();
				com.vividsolutions.jts.geom.Polygon[] polys = new com.vividsolutions.jts.geom.Polygon[polyNums];
				
				for(int k=0;k<polyNums;k++){
					Polygon pt = (Polygon)polyColl.getGeometry(k);
					
					int m=0;
					com.vividsolutions.jts.geom.Coordinate[] jtsMCoord = new com.vividsolutions.jts.geom.Coordinate[pt.getNumPoints()];
					for(Coordinate c:pt.getCoordinates()){
						com.vividsolutions.jts.geom.Coordinate cc = new com.vividsolutions.jts.geom.Coordinate(c.getX(),c.getY());
						jtsMCoord[m++] = cc;
					}
					
					com.vividsolutions.jts.geom.LinearRing pshell = factory.createLinearRing(jtsMCoord);
					com.vividsolutions.jts.geom.Polygon py = factory.createPolygon(pshell, null);
					
					polys[k] = py;
				}
				geom = factory.createMultiPolygon(polys);
				break;
			default:
				break;
		}
		return geom.intersects(changeToJTSGeometry(g));
	}

	private com.vividsolutions.jts.geom.Geometry changeToJTSGeometry(Geometry g){
		com.vividsolutions.jts.geom.Geometry geom = null;
		Coordinate[] coords = null;
		GeometryType type = g.getGeometryType();
		switch (type) {
			case POINT:
				coords = g.getCoordinates();
				com.vividsolutions.jts.geom.Coordinate coord = new com.vividsolutions.jts.geom.Coordinate(coords[0].getX(),coords[0].getY());
				geom = factory.createPoint(coord);
				break;
			case POLYLINE:
				coords = g.getCoordinates();
				
				com.vividsolutions.jts.geom.Coordinate[] jtsLine = new com.vividsolutions.jts.geom.Coordinate[coords.length];
				int i=0;
				for(Coordinate c:coords){
					com.vividsolutions.jts.geom.Coordinate cc = new com.vividsolutions.jts.geom.Coordinate(c.getX(),c.getY());
					jtsLine[i++] = cc;
				}
				
				geom = factory.createLineString(jtsLine);
				break;
			case POLYGON:
				coords = g.getCoordinates();
				
				com.vividsolutions.jts.geom.Coordinate[] jtsPoly = new com.vividsolutions.jts.geom.Coordinate[coords.length];
				int j=0;
				for(Coordinate c:coords){
					com.vividsolutions.jts.geom.Coordinate cc = new com.vividsolutions.jts.geom.Coordinate(c.getX(),c.getY());
					jtsPoly[j++] = cc;
				}
				
				com.vividsolutions.jts.geom.LinearRing shell = factory.createLinearRing(jtsPoly);
				geom = factory.createPolygon(shell, null);
				break;
			case MULTIPOINT:
				AbstractGeometryCollection pointColl = (AbstractGeometryCollection)this;
				int pointNums = pointColl.getNumGeometry();
				com.vividsolutions.jts.geom.Point[] pts = new com.vividsolutions.jts.geom.Point[pointNums];
				
				for(int k=0;k<pointNums;k++){
					Point pt = (Point)pointColl.getGeometry(k);
					com.vividsolutions.jts.geom.Coordinate mp = new com.vividsolutions.jts.geom.Coordinate(pt.getX(),pt.getY());
					com.vividsolutions.jts.geom.Point jtsPoint = factory.createPoint(mp);
					pts[k] = jtsPoint;
				}
				
				geom = factory.createMultiPoint(pts);
				break;
			case MULTIPOLYLINE:
				AbstractGeometryCollection lineColl = (AbstractGeometryCollection)this;
				int lineNums = lineColl.getNumGeometry();
				com.vividsolutions.jts.geom.LineString[] lineStrings = new com.vividsolutions.jts.geom.LineString[lineNums];
				
				for(int k=0;k<lineNums;k++){
					Polyline pt = (Polyline)lineColl.getGeometry(k);
					
					int m=0;
					com.vividsolutions.jts.geom.Coordinate[] jtsMCoord = new com.vividsolutions.jts.geom.Coordinate[pt.getNumPoints()];
					for(Coordinate c:pt.getCoordinates()){
						com.vividsolutions.jts.geom.Coordinate cc = new com.vividsolutions.jts.geom.Coordinate(c.getX(),c.getY());
						jtsMCoord[m++] = cc;
					}
					
					
					com.vividsolutions.jts.geom.LineString jtsMLine = factory.createLineString(jtsMCoord);
					geom = lineStrings[k] = jtsMLine;
				}
				factory.createMultiLineString(lineStrings);
				break;
			case MULTIPOLYGON:
				AbstractGeometryCollection polyColl = (AbstractGeometryCollection)this;
				int polyNums = polyColl.getNumGeometry();
				com.vividsolutions.jts.geom.Polygon[] polys = new com.vividsolutions.jts.geom.Polygon[polyNums];
				
				for(int k=0;k<polyNums;k++){
					Polygon pt = (Polygon)polyColl.getGeometry(k);
					
					int m=0;
					com.vividsolutions.jts.geom.Coordinate[] jtsMCoord = new com.vividsolutions.jts.geom.Coordinate[pt.getNumPoints()];
					for(Coordinate c:pt.getCoordinates()){
						com.vividsolutions.jts.geom.Coordinate cc = new com.vividsolutions.jts.geom.Coordinate(c.getX(),c.getY());
						jtsMCoord[m++] = cc;
					}
					
					com.vividsolutions.jts.geom.LinearRing pshell = factory.createLinearRing(jtsMCoord);
					com.vividsolutions.jts.geom.Polygon py = factory.createPolygon(pshell, null);
					
					polys[k] = py;
				}
				geom = factory.createMultiPolygon(polys);
				break;
			default:
				break;
		}
		
		
		
		return geom;
	}
	
	@Override
	public boolean crosses(Geometry g) {
		return false;
	}

	@Override
	public boolean within(Geometry g) {
		return false;
	}

	@Override
	public boolean contains(Geometry g) {
		return false;
	}

	@Override
	public boolean overlaps(Geometry g) {
		return false;
	}

	@Override
	public boolean covers(Geometry g) {
		return false;
	}

	@Override
	public boolean coveredBy(Geometry g) {
		return false;
	}

	@Override
	public boolean relate(Geometry g, String intersectionPattern) {
		return false;
	}

	@Override
	public boolean equals(Geometry g) {
		return false;
	}

	@Override
	public String toText() {
		return null;
	}

	@Override
	public Geometry buffer(double distance) {
		com.vividsolutions.jts.geom.Geometry g = null;
		Coordinate[] coords = null;
		GeometryType type = getGeometryType();
		switch (type) {
			case POINT:
				coords = getCoordinates();
				com.vividsolutions.jts.geom.Coordinate coord = new com.vividsolutions.jts.geom.Coordinate(coords[0].getX(),coords[0].getY());
				g = factory.createPoint(coord);
				break;
			case POLYLINE:
				coords = getCoordinates();
				
				com.vividsolutions.jts.geom.Coordinate[] jtsLine = new com.vividsolutions.jts.geom.Coordinate[coords.length];
				int i=0;
				for(Coordinate c:coords){
					com.vividsolutions.jts.geom.Coordinate cc = new com.vividsolutions.jts.geom.Coordinate(c.getX(),c.getY());
					jtsLine[i++] = cc;
				}
				
				g = factory.createLineString(jtsLine);
				break;
			case POLYGON:
				coords = getCoordinates();
				
				com.vividsolutions.jts.geom.Coordinate[] jtsPoly = new com.vividsolutions.jts.geom.Coordinate[coords.length];
				int j=0;
				for(Coordinate c:coords){
					com.vividsolutions.jts.geom.Coordinate cc = new com.vividsolutions.jts.geom.Coordinate(c.getX(),c.getY());
					jtsPoly[j++] = cc;
				}
				
				com.vividsolutions.jts.geom.LinearRing shell = factory.createLinearRing(jtsPoly);
				g = factory.createPolygon(shell, null);
				break;
			case MULTIPOINT:
				AbstractGeometryCollection pointColl = (AbstractGeometryCollection)this;
				int pointNums = pointColl.getNumGeometry();
				com.vividsolutions.jts.geom.Point[] pts = new com.vividsolutions.jts.geom.Point[pointNums];
				
				for(int k=0;k<pointNums;k++){
					Point pt = (Point)pointColl.getGeometry(k);
					com.vividsolutions.jts.geom.Coordinate mp = new com.vividsolutions.jts.geom.Coordinate(pt.getX(),pt.getY());
					com.vividsolutions.jts.geom.Point jtsPoint = factory.createPoint(mp);
					pts[k] = jtsPoint;
				}
				
				g = factory.createMultiPoint(pts);
				break;
			case MULTIPOLYLINE:
				AbstractGeometryCollection lineColl = (AbstractGeometryCollection)this;
				int lineNums = lineColl.getNumGeometry();
				com.vividsolutions.jts.geom.LineString[] lineStrings = new com.vividsolutions.jts.geom.LineString[lineNums];
				
				for(int k=0;k<lineNums;k++){
					Polyline pt = (Polyline)lineColl.getGeometry(k);
					
					int m=0;
					com.vividsolutions.jts.geom.Coordinate[] jtsMCoord = new com.vividsolutions.jts.geom.Coordinate[pt.getNumPoints()];
					for(Coordinate c:pt.getCoordinates()){
						com.vividsolutions.jts.geom.Coordinate cc = new com.vividsolutions.jts.geom.Coordinate(c.getX(),c.getY());
						jtsMCoord[m++] = cc;
					}
					
					
					com.vividsolutions.jts.geom.LineString jtsMLine = factory.createLineString(jtsMCoord);
					lineStrings[k] = jtsMLine;
				}
				g = factory.createMultiLineString(lineStrings);
				break;
			case MULTIPOLYGON:
				AbstractGeometryCollection polyColl = (AbstractGeometryCollection)this;
				int polyNums = polyColl.getNumGeometry();
				com.vividsolutions.jts.geom.Polygon[] polys = new com.vividsolutions.jts.geom.Polygon[polyNums];
				
				for(int k=0;k<polyNums;k++){
					Polygon pt = (Polygon)polyColl.getGeometry(k);
					
					int m=0;
					com.vividsolutions.jts.geom.Coordinate[] jtsMCoord = new com.vividsolutions.jts.geom.Coordinate[pt.getNumPoints()];
					for(Coordinate c:pt.getCoordinates()){
						com.vividsolutions.jts.geom.Coordinate cc = new com.vividsolutions.jts.geom.Coordinate(c.getX(),c.getY());
						jtsMCoord[m++] = cc;
					}
					
					com.vividsolutions.jts.geom.LinearRing pshell = factory.createLinearRing(jtsMCoord);
					com.vividsolutions.jts.geom.Polygon py = factory.createPolygon(pshell, null);
					
					polys[k] = py;
				}
				g = factory.createMultiPolygon(polys);
				break;
			default:
				break;
		}
		
		if(g==null){
			return null;
		}
		BufferOp op = new BufferOp(g);
		com.vividsolutions.jts.geom.Geometry gg = op.getResultGeometry(distance);
		
		Geometry geometry = null;
		if(gg.isSimple()){
			com.vividsolutions.jts.geom.Coordinate[] rcoords = gg.getCoordinates();
			
			Coordinate[] coordArray = new Coordinate[rcoords.length];
			int t = 0;
			for(com.vividsolutions.jts.geom.Coordinate rc:rcoords){
				coordArray[t] = new DefaultCoordinate(rc.x, rc.y);
				t++;
			}
			geometry = new DefaultPolygon(coordArray);
		}else{
			int num = gg.getNumGeometries();
			Polygon[] polygons = new Polygon[num];
			for(int t=0;t<num;t++){
				com.vividsolutions.jts.geom.Geometry rg = gg.getGeometryN(t);
				
				com.vividsolutions.jts.geom.Coordinate[] rcoords = rg.getCoordinates();
				
				Coordinate[] coordArray = new Coordinate[rcoords.length];
				int n = 0;
				for(com.vividsolutions.jts.geom.Coordinate rc:rcoords){
					coordArray[n] = new DefaultCoordinate(rc.x, rc.y);
				}
				Polygon gp = new DefaultPolygon(coordArray);
				polygons[t] = gp;
			}
			geometry = new DefaultMultiPolygon(polygons);
		}
		
		return geometry;
	}

	@Override
	public Geometry convexHull() {
		return null;
	}

	@Override
	public Geometry intersection(Geometry other) {
		return null;
	}

	@Override
	public Geometry union(Geometry other) {
		return null;
	}

	@Override
	public Geometry difference(Geometry other) {
		return null;
	}

	@Override
	public Geometry symDifference(Geometry other) {
		return null;
	}

	@Override
	public Geometry union() {
		return null;
	}

	@Override
	public boolean isGeometryCollection(Geometry g) {
		return g==null?false:g instanceof GeometryCollection;
	}

	public abstract GeometryType getGeometryType();
	
	public abstract Coordinate[] getCoordinates();
	
}
