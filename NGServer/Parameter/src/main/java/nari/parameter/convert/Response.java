package nari.parameter.convert;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.Xml.impl.ConfigSearchService;
import nari.Xml.interfaces.ConfigSearch;
import nari.parameter.bean.GeometryPair;
import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.SymbolPair;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class Response {
	
	private static Logger logger = LoggerManager.getLogger(Response.class);
	
	public static String convert(Object resp) {
		JSONObject obj = JSONObject.fromObject(resp);
		return obj.toString();
	}

	public static String convert(Object resp, JsonConfig config) {
		JSONObject obj = JSONObject.fromObject(resp, config);
		return obj.toString();
	}

	// 变成geojason格式字符串(有详细信息)
	public static String forDetailFeildGJson(QueryResult[] respResult) {
		if (respResult == null || respResult.length == 0) {
			logger.info("查无数据");
			return "查无数据";
		}
		
		long a = System.currentTimeMillis();
		// JSONObject obj = JSONObject.fromObject(resp);
		String strgeojson = "";
		
/*************************************************************************String 方法开始*************************************************************************/
		String head = "{\"type\":\"FeatureCollection\","
				+ "\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"urn:ogc:def:crs:OGC:1.3:CRS84\"}},"
				+ "\"features\":[";
		
		//每一条数据
		String bodyHead = "{\"type\":\"Feature\"";
		List<String> bodyList = new ArrayList<String>();
		String bodyTail = "}";
		
		String tail = "]}";
		
		int classCount = respResult.length;
		String properBody = ",\"properties\":{\"psrDef\":\"" + null + "\",\"DEVICETYPEID\":\"" + null + "\",\"DEVICESTATE\":\""
				+ null + "\"}";
		
		String geometryBody = ",\"geometry\":{\"type\":\"" + null
				+ "\",\"coordinates\":\"" + null + "\"}";

		for (int i = 0; i < classCount; i++) {
			// 得到每个classId对应的数据
			QueryResult eachResult = respResult[i];
			if (eachResult == null || eachResult.getRecords() == null) {
				continue;
			}
			JSONObject psrDefJSONOb = JSONObject.fromObject(eachResult
					.getPsrDef());
			int recordCount = eachResult.getRecords().length;
			QueryRecord[] record = eachResult.getRecords();

			for (int j = 0; j < recordCount; j++) {
				// 得到每条记录对应数据
				QueryRecord eachRecord = record[j];
				if (eachRecord == null) {
					continue;
				}
				
				QueryField[] fields = eachRecord.getFields();
				
				StringBuffer fieldBody = new StringBuffer();
				JSONArray fieldsJSONAr = null;
				if (fields == null) {
					fieldBody.append("");
				} else {
					fieldsJSONAr = JSONArray.fromObject(fields);
					// 返回字段-值信息
					int fieldsNum = fields.length;
					String fieldName = "";
					String fieldValue = "";
					for (int k = 0; k < fieldsNum; k++) {
						if (fields[k].getFieldName() != null) {
							fieldName = fields[k].getFieldName().toString();
						}
						if (fields[k].getFieldValue() != null) {
							fieldValue = fields[k].getFieldValue().toString();
						}

						// 最外层只返回"OID","SBMC","符号角度",“符号大小”,"设备子类型"
						// if(!(fieldName.equalsIgnoreCase("OID")||fieldName.equalsIgnoreCase("SBMC")||fieldName.equalsIgnoreCase("FHJD")||fieldName.equalsIgnoreCase("FHDX")
						// ||fieldName.equalsIgnoreCase("SBZLX")||fieldName.equalsIgnoreCase("DYDJ")||fieldName.equalsIgnoreCase("SBID")
						// ||fieldName.equalsIgnoreCase("SSXL"))){
						// continue;
						// }
						fieldBody.append(",\"" + fieldName + "\":\""
								+ fieldValue + "\"");
					}
				}

				// 返回显示符号所需symbol属性值
				String devtypeId = null;
				String symbolId = null;
				SymbolPair symbol = eachRecord.getSymbol();
				if (symbol != null) {
					symbolId = symbol.getSymbolId().toString();
//					String HdevtypeId = symbol.getDevtypeId().toString();
//					if (HdevtypeId.isEmpty()) {
//						devtypeId = "";
//					} else {
//						devtypeId = String.valueOf(Integer.parseInt(
//								HdevtypeId.substring(2), 16));
//					}
				}

				// 返回GEOJSON的properties属性信息
				properBody = ",\"properties\":{\"psrDef\":" + psrDefJSONOb
						+ ",\"fields\":" + fieldsJSONAr + fieldBody.toString()
						+ ",\"DEVICESTATE\":\"" + symbolId + "\"}";

				GeometryPair geom = eachRecord.getGeom();
				// geomtry不能为空
				if (geom == null) {
					String type = null;
					String coordinates = null;
					geometryBody = ",\"geometry\":{\"type\":" + type
							+ ",\"coordinates\":\"" + coordinates + "\"}";
				} else {
					String type = geom.getGeometryType();
					double[] coords = geom.getCoords();
					// 坐标数未偶数，可得到坐标点个数
					int natesCount = (coords.length) / 2;
					String[] nates = new String[natesCount];
					// 得到坐标组合成点
					for (int k = 0; k < natesCount; k++) {
//						String xCoords = String.valueOf(coords[(2 * k)]);
//						String yCoords = String.valueOf(coords[(2 * k + 1)]);
						 //对于偏移量+0.002,+0.003作还原
						 String xCoords = String.valueOf(coords[(2*k)]);
						 String yCoords = String.valueOf(coords[(2 * k + 1)]);
						nates[k] = "[" + xCoords + "," + yCoords + "]";
					}
					// StringBuffer coordinates = new StringBuffer();
					String coordinates = "";
					// 变成标准的GEOJSON形状类型
					if ("point".equalsIgnoreCase(type)) {
						type = "Point";
						// coordinates.append(nates[0]);
						coordinates = nates[0];
					} else if ("LineString".equalsIgnoreCase(type)) {
						type = "LineString";
						// coordinates.append(nates[0]);
						coordinates = nates[0];
						for (int k = 1; k < natesCount; k++) {
							// coordinates.append(coordinates+","+nates[k]);
							coordinates = coordinates + "," + nates[k];
						}
						// coordinates.append("["+coordinates+"]");
						coordinates = "[" + coordinates + "]";
					} else if ("MULTIPOINT".equalsIgnoreCase(type)) {
						type = "MultiPoint";
						// coordinates.append(nates[0]);
						coordinates = nates[0];
						for (int k = 1; k < natesCount; k++) {
							// coordinates.append(coordinates+","+nates[k]);
							coordinates = coordinates + "," + nates[k];
						}
						// coordinates.append("["+coordinates+"]");
						coordinates = "[" + coordinates + "]";
					} else if ("POLYGON".equalsIgnoreCase(type)) {
						type = "Polygon";
						// coordinates.append(nates[0]);
						coordinates = nates[0];
						for (int k = 1; k < natesCount; k++) {
							// coordinates.append(coordinates+","+nates[k]);
							coordinates = coordinates + "," + nates[k];
						}
						// coordinates.append("[["+coordinates+"]]");
						coordinates = "[[" + coordinates + "]]";
					} else if ("MultiLineString".equalsIgnoreCase(type)) {
						type = "MultiLineString";
						int other = (int) (geom.getOther());
						int[] startDouble = geom.getStartDouble();
						if (other != startDouble.length) { // 若有头
							int[] OldStartDouble = startDouble.clone();
							// 去头
							startDouble = new int[OldStartDouble.length - 1];
							for (int k = 0; k < startDouble.length; k++) {
								startDouble[k] = OldStartDouble[k + 1];
							}
						}
						int LineCount = startDouble.length;
						StringBuffer[] singleLine = new StringBuffer[LineCount];
						// 对每一个lineString进行组装
						int[] StartNatesNum = new int[LineCount];
						for (int k = 0; k < LineCount; k++) {
							singleLine[k] = new StringBuffer();
							StartNatesNum[k] = (startDouble[k] + 1) / 2; // 开始的点若为1,即nates[0]
							if (k == 0) {
								continue;
							} else {
								singleLine[k - 1].append("["
										+ nates[StartNatesNum[k - 1] - 1]);
								for (int m = StartNatesNum[k - 1]; m < StartNatesNum[k] - 1; m++) {
									singleLine[k - 1].append("," + nates[m]);
								}
								singleLine[k - 1].append("]");
							}
						}// 循环完还剩最后一个没有组装
						try {
							singleLine[LineCount - 1].append("["
									+ nates[StartNatesNum[LineCount - 1] - 1]);
						} catch (Exception e) {
							e.printStackTrace();
							logger.info(j);
						}
						for (int m = StartNatesNum[LineCount - 1]; m < nates.length; m++) {
							singleLine[LineCount - 1].append("," + nates[m]);
						}
						singleLine[LineCount - 1].append("]"); // 所有线组装完成

						// coordinates.append(singleLine[0]);
						coordinates = singleLine[0].toString();
						for (int k = 1; k < LineCount; k++) {
							// coordinates.append(coordinates+","+singleLine[k]);
							coordinates = coordinates + ","
									+ singleLine[k].toString();
						}
						// coordinates.append("["+coordinates+"]");
						coordinates = "[" + coordinates + "]";
					} else if ("MULTIPOLYGON".equalsIgnoreCase(type)) {
						type = "MultiPolygon";
						// coordinates.append(nates[0]);
						coordinates = nates[0];
						for (int k = 1; k < natesCount; k++) {
							// coordinates.append(coordinates+","+nates[k]);
							coordinates = coordinates + "," + nates[k];
						}
						// coordinates.append("[[["+coordinates+"]]]");
						coordinates = "[[[" + coordinates + "]]]";
					}
					geometryBody = ",\"geometry\":{\"type\":\"" + type
							+ "\",\"coordinates\":" + coordinates.toString()
							+ "}";
				}
				bodyList.add(bodyHead + properBody + geometryBody + bodyTail);
			}
		}
		int bodyCount = bodyList.size();
		// 若查询无数据
		if (bodyCount == 0) {
			bodyList.add(bodyHead + properBody + geometryBody + bodyTail);
		}
		StringBuffer body = new StringBuffer(bodyList.get(0));
		if (bodyCount != 1) {
			for (int i = 1; i < bodyCount; i++) {
				body = body.append(",");
				body = body.append(bodyList.get(i));
			}
		}
		strgeojson = head + body.toString() + tail;
/********************************************************************String 方法结束******************************************************************************/
		
/********************************************************************fastJSON 方法开始****************************************************************************/
//		int classCount = respResult.length;
//		List<GeoJsonFeature> featuresList = new ArrayList<GeoJsonFeature>();
//
//		for (int i = 0; i < classCount; i++) {
//			// 得到每个classId对应的数据
//			QueryResult eachResult = respResult[i];
//			if (eachResult == null || eachResult.getRecords() == null) {
//				continue;
//			}
//			int recordCount = eachResult.getRecords().length;
//			QueryRecord[] record = eachResult.getRecords();
//			for (int j = 0; j < recordCount; j++) {
//				// 得到每条记录对应数据
//				QueryRecord eachRecord = record[j];
//				if (eachRecord == null) {
//					continue;
//				}
//				Map<String,Object> properties = new HashMap<String,Object>();
//				JSONObject psrDefJSONOb = JSONObject.fromObject(eachResult.getPsrDef());
//				if(psrDefJSONOb.isEmpty() || psrDefJSONOb.isNullObject()){
//					properties.put("psrDef", "null");
//				}else{
//				properties.put("psrDef", psrDefJSONOb);
//				}
//				
//				//属性fields信息
//				QueryField[] fields = eachRecord.getFields();
//				if (fields == null) {
//					continue;
//				}
//				int fieldsNum = fields.length;
//				for (int k = 0; k < fieldsNum; k++) {
//					String fieldName = fields[k].getFieldName();
//					String fieldValue = fields[k].getFieldValue();
//					properties.put(fieldName, fieldValue);
//				}	//field层结束
//				//放总体fields(即详细)
//				JSONArray fieldsJSONAr = JSONArray.fromObject(fields);
//				properties.put("fileds", fieldsJSONAr);
//				
//				//属性符号symbol信息
//				String symbolId = "";
//				SymbolPair symbol = eachRecord.getSymbol();
//				if (symbol != null) {
//					symbolId = symbol.getSymbolId().toString();
//				}
//				properties.put("DEVICESTATE", symbolId);
//				//属性填装结束
//				
//				//Geometry属性装填开始
//				GeometryPair geom = eachRecord.getGeom();
//				// geomtry不能为空
//				String type = "";
//				String coordinates = "";
//				if (geom != null) {
//					type = geom.getGeometryType();
//					double[] coords = geom.getCoords();
//					// 坐标数未偶数，可得到坐标点个数
//					int natesCount = (coords.length) / 2;
//					String[] nates = new String[natesCount];
//					// 得到坐标组合成点
//					for (int k = 0; k < natesCount; k++) {
////		String xCoords = String.valueOf(coords[(2 * k)]);
////		String yCoords = String.valueOf(coords[(2 * k + 1)]);
//		 //对于偏移量+0.002,+0.003作还原
//		 String xCoords = String.valueOf(coords[(2*k)]);
//		 String yCoords = String.valueOf(coords[(2 * k + 1)]);
//						nates[k] = "[" + xCoords + "," + yCoords + "]";
//					}
//					// StringBuffer coordinates = new StringBuffer();
//					// 变成标准的GEOJSON形状类型
//					if ("point".equalsIgnoreCase(type)) {
//						type = "Point";
//						// coordinates.append(nates[0]);
//						coordinates = nates[0];
//					} else if ("LineString".equalsIgnoreCase(type)) {
//						type = "LineString";
//						// coordinates.append(nates[0]);
//						coordinates = nates[0];
//						for (int k = 1; k < natesCount; k++) {
//							// coordinates.append(coordinates+","+nates[k]);
//							coordinates = coordinates + "," + nates[k];
//						}
//						// coordinates.append("["+coordinates+"]");
//						coordinates = "[" + coordinates + "]";
//					} else if ("MULTIPOINT".equalsIgnoreCase(type)) {
//						type = "MultiPoint";
//						// coordinates.append(nates[0]);
//						coordinates = nates[0];
//						for (int k = 1; k < natesCount; k++) {
//							// coordinates.append(coordinates+","+nates[k]);
//							coordinates = coordinates + "," + nates[k];
//						}
//						// coordinates.append("["+coordinates+"]");
//						coordinates = "[" + coordinates + "]";
//					} else if ("POLYGON".equalsIgnoreCase(type)) {
//						type = "Polygon";
//						// coordinates.append(nates[0]);
//						coordinates = nates[0];
//						for (int k = 1; k < natesCount; k++) {
//							// coordinates.append(coordinates+","+nates[k]);
//							coordinates = coordinates + "," + nates[k];
//						}
//						// coordinates.append("[["+coordinates+"]]");
//						coordinates = "[[" + coordinates + "]]";
//					} else if ("MultiLineString".equalsIgnoreCase(type)) {
//						type = "MultiLineString";
//						int other = (int) (geom.getOther());
//						int[] startDouble = geom.getStartDouble();
//						if (other != startDouble.length) { // 若有头
//							int[] OldStartDouble = startDouble.clone();
//							// 去头
//							startDouble = new int[OldStartDouble.length - 1];
//							for (int k = 0; k < startDouble.length; k++) {
//								startDouble[k] = OldStartDouble[k + 1];
//							}
//						}
//						int LineCount = startDouble.length;
//						StringBuffer[] singleLine = new StringBuffer[LineCount];
//						// 对每一个lineString进行组装
//						int[] StartNatesNum = new int[LineCount];
//						for (int k = 0; k < LineCount; k++) {
//							singleLine[k] = new StringBuffer();
//							StartNatesNum[k] = (startDouble[k] + 1) / 2; // 开始的点若为1,即nates[0]
//							if (k == 0) {
//								continue;
//							} else {
//								singleLine[k - 1].append("["
//										+ nates[StartNatesNum[k - 1] - 1]);
//								for (int m = StartNatesNum[k - 1]; m < StartNatesNum[k] - 1; m++) {
//									singleLine[k - 1].append("," + nates[m]);
//								}
//								singleLine[k - 1].append("]");
//							}
//						}// 循环完还剩最后一个没有组装
//						try {
//							singleLine[LineCount - 1].append("["
//									+ nates[StartNatesNum[LineCount - 1] - 1]);
//						} catch (Exception e) {
//							e.printStackTrace();
//							logger.info(j);
//						}
//						for (int m = StartNatesNum[LineCount - 1]; m < nates.length; m++) {
//							singleLine[LineCount - 1].append("," + nates[m]);
//						}
//						singleLine[LineCount - 1].append("]"); // 所有线组装完成
//
//						// coordinates.append(singleLine[0]);
//						coordinates = singleLine[0].toString();
//						for (int k = 1; k < LineCount; k++) {
//							// coordinates.append(coordinates+","+singleLine[k]);
//							coordinates = coordinates + ","
//									+ singleLine[k].toString();
//						}
//						// coordinates.append("["+coordinates+"]");
//						coordinates = "[" + coordinates + "]";
//					} else if ("MULTIPOLYGON".equalsIgnoreCase(type)) {
//						type = "MultiPolygon";
//						// coordinates.append(nates[0]);
//						coordinates = nates[0];
//						for (int k = 1; k < natesCount; k++) {
//							// coordinates.append(coordinates+","+nates[k]);
//							coordinates = coordinates + "," + nates[k];
//						}
//						// coordinates.append("[[["+coordinates+"]]]");
//						coordinates = "[[[" + coordinates + "]]]";
//					}
//				}
//				GeoJsonGeometry geoJsonGeometry = new GeoJsonGeometry(type,coordinates);
//				GeoJsonFeature feature = new GeoJsonFeature(properties,geoJsonGeometry);
//				featuresList.add(feature);
//			}	//记录层结束
//		}	//class层结束
//		
//		GeoJsonFeature[] features = new GeoJsonFeature[featuresList.size()];
//		features = featuresList.toArray(features);
//		GeoJson geoJson = new GeoJson(features);
//		strgeojson = JSON.toJSONString(geoJson);
/********************************************************fastJSON 方法结束****************************************************************/

		long b = System.currentTimeMillis();
		logger.info("JSON转换时间:" + (b - a) + "ms");
		return strgeojson;
	}
	
	// 变成geojason格式字符串（无详细信息）
	public static String forGeoJason(QueryResult[] respResult) {
		long a = System.currentTimeMillis();
		// JSONObject obj = JSONObject.fromObject(resp);
		String strgeojson = "";
		
/**********************************************************String 方法开始*****************************************************************/	
		String head = "{\"type\":\"FeatureCollection\","
				+ "\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"urn:ogc:def:crs:OGC:1.3:CRS84\"}},"
				+ "\"features\":[";
		String bodyHead = "{\"type\":\"Feature\"";
		String bodyTail = "}";
		String tail = "]}";
		List<String> bodyList = new ArrayList<String>();
		int classCount = respResult.length;

		String properBody = ",\"properties\":{\"psrDef\":" + null
				+ ",\"DEVICETYPEID\":\"" + null + "\",\"DEVICESTATE\":\""
				+ null + "\"}";
		String geometryBody = ",\"geometry\":{\"type\":\"" + null
				+ "\",\"coordinates\":\"" + null + "\"}";
		for (int i = 0; i < classCount; i++) {
			// 得到每个classId对应的数据
			QueryResult eachResult = respResult[i];
			if (eachResult == null || eachResult.getRecords() == null) {
				continue;
			}
			JSONObject psrDefJSONOb = JSONObject.fromObject(eachResult
					.getPsrDef());
			int recordCount = eachResult.getRecords().length;
			QueryRecord[] record = eachResult.getRecords();

			for (int j = 0; j < recordCount; j++) {
				// 得到每条记录对应数据
				QueryRecord eachRecord = record[j];
				if (eachRecord == null) {
					continue;
				}
				QueryField[] fields = eachRecord.getFields();
				if(fields == null || fields.length == 0){
					logger.info("返回字段为空");
					continue;
				}

				// 返回字段-值信息
				int fieldsNum = fields.length;
				StringBuffer fieldBody = new StringBuffer();
				String fieldName = "";
				String fieldValue = "";
				for (int k = 0; k < fieldsNum; k++) {
					if (fields[k].getFieldName() != null) {
						fieldName = fields[k].getFieldName().toString();
					}
					if (fields[k].getFieldValue() != null) {
						fieldValue = fields[k].getFieldValue().toString();
					}

					// 只返回"OID","SBMC","符号角度",“符号大小”,"设备子类型"
					// if(!(fieldName.equalsIgnoreCase("OID")||fieldName.equalsIgnoreCase("SBMC")||fieldName.equalsIgnoreCase("FHJD")||fieldName.equalsIgnoreCase("FHDX")
					// ||fieldName.equalsIgnoreCase("SBZLX")||fieldName.equalsIgnoreCase("SFBZ")||fieldName.equalsIgnoreCase("SBZLX")||fieldName.equalsIgnoreCase("SBZLX")
					// ||fieldName.equalsIgnoreCase("SBZLX")||fieldName.equalsIgnoreCase("SBZLX")||fieldName.equalsIgnoreCase("SBZLX"))){
					// continue;
					// }
					fieldBody.append(",\"" + fieldName + "\":\"" + fieldValue
							+ "\"");
				}

				// //返回symbol属性
				// String symbol = null;
				// if(!record.getJSONObject("symbol").isNullObject()){
				// symbol = record.getJSONObject("symbol").toString();
				// }
				// 返回显示符号所需symbol属性值
//				String devtypeId = null;
				String symbolId = null;
				SymbolPair symbol = eachRecord.getSymbol();
				if (symbol != null) {
					symbolId = symbol.getSymbolId().toString();
//					String HdevtypeId = symbol.getDevtypeId().toString();
//					if (HdevtypeId.isEmpty()) {
//						devtypeId = "";
//					} else {
//						devtypeId = String.valueOf(Integer.parseInt(
//								HdevtypeId.substring(2), 16));
//					}
				}
				// 返回DZGEOJSON内properties属性信息
				properBody = ",\"properties\":{\"psrDef\":" + psrDefJSONOb
						+ fieldBody.toString() + ",\"DEVICESTATE\":\"" + symbolId
						+ "\"}";

				GeometryPair geom = eachRecord.getGeom();
				// geomtry不能为空
				if (geom == null) {
					String type = null;
					String coordinates = null;
					geometryBody = ",\"geometry\":{\"type\":" + type
							+ ",\"coordinates\":\"" + coordinates + "\"}";
				} else {
					String type = geom.getGeometryType();
					double[] coords = geom.getCoords();
					// 坐标数未偶数，可得到坐标点个数
					int natesCount = (coords.length) / 2;
					String[] nates = new String[natesCount];
					// 得到坐标组合成点
					for (int k = 0; k < natesCount; k++) {
//						String xCoords = String.valueOf(coords[(2 * k)]);
//						String yCoords = String.valueOf(coords[(2 * k + 1)]);
						 //对于偏移量+0.002,+0.003作还原
						 String xCoords = String.valueOf(coords[(2*k)]);
						 String yCoords = String.valueOf(coords[(2 * k + 1)]);
						nates[k] = "[" + xCoords + "," + yCoords + "]";
					}
					// StringBuffer coordinates = new StringBuffer();
					String coordinates = "";
					// 变成标准的GEOJSON形状类型
					if ("point".equalsIgnoreCase(type)) {
						type = "Point";
						// coordinates.append(nates[0]);
						coordinates = nates[0];
					} else if ("LineString".equalsIgnoreCase(type)) {
						type = "LineString";
						// coordinates.append(nates[0]);
						coordinates = nates[0];
						for (int k = 1; k < natesCount; k++) {
							// coordinates.append(coordinates+","+nates[k]);
							coordinates = coordinates + "," + nates[k];
						}
						// coordinates.append("["+coordinates+"]");
						coordinates = "[" + coordinates + "]";
					} else if ("MULTIPOINT".equalsIgnoreCase(type)) {
						type = "MultiPoint";
						// coordinates.append(nates[0]);
						coordinates = nates[0];
						for (int k = 1; k < natesCount; k++) {
							// coordinates.append(coordinates+","+nates[k]);
							coordinates = coordinates + "," + nates[k];
						}
						// coordinates.append("["+coordinates+"]");
						coordinates = "[" + coordinates + "]";
					} else if ("POLYGON".equalsIgnoreCase(type)) {
						type = "Polygon";
						// coordinates.append(nates[0]);
						coordinates = nates[0];
						for (int k = 1; k < natesCount; k++) {
							// coordinates.append(coordinates+","+nates[k]);
							coordinates = coordinates + "," + nates[k];
						}
						// coordinates.append("[["+coordinates+"]]");
						coordinates = "[[" + coordinates + "]]";
					} else if ("MultiLineString".equalsIgnoreCase(type)) {
						type = "MultiLineString";
						int other = (int) (geom.getOther());
						int[] startDouble = geom.getStartDouble();
						if (other != startDouble.length) { // 若有头
							int[] OldStartDouble = startDouble.clone();
							// 去头
							startDouble = new int[OldStartDouble.length - 1];
							for (int k = 0; k < startDouble.length; k++) {
								startDouble[k] = OldStartDouble[k + 1];
							}
						}
						int LineCount = startDouble.length;
						StringBuffer[] singleLine = new StringBuffer[LineCount];
						// 对每一个lineString进行组装
						int[] StartNatesNum = new int[LineCount];
						for (int k = 0; k < LineCount; k++) {
							singleLine[k] = new StringBuffer();
							StartNatesNum[k] = (startDouble[k] + 1) / 2; // 开始的点若为1,即nates[0]
							if (k == 0) {
								continue;
							} else {
								singleLine[k - 1].append("["
										+ nates[StartNatesNum[k - 1] - 1]);
								for (int m = StartNatesNum[k - 1]; m < StartNatesNum[k] - 1; m++) {
									singleLine[k - 1].append("," + nates[m]);
								}
								singleLine[k - 1].append("]");
							}
						}// 循环完还剩最后一个没有组装
						try {
							singleLine[LineCount - 1].append("["
									+ nates[StartNatesNum[LineCount - 1] - 1]);
						} catch (Exception e) {
							e.printStackTrace();
							logger.info(j);
						}
						for (int m = StartNatesNum[LineCount - 1]; m < nates.length; m++) {
							singleLine[LineCount - 1].append("," + nates[m]);
						}
						singleLine[LineCount - 1].append("]"); // 所有线组装完成

						// coordinates.append(singleLine[0]);
						coordinates = singleLine[0].toString();
						for (int k = 1; k < LineCount; k++) {
							// coordinates.append(coordinates+","+singleLine[k]);
							coordinates = coordinates + ","
									+ singleLine[k].toString();
						}
						// coordinates.append("["+coordinates+"]");
						coordinates = "[" + coordinates + "]";
					} else if ("MULTIPOLYGON".equalsIgnoreCase(type)) {
						type = "MultiPolygon";
						// coordinates.append(nates[0]);
						coordinates = nates[0];
						for (int k = 1; k < natesCount; k++) {
							// coordinates.append(coordinates+","+nates[k]);
							coordinates = coordinates + "," + nates[k];
						}
						// coordinates.append("[[["+coordinates+"]]]");
						coordinates = "[[[" + coordinates + "]]]";
					}
					geometryBody = ",\"geometry\":{\"type\":\"" + type
							+ "\",\"coordinates\":" + coordinates.toString()
							+ "}";
				}
				bodyList.add(bodyHead + properBody + geometryBody + bodyTail);
			}
		}
		int bodyCount = bodyList.size();
		// 若查询无数据
		if (bodyCount == 0) {
			bodyList.add(bodyHead + properBody + geometryBody + bodyTail);
		}

		StringBuffer body = new StringBuffer(bodyList.get(0));
		if (bodyCount != 1) {
			for (int i = 1; i < bodyCount; i++) {
				body = body.append(",");
				body = body.append(bodyList.get(i));
			}
		}
		strgeojson = head + body.toString() + tail;
		// logger.info(strgeojson); //严重占时间
/**********************************************************String 方法结束*****************************************************************/	

		
/********************************************************fastJSON 方法开始 ****************************************************************/
//		int classCount = respResult.length;
//		List<GeoJsonFeature> featuresList = new ArrayList<GeoJsonFeature>();
//
//		for (int i = 0; i < classCount; i++) {
//			// 得到每个classId对应的数据
//			QueryResult eachResult = respResult[i];
//			if (eachResult == null || eachResult.getRecords() == null) {
//				continue;
//			}
//			int recordCount = eachResult.getRecords().length;
//			QueryRecord[] record = eachResult.getRecords();
//			for (int j = 0; j < recordCount; j++) {
//				// 得到每条记录对应数据
//				QueryRecord eachRecord = record[j];
//				if (eachRecord == null) {
//					continue;
//				}
//				Map<String,Object> properties = new HashMap<String,Object>();
//				JSONObject psrDefJSONOb = JSONObject.fromObject(eachResult.getPsrDef());
//				if(psrDefJSONOb.isEmpty() || psrDefJSONOb.isNullObject()){
//					properties.put("psrDef", "null");
//				}else{
//				properties.put("psrDef", psrDefJSONOb);
//				}
//				properties.put("psrDef", psrDefJSONOb);
//				
//				//属性fields信息
//				QueryField[] fields = eachRecord.getFields();
//				if (fields == null) {
//					continue;
//				}
//				int fieldsNum = fields.length;
//				for (int k = 0; k < fieldsNum; k++) {
//					String fieldName = fields[k].getFieldName();
//					String fieldValue = fields[k].getFieldValue();
//					properties.put(fieldName, fieldValue);
//				}	//field层结束
////				//放总体fields(即详细)
////				JSONArray fieldsJSONAr = JSONArray.fromObject(fields);
////				properties.put("fileds", fieldsJSONAr);
//				
//				//属性符号symbol信息
//				String symbolId = "";
//				SymbolPair symbol = eachRecord.getSymbol();
//				if (symbol != null) {
//					symbolId = symbol.getSymbolId().toString();
//				}
//				properties.put("DEVICESTATE", symbolId);
//				//属性填装结束
//				
//				//Geometry属性装填开始
//				GeometryPair geom = eachRecord.getGeom();
//				// geomtry不能为空
//				String type = "";
//				String coordinates = "";
//				if (geom != null) {
//					type = geom.getGeometryType();
//					double[] coords = geom.getCoords();
//					// 坐标数未偶数，可得到坐标点个数
//					int natesCount = (coords.length) / 2;
//					String[] nates = new String[natesCount];
//					// 得到坐标组合成点
//					for (int k = 0; k < natesCount; k++) {
//						String xCoords = String.valueOf(coords[(2 * k)]);
//						String yCoords = String.valueOf(coords[(2 * k + 1)]);
//						// //对于偏移量+0.002,+0.003作还原
//						// String xCoords = String.valueOf(coords[(2*k)]-0.002);
//						// String yCoords =
//						// String.valueOf(coords[(2*k+1)]-0.003);
//						nates[k] = "[" + xCoords + "," + yCoords + "]";
//					}
//					// StringBuffer coordinates = new StringBuffer();
//					// 变成标准的GEOJSON形状类型
//					if ("point".equalsIgnoreCase(type)) {
//						type = "Point";
//						// coordinates.append(nates[0]);
//						coordinates = nates[0];
//					} else if ("LineString".equalsIgnoreCase(type)) {
//						type = "LineString";
//						// coordinates.append(nates[0]);
//						coordinates = nates[0];
//						for (int k = 1; k < natesCount; k++) {
//							// coordinates.append(coordinates+","+nates[k]);
//							coordinates = coordinates + "," + nates[k];
//						}
//						// coordinates.append("["+coordinates+"]");
//						coordinates = "[" + coordinates + "]";
//					} else if ("MULTIPOINT".equalsIgnoreCase(type)) {
//						type = "MultiPoint";
//						// coordinates.append(nates[0]);
//						coordinates = nates[0];
//						for (int k = 1; k < natesCount; k++) {
//							// coordinates.append(coordinates+","+nates[k]);
//							coordinates = coordinates + "," + nates[k];
//						}
//						// coordinates.append("["+coordinates+"]");
//						coordinates = "[" + coordinates + "]";
//					} else if ("POLYGON".equalsIgnoreCase(type)) {
//						type = "Polygon";
//						// coordinates.append(nates[0]);
//						coordinates = nates[0];
//						for (int k = 1; k < natesCount; k++) {
//							// coordinates.append(coordinates+","+nates[k]);
//							coordinates = coordinates + "," + nates[k];
//						}
//						// coordinates.append("[["+coordinates+"]]");
//						coordinates = "[[" + coordinates + "]]";
//					} else if ("MultiLineString".equalsIgnoreCase(type)) {
//						type = "MultiLineString";
//						int other = (int) (geom.getOther());
//						int[] startDouble = geom.getStartDouble();
//						if (other != startDouble.length) { // 若有头
//							int[] OldStartDouble = startDouble.clone();
//							// 去头
//							startDouble = new int[OldStartDouble.length - 1];
//							for (int k = 0; k < startDouble.length; k++) {
//								startDouble[k] = OldStartDouble[k + 1];
//							}
//						}
//						int LineCount = startDouble.length;
//						StringBuffer[] singleLine = new StringBuffer[LineCount];
//						// 对每一个lineString进行组装
//						int[] StartNatesNum = new int[LineCount];
//						for (int k = 0; k < LineCount; k++) {
//							singleLine[k] = new StringBuffer();
//							StartNatesNum[k] = (startDouble[k] + 1) / 2; // 开始的点若为1,即nates[0]
//							if (k == 0) {
//								continue;
//							} else {
//								singleLine[k - 1].append("["
//										+ nates[StartNatesNum[k - 1] - 1]);
//								for (int m = StartNatesNum[k - 1]; m < StartNatesNum[k] - 1; m++) {
//									singleLine[k - 1].append("," + nates[m]);
//								}
//								singleLine[k - 1].append("]");
//							}
//						}// 循环完还剩最后一个没有组装
//						try {
//							singleLine[LineCount - 1].append("["
//									+ nates[StartNatesNum[LineCount - 1] - 1]);
//						} catch (Exception e) {
//							e.printStackTrace();
//							logger.info(j);
//						}
//						for (int m = StartNatesNum[LineCount - 1]; m < nates.length; m++) {
//							singleLine[LineCount - 1].append("," + nates[m]);
//						}
//						singleLine[LineCount - 1].append("]"); // 所有线组装完成
//
//						// coordinates.append(singleLine[0]);
//						coordinates = singleLine[0].toString();
//						for (int k = 1; k < LineCount; k++) {
//							// coordinates.append(coordinates+","+singleLine[k]);
//							coordinates = coordinates + ","
//									+ singleLine[k].toString();
//						}
//						// coordinates.append("["+coordinates+"]");
//						coordinates = "[" + coordinates + "]";
//					} else if ("MULTIPOLYGON".equalsIgnoreCase(type)) {
//						type = "MultiPolygon";
//						// coordinates.append(nates[0]);
//						coordinates = nates[0];
//						for (int k = 1; k < natesCount; k++) {
//							// coordinates.append(coordinates+","+nates[k]);
//							coordinates = coordinates + "," + nates[k];
//						}
//						// coordinates.append("[[["+coordinates+"]]]");
//						coordinates = "[[[" + coordinates + "]]]";
//					}
//				}
//				GeoJsonGeometry geoJsonGeometry = new GeoJsonGeometry(type,coordinates);
//				GeoJsonFeature feature = new GeoJsonFeature(properties,geoJsonGeometry);
//				featuresList.add(feature);
//			}	//记录层结束
//		}	//class层结束
//		
//		GeoJsonFeature[] features = new GeoJsonFeature[featuresList.size()];
//		features = featuresList.toArray(features);
//		GeoJson geoJson = new GeoJson(features);
//		strgeojson = JSON.toJSONString(geoJson);
/********************************************************fastJSON 方法结束 ****************************************************************/
		
		long b = System.currentTimeMillis();
		logger.info("JSON转换时间:" + (b - a) + "ms");
		return strgeojson;
	}

	/**
	 * transform result to geojson
	 * 
	 * @param respResult
	 *            results get from MongoDB
	 * */
	public static String forMongoGeoJson(QueryResult[] respResult) {

		long a = System.currentTimeMillis();
		if (respResult == null) {
			return null;
		}
		// JSONObject obj = JSONObject.fromObject(resp);
		String strgeojson = "";
		String head = "{\"type\":\"FeatureCollection\","
				+ "\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"urn:ogc:def:crs:OGC:1.3:CRS84\"}},"
				+ "\"features\":[";
		String bodyHead = "{\"type\":\"Feature\"";
		String bodyTail = "}";
		String tail = "]}";
		// List<String> bodyList = new ArrayList<String>();

		int classCount = respResult.length;
		int featureCount = 0;
		for (int i = 0; i < classCount; ++i) {
			int size = respResult[i].getCount();
			featureCount += size;
		}
		String[] bodys = new String[featureCount];
		int counterBodys = 0;
		String properBody = ",\"properties\":{\"psrDef\":" + null
				+ ",\"DEVICETYPEID\":\"" + null + "\",\"DEVICESTATE\":\""
				+ null + "\"}";
		String geometryBody = ",\"geometry\":{\"type\":\"" + null
				+ "\",\"coordinates\":" + null + "}";
		for (int i = 0; i < classCount; i++) {
			// 得到每个classId对应的数据
			QueryResult eachResult = respResult[i];
			if (eachResult == null || eachResult.getRecords() == null) {
				continue;
			}
			JSONObject psrDefJSONOb = JSONObject.fromObject(eachResult
					.getPsrDef());
			int recordCount = eachResult.getRecords().length;
			QueryRecord[] record = eachResult.getRecords();

			// //返回professionName
			// String professionName = eachResult.getProfessionName();

			for (int j = 0; j < recordCount; j++) {
				// 得到每条记录对应数据
				QueryRecord eachRecord = record[j];
				if (eachRecord == null) {
					continue;
				}
				QueryField[] fields = eachRecord.getFields();

				// 返回字段-值信息
				int fieldsNum = fields.length;
				StringBuffer fieldBody = new StringBuffer();
				String fieldName = "";
				String fieldValue = "";
				for (int k = 0; k < fieldsNum; k++) {
					fieldValue = "null";
					
					if (fields[k].getFieldName() != null) {
						fieldName = fields[k].getFieldName().toString();
					}
					if (fields[k].getFieldValue() != null) {
						fieldValue = fields[k].getFieldValue().toString();
					}
					fieldBody.append(",\"" + fieldName + "\":\"" + fieldValue
							+ "\"");
				}
				// 返回显示符号所需symbol属性值
//				String devtypeId = null;
				String symbolId = null;
				SymbolPair symbol = eachRecord.getSymbol();
				if (symbol != null) {
					symbolId = symbol.getSymbolId().toString();
//					String HdevtypeId = symbol.getDevtypeId().toString();
//					if (HdevtypeId.isEmpty()) {
//						devtypeId = "";
//					} else {
//						devtypeId = String.valueOf(Integer.parseInt(
//								HdevtypeId.substring(2), 16));
//					}
				}
				// 返回DZGEOJSON内properties属性信息
				properBody = ",\"properties\":{\"psrDef\":" + psrDefJSONOb
						+ fieldBody.toString() + ",\"DEVICESTATE\":\"" + symbolId
						+ "\"}";

				geometryBody = ",\"geometry\":" + eachRecord.getGeoJson();
				// bodyList.add(bodyHead + properBody + geometryBody +
				// bodyTail);
				bodys[counterBodys++] = bodyHead + properBody + geometryBody
						+ bodyTail;
			}
		}
		// int bodyCount = bodyList.size();
		int bodyCount = bodys.length;
		// 若查询无数据
		if (bodyCount == 0) {
			strgeojson = head + tail;
		}
		else{
			// StringBuffer body = new StringBuffer(bodyList.get(0));
			StringBuffer body = new StringBuffer(bodys[0]);
			if (bodyCount != 1) {
				for (int i = 1; i < bodyCount; i++) {
					body = body.append(",");
					// body = body.append(bodyList.get(i));
					body = body.append(bodys[i]);
				}
			}
			strgeojson = head + body.toString() + tail;
		}
		
		// logger.info(strgeojson); //严重占时间
		long b = System.currentTimeMillis();
		logger.info("JSON转换时间:" + (b - a) + "ms");
		return strgeojson;
	}

	/**
		 * 
		 * */
	public static String forGeoJsonForConditionQuery(QueryResult[] respResult) {

		long a = System.currentTimeMillis();

		// JSONObject obj = JSONObject.fromObject(resp);
		String strgeojson = "";
		String head = "{\"type\":\"FeatureCollection\","
				+ "\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"urn:ogc:def:crs:OGC:1.3:CRS84\"}},"
				+ "\"features\":[";
		String bodyHead = "{\"type\":\"Feature\"";
		String bodyTail = "}";
		String tail = "]}";

		String properBody = ",\"properties\":{\"psrDef\":" + null
				+ ",\"DEVICETYPEID\":\"" + null + "\",\"DEVICESTATE\":\""
				+ null + "\"}";
		String geometryBody = ",\"geometry\":{\"type\":\"" + null
				+ "\",\"coordinates\":" + null + "}";
		// List<String> bodyList = new ArrayList<String>();
		if (respResult == null) {
			return head + bodyHead + properBody + geometryBody + bodyTail
					+ tail;
		}
		int classCount = respResult.length;
		int featureCount = 0;
		for (int i = 0; i < classCount; ++i) {
			int size = respResult[i].getCount();
			featureCount += size;
		}
		String[] bodys = new String[featureCount];
		int counterBodys = 0;

		for (int i = 0; i < classCount; i++) {
			// 得到每个classId对应的数据
			QueryResult eachResult = respResult[i];
			if (eachResult == null || eachResult.getRecords() == null) {
				continue;
			}
			JSONObject psrDefJSONOb = JSONObject.fromObject(eachResult
					.getPsrDef());
			int recordCount = eachResult.getRecords().length;
			QueryRecord[] record = eachResult.getRecords();

			// //返回professionName
			// String professionName = eachResult.getProfessionName();

			for (int j = 0; j < recordCount; j++) {
				// 得到每条记录对应数据
				QueryRecord eachRecord = record[j];
				if (eachRecord == null) {
					continue;
				}
				QueryField[] fields = eachRecord.getFields();
				JSONArray fieldsJSONAr = JSONArray.fromObject(fields);
				// 返回字段-值信息
				int fieldsNum = fields.length;
				StringBuffer fieldBody = new StringBuffer();
				String fieldName = "";
				String fieldValue = "";
				for (int k = 0; k < fieldsNum; k++) {
					if (fields[k].getFieldName() != null) {
						fieldName = fields[k].getFieldName().toString();
					}
					if (fields[k].getFieldValue() != null) {
						fieldValue = fields[k].getFieldValue().toString();
					}
					fieldBody.append(",\"" + fieldName + "\":\"" + fieldValue
							+ "\"");
				}
				// 返回显示符号所需symbol属性值
//				String devtypeId = null;
				String symbolId = null;
				SymbolPair symbol = eachRecord.getSymbol();
				if (symbol != null) {
					symbolId = symbol.getSymbolId().toString();
//					String HdevtypeId = symbol.getDevtypeId().toString();
//					if (HdevtypeId.isEmpty()) {
//						devtypeId = "";
//					} else {
//						devtypeId = String.valueOf(Integer.parseInt(
//								HdevtypeId.substring(2), 16));
//					}
				}
				// 返回DZGEOJSON内properties属性信息
				// properBody = ",\"properties\":{\"psrDef\":" + psrDefJSONOb
				// + fieldBody.toString() + ",\"DEVICETYPEID\":\""
				// + devtypeId + "\",\"DEVICESTATE\":\"" + symbolId
				// + "\"}";
				properBody = ",\"properties\":{\"psrDef\":" + psrDefJSONOb
						+ ",\"fields\":" + fieldsJSONAr + fieldBody.toString()
						+ ",\"DEVICESTATE\":\"" + symbolId + "\"}";
				geometryBody = ",\"geometry\":" + eachRecord.getGeoJson();
				// bodyList.add(bodyHead + properBody + geometryBody +
				// bodyTail);
				bodys[counterBodys++] = bodyHead + properBody + geometryBody
						+ bodyTail;
			}
		}
		// int bodyCount = bodyList.size();
		int bodyCount = bodys.length;
		// 若查询无数据
		if (bodyCount == 0) {
			// bodyList.add(bodyHead + properBody + geometryBody + bodyTail);
			bodys[counterBodys++] = bodyHead + properBody + geometryBody
					+ bodyTail;
		}
		// StringBuffer body = new StringBuffer(bodyList.get(0));
		StringBuffer body = new StringBuffer(bodys[0]);
		if (bodyCount != 1) {
			for (int i = 1; i < bodyCount; i++) {
				body = body.append(",");
				// body = body.append(bodyList.get(i));
				body = body.append(bodys[i]);
			}
		}
		strgeojson = head + body.toString() + tail;
		// logger.info(strgeojson); //严重占时间
		long b = System.currentTimeMillis();
		logger.info("JSON转换时间:" + (b - a) + "ms");
		return strgeojson;
	}

	public static String forGeoJsonForConditionQuery2(QueryResult[] respResult) {

		long a = System.currentTimeMillis();

		// JSONObject obj = JSONObject.fromObject(resp);
		String strgeojson = "";
		String head = "{\"type\":\"FeatureCollection\","
				+ "\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"urn:ogc:def:crs:OGC:1.3:CRS84\"}},"
				+ "\"features\":[";
		String bodyHead = "{\"type\":\"Feature\"";
		String bodyTail = "}";
		String tail = "]}";

		String properBody = ",\"properties\":{\"psrDef\":" + null
				+ ",\"DEVICETYPEID\":\"" + null + "\",\"DEVICESTATE\":\""
				+ null + "\"}";
		String geometryBody = ",\"geometry\":{\"type\":\"" + null
				+ "\",\"coordinates\":" + null + "}";
		// List<String> bodyList = new ArrayList<String>();
		if (respResult == null) {
			return head + bodyHead + properBody + geometryBody + bodyTail
					+ tail;
		}
		int classCount = respResult.length;
		int featureCount = 0;
		for (int i = 0; i < classCount; ++i) {
			int size = respResult[i].getCount();
			featureCount += size;
		}
		String[] bodys = new String[featureCount];
		int counterBodys = 0;

		for (int i = 0; i < classCount; i++) {
			// 得到每个classId对应的数据
			QueryResult eachResult = respResult[i];
			if (eachResult == null || eachResult.getRecords() == null) {
				continue;
			}
			JSONObject psrDefJSONOb = JSONObject.fromObject(eachResult
					.getPsrDef());
			int recordCount = eachResult.getRecords().length;
			QueryRecord[] record = eachResult.getRecords();

			// //返回professionName
			// String professionName = eachResult.getProfessionName();

			for (int j = 0; j < recordCount; j++) {
				// 得到每条记录对应数据
				QueryRecord eachRecord = record[j];
				if (eachRecord == null) {
					continue;
				}
				QueryField[] fields = eachRecord.getFields();
				JSONArray fieldsJSONAr = JSONArray.fromObject(fields);
				// 返回字段-值信息
				int fieldsNum = fields.length;
				StringBuffer fieldBody = new StringBuffer();
				String fieldName = "";
				String fieldValue = "";
				for (int k = 0; k < fieldsNum; k++) {
					if (fields[k].getFieldName() != null) {
						fieldName = fields[k].getFieldName().toString();
					}
					if (fields[k].getFieldValue() != null) {
						fieldValue = fields[k].getFieldValue().toString();
					}
					fieldBody.append(",\"" + fieldName + "\":\"" + fieldValue
							+ "\"");
				}
				// 返回显示符号所需symbol属性值
//				String devtypeId = null;
				String symbolId = null;
				SymbolPair symbol = eachRecord.getSymbol();
				if (symbol != null) {
					symbolId = symbol.getSymbolId().toString();
//					String HdevtypeId = symbol.getDevtypeId().toString();
//					if (HdevtypeId.isEmpty()) {
//						devtypeId = "";
//					} else {
//						devtypeId = String.valueOf(Integer.parseInt(
//								HdevtypeId.substring(2), 16));
//					}
				}
				// 返回DZGEOJSON内properties属性信息
				// properBody = ",\"properties\":{\"psrDef\":" + psrDefJSONOb
				// + fieldBody.toString() + ",\"DEVICETYPEID\":\""
				// + devtypeId + "\",\"DEVICESTATE\":\"" + symbolId
				// + "\"}";
				properBody = ",\"properties\":{\"psrDef\":" + psrDefJSONOb
						//+ ",\"fields\":" + fieldsJSONAr 
						+ fieldBody.toString()
						+ ",\"DEVICESTATE\":\"" + symbolId + "\"}";
				geometryBody = ",\"geometry\":" + eachRecord.getGeoJson();
				// bodyList.add(bodyHead + properBody + geometryBody +
				// bodyTail);
				bodys[counterBodys++] = bodyHead + properBody + geometryBody
						+ bodyTail;
			}
		}
		// int bodyCount = bodyList.size();
		int bodyCount = bodys.length;
		// 若查询无数据
		if (bodyCount == 0) {
			// bodyList.add(bodyHead + properBody + geometryBody + bodyTail);
			bodys[counterBodys++] = bodyHead + properBody + geometryBody
					+ bodyTail;
		}
		// StringBuffer body = new StringBuffer(bodyList.get(0));
		StringBuffer body = new StringBuffer(bodys[0]);
		if (bodyCount != 1) {
			for (int i = 1; i < bodyCount; i++) {
				body = body.append(",");
				// body = body.append(bodyList.get(i));
				body = body.append(bodys[i]);
			}
		}
		strgeojson = head + body.toString() + tail;
		// logger.info(strgeojson); //严重占时间
		long b = System.currentTimeMillis();
		logger.info("JSON转换时间:" + (b - a) + "ms");
		return strgeojson;
	}

	/**
	 * transform result to geojson
	 * 
	 * @param respResult
	 *            results get from MongoDB
	 * */
	public static String forGeoJsonForSpatialQuery(QueryResult[] respResult) {

		long a = System.currentTimeMillis();
		if (respResult == null) {
			return null;
		}
		// JSONObject obj = JSONObject.fromObject(resp);
		String strgeojson = "";
		String head = "{\"type\":\"FeatureCollection\","
				+ "\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"urn:ogc:def:crs:OGC:1.3:CRS84\"}},"
				+ "\"features\":[";
		String bodyHead = "{\"type\":\"Feature\"";
		String bodyTail = "}";
		String tail = "]}";
		// List<String> bodyList = new ArrayList<String>();

		int classCount = respResult.length;
		int featureCount = 0;
		for (int i = 0; i < classCount; ++i) {
			int size = respResult[i].getCount();
			featureCount += size;
		}
		String[] bodys = new String[featureCount];
		int counterBodys = 0;
		String properBody = ",\"properties\":{\"psrDef\":" + null
				+ ",\"DEVICETYPEID\":\"" + null + "\",\"DEVICESTATE\":\""
				+ null + "\"}";

		String geometryBody = ",\"geometry\":{\"type\":\"" + null
				+ "\",\"coordinates\":" + null + "}";
		for (int i = 0; i < classCount; i++) {
			// 得到每个classId对应的数据
			QueryResult eachResult = respResult[i];
			if (eachResult == null || eachResult.getRecords() == null) {
				continue;
			}
			JSONObject psrDefJSONOb = JSONObject.fromObject(eachResult
					.getPsrDef());
			int recordCount = eachResult.getRecords().length;
			QueryRecord[] record = eachResult.getRecords();

			// //返回professionName
			// String professionName = eachResult.getProfessionName();

			for (int j = 0; j < recordCount; j++) {
				// 得到每条记录对应数据
				QueryRecord eachRecord = record[j];
				if (eachRecord == null) {
					continue;
				}
				QueryField[] fields = eachRecord.getFields();
				JSONArray fieldsJSONAr = JSONArray.fromObject(fields);
				// 返回字段-值信息
				int fieldsNum = fields.length;
				StringBuffer fieldBody = new StringBuffer();
				String fieldName = "";
				String fieldValue = "";
				for (int k = 0; k < fieldsNum; k++) {
					if (fields[k].getFieldName() != null) {
						fieldName = fields[k].getFieldName().toString();
					}
					if (fields[k].getFieldValue() != null) {
						fieldValue = fields[k].getFieldValue().toString();
					}
					fieldBody.append(",\"" + fieldName + "\":\"" + fieldValue
							+ "\"");
				}
				// 返回显示符号所需symbol属性值
//				String devtypeId = null;
				String symbolId = null;
				SymbolPair symbol = eachRecord.getSymbol();
				if (symbol != null) {
					symbolId = symbol.getSymbolId().toString();
//					String HdevtypeId = symbol.getDevtypeId().toString();
//					if (HdevtypeId.isEmpty()) {
//						devtypeId = "";
//					} else {
//						devtypeId = String.valueOf(Integer.parseInt(
//								HdevtypeId.substring(2), 16));
//					}
				}
				// 返回DZGEOJSON内properties属性信息
				// properBody = ",\"properties\":{\"psrDef\":" + psrDefJSONOb+
				// fieldBody.toString() + ",\"DEVICETYPEID\":\""+ devtypeId +
				// "\",\"DEVICESTATE\":\"" + symbolId+ "\"}";
				properBody = ",\"properties\":{\"psrDef\":" + psrDefJSONOb
						+ ",\"fields\":" + fieldsJSONAr + fieldBody.toString()
						+ ",\"DEVICESTATE\":\"" + symbolId + "\"}";
				geometryBody = ",\"geometry\":" + eachRecord.getGeoJson();
				// bodyList.add(bodyHead + properBody + geometryBody +
				// bodyTail);
				bodys[counterBodys++] = bodyHead + properBody + geometryBody
						+ bodyTail;
			}
		}
		// int bodyCount = bodyList.size();
		int bodyCount = bodys.length;
		// 若查询无数据
		if (bodyCount == 0) {
			// bodyList.add(bodyHead + properBody + geometryBody + bodyTail);
			bodys[counterBodys++] = bodyHead + properBody + geometryBody
					+ bodyTail;
		}
		// StringBuffer body = new StringBuffer(bodyList.get(0));
		StringBuffer body = new StringBuffer(bodys[0]);
		if (bodyCount != 1) {
			for (int i = 1; i < bodyCount; i++) {
				body = body.append(",");
				// body = body.append(bodyList.get(i));
				body = body.append(bodys[i]);
			}
		}
		strgeojson = head + body.toString() + tail;
		// logger.info(strgeojson); //严重占时间
		long b = System.currentTimeMillis();
		logger.info("JSON转换时间:" + (b - a) + "ms");
		return strgeojson;
	}

	/**
	 * transform resut to geojson in the DZ
	 * 
	 * @param respResult
	 *            results get from MongoD
	 * */
	public static String forDZGeoJson(QueryResult[] respResult) {

		long a = System.currentTimeMillis();

		String strgeojson = "";
		String head = "{\"type\":\"FeatureCollection\","
				+ "\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"urn:ogc:def:crs:OGC:1.3:CRS84\"}},"
				+ "\"features\":[";
		String bodyHead = "{\"type\":\"Feature\"";
		String bodyTail = "}";
		String tail = "]}";
		List<String> bodyList = new ArrayList<String>();

		String properBody = ",\"properties\":{\"psrDef\":" + null
				+ ",\"DEVICETYPEID\":\"" + null + "\",\"DEVICESTATE\":\""
				+ null + "\"}";
		String geometryBody = ",\"geometry\":{\"type\":\"" + null
				+ "\",\"coordinates\":" + null + "}";

		if (respResult == null || respResult.length == 0) {
			return head + bodyHead + properBody + geometryBody + bodyTail
					+ tail;
		}

		int classCount = respResult.length;
		for (int i = 0; i < classCount; i++) {
			// 得到每个classId对应的数据
			QueryResult eachResult = respResult[i];
			if (eachResult == null || eachResult.getRecords() == null
					|| eachResult.getRecords().length == 0) {
				continue;
			}
			JSONObject psrDefJSONOb = JSONObject.fromObject(eachResult
					.getPsrDef());
			int recordCount = eachResult.getRecords().length;
			QueryRecord[] record = eachResult.getRecords();

			for (int j = 0; j < recordCount; j++) {
				// 得到每条记录对应数据
				QueryRecord eachRecord = record[j];
				if (eachRecord == null) {
					continue;
				}
				QueryField[] fields = eachRecord.getFields();
				// 返回字段-值信息
				int fieldsNum = fields.length;
				StringBuffer fieldBody = new StringBuffer();
				String fieldName = "";
				String fieldValue = "";
				for (int k = 0; k < fieldsNum; k++) {
					if (fields[k].getFieldName() != null) {
						fieldName = fields[k].getFieldName().toString();
					}
					if (fields[k].getFieldValue() != null) {
						fieldValue = fields[k].getFieldValue().toString();
					}
					// 只返回"OID","SBMC","符号角度",“符号大小”,"设备子类型"
					fieldBody.append(",\"" + fieldName + "\":\"" + fieldValue
							+ "\"");
				}

				// //返回symbol属性
				// 返回显示符号所需symbol属性值
//				String devtypeId = null;
				String symbolId = null;
				SymbolPair symbol = eachRecord.getSymbol();
				if (symbol != null) {
					symbolId = symbol.getSymbolId().toString();
//					String HdevtypeId = symbol.getDevtypeId().toString();
//					if (HdevtypeId.isEmpty()) {
//						devtypeId = "";
//					} else {
//						devtypeId = String.valueOf(Integer.parseInt(
//								HdevtypeId.substring(2), 16));
//					}
				}
				// 返回DZGEOJSON内properties属性信息
				properBody = ",\"properties\":{\"psrDef\":" + psrDefJSONOb
						+ fieldBody.toString() + ",\"DEVICESTATE\":\"" + symbolId
						+ "\"}";
				geometryBody = ",\"geometry\":" + eachRecord.getGeoJson();
				bodyList.add(bodyHead + properBody + geometryBody + bodyTail);
			}
		}

		int bodyCount = bodyList.size();
		// 若查询无数据
		if (bodyCount == 0) {
			bodyList.add(bodyHead + properBody + geometryBody + bodyTail);
		}

		StringBuffer body = new StringBuffer(bodyList.get(0));
		if (bodyCount != 1) {
			for (int i = 1; i < bodyCount; i++) {
				body = body.append(",");
				body = body.append(bodyList.get(i));
			}
		}
		strgeojson = head + body.toString() + tail;
		// logger.info(strgeojson); //严重占时间
		long b = System.currentTimeMillis();
		logger.info("JSON转换时间:" + (b - a) + "ms");
		return strgeojson;
	}

}
