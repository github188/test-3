package nari.FormatConversion.GeoJSONtoSHP;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class GeoJSONtoSHP {
	
	private Logger logger = LoggerManager.getLogger(this.getClass());
	
	public boolean geoJSONtoSHP(JSONObject GEOJSON, String filepath) {
		try {

			// 创建shape文件对象(filepath格式类似D:\\1.shp)
			File file = new File(filepath);
			Map<String, Serializable> params = new HashMap<String, Serializable>();
			params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
			// params.put("create spatial index", Boolean.TRUE);
			ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory()
					.createNewDataStore(params);
			// 定义图形信息和属性信息
			SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
			tb.setCRS(DefaultGeographicCRS.WGS84);
			tb.setName("shapefile");
			// SHP的geo特性类别必须一致
			JSONArray jsonArray = GEOJSON.getJSONArray("features");
			String type0 = jsonArray.getJSONObject(0).getJSONObject("geometry")
					.getString("type");
			for (int i = 0; i < jsonArray.size(); i++) {
				String type = jsonArray.getJSONObject(i).getJSONObject("geometry").getString("type");
				if (!(type0.equalsIgnoreCase(type))) {
					logger.info("该GEOJSON地理信息种类不一致，无法放入一个SHP格式中");
					return false;
				}
				if (type0.equalsIgnoreCase("point")) {
					tb.add("the_geom", Point.class);
				} else if (type0.equalsIgnoreCase("LineString")) {
					tb.add("the_geom", LineString.class);
				} else if (type0.equalsIgnoreCase("Polygon")) {
					tb.add("the_geom", Polygon.class);
				} else if (type0.equalsIgnoreCase("MultiPoint")) {
					tb.add("the_geom", MultiPoint.class);
				} else if (type0.equalsIgnoreCase("MultiLineString")) {
					tb.add("the_geom", MultiLineString.class);
				} else if (type0.equalsIgnoreCase("MultiPolygon")) {
					tb.add("the_geom", MultiPolygon.class);
				}
			}
			// 定义SHP所需要的其他属性
			JSONObject proper0 = jsonArray.getJSONObject(0).getJSONObject(
					"properties");
			int properNum = proper0.names().size();
			String[] properName = new String[properNum];
			for (int i = 0; i < properNum; i++) {
				properName[i] = proper0.names().getString(i);
				tb.add(properName[i], String.class);
			}
			ds.createSchema(tb.buildFeatureType());
			ds.setCharset(Charset.forName("GBK"));
			// 设置Writer
			FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds
					.getFeatureWriter(ds.getTypeNames()[0],
							Transaction.AUTO_COMMIT);

			for (int i = 0; i < jsonArray.size(); i++) {
				// 写下一条
				SimpleFeature feature = writer.next();
				JSONArray coordinate = jsonArray.getJSONObject(0)
						.getJSONObject("geometry").getJSONArray("coordinates");

				if (type0.equalsIgnoreCase("point")) {
					double[] coords = new double[coordinate.size()];
					for (int j = 0; j < coordinate.size(); j++) {
						coords[j] = coordinate.getDouble(j);
					}
					feature.setAttribute("the_geom", new GeometryFactory()
							.createPoint(new Coordinate(coords[0], coords[1])));
				} else if (type0.equalsIgnoreCase("LineString")) {
					int natesNum = coordinate.size();
					double[] doubles = new double[natesNum * 2];
					Coordinate[] nates = new Coordinate[natesNum];
					for (int j = 0; j < natesNum; j++) {
						doubles[2 * j] = coordinate.getJSONArray(j)
								.getDouble(0);
						doubles[2 * j + 1] = coordinate.getJSONArray(j)
								.getDouble(1);
						nates[j] = new Coordinate(doubles[2 * j],
								doubles[2 * j] + 1);
					}
					feature.setAttribute("the_geom",
							new GeometryFactory().createLineString(nates));
				} else if (type0.equalsIgnoreCase("Polygon")) {
					int natesNum = coordinate.getJSONArray(0).size();
					double[] doubles = new double[natesNum * 2];
					Coordinate[] nates = new Coordinate[natesNum];
					for (int j = 0; j < natesNum; j++) {
						doubles[2 * j] = coordinate.getJSONArray(0)
								.getJSONArray(j).getDouble(0);
						doubles[2 * j + 1] = coordinate.getJSONArray(0)
								.getJSONArray(j).getDouble(1);
						nates[j] = new Coordinate(doubles[2 * j],
								doubles[2 * j] + 1);
					}
					feature.setAttribute("the_geom", new GeometryFactory()
							.createPolygon(new GeometryFactory()
									.createLinearRing(nates), null));
				} else if (type0.equalsIgnoreCase("MultiPoint")) {
					int natesNum = coordinate.size();
					double[] doubles = new double[natesNum * 2];
					Coordinate[] nates = new Coordinate[natesNum];
					for (int j = 0; j < natesNum; j++) {
						doubles[2 * j] = coordinate.getJSONArray(j)
								.getDouble(0);
						doubles[2 * j + 1] = coordinate.getJSONArray(j)
								.getDouble(1);
						nates[j] = new Coordinate(doubles[2 * j],
								doubles[2 * j] + 1);
					}
					feature.setAttribute("the_geom",
							new GeometryFactory().createMultiPoint(nates));
				} else if (type0.equalsIgnoreCase("MultiLineString")) {
					int lineNum = coordinate.size();
					LineString[] lineStrings = new LineString[lineNum];
					for (int j = 0; j < lineNum; i++) {
						int natesNum = coordinate.getJSONArray(j).size();
						double[] doubles = new double[natesNum * 2];
						Coordinate[] nates = new Coordinate[natesNum];
						for (int k = 0; k < natesNum; k++) {
							doubles[2 * j] = coordinate.getJSONArray(j)
									.getJSONArray(k).getDouble(0);
							doubles[2 * j + 1] = coordinate.getJSONArray(j)
									.getJSONArray(k).getDouble(1);
							nates[j] = new Coordinate(doubles[2 * j],
									doubles[2 * j] + 1);
						}
						lineStrings[j] = new GeometryFactory()
								.createLineString(nates);
					}
					feature.setAttribute("the_geom", new GeometryFactory()
							.createMultiLineString(lineStrings));
				} else if (type0.equalsIgnoreCase("MultiPolygon")) {
					int polygonNum = coordinate.size();
					Polygon[] polygons = new Polygon[polygonNum];
					for (int j = 0; j < polygonNum; i++) {
						int natesNum = coordinate.getJSONArray(0)
								.getJSONArray(j).size();
						double[] doubles = new double[natesNum * 2];
						Coordinate[] nates = new Coordinate[natesNum];
						for (int k = 0; k < natesNum; k++) {
							doubles[2 * j] = coordinate.getJSONArray(0)
									.getJSONArray(j).getJSONArray(k)
									.getDouble(0);
							doubles[2 * j + 1] = coordinate.getJSONArray(0)
									.getJSONArray(j).getJSONArray(k)
									.getDouble(1);
							nates[j] = new Coordinate(doubles[2 * j],
									doubles[2 * j] + 1);
						}
						polygons[j] = new GeometryFactory().createPolygon(
								new GeometryFactory().createLinearRing(nates),
								null);
					}
					feature.setAttribute("the_geom",
							new GeometryFactory().createMultiPolygon(polygons));
				}
				// 得到属性特性,将其放入shp中
				JSONObject proper = jsonArray.getJSONObject(i).getJSONObject(
						"properties");
				for (int j = 0; j < properNum; j++) {
					// 为每个非geo属性赋值
					feature.setAttribute(properName[i],
							proper.getString(properName[i]));
				}
			}
			writer.write();
			writer.close();
			ds.dispose();

			// //读取刚写完shape文件的图形信息
			// ShpFiles shpFiles = new ShpFiles(filepath);
			// ShapefileReader reader = new ShapefileReader(shpFiles, false,
			// true, new GeometryFactory(), false);
			// try {
			// while (reader.hasNext()) {
			// logger.info(reader.nextRecord().shape());
			// }
			// } finally { 
			// reader.close();
			// }
		} catch (Exception e) {
		}
		logger.info("转换成功");
		return true;
	}

}
