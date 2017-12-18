package nari.SpatialIndex.geom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.vividsolutions.jts.geom.Triangle;

import oracle.spatial.geometry.JGeometry;

public class GeometryBuilder {

	private static final AtomicReference<GeometryBuilder> builder = new AtomicReference<GeometryBuilder>();
	
	public static GeometryBuilder getBuilder(){
		if(builder.get()==null){
			GeometryBuilder gb = new GeometryBuilder();
			builder.compareAndSet(null, gb);
		}
		return builder.get();
	}
	
	public JGeometry createGeometry(GeometryType gtype,CoordinateSequence... seqs){
		JGeometry geom = null;
		
		Object[] o = new Object[seqs.length];
		int k=0;
		for(CoordinateSequence seq:seqs){
			Coordinate[] coords = seq.toCoordinateArray();
			
			double[] coordArr = new double[coords.length*2];
			int i=0;
			for(Coordinate c:coords){
				coordArr[i++] = c.getX();
				coordArr[i++] = c.getY();
			}
			o[k] = coordArr;
		}
		
		switch (gtype) {
			case POINT:
				geom = JGeometry.createPoint((double[])o[0], 2, 0);
				break;
			case POLYLINE:
				geom = JGeometry.createLinearLineString((double[])o[0], 2, 0);
				break;
			case POLYGON:
				geom = JGeometry.createLinearPolygon((double[])o[0], 2, 0);
				break;
			case MULTIPOINT:
				geom = JGeometry.createMultiPoint(o, 2, 0);
				break;
			case MULTIPOLYLINE:
				geom = JGeometry.createLinearMultiLineString(o, 2, 0);
				break;
			case MULTIPOLYGON:
				geom = JGeometry.createLinearPolygon(o, 2, 0);
				break;
			default:
				break;
		}
		return geom;
	}
	
	public Geometry buildGeometry(JGeometry geom) {
		int type = geom.getType();
		int[] ele = geom.getElemInfo();

		int interpretation = 1;
		if (ele != null) {
			interpretation = ele[2];
		}

		Geometry geometry = null;
		switch (type) {
			case 1:
				geometry = createPoint(geom);
				break;
			case 2:
				geometry = createPolyline(geom, interpretation);
				break;
			case 3:
				geometry = createPolygon(geom, interpretation);
				break;
			case 6:
				int[] eles = geom.getElemInfo();
				double[] original = geom.getOrdinatesArray();
				int[] offset = new int[eles.length / 3];
				int k = 0;
				for (int i = 0; i < eles.length; i += 3) {
					offset[(k++)] = eles[i];
				}
	
				Polyline[] polyline = new Polyline[offset.length];
				int n = 0;
				double[] lingRing = null;
				for (int of = 0; of < offset.length - 1; of++) {
					lingRing = Arrays.copyOfRange(original, offset[of] - 1, offset[(of + 1)] - 1);
					CoordinateSequence seq = new DefaultCoordinateSequence(lingRing);
					polyline[n++] = new DefaultPolyline(seq);
				}
				
				lingRing = Arrays.copyOfRange(original, offset[(offset.length - 1)] - 1, original.length);
				
				CoordinateSequence seq = new DefaultCoordinateSequence(lingRing);
				polyline[n++] = new DefaultPolyline(seq);
				geometry = new DefaultMultiPolyline(polyline);
			}
		return geometry;
	}
	
	private Polyline createPolyline(JGeometry geom,int interpretation){
		double[] coordinate = geom.getOrdinatesArray();
		Polyline polyline = null;
		if (interpretation == 2) {
			Coordinate[] source = new Coordinate[coordinate.length / 2];
			int m = 0;
			for (int n = 0; n < coordinate.length; n += 2) {
				source[m++] = new DefaultCoordinate(coordinate[n], coordinate[(n + 1)]);
			}

			List<Coordinate> hull = new ArrayList<Coordinate>();
			List<Coordinate> coords = new ArrayList<Coordinate>();
			int k = 0;
			int tt = (source.length - 3) / 2;
			for (int i = 0; i < source.length + tt; i++) {
				if ((i % 3 == 0) && (i > 2)) {
					k--;
				}
				coords.add(source[k]);
				k++;
			}

			Coordinate c1 = null;
			Coordinate c2 = null;
			Coordinate c3 = null;

			for (int s = 0; s < coords.size(); s++) {
				if (((s + 1) / 3 == 0) && (s >= 2)) {
					c1 = new DefaultCoordinate(coords.get(s - 2).getX(), coords.get(s - 2).getY());
					c2 = new DefaultCoordinate(coords.get(s - 1).getX(), coords.get(s - 1).getY());
					c3 = new DefaultCoordinate(coords.get(s).getX(), coords.get(s).getY());
					hull.addAll(getHull(c1, c2, c3));
				}
			}
			Coordinate[] coordArray = new Coordinate[hull.size()];
			coordArray = hull.toArray(coordArray);
			polyline = new DefaultPolyline(coordArray);
		} else {
			CoordinateSequence seq = new DefaultCoordinateSequence(coordinate);
			polyline = new DefaultPolyline(seq);
		}
		return polyline;
	}
	
	private Polygon createPolygon(JGeometry geom,int interpretation){
		double[] coordinate = geom.getOrdinatesArray();
		Polygon polygon = null;
		switch (interpretation) {
			case 2:
				Coordinate[] source = new Coordinate[coordinate.length / 2];
				int m = 0;
				for (int n = 0; n < coordinate.length; n += 2) {
					source[m++] = new DefaultCoordinate(coordinate[n],coordinate[(n + 1)]);
				}

				List<Coordinate> hull = new ArrayList<Coordinate>();
				List<Coordinate> coords = new ArrayList<Coordinate>();
				int k = 0;
				int tt = (source.length - 3) / 2;
				for (int i = 0; i < source.length + tt; i++) {
					if ((i % 3 == 0) && (i > 2)) {
						k--;
					}
					coords.add(source[k]);
					k++;
				}

				Coordinate c1 = null;
				Coordinate c2 = null;
				Coordinate c3 = null;

				for (int s = 0; s < coords.size(); s++) {
					if (((s + 1) % 3 == 0) && (s >= 2)) {
						c1 = new DefaultCoordinate(coords.get(s - 2).getX(),coords.get(s - 2).getY());
						c2 = new DefaultCoordinate(coords.get(s - 1).getX(),coords.get(s - 1).getY());
						c3 = new DefaultCoordinate(coords.get(s).getX(),coords.get(s).getY());
						hull.addAll(getHull(c1, c2, c3));
					}
				}
				hull.add(hull.get(0));
				Coordinate[] coordArray = new Coordinate[hull.size()];
				coordArray = hull.toArray(coordArray);
				polygon = new DefaultPolygon(coordArray);
				break;
			case 3:
				Coordinate leftBottom = new DefaultCoordinate(coordinate[0],coordinate[1]);
				Coordinate rightTop = new DefaultCoordinate(coordinate[2],coordinate[3]);
				double xmin = leftBottom.getX();
				double ymin = rightTop.getY();
				double xmax = rightTop.getX();
				double ymax = leftBottom.getY();
				double[] c = new double[]{xmax, ymax, xmax, ymin, xmin, ymin, xmin, ymax, xmax, ymax};
				CoordinateSequence seq = new DefaultCoordinateSequence(c);
				polygon = new DefaultPolygon(seq);
				break;
			case 4:
				Coordinate rc1 = new DefaultCoordinate(coordinate[0], coordinate[1]);
				Coordinate rc2 = new DefaultCoordinate(coordinate[2], coordinate[3]);
				Coordinate rc3 = new DefaultCoordinate(coordinate[4], coordinate[5]);
				Coordinate[] rcoords = getCircle(rc1, rc2, rc3);
				polygon = new DefaultPolygon(rcoords);
				break;
			default:
				if (coordinate.length == 2) {
					double[] cc = new double[]{coordinate[0], coordinate[1], coordinate[2], coordinate[1], coordinate[2], coordinate[3], coordinate[0], coordinate[3], coordinate[0], coordinate[1]};
					CoordinateSequence cseq = new DefaultCoordinateSequence(cc);
					polygon = new DefaultPolygon(cseq);
				} else if ((coordinate[0] == coordinate[(coordinate.length - 2)]) && (coordinate[1] == coordinate[(coordinate.length - 1)])) {
					Coordinate[] cArray = new Coordinate[coordinate.length/2];
					int n=0;
					for (int i = 0; i < coordinate.length; i += 2){
						cArray[n++] = new DefaultCoordinate(coordinate[i], coordinate[(i + 1)]);
					}
					polygon = new DefaultPolygon(cArray);
				} else {
					Coordinate[] cArray = new Coordinate[coordinate.length/2 + 1];
					int n=0;
					for (int i = 0; i < coordinate.length; i += 2){
						cArray[n++] = new DefaultCoordinate(coordinate[i], coordinate[(i + 1)]);
					}
					cArray[n++] = new DefaultCoordinate(coordinate[0], coordinate[1]);
					polygon = new DefaultPolygon(cArray);
				}
		}
		return polygon;
	}
	
	private Point createPoint(JGeometry geom){
		double[] coordinate = geom.getMBR();
		if(coordinate==null || coordinate.length==0){
			coordinate = geom.getOrdinatesArray();
		}
		Point geometry = new DefaultPoint(coordinate[0],coordinate[1]);
		return geometry;
	}
	
	private static List<Coordinate> getHull(Coordinate c1, Coordinate c2, Coordinate c3) {
		List<Coordinate> hull = new ArrayList<Coordinate>();
		
		Coordinate[] coords = getCircle(c1, c2, c3);
		Coordinate newC1 = computeCoordinate(coords, c1);
		Coordinate newC2 = computeCoordinate(coords, c2);
		Coordinate newC3 = computeCoordinate(coords, c3);
		
		int index = 0;
		int i = 0;
		for (Coordinate c : coords) {
			if ((c.getX() == newC1.getX()) && (c.getY() == newC1.getY())) {
				index = i;
				break;
			}
			i++;
		}
		
		Coordinate[] newCoords = new Coordinate[coords.length];
		int n = 0;
		
		for (int k = index; k < coords.length; k++) {
			newCoords[(n++)] = coords[k];
		}
		for (int k = 0; k < index; k++) {
			newCoords[(n++)] = coords[k];
		}
		
		boolean second = false;
		boolean third = false;
		boolean first = false;
		for (Coordinate c : newCoords) {
			hull.add(c);
			
			if ((c.getX() == newC1.getX()) && (c.getY() == newC1.getY())) {
				first = true;
				if (third) {
					if (second) {
						return hull;
					}
					hull = new ArrayList<Coordinate>();
				}
			}
			
			if ((c.getX() == newC2.getX()) && (c.getY() == newC2.getY())) {
				second = true;
			}
			
			if ((c.getX() == newC3.getX()) && (c.getY() == newC3.getY())) {
				third = true;
				if (first) {
					if (second) {
						return hull;
					}
					hull = new ArrayList<Coordinate>();
				}
			}
		}
		
		return hull;
	}

	private static Coordinate computeCoordinate(Coordinate[] coords, Coordinate coord) {
		double dis = 100.0D;
		Coordinate tmp = null;
		Coordinate[] arrayOfCoordinate = coords; 
		int j = coords.length; 
		for (int i = 0; i < j; i++) {
			Coordinate c = arrayOfCoordinate[i];
			if (c.distance(coord) <= dis) {
				dis = c.distance(coord);
				tmp = c;
			}
		}
		return tmp;
	}
	  
	private static Coordinate[] getCircle(Coordinate c1, Coordinate c2, Coordinate c3) {
		com.vividsolutions.jts.geom.Coordinate jc1 = new com.vividsolutions.jts.geom.Coordinate(c1.getX(),c1.getY());
		com.vividsolutions.jts.geom.Coordinate jc2 = new com.vividsolutions.jts.geom.Coordinate(c1.getX(),c1.getY());
		com.vividsolutions.jts.geom.Coordinate jc3 = new com.vividsolutions.jts.geom.Coordinate(c1.getX(),c1.getY());
		
		com.vividsolutions.jts.geom.Coordinate center = Triangle.circumcentre(jc1, jc2, jc3);
		double dis = center.distance(jc1);
		com.vividsolutions.jts.geom.GeometryFactory fac = new com.vividsolutions.jts.geom.GeometryFactory();
		com.vividsolutions.jts.geom.Point pt = fac.createPoint(center);
		com.vividsolutions.jts.geom.Geometry geo = pt.buffer(dis);
		com.vividsolutions.jts.geom.Polygon pg = (com.vividsolutions.jts.geom.Polygon)geo;
		
		com.vividsolutions.jts.geom.Coordinate[] coords = pg.getCoordinates();
		
		Coordinate[] cs = new Coordinate[coords.length];
		int i=0;
		for(com.vividsolutions.jts.geom.Coordinate c:coords){
			cs[i++] = new DefaultCoordinate(c.x, c.y);
		}
		return cs;
	}
}
