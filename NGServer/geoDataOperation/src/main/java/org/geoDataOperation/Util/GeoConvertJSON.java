package org.geoDataOperation.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geoDataOperation.bean.GeometryMessage;
import org.geoDataOperation.bean.GeometryProperty;
import org.geotools.geojson.geom.GeometryJSON;
import org.json.JSONArray;
import org.json.JSONObject;

import com.vividsolutions.jts.geom.Geometry;


public class GeoConvertJSON {
	
	public String srid = "urn:ogc:def:crs:OGC:1.3:CRS84";
	
	public String getSrid() {
		return srid;
	}

	public void setSrid(String srid) {
		this.srid = srid;
	}

	public List<GeometryMessage> GeoJson2Geometry(String geoJSONString){
		List<GeometryMessage> GeometryMessageList = new ArrayList<GeometryMessage>();
		
		JSONObject geojson = new JSONObject(geoJSONString);
		JSONArray features = geojson.getJSONArray("features");
		int featuresCount = features.length();
		for(int i=0;i<featuresCount;i++){
			GeometryMessage geometryMessage = new GeometryMessage();
			JSONObject featureJSON = features.getJSONObject(i);
			
			//属性的装载
			JSONObject propertiesJSON = featureJSON.getJSONObject("properties");
			List<GeometryProperty> propertitesList = new ArrayList<GeometryProperty>();
			Iterator<String> propertitesKey = propertiesJSON.keys();
			while(propertitesKey.hasNext()){
				GeometryProperty propertity = new GeometryProperty();
				String properName = propertitesKey.next();
				propertity.setPropertyName(properName);
				
				String value = "";
				Object valueObject = propertiesJSON.get(properName);
				value = String.valueOf(valueObject);	//若为jasonArray也可以转
				propertity.setPropertyValue(value);
				
				propertitesList.add(propertity);
			}	//每一个属性都装载完毕
			GeometryProperty[] propertites = new GeometryProperty[propertitesList.size()];
			propertites = propertitesList.toArray(propertites);
			geometryMessage.setPropertites(propertites);
			
			//geometry字段的装载
			JSONObject geometryJSON = featureJSON.getJSONObject("geometry");
			GeometryJSON geomjson = new GeometryJSON();
			try {
				Geometry geometry = geomjson.read(geometryJSON.toString());
				geometryMessage.setGeometry(geometry);
			} catch (IOException e) {
				System.out.println("######## wrongOccurred when GeoJson2Geometry() #########");
				e.printStackTrace();
			}
			
			GeometryMessageList.add(geometryMessage);
		}	//所有features数据组装完成
		return GeometryMessageList;
	}
	
	public String Geometry2GeoJson(List<GeometryMessage> GeometryMessageList){
		String geoJsonString = "";
		
		//总体geojson
		JSONObject geojson = new JSONObject();
		
		//设置头
		geojson.put("type", "FeatureCollection");
		JSONObject crsjson = new JSONObject();
		crsjson.put("type", "name");
		crsjson.put("propertites", new JSONObject().put("name", srid));
		geojson.put("crs", crsjson);
		
		//设置主要数据
		JSONArray featuresJsonArray = new JSONArray();
		int featuresCount = GeometryMessageList.size();
		for(int i= 0;i<featuresCount;i++){	//每一条数据
			JSONObject featureJSON = new JSONObject();
			featureJSON.put("type", "Feature");
			GeometryMessage geometryMessage = GeometryMessageList.get(i);
			
			//装属性
			GeometryProperty[] properties = geometryMessage.getPropertites();
			JSONObject propertitesJSON = loadPro2JSONSimply(properties);
			featureJSON.put("properties", propertitesJSON);
			
			//装地理信息
			GeometryJSON geomJson = new GeometryJSON();
			Geometry geom = geometryMessage.getGeometry();
			JSONObject geometryJSONObject = new JSONObject(geomJson.toString(geom));
			featureJSON.put("geometry", geometryJSONObject);
			
			//添加至集合
			featuresJsonArray.put(featureJSON);
		}	//所有主要数据装载完成
		geojson.put("features", featuresJsonArray);
		
		geoJsonString = geojson.toString();
		return geoJsonString;
	}
	
	/**
	 * 简便地以name-value地方法装属性
	 * @param properties
	 * @return JSONObject
	 */
	private JSONObject loadPro2JSONSimply(GeometryProperty[] properties){
		JSONObject propertitesJSON = new JSONObject();
		for(GeometryProperty property : properties){
			String name = property.getPropertyName();
			String value = property.getPropertyValue();
			propertitesJSON.put(name, value);
		}
		return propertitesJSON;
	}
	
	
	
	public static void main(String[] args) {
		String testString = "{haha:123,hehe:\"0\",heihei:[4,5,6]}";
		JSONObject json = new JSONObject(testString);
		
//		GeometryFactory fac = new GeometryFactory();
//		Geometry geom = fac.createPoint(new Coordinate(100, 25)).buffer(0.2);
//		GeometryJSON geomJson = new GeometryJSON();
//		String geomStr = geomJson.toString(geom);
//		System.out.println(geomStr);
		
	}
	
	
}
