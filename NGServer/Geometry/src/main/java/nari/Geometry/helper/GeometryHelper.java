package nari.Geometry.helper;

import java.util.Arrays;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.simplify.DouglasPeuckerLineSimplifier;


//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import com.vividsolutions.jts.geom.Triangle;
//
//import nari.Geometry.Coordinate;
//import nari.Geometry.CoordinateSequence;
//import nari.Geometry.DefaultCoordinate;
//import nari.Geometry.DefaultCoordinateSequence;
//import nari.Geometry.DefaultMultiPolyline;
//import nari.Geometry.DefaultPoint;
//import nari.Geometry.DefaultPolygon;
//import nari.Geometry.DefaultPolyline;
//import nari.Geometry.Geometry;
//import nari.Geometry.GeometryBuilder;
//import nari.Geometry.Point;
//import nari.Geometry.Polygon;
//import nari.Geometry.Polyline;
//
//import oracle.spatial.geometry.JGeometry;
//import oracle.sql.STRUCT;

public class GeometryHelper {
	
//	public static Geometry buildGeometry(int type,int[] ele,double[] coordinates) {
//		int interpretation = 1;
//		if (ele != null && type!=1) {
//			interpretation = ele[2];
//		}
//		
//		Geometry geometry = null;
//		switch (type) {
//			case 1:
//				geometry = createPoint(coordinates);
//				break;
//			case 2:
//				geometry = createPolyline(coordinates, interpretation);
//				break;
//			case 3:
//				geometry = createPolygon(coordinates, interpretation);
//				break;
//			case 6:
//				int[] eles = ele;
//				double[] original = coordinates;
//				int[] offset = new int[eles.length / 3];
//				int k = 0;
//				for (int i = 0; i < eles.length; i += 3) {
//					offset[(k++)] = eles[i];
//				}
//	
//				Polyline[] polyline = new Polyline[offset.length];
//				int n = 0;
//				double[] lingRing = null;
//				for (int of = 0; of < offset.length - 1; of++) {
//					lingRing = Arrays.copyOfRange(original, offset[of] - 1, offset[(of + 1)] - 1);
//					CoordinateSequence seq = new DefaultCoordinateSequence(lingRing);
//					polyline[n++] = new DefaultPolyline(seq);
//				}
//				
//				lingRing = Arrays.copyOfRange(original, offset[(offset.length - 1)] - 1, original.length);
//				
//				CoordinateSequence seq = new DefaultCoordinateSequence(lingRing);
//				polyline[n++] = new DefaultPolyline(seq);
//				geometry = new DefaultMultiPolyline(polyline);
//			default:
//				break;
//		}
//		return geometry;
//	}
	
//	public static Geometry buildGeometry(Object geomObj) {
//		JGeometry geom = readGeometry(geomObj);
//		if(geom==null){
//			return null;
//		}
//		
//		Geometry geometry = GeometryBuilder.getBuilder().buildGeometry(geom);
//		int type = geom.getType();
//		int[] ele = geom.getElemInfo();
//
//		int interpretation = 1;
//		if (ele != null) {
//			interpretation = ele[2];
//		}
//
//		Geometry geometry = null;
//		switch (type) {
//			case 1:
//				geometry = createPoint(geom.getMBR());
//				break;
//			case 2:
//				geometry = createPolyline(geom.getOrdinatesArray(), interpretation);
//				break;
//			case 3:
//				geometry = createPolygon(geom.getOrdinatesArray(), interpretation);
//				break;
//			case 6:
//				int[] eles = geom.getElemInfo();
//				double[] original = geom.getOrdinatesArray();
//				int[] offset = new int[eles.length / 3];
//				int k = 0;
//				for (int i = 0; i < eles.length; i += 3) {
//					offset[(k++)] = eles[i];
//				}
//	
//				Polyline[] polyline = new Polyline[offset.length];
//				int n = 0;
//				double[] lingRing = null;
//				for (int of = 0; of < offset.length - 1; of++) {
//					lingRing = Arrays.copyOfRange(original, offset[of] - 1, offset[(of + 1)] - 1);
//					CoordinateSequence seq = new DefaultCoordinateSequence(lingRing);
//					polyline[n++] = new DefaultPolyline(seq);
//				}
//				
//				lingRing = Arrays.copyOfRange(original, offset[(offset.length - 1)] - 1, original.length);
//				
//				CoordinateSequence seq = new DefaultCoordinateSequence(lingRing);
//				polyline[n++] = new DefaultPolyline(seq);
//				geometry = new DefaultMultiPolyline(polyline);
//			default:
//				break;
//		}
//		return geometry;
//	}
	
//	public static JGeometry readGeometry(Object geoObject){
//		STRUCT struct = null;
//		Method method = null;
//		JGeometry geom = null;
//		if ("weblogic.jdbc.wrapper.Struct_oracle_sql_STRUCT".equals(geoObject.getClass().getName())){
//			try {
//				method = geoObject.getClass().getMethod("getVendorObj", new Class[0]);
//				struct = (STRUCT)method.invoke(geoObject, new Object[0]);
//			} catch (SecurityException e) {
//				e.printStackTrace();
//			} catch (NoSuchMethodException e) {
//				e.printStackTrace();
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			} catch (InvocationTargetException e) {
//				e.printStackTrace();
//			}
//		} else {
//			struct = (STRUCT)geoObject;
//		}
//		try {
//			geom = JGeometry.load(struct);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return geom;
//	}
	
//	private static Polyline createPolyline(double[] coordinate,int interpretation){
//		Polyline polyline = null;
//		if (interpretation == 2) {
//			Coordinate[] source = new Coordinate[coordinate.length / 2];
//			int m = 0;
//			for (int n = 0; n < coordinate.length; n += 2) {
//				source[m++] = new DefaultCoordinate(coordinate[n], coordinate[(n + 1)]);
//			}
//
//			List<Coordinate> hull = new ArrayList<Coordinate>();
//			List<Coordinate> coords = new ArrayList<Coordinate>();
//			int k = 0;
//			int tt = (source.length - 3) / 2;
//			for (int i = 0; i < source.length + tt; i++) {
//				if ((i % 3 == 0) && (i > 2)) {
//					k--;
//				}
//				coords.add(source[k]);
//				k++;
//			}
//
//			Coordinate c1 = null;
//			Coordinate c2 = null;
//			Coordinate c3 = null;
//
//			for (int s = 0; s < coords.size(); s++) {
//				if (((s + 1) / 3 == 0) && (s >= 2)) {
//					c1 = new DefaultCoordinate(coords.get(s - 2).getX(), coords.get(s - 2).getY());
//					c2 = new DefaultCoordinate(coords.get(s - 1).getX(), coords.get(s - 1).getY());
//					c3 = new DefaultCoordinate(coords.get(s).getX(), coords.get(s).getY());
//					hull.addAll(getHull(c1, c2, c3));
//				}
//			}
//			Coordinate[] coordArray = new Coordinate[hull.size()];
//			coordArray = hull.toArray(coordArray);
//			polyline = new DefaultPolyline(coordArray);
//		} else {
//			CoordinateSequence seq = new DefaultCoordinateSequence(coordinate);
//			polyline = new DefaultPolyline(seq);
//		}
//		return polyline;
//	}
//	
//	private static Polygon createPolygon(double[] coordinate,int interpretation){
//		Polygon polygon = null;
//		switch (interpretation) {
//			case 2:
//				Coordinate[] source = new Coordinate[coordinate.length / 2];
//				int m = 0;
//				for (int n = 0; n < coordinate.length; n += 2) {
//					source[m++] = new DefaultCoordinate(coordinate[n],coordinate[(n + 1)]);
//				}
//
//				List<Coordinate> hull = new ArrayList<Coordinate>();
//				List<Coordinate> coords = new ArrayList<Coordinate>();
//				int k = 0;
//				int tt = (source.length - 3) / 2;
//				for (int i = 0; i < source.length + tt; i++) {
//					if ((i % 3 == 0) && (i > 2)) {
//						k--;
//					}
//					coords.add(source[k]);
//					k++;
//				}
//
//				Coordinate c1 = null;
//				Coordinate c2 = null;
//				Coordinate c3 = null;
//
//				for (int s = 0; s < coords.size(); s++) {
//					if (((s + 1) % 3 == 0) && (s >= 2)) {
//						c1 = new DefaultCoordinate(coords.get(s - 2).getX(),coords.get(s - 2).getY());
//						c2 = new DefaultCoordinate(coords.get(s - 1).getX(),coords.get(s - 1).getY());
//						c3 = new DefaultCoordinate(coords.get(s).getX(),coords.get(s).getY());
//						hull.addAll(getHull(c1, c2, c3));
//					}
//				}
//				hull.add(hull.get(0));
//				Coordinate[] coordArray = new Coordinate[hull.size()];
//				coordArray = hull.toArray(coordArray);
//				polygon = new DefaultPolygon(coordArray);
//				break;
//			case 3:
//				Coordinate leftBottom = new DefaultCoordinate(coordinate[0],coordinate[1]);
//				Coordinate rightTop = new DefaultCoordinate(coordinate[2],coordinate[3]);
//				double xmin = leftBottom.getX();
//				double ymin = rightTop.getY();
//				double xmax = rightTop.getX();
//				double ymax = leftBottom.getY();
//				double[] c = new double[]{xmax, ymax, xmax, ymin, xmin, ymin, xmin, ymax, xmax, ymax};
//				CoordinateSequence seq = new DefaultCoordinateSequence(c);
//				polygon = new DefaultPolygon(seq);
//				break;
//			case 4:
//				Coordinate rc1 = new DefaultCoordinate(coordinate[0], coordinate[1]);
//				Coordinate rc2 = new DefaultCoordinate(coordinate[2], coordinate[3]);
//				Coordinate rc3 = new DefaultCoordinate(coordinate[4], coordinate[5]);
//				Coordinate[] rcoords = getCircle(rc1, rc2, rc3);
//				polygon = new DefaultPolygon(rcoords);
//				break;
//			default:
//				if (coordinate.length == 2) {
//					double[] cc = new double[]{coordinate[0], coordinate[1], coordinate[2], coordinate[1], coordinate[2], coordinate[3], coordinate[0], coordinate[3], coordinate[0], coordinate[1]};
//					CoordinateSequence cseq = new DefaultCoordinateSequence(cc);
//					polygon = new DefaultPolygon(cseq);
//				} else if ((coordinate[0] == coordinate[(coordinate.length - 2)]) && (coordinate[1] == coordinate[(coordinate.length - 1)])) {
//					Coordinate[] cArray = new Coordinate[coordinate.length/2];
//					int n=0;
//					for (int i = 0; i < coordinate.length; i += 2){
//						cArray[n++] = new DefaultCoordinate(coordinate[i], coordinate[(i + 1)]);
//					}
//					polygon = new DefaultPolygon(cArray);
//				} else {
//					Coordinate[] cArray = new Coordinate[coordinate.length/2 + 1];
//					int n=0;
//					for (int i = 0; i < coordinate.length; i += 2){
//						cArray[n++] = new DefaultCoordinate(coordinate[i], coordinate[(i + 1)]);
//					}
//					cArray[n++] = new DefaultCoordinate(coordinate[0], coordinate[1]);
//					polygon = new DefaultPolygon(cArray);
//				}
//		}
//		return polygon;
//	}
//	
//	private static Point createPoint(double[] coordinate){
//		Point geometry = new DefaultPoint(coordinate[0],coordinate[1]);
//		return geometry;
//	}
//	
//	private static List<Coordinate> getHull(Coordinate c1, Coordinate c2, Coordinate c3) {
//		List<Coordinate> hull = new ArrayList<Coordinate>();
//		
//		Coordinate[] coords = getCircle(c1, c2, c3);
//		Coordinate newC1 = computeCoordinate(coords, c1);
//		Coordinate newC2 = computeCoordinate(coords, c2);
//		Coordinate newC3 = computeCoordinate(coords, c3);
//		
//		int index = 0;
//		int i = 0;
//		for (Coordinate c : coords) {
//			if ((c.getX() == newC1.getX()) && (c.getY() == newC1.getY())) {
//				index = i;
//				break;
//			}
//			i++;
//		}
//		
//		Coordinate[] newCoords = new Coordinate[coords.length];
//		int n = 0;
//		
//		for (int k = index; k < coords.length; k++) {
//			newCoords[(n++)] = coords[k];
//		}
//		for (int k = 0; k < index; k++) {
//			newCoords[(n++)] = coords[k];
//		}
//		
//		boolean second = false;
//		boolean third = false;
//		boolean first = false;
//		for (Coordinate c : newCoords) {
//			hull.add(c);
//			
//			if ((c.getX() == newC1.getX()) && (c.getY() == newC1.getY())) {
//				first = true;
//				if (third) {
//					if (second) {
//						return hull;
//					}
//					hull = new ArrayList<Coordinate>();
//				}
//			}
//			
//			if ((c.getX() == newC2.getX()) && (c.getY() == newC2.getY())) {
//				second = true;
//			}
//			
//			if ((c.getX() == newC3.getX()) && (c.getY() == newC3.getY())) {
//				third = true;
//				if (first) {
//					if (second) {
//						return hull;
//					}
//					hull = new ArrayList<Coordinate>();
//				}
//			}
//		}
//		
//		return hull;
//	}
//	
//	private static Coordinate computeCoordinate(Coordinate[] coords, Coordinate coord) {
//		double dis = 100.0D;
//		Coordinate tmp = null;
//		Coordinate[] arrayOfCoordinate = coords; 
//		int j = coords.length; 
//		for (int i = 0; i < j; i++) {
//			Coordinate c = arrayOfCoordinate[i];
//			if (c.distance(coord) <= dis) {
//				dis = c.distance(coord);
//				tmp = c;
//			}
//		}
//		return tmp;
//	}
//	  
//	private static Coordinate[] getCircle(Coordinate c1, Coordinate c2, Coordinate c3) {
//		com.vividsolutions.jts.geom.Coordinate jc1 = new com.vividsolutions.jts.geom.Coordinate(c1.getX(),c1.getY());
//		com.vividsolutions.jts.geom.Coordinate jc2 = new com.vividsolutions.jts.geom.Coordinate(c1.getX(),c1.getY());
//		com.vividsolutions.jts.geom.Coordinate jc3 = new com.vividsolutions.jts.geom.Coordinate(c1.getX(),c1.getY());
//		
//		com.vividsolutions.jts.geom.Coordinate center = Triangle.circumcentre(jc1, jc2, jc3);
//		double dis = center.distance(jc1);
//		com.vividsolutions.jts.geom.GeometryFactory fac = new com.vividsolutions.jts.geom.GeometryFactory();
//		com.vividsolutions.jts.geom.Point pt = fac.createPoint(center);
//		com.vividsolutions.jts.geom.Geometry geo = pt.buffer(dis);
//		com.vividsolutions.jts.geom.Polygon pg = (com.vividsolutions.jts.geom.Polygon)geo;
//		
//		com.vividsolutions.jts.geom.Coordinate[] coords = pg.getCoordinates();
//		
//		Coordinate[] cs = new Coordinate[coords.length];
//		int i=0;
//		for(com.vividsolutions.jts.geom.Coordinate c:coords){
//			cs[i++] = new DefaultCoordinate(c.x, c.y);
//		}
//		return cs;
//	}
	public static void main(String[] args) {
		
		String str = "120.06776,33.28411,120.06897,33.28129,120.06907,33.27869,120.0692,33.2753,120.0696,33.2723,120.0699,33.26864,120.0701,33.26489,120.0704,33.261,120.0676,33.25867,120.0647,33.25632,120.0614,33.25363,120.0582,33.251,120.05828,33.24713,120.05836,33.24299,120.05842,33.24033,120.0585,33.2361,120.05858,33.23232,120.05866,33.22818,120.05874,33.22445,120.05882,33.22063,120.05889,33.21707,120.059,33.21308,120.05906,33.20884,120.05913,33.2051,120.0592,33.20177,120.05926,33.19865,120.05934,33.19451,120.05942,33.19088,120.0595,33.18711,120.05958,33.18295,120.05952,33.18118,120.062,33.1745,120.06248,33.17157,120.06115,33.16748,120.05978,33.163,120.05853,33.15865,120.0576,33.15517,120.0563,33.15142,120.05532,33.14813,120.0543,33.1451,120.05553,33.14242,120.0572,33.1394,120.05922,33.13577,120.06088,33.13225,120.06272,33.1289,120.06413,33.1261,120.06415,33.12293,120.06405,33.119,120.06402,33.11525,120.06387,33.11165,120.0639,33.10872,120.06373,33.10447,120.06353,33.10192,120.06452,33.09762,120.06448,33.09518,120.0656,33.09177,120.06613,33.08855,120.06685,33.08463,120.06777,33.07982,120.06835,33.07552,120.06947,33.07217,120.06993,33.0692,120.07045,33.06508,120.0711,33.06122,120.07165,33.05803,120.07141,33.05469,120.06862,33.05112,120.06712,33.04742,120.06745,33.04403,120.06754,33.03984,120.06786,33.03551,120.06853,33.031,120.06887,33.02634,120.06961,33.02279,120.07014,33.01891,120.07077,33.0154,120.07165,33.01161,120.07257,33.00732,120.0728,33.00387,120.07257,33.00127,120.07225,32.9977,120.07198,32.99392,120.07155,32.99043,120.07147,32.9865,120.07108,32.98235,120.07078,32.97843,120.07058,32.97442,120.07032,32.97162,120.07002,32.96817,120.0696,32.96527,120.0691,32.96183,120.06965,32.95853,120.069,32.95427,120.06858,32.95097,120.06815,32.94658,120.06808,32.94355,120.06773,32.93853,120.06747,32.93473,120.06712,32.93052,120.06672,32.92595,120.06645,32.92113,120.06608,32.9173,120.06657,32.91392,120.06717,32.91038,120.06768,32.90635,120.06815,32.90247,120.06882,32.89863,120.06873,32.89487,120.0686,32.89132,120.06843,32.88818,120.06837,32.88423,120.06835,32.88103,120.06815,32.87712,120.06813,32.8725,120.06802,32.86878,120.0679,32.8645,120.06772,32.86055,120.0679,32.85643,120.06792,32.85215,120.06742,32.84797,120.06727,32.84407,120.06712,32.84002,120.06825,32.83628,120.06915,32.83273,120.07128,32.82778,120.07152,32.82513,120.07208,32.82095,120.07365,32.8174,120.07467,32.81335,120.07552,32.81013,120.07637,32.8075,120.07832,32.80508,120.07842,32.80098,120.07963,32.79665,120.0808,32.79258,120.08235,32.78812,120.08358,32.78447,120.08328,32.78048,120.08221,32.7767,120.08235,32.77272,120.08227,32.7693,120.08208,32.76542,120.08188,32.7617,120.0816,32.7582,120.08153,32.75457,120.08132,32.75088,120.08117,32.74772,120.08193,32.74323,120.08072,32.73882,120.08052,32.73483,120.08018,32.73113,120.08003,32.72693,120.07988,32.72288,120.07965,32.71963,120.07962,32.71667,120.07815,32.71368,120.07655,32.71067,120.07452,32.70688,120.07262,32.70302,120.07023,32.69862,120.0688,32.69573,120.06685,32.69207,120.06558,32.68963,120.06422,32.6869,120.06427,32.68288,120.0628,32.67947,120.06203,32.67552,120.06123,32.67142,120.06047,32.66752,120.05977,32.66403,120.05927,32.66063,120.0584,32.65702,120.05772,32.6536,120.05702,32.64928,120.05598,32.64553,120.05663,32.64248,120.05698,32.6396,120.05763,32.635,120.05805,32.63188,120.05868,32.62718,120.05922,32.6229,120.05963,32.62002,120.06,32.61683,120.06007,32.61288,120.0602,32.61008,120.06083,32.6068,120.06087,32.60558,120.05823,32.60403,120.05418,32.6024,120.05008,32.60085,120.04786,32.60093,120.04296,32.60114,120.03935,32.60125,120.03548,32.60142,120.0307,32.60167,120.02559,32.60208,120.02045,32.60209,120.0156,32.60218,120.01096,32.60246,120.01058,32.60399,120.00995,32.60785,120.0667,33.2875,120.06744,33.28545";
		String[] coords = str.split(",");  
		long s = System.currentTimeMillis();
		Coordinate[] pts = new Coordinate[coords.length/2];
		int i=0;
		for(int k=0;k<coords.length;k=k+2){
			pts[i] = new Coordinate(Double.parseDouble(coords[k]),Double.parseDouble(coords[k+1]));
			i++;
		}
		System.out.println(Arrays.toString(pts));
		Coordinate[] results = DouglasPeuckerLineSimplifier.simplify(pts, 0.0001);
		System.out.println(Arrays.toString(results));
		StringBuffer b = new StringBuffer();
		b.append("[");
		for(Coordinate c:results){
			b.append("[").append(c.x+0.1).append(",").append(c.y).append("]").append(",");
		}
		b.append("]");
		System.out.println(b.toString());
		long e = System.currentTimeMillis();
		
		System.out.println(e-s);
	}
}
