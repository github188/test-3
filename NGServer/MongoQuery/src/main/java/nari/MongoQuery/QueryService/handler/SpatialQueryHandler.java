package nari.MongoQuery.QueryService.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.geotools.geojson.geom.GeometryJSON;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.vividsolutions.jts.geom.Coordinate;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.MongoClient.activator.MongoActivator;
import nari.MongoQuery.MongoQueryActivator;
import nari.MongoQuery.Adapter.SymbolAdapterImpl;
import nari.MongoQuery.Util.BsonUtil;
import nari.MongoQuery.Util.MongoDBUtil;

import nari.model.ModelActivator;
import nari.model.bean.FieldDef;
import nari.model.bean.FieldDetail;
import nari.model.bean.SymbolDef;
import nari.model.device.DeviceModel;
import nari.model.device.ModelService;
import nari.model.symbol.SymbolAdapter;
import nari.parameter.QueryService.SpatialQuery.SpatialQueryRequest;
import nari.parameter.QueryService.SpatialQuery.SpatialQueryResponse;
import nari.parameter.bean.GeometryPair;

import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.SymbolPair;
import nari.parameter.bean.TypeCondition;
import nari.parameter.code.ReturnCode;

/**
 * 查询过程中考虑字段类型，不都是String类型
 * */
public class SpatialQueryHandler {
	private Logger logger = LoggerManager.getLogger(this.getClass());
	private GeometryFactory geometryFactory = null;
	private SymbolAdapter symbolAdapter = null;
	/**
	 * 
	 * */
	public SpatialQueryHandler (){
		geometryFactory = new GeometryFactory();
		symbolAdapter = MongoQueryActivator.symbolAdapter;	
	}
	
	/**
	 * 空间查询执行函数
	 * @param request
	 * */
	public SpatialQueryResponse spatialQuery(SpatialQueryRequest request) {
		SpatialQueryResponse resp = new SpatialQueryResponse();
		GeometryPair geoPair = request.getGeom();
		BsonValue geoJson = getGeoJson(geoPair);
		BsonDocument spatialQuery = new BsonDocument();
		spatialQuery.append("Geometry", new BsonDocument().append("$geoIntersects", new BsonDocument().append("$geometry", geoJson)));

		TypeCondition[] cond = request.getConds();
		List<QueryResult> results = new ArrayList<QueryResult>(cond.length);
		
		for(int i = 0; i < cond.length; ++i) {
			if(cond[i].getPsrTypeSys().equals("classId")){
				int classId = Integer.parseInt(cond[i].getPsrType());
				QueryResult queryResult = getQueryResult(classId, cond[i], spatialQuery);
				if (null != queryResult && queryResult.getCount() > 0) {
					results.add(queryResult);
				}
			} else {
				String equId = cond[i].getPsrType();
				List<Integer> classIds = ModelActivator.getClassIdByEquId(equId);
				for (Integer classId : classIds) {
					QueryResult queryResult = getQueryResult(classId, cond[i], spatialQuery);
					if (null != queryResult && queryResult.getCount() > 0) {
						results.add(queryResult);
					}
				}
			}
		}
		if(results.size() > 0){
			resp.setResult(results.toArray(new QueryResult[results.size()]));
			resp.setCode(ReturnCode.SUCCESS);
		}else{
			resp.setCode(ReturnCode.NODATA);
		}
		return resp;
	}
	
	/**
	 * 根据geometrypair生成bson格式的geojson
	 * */
	private BsonValue getGeoJson(GeometryPair geoPair){
		String geometryType = geoPair.getGeometryType();
		
		double[] coords = geoPair.getCoords();
		int clength = coords.length / 2;
		Coordinate [] coordinates = new Coordinate[clength];
		for(int i = 0; i < clength; ++i){
			coordinates[i] = new Coordinate(coords[2 *i], coords[2 * i + 1]);
		}
		double bufferLen = geoPair.getOther();
		Polygon polygon = null;
		if(geometryType.equals("1")){
			Point point = geometryFactory.createPoint(coordinates[0]);
			polygon = (Polygon)point.buffer(bufferLen);
			
		}else if(geometryType.equals("2")){
			
			LineString line = geometryFactory.createLineString(coordinates);
			polygon = (Polygon)line.buffer(bufferLen);
			
		}else if(geometryType.equals("3")){
			LinearRing shell = geometryFactory.createLinearRing(coordinates);
			polygon = geometryFactory.createPolygon(shell, null);
		}else{
			logger.info("input data type error");
			return null;
		}
		GeometryJSON geojson = new GeometryJSON(12);
		OutputStream os = new ByteArrayOutputStream();

		try {
			geojson.write(polygon, os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BsonValue bsGeo = BsonDocument.parse(os.toString());
		return bsGeo;
		
	}
		
	private QueryResult getQueryResult(int classId, TypeCondition cond, BsonDocument spatialQuery) {
		
		MongoCollection<BsonDocument> model = MongoDBUtil.instance.getCollection(classId);
		if(model == null){
			logger.info("classid " + String.valueOf(classId) + "not found in database !");
			return null;
		}
		
		Map<String, String> alias = MongoQueryActivator.ALIAS.get(String.valueOf(classId));
		
		String[] queryField = cond.getReturnField();
		if (queryField == null || queryField.length == 0) {
			ModelService ms = MongoQueryActivator.modelService;
			DeviceModel modelDev = DeviceModel.NONE;
			try {
				modelDev = ms.fromClass(String.valueOf(classId), false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			FieldDef fieldDef = modelDev.getFieldDef();
			Iterator<FieldDetail> it = fieldDef.details();
			List<FieldDetail> fieldList = new ArrayList<FieldDetail>();
			while(it.hasNext()){
				FieldDetail fd = it.next();
				if( fd.getFieldName().equals("KZ") ||
				    fd.getFieldName().equals("RLBZ") ||
					fd.getFieldName().equals("SSKX") ){
					continue;
				}
				fieldList.add(fd);
			}
			
			int count = fieldList.size();
			queryField = new String[count];
			for(int j=0;j<count;j++){
				queryField[j] = fieldList.get(j).getFieldName();
			}
		}
		String collectionName = model.getNamespace().getCollectionName();
		Map<String, Integer> fieldTypes = MongoQueryActivator.FIELDTYPES.get(collectionName);
		
		if (fieldTypes == null) {
			return null;
		}
		
		BsonDocument conditionQuery = MongoDBUtil.getExps(fieldTypes, cond.getPairs(), cond.getLink());
		
		MongoCursor<BsonDocument> cursor = null;
		if(conditionQuery == null){
			cursor = model.find(spatialQuery).batchSize(500).iterator();
		}else{
			BsonDocument queryDoc = new BsonDocument();
			
			BsonArray query = new BsonArray();
			query.add(0, spatialQuery);
			query.add(1, conditionQuery);
			queryDoc.append("$and", query);
			cursor = model.find(queryDoc).batchSize(500).iterator();
		}
		if(cursor == null){
			logger.info("classid " + String.valueOf(classId) + " not found in spatialQuery");
			return null;
		}
		
		List<QueryRecord> records = getQueryRecords(classId, cursor, queryField, false, true);
		
		QueryResult result = new QueryResult();
		result.setRecords(records.toArray(new QueryRecord[records.size()]));
		result.setCount(records.size());
		return result;
		/**设备类型暂时不用添加*/
	}
	
	/**
	 * to get requied fields with curor of dataset searched, 
	 * @param queryfield
	 * @isQueryTopo
	 * @isQueryGeometry
	 * */
	private List<QueryRecord> getQueryRecords(int classId, MongoCursor<BsonDocument> cursor, String[] queryField, 
			boolean isQueryTopo, boolean isQueryGeometry){
		
		if(cursor != null){
			// deal with query results;
			List<QueryRecord> recordList = new ArrayList<QueryRecord>(1000);
			Map<String, String> alias = MongoQueryActivator.ALIAS.get(String.valueOf(classId));
			while (cursor.hasNext()){
				BsonDocument dev = cursor.next();
				
				QueryRecord record = new QueryRecord();
				
				List<QueryField> fieldList = new ArrayList<QueryField>();
				// 
				if(isQueryGeometry){
					String geoString = dev.get("Geometry").toString();
					record.setGeoJson(geoString);
				}
				
				if(isQueryTopo){
					
				}
				
				int qlength = queryField.length;
				for(int i = 0; i < qlength; ++i){
					QueryField singleField = new QueryField();	
					
					String fieldName = queryField[i];
					singleField.setFieldName(fieldName);
					
					if(dev.containsKey(fieldName)){
						String fieldValue = MongoDBUtil.bsonToString(dev.get(fieldName));
						if (fieldValue != null) {
							singleField.setFieldValue(fieldValue);
						}		
					}
					
					String alia = alias.get(fieldName);
					singleField.setFieldAlias(alia);
					
					fieldList.add(singleField);	
					// add "DYZ" field for real DYDJ 
					if(fieldName.equals("DYDJ")){
//						String dydj = dev.getString(fieldName).getValue();
						String dydj = BsonUtil.BsonToString(dev.get(fieldName));
						if(dydj == null || dydj.equals("")){
							continue;
						}else{
							String dyz = "";
							if(dydj.equalsIgnoreCase("0")){
								dyz = "0";
							}else{								
								String codename = MongoQueryActivator.CODEDEFID.get(dydj);
								if(codename.equals("") || codename == null){
									dyz = "0";
								}else{
									dyz = codename;
								}
							}
							QueryField fielddyz = new QueryField();
							fielddyz.setFieldName("DYZ");
							fielddyz.setFieldValue(dyz);
							
							fieldList.add(fielddyz);
						}//else
					}
				}//for
				
				record.setFields(fieldList.toArray(new QueryField[fieldList.size()]));
				recordList.add(record);
				
				SymbolDef symdef = null;	
				String devModelId = String.valueOf(dev.getInt32("SBZLX").getValue());
				Iterator<SymbolDef> itertor = null;
				try {
					itertor = symbolAdapter.search(devModelId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				symdef = SymbolAdapterImpl.searchSymbol(itertor, dev);
				if(symdef != null){
					SymbolPair symbol = new SymbolPair();
					symbol.setModelId(symdef.getModelId());
					symbol.setSymbolValue(symdef.getSymbolValue());
					symbol.setSymbolId(symdef.getSymbolId());
//					symbol.setDevtypeId(symdef.getDevTypeId());
					record.setSymbol(symbol);
				}
			}//while
			cursor.close();
			return recordList;
		}//if
		return null;
	}
}

