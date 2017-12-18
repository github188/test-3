package nari.MongoQuery.QueryService.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.MongoQuery.MongoQueryActivator;
import nari.MongoQuery.Adapter.SymbolAdapterImpl;
import nari.MongoQuery.QueryService.bean.ThematicDevRequest;
import nari.MongoQuery.QueryService.bean.ThematicDevResponse;
import nari.MongoQuery.Util.BsonUtil;
import nari.MongoQuery.Util.MongoDBUtil;
import nari.model.TableName;
import nari.model.bean.FieldDetail;
import nari.model.bean.SymbolDef;
import nari.model.device.DeviceModel;
import nari.model.device.ModelService;
import nari.model.symbol.SymbolAdapter;
import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.SymbolPair;
import nari.parameter.code.ReturnCode;

import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonValue;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class ThematicDevHandler {

	private Logger logger = LoggerManager.getLogger(this.getClass());
	private DbAdaptor db = MongoQueryActivator.dbAdaptor;
	private ModelService ms = MongoQueryActivator.modelService;
	private SymbolAdapter symAdapter = MongoQueryActivator.symbolAdapter;
	
	private  MongoCollection<BsonDocument> collectionThematicDev = null;	//专题图设备集合
	private  MongoCollection<BsonDocument> collectionTexttureDev = null;	//图形设备集合
	
	//专题图属性名称
	String[] thematicPros = new String[]{"Geometry","FHDX","FHJD","SFBZ","BZDX","BZYS", "BZFW", "PLFS","DHZS","BZXSZD","BZNR","X","Y"};
	String[] textureFields = null;
//	private MongoCollection<BsonDocument> collectionTable = null;

	public ThematicDevResponse getThematicDev(ThematicDevRequest req) {
		ThematicDevResponse resp = new ThematicDevResponse();

		String mapId = req.getMapId(); // 图类型
		String documentId = req.getDocumentId(); // 图实例
		if ("".equalsIgnoreCase(mapId) || "".equalsIgnoreCase(documentId)) {
			logger.error("传入参数缺少必须值");
			resp.setCode(ReturnCode.NULL);
			return resp;
		}
		
//		if(collectionDocumentModel == null){
//			collectionDocumentModel = MongoDBUtil.instance.getCollection("CONF_DOCUMENTMODEL");
//		}
//		
//		//根据mapid查找对应图类型
//		BsonDocument documentModelQuery = new BsonDocument();
//		documentModelQuery.put("MAPID",new BsonInt32(Integer.parseInt(mapId)));
//		MongoCursor<BsonDocument> documentModelCursor = collectionDocumentModel.find(documentModelQuery).iterator();		
//		
//		while(documentModelCursor.hasNext()){
//		BsonDocument documentModeldoc = documentModelCursor.next();
//		String tableName = documentModeldoc.getString("geotablename".toUpperCase()).getValue();
		
		String thematicSql = "select * from " + TableName.CONF_DOCUMENTMODEL + " where mapId = "
				+ mapId;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = db.findAllMap(thematicSql);
		} catch (SQLException e) {
			logger.error("数据库查询出错");
			resp.setCode(ReturnCode.SQLERROR);
			return resp;
		}
		if(list == null || list.size() == 0){
			logger.error("无数据,传入值可能有误");
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}

		List<QueryResult> queryResultList = new ArrayList<QueryResult>();
		
		for (int i = 0; i < list.size(); i++) {		//mapid对应的每种设备类型层
			QueryResult result = new QueryResult();
			Map<String, Object> recordMap = list.get(i);
			
			String tableName = String.valueOf(recordMap.get("geotablename"));
			if(tableName.startsWith("T_TX")){
				continue;
			}
			
			//每种图形查询返回字段
			 String classId = String.valueOf(recordMap.get("classid"));
			 DeviceModel model = null;
			 try {
				 model = ms.fromClass(classId, false);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("模型创建时出错");
					resp.setCode(ReturnCode.BUILDMODEL);
					return resp;
			 }
			Iterator<FieldDetail> fieldIt = model.getFieldDef().details();
			List<String> textureFeildList = new ArrayList<String>();
			List<String> thematicProList = new ArrayList<String>();
			for(String thematicPro : thematicPros){
				thematicProList.add(thematicPro);
			}
			while (fieldIt.hasNext()) {
				FieldDetail field= fieldIt.next();
				String fieldName = field.getFieldName();
				if("shape".equalsIgnoreCase(fieldName)){	//去掉shape字段
					continue;
				}
				textureFeildList.add(fieldName);
			}
			textureFeildList.removeAll(thematicProList);		//去掉地理图中与专题图重复的属性
			textureFields = new String[textureFeildList.size()];
			textureFields = textureFeildList.toArray(textureFields);
			
			//得到每个专题图所有数据
			collectionTexttureDev = MongoDBUtil.instance.getCollection(Integer.parseInt(classId));
			//得到每个专题图所有数据
			collectionThematicDev = MongoDBUtil.instance.getCollection(tableName);
			
			//根据sstsl找到该专题图对应设备
			BsonDocument thematicDevQuery = new BsonDocument();
			thematicDevQuery.put("SSTSL",new BsonInt32(Integer.parseInt(documentId)));
			MongoCursor<BsonDocument> thematicDevCursor = collectionThematicDev.find(thematicDevQuery).iterator();
			
			
			List<QueryRecord> queryRecordList = new ArrayList<QueryRecord>();
			
			//每种专题图每一个设备
			while(thematicDevCursor.hasNext()){
				List<QueryField> recordFieldList = new ArrayList<QueryField>();
				QueryRecord record = new QueryRecord();
				//得到专题图设备记录
				BsonDocument thematicDevdoc = thematicDevCursor.next();
				//将专题图数据放入记录中
				getRecordFromBson(record, recordFieldList, thematicDevdoc, thematicPros);
				
				//得到txid从而查询图形表获得图形属性数据
				int txid = thematicDevdoc.get("TXID").asInt32().getValue();
				if("null".equalsIgnoreCase(String.valueOf(txid))){	//若无txId则去掉该设备
					continue;
				} 
				
				//根据txid得到专题图对应图形数据
				BsonDocument texttureDevQuery = new BsonDocument();
				texttureDevQuery.put("OID",new BsonInt32(txid));
				MongoCursor<BsonDocument> texttureDevCursor = collectionTexttureDev.find(texttureDevQuery).iterator();
				while(texttureDevCursor.hasNext()){	//得到第一个
					//得到图形设备记录
					BsonDocument texttureDevdoc = texttureDevCursor.next();
					//将专题图数据放入记录中
					getRecordFromBson(record, recordFieldList, texttureDevdoc, textureFields);
					break;
				}
				texttureDevCursor.close();
				 
				QueryField[] recordFields = new QueryField[recordFieldList.size()];
				recordFields = recordFieldList.toArray(recordFields);
				record.setFields(recordFields);
				 
				//将record放入recordList中
				queryRecordList.add(record);
			}
			thematicDevCursor.close();
			//得到所有record
			int recordCount = queryRecordList.size();
			QueryRecord[] records = new QueryRecord[recordCount];
			records = queryRecordList.toArray(records);
			//放入result中
			result.setRecords(records);
			result.setCount(recordCount);
			queryResultList.add(result);
		}
		//放入response
		QueryResult[] results = new QueryResult[queryResultList.size()];
		results = queryResultList.toArray(results);
		resp.setResult(results);
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}
	
	/**
	 * 将bson格式化放入一个Record中
	 * @param oldRecord
	 * @param doc
	 * @param fields
	 * @return
	 */
	private List<QueryField> getRecordFromBson(QueryRecord oldRecord, List<QueryField> recordFieldList,
			                                   BsonDocument doc, String[] fields){
		for(String field : fields){
			//把MongoGeometry数据放入record
			if("Geometry".equalsIgnoreCase(field)){
				BsonValue geoValue = doc.get("Geometry");
				if (geoValue != null){
					String geoJson =  geoValue.toString();
					oldRecord.setGeoJson(geoJson);
				}
				continue;
			}
			
			//把symbol放入record中
			if("SBZLX".equalsIgnoreCase(field)){
				SymbolDef symdef = null;
				BsonValue bsonValue = doc.get("SBZLX");
				if (bsonValue != null){
					String devModelId = BsonUtil.BsonToString(bsonValue);
					Iterator<SymbolDef> itertor = null;
					try {
						itertor = symAdapter.search(devModelId);
					} catch (Exception e) {
						e.printStackTrace();
					}
					symdef = SymbolAdapterImpl.searchSymbol(itertor, doc);
					if(symdef != null){
						SymbolPair symbol = new SymbolPair();
						symbol.setModelId(symdef.getModelId());
						symbol.setSymbolValue(symdef.getSymbolValue());
						symbol.setSymbolId(symdef.getSymbolId());
						oldRecord.setSymbol(symbol);
					}
				}
			}
			
			//把field放进record里
			BsonValue basonValue = doc.get(field);
			if (basonValue != null){
				String value = BsonUtil.BsonToString(basonValue);
				QueryField recordField = new QueryField();
				recordField.setFieldName(field);
				recordField.setFieldValue(value);
				recordFieldList.add(recordField);
			}
		}
		
		return recordFieldList;
	}
}



