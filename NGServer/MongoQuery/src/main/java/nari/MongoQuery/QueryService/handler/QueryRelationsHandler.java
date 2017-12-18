package nari.MongoQuery.QueryService.handler;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonInt32;

import com.mongodb.CursorType;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;


import nari.Dao.interfaces.DbAdaptor;
import nari.Geometry.Coordinate;
import nari.Geometry.Geometry;
import nari.Geometry.GeometryCollection;
import nari.Geometry.GeometryType;
import nari.Geometry.Polyline;
import nari.MongoQuery.MongoQueryActivator;
import nari.MongoQuery.Adapter.SymbolAdapterImpl;
import nari.MongoQuery.QueryService.bean.QueryContainsRequest;
import nari.MongoQuery.QueryService.bean.QueryContainsResponse;
import nari.MongoQuery.QueryService.bean.QueryParentsRequest;
import nari.MongoQuery.QueryService.bean.QueryParentsResponse;
import nari.MongoQuery.QueryService.bean.QueryRelationsRequest;
import nari.MongoQuery.QueryService.bean.QueryRelationsResponse;
import nari.MongoQuery.Util.BsonUtil;
import nari.MongoQuery.Util.MongoDBUtil;
import nari.model.ModelActivator;
import nari.model.TableName;
import nari.model.bean.FieldDetail;
import nari.model.bean.GeometryDef;
import nari.model.bean.SubClassDef;
import nari.model.bean.SymbolDef;
import nari.model.device.Device;
import nari.model.device.DeviceModel;
import nari.model.device.ModelService;
import nari.model.device.ResultSet;
import nari.model.device.SpatialDevice;
import nari.model.device.filter.CriteriaBuilder;
import nari.model.device.filter.Expression;
import nari.model.relation.Relation;
import nari.model.relation.RelationDef;
import nari.model.symbol.SymbolAdapter;
import nari.parameter.bean.GeometryPair;
import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.SymbolPair;
import nari.parameter.code.PsrTypeSystem;
import nari.parameter.code.ReturnCode;

public class QueryRelationsHandler {

	private ModelService ms = MongoQueryActivator.modelService;
	private DbAdaptor db = MongoQueryActivator.dbAdaptor;
	private SymbolAdapter sym = MongoQueryActivator.symbolAdapter;

	static class RelationDeviceInfo {
		
		public static final RelationDeviceInfo NONE = new RelationDeviceInfo();
		public String modelId;
		public String oid;
		
		public RelationDeviceInfo() {
			this.modelId = null;
			this.oid = null;
		}
		
		public RelationDeviceInfo(String modelId, String oid) {
			this.modelId = modelId;
			this.oid = oid;
		}
	}
	
	/**
	 * 通用关系查询模块
	 * @param req
	 * @return
	 */
	public QueryRelationsResponse queryRelations(QueryRelationsRequest req) {
		
		QueryRelationsResponse resp = new QueryRelationsResponse();
		RelationDeviceInfo relationDeviceInfo = getDeviceInfo(req.getPsrType(), 
				req.getPsrId(), req.getPsrTypeSys());
		if (RelationDeviceInfo.NONE == relationDeviceInfo) {
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		
		Iterator<Relation> relations = getRelations(relationDeviceInfo.modelId, 
				req.getRelationTypes(), req.getRelationFields());
		
		boolean isFilter = false;
		Set<Integer> displayModelIds = 
				getDisplayModelIds(req.getDisplayPsrTypes(), req.getPsrTypeSys());
		if (displayModelIds != null) {
			isFilter = true;
		}
		
		List<QueryResult> queryResults = new ArrayList<QueryResult>();
		while (relations.hasNext()) {
			Relation relation = relations.next();
			
			if (isFilter && !displayModelIds.contains(relation.getRModelId())) {
				continue;
			}
			
			QueryResult queryResult = null;
			try {
				queryResult = getQueryResult(relation, relationDeviceInfo.oid);
			} catch (Exception e) {
				System.out.println("设备（modelId：" 
						+ relationDeviceInfo.modelId 
						+ "，oid：" 
						+ relationDeviceInfo.oid +"）针对关系（"
						+ relation + "）查询出错。");
				e.printStackTrace();
				continue;
			}
			if (null == queryResult) {
				continue;
			}
			queryResults.add(queryResult);
		}
		if (queryResults.isEmpty()) {
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		
		resp.setResult(queryResults.toArray(new QueryResult[queryResults.size()]));
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}

	
	/**
	 * 查询大馈线下设备，包含线路和电站
	 * @param req
	 * @return
	 */
	public QueryRelationsResponse queryFeederLine(QueryRelationsRequest req) {	
		QueryRelationsResponse resp = new QueryRelationsResponse();
		RelationDeviceInfo relationDeviceInfo = getDeviceInfo(req.getPsrType(), 
				req.getPsrId(), req.getPsrTypeSys());
		if (RelationDeviceInfo.NONE == relationDeviceInfo) {
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		
		Iterator<Relation> relations = getRelations(relationDeviceInfo.modelId, 
				req.getRelationTypes(), req.getRelationFields());
		
		boolean isFilter = false;
		Set<Integer> displayModelIds = getDisplayModelIds(req.getDisplayPsrTypes(), req.getPsrTypeSys());
		if (displayModelIds != null) {
			isFilter = true;
		}
		
		List<QueryResult> queryResults = new ArrayList<QueryResult>();
		while (relations.hasNext()) {
			Relation relation = relations.next();
			
			if (isFilter && !displayModelIds.contains(relation.getRModelId())) {
				continue;
			}
			
			QueryResult queryResult = null;
			try {
				queryResult = getQueryResult(relation, relationDeviceInfo.oid);
			} catch (Exception e) {
				System.out.println("设备（modelId：" 
						+ relationDeviceInfo.modelId 
						+ "，oid：" 
						+ relationDeviceInfo.oid +"）针对关系（"
						+ relation + "）查询出错。");
				e.printStackTrace();
				continue;
			}
			if (null == queryResult) {
				continue;
			}
			queryResults.add(queryResult);
		}
		
		//添加线路
		try {
			QueryResult queryResultXl = getQueryResultByTbName("T_TX_ZWYC_XL", "SSDKX", relationDeviceInfo.oid);
			if(queryResultXl != null){
				queryResults.add(queryResultXl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//添加电站
		try {
			QueryResult queryResultDz = getDzByFeedline(relationDeviceInfo.oid);
			if(queryResultDz != null){
				queryResults.add(queryResultDz);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (queryResults.isEmpty()) {
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		
		resp.setResult(queryResults.toArray(new QueryResult[queryResults.size()]));
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}

	
	/**
	 * 查询子设备
	 */
	public QueryContainsResponse queryContains(QueryContainsRequest req) {
		
		QueryContainsResponse resp = new QueryContainsResponse();
		RelationDeviceInfo relationDeviceInfo = getDeviceInfo(req.getPsrType(), 
				req.getPsrId(), req.getPsrTypeSys());
		if (RelationDeviceInfo.NONE == relationDeviceInfo) {
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		
		Iterator<Relation> relations = getRelations(relationDeviceInfo.modelId, 
				new String[]{ "CONTAIN" }, req.getRelationFields());
		
		boolean isFilter = false;
		Set<Integer> displayModelIds = 
				getDisplayModelIds(req.getDisplayPsrTypes(), req.getPsrTypeSys());
		if (displayModelIds != null) {
			isFilter = true;
		}
		
		List<QueryResult> queryResults = new ArrayList<QueryResult>();
		while (relations.hasNext()) {
			Relation relation = relations.next();
			
			if (isFilter && !displayModelIds.contains(relation.getRModelId())) {
				continue;
			}
			
			QueryResult queryResult = null;
			try {
				queryResult = getQueryResult(relation, relationDeviceInfo.oid);
			} catch (Exception e) {
				System.out.println("设备（modelId：" 
						+ relationDeviceInfo.modelId 
						+ "，oid：" 
						+ relationDeviceInfo.oid +"）针对关系（"
						+ relation + "）查询出错。");
				e.printStackTrace();
				continue;
			}
			queryResults.add(queryResult);
		}
		if (queryResults.isEmpty()) {
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		
		resp.setResult(queryResults.toArray(new QueryResult[queryResults.size()]));
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}
	
	
	/**
	 * 查询父设备
	 */
	public QueryParentsResponse queryParents(QueryParentsRequest req) {
		
		QueryParentsResponse resp = new QueryParentsResponse();
		RelationDeviceInfo relationDeviceInfo = getDeviceInfo(req.getPsrType(), 
				req.getPsrId(), req.getPsrTypeSys());
		if (RelationDeviceInfo.NONE == relationDeviceInfo) {
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		
		Iterator<Relation> relations = getRelations(relationDeviceInfo.modelId, 
				new String[]{ "PARENT" }, req.getRelationFields());
		
		boolean isFilter = false;
		Set<Integer> displayModelIds = 
				getDisplayModelIds(req.getDisplayPsrTypes(), req.getPsrTypeSys());
		if (displayModelIds != null) {
			isFilter = true;
		}
		
		List<QueryResult> queryResults = new ArrayList<QueryResult>();
		while (relations.hasNext()) {
			Relation relation = relations.next();
			
			if (isFilter && !displayModelIds.contains(relation.getRModelId())) {
				continue;
			}
			
			QueryResult queryResult = null;
			try {
				queryResult = getQueryResult(relation, relationDeviceInfo.oid);
			} catch (Exception e) {
				System.out.println("设备（modelId：" 
						+ relationDeviceInfo.modelId 
						+ "，oid：" 
						+ relationDeviceInfo.oid +"）针对关系（"
						+ relation + "）查询出错。");
				e.printStackTrace();
				continue;
			}
			queryResults.add(queryResult);
		}
		if (queryResults.isEmpty()) {
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		
		resp.setResult(queryResults.toArray(new QueryResult[queryResults.size()]));
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}
		
	private Set<Integer> getDisplayModelIds(String[] displayPsrTypes, String psrTypeSys) {
		
		if (displayPsrTypes == null || displayPsrTypes.length <= 0) {
			return null;
		}
		
		Set<Integer> displayModelIds = new HashSet<Integer>();
		if (PsrTypeSystem.EQUIPMENT_ID.equalsIgnoreCase(psrTypeSys)) {
			
			for (int i = 0; i < displayPsrTypes.length; i++) {
				List<Integer> classIds = ModelActivator.getClassIdByEquId(displayPsrTypes[i]);
				for (Integer classId : classIds) {
					List<String> modelIds = getModelIdsByClassId(classId.toString());
					for (String modelId : modelIds) {
						displayModelIds.add(Integer.valueOf(modelId));
					}
				}
			}
			
			
		} else if (PsrTypeSystem.CLASS_ID.equalsIgnoreCase(psrTypeSys)) {
			
			for (int i = 0; i < displayPsrTypes.length; i++) {
				List<String> modelIds = getModelIdsByClassId(displayPsrTypes[i]);
				for (String modelId : modelIds) {
					displayModelIds.add(Integer.valueOf(modelId));
				}
			}
			
		} else {
			
			for (int i = 0; i < displayPsrTypes.length; i++) {
				displayModelIds.add(Integer.valueOf(displayPsrTypes[i]));
			}
			
		}
		
		return displayModelIds;
		
	}
	
	private List<String> getModelIdsByClassId(String classId) {
			
			DeviceModel model = null;
			try {
				model = ms.fromClass(classId, false);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
			
			SubClassDef[] modelDefs = model.getSubClassDef();
			if (null == modelDefs) {
				return null;
			}
			List<String> modelIds = new ArrayList<String>(modelDefs.length);
			for (SubClassDef modelDef : modelDefs) {
				modelIds.add(modelDef.getModelId());
			}
			
			return modelIds;
		}
	
	private RelationDeviceInfo getDeviceInfo(String psrType, String psrId, String psrTypeSys) {
		
		RelationDeviceInfo relationDeviceInfo = RelationDeviceInfo.NONE;
		if (PsrTypeSystem.EQUIPMENT_ID.equalsIgnoreCase(psrTypeSys)) {
			// equipmentId
			List<Integer> classIds = ModelActivator.getClassIdByEquId(psrType);
			boolean hasModelIdAndOid = false;
			for (Integer classId : classIds) {
				relationDeviceInfo = getDeviceInfoByClass(classId.toString(), psrId);
				if (RelationDeviceInfo.NONE != relationDeviceInfo) {
					hasModelIdAndOid = true;
					break;
				}
			}
			if (!hasModelIdAndOid) {
				System.out.println("未找到的设备类型：(equipmentId)" 
						+ psrType 
						+ ", 设备ID：" 
						+ psrId);
			}
		} else if (PsrTypeSystem.CLASS_ID.equalsIgnoreCase(psrTypeSys)) {
			// classId
			relationDeviceInfo = getDeviceInfoByClass(psrType, psrId);
			if (RelationDeviceInfo.NONE == relationDeviceInfo) {
				System.out.println("未找到的设备类型：(classId)" 
						+ psrType
						+ ", 设备ID：" 
						+ psrId);
			}
		} else {
			// modelId
			relationDeviceInfo = getDeviceInfoBySubClass(psrType, psrId);
			if (RelationDeviceInfo.NONE == relationDeviceInfo) {
				System.out.println("未找到的设备子类型：(modelId)" 
						+ psrType 
						+ ", 设备ID：" 
						+ psrId);
			}
		}
		
		return relationDeviceInfo;
	}
		
	/**
	 * 根据ClassID和SBID获取ModelID和OID
	 * @param [in] classId
	 * @param [in] sbId
	 * @return 
	 */
	private RelationDeviceInfo getDeviceInfoByClass(String classId, String sbId) {
		
		DeviceModel model = null;
		try {
			model = ms.fromClass(classId, false);
		} catch (Exception e) {
			e.printStackTrace();
			return RelationDeviceInfo.NONE;
		}

		int classID = Integer.parseInt(classId);
		
		MongoCollection<BsonDocument> collection = MongoDBUtil.instance.getCollection(classID);
		if(collection == null){
				System.out.println("no data with modelid " + classId +"!");
				return null;
		}
		
		MongoCursor<BsonDocument> cursor = collection.find(eq("SBID", sbId)).batchSize(100).iterator();

		if(cursor.hasNext()){
			BsonDocument dev = cursor.next();
			String oid = MongoDBUtil.bsonToString(dev.get("OID"));
			
			String sbzlx = MongoDBUtil.bsonToString(dev.get("SBZLX"));
			
			return new RelationDeviceInfo(sbzlx, oid);
		}
		
		
		return RelationDeviceInfo.NONE;
	}
	
	/**
	 * 根据ClassID和SBID获取ModelID和OID
	 * @param [in] modelId
	 * @param [in] sbId
	 * @return
	 */
	private RelationDeviceInfo getDeviceInfoBySubClass(String modelId, String sbId) {
		
		DeviceModel model = null;
		try {
			model = ms.fromSubClass(modelId, false);
		} catch (Exception e) {
			e.printStackTrace();
			return RelationDeviceInfo.NONE;
		}
		
		int classID = Integer.parseInt(model.getClassDef().getClassId());
		
		MongoCollection<BsonDocument> collection = MongoDBUtil.instance.getCollection(classID);
		if(collection == null){
				System.out.println("no data with modelid " + classID +"!");
				return null;
		}
		
		MongoCursor<BsonDocument> cursor = collection.find(eq("SBID", sbId)).batchSize(100).iterator();

		if(cursor.hasNext()){
			BsonDocument dev = cursor.next();
			String oid = MongoDBUtil.bsonToString(dev.get("OID"));
			cursor.close();
			return new RelationDeviceInfo(modelId, oid);
		}
		
		cursor.close();
		
		return RelationDeviceInfo.NONE;
	}
		
	/**
	 * 获取所有的关系
	 * @param modelId
	 * @param relationTypes
	 * @param relationFields
	 * @return
	 */
	private Iterator<Relation> getRelations(String modelId, String[] relationTypes, String[] relationFields) {
		
		List<RelationDef> relationDefs = new ArrayList<RelationDef>();
		if (null != relationTypes && relationTypes.length > 0) {
			for (String relationType : relationTypes) {
				int relationId = getRelationId(relationType);
				RelationDef relationDef = null;
				try {
					relationDef = ms.getRelationDef(relationId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (null == relationDef) {
					continue;
				}
				relationDefs.add(relationDef);
			}
		}
		RelationDef[] relationDefArr = null;
		if (!relationDefs.isEmpty()) {
			relationDefArr = relationDefs.toArray(new RelationDef[relationDefs.size()]);
		}
		
		Iterator<Relation> relations = null;
		try {
			relations = ms.relateToSubClass(modelId, relationDefArr, relationFields);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return relations;
	}

	
	/**
	 * 
	 * @param classId
	 * @param relationField
	 * @param oid
	 * @return
	 */
	private QueryResult getQueryResultByTbName(String TableName, String fieldName, String oid) throws Exception {
		MongoCollection<BsonDocument> collection = MongoDBUtil.instance.getCollection(TableName);
		if(collection == null){
				System.out.println("no data with modelid " + TableName +"!");
				return null;
		}
		
		MongoCursor<BsonDocument> cursor = null;
		if(Long.parseLong(oid)> Integer.MAX_VALUE){
			cursor = collection.find(eq(fieldName, Long.parseLong(oid))).iterator();
		}else{
			cursor = collection.find(eq(fieldName, Integer.parseInt(oid))).iterator();
		}

		List<QueryRecord> records = new ArrayList<QueryRecord>(500);
		
		while(cursor.hasNext()){
			BsonDocument dev = cursor.next();
			QueryRecord record = new QueryRecord();
			
			QueryField[] fields = getFields(dev,false);
			record.setFields(fields);
			
			// return shape
			String geojson = dev.get("Geometry").toString();
			record.setGeoJson(geojson);
			
			SymbolDef symdef = null;	
			String devModelId = dev.getString("SBZLX").getValue(); 
			Iterator<SymbolDef> itertor = null;
			try {
				itertor = sym.search(devModelId);
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
//				symbol.setDevtypeId(symdef.getDevTypeId());
				record.setSymbol(symbol);
			}
			records.add(record);
		}
		cursor.close();
		
		if(records.size() > 0){
			QueryResult result = new QueryResult();
			result.setRecords(records.toArray(new QueryRecord[records.size()]));
			result.setCount(records.size());
			return result;
		}
		else{
			return null;
		}
	}
	
	
	/**
	 * 
	 * @param classId
	 * @param relationField
	 * @param oid
	 * @return
	 */
	private QueryResult getDzByFeedline(String feedLineID) throws Exception {
		//先在关联表查电站oid
		String tableName = "T_TX_ZWYC_SBSSDKX";
		MongoCollection<BsonDocument> collection = MongoDBUtil.instance.getCollection(tableName);
		if(collection == null){
				System.out.println("no data with modelid " + tableName +"!");
				return null;
		}
		
		String fieldName = "DKXID";
		MongoCursor<BsonDocument> cursor = null;
		if(Long.parseLong(feedLineID)> Integer.MAX_VALUE){
			cursor = collection.find(eq(fieldName, Long.parseLong(feedLineID))).iterator();
		}else{
			cursor = collection.find(eq(fieldName, Integer.parseInt(feedLineID))).iterator();
		}
		
		//MongoCursor<BsonDocument> cursor = collection.find().batchSize(100).iterator();
		
		BsonDocument dzOidConExp = new BsonDocument();
		BsonArray oidArr = new BsonArray();
		while(cursor.hasNext()){
			BsonDocument dev = cursor.next();
			oidArr.add(dev.get("TXID"));
		}
		
		cursor.close();
		
		if(oidArr.size() != 0){
			//再根据电站oid集合查电站数据
			dzOidConExp.append("OID", new BsonDocument().append("$in", oidArr));

			MongoCollection<BsonDocument> dzColl = MongoDBUtil.instance.getCollection("T_TX_ZNYC_DZ");
			MongoCursor<BsonDocument> cursorDz = dzColl.find(dzOidConExp).iterator();
			
			List<QueryRecord> records = new ArrayList<QueryRecord>(500);
			
			while (cursorDz.hasNext()) {
				BsonDocument dz = cursorDz.next();
				
				QueryRecord record = new QueryRecord();
				
				QueryField[] fields = getFields(dz,false);
				record.setFields(fields);
				
				// return shape
				String geojson = dz.get("Geometry").toString();
				record.setGeoJson(geojson);
				
				SymbolDef symdef = null;	
				String devModelId = MongoDBUtil.bsonToString(dz.get("SBZLX"));
				Iterator<SymbolDef> itertor = null;
				try {
					itertor = sym.search(devModelId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				symdef = SymbolAdapterImpl.searchSymbol(itertor, dz);
				if(symdef != null){
					SymbolPair symbol = new SymbolPair();
					symbol.setModelId(symdef.getModelId());
					symbol.setSymbolValue(symdef.getSymbolValue());
					symbol.setSymbolId(symdef.getSymbolId());
//					symbol.setDevtypeId(symdef.getDevTypeId());
					record.setSymbol(symbol);
				}
				records.add(record);
			}
			
			cursorDz.close();
			
			if(records.size() > 0){
				QueryResult result = new QueryResult();
				result.setRecords(records.toArray(new QueryRecord[records.size()]));
				result.setCount(records.size());
				return result;
			}
			else{
				return null;
			}
		}
		return null;
	}
	
	
	
	/**
	 * 根据一个关系和OID获取查询结果
	 * @param relation
	 * @param oid
	 * @return
	 */
	private QueryResult getQueryResult(Relation relation, String oid) throws Exception {
		
		DeviceModel model = ms.fromSubClass(String.valueOf(relation.getRModelId()), false);
		
		Iterator<FieldDetail> fieldIter = model.getFieldDef().details();
		List<String> returnFieldList = new ArrayList<String>();
		while (fieldIter.hasNext()) {
			String fieldName = fieldIter.next().getFieldName();
			returnFieldList.add(fieldName);
		}
		
		int classID = Integer.parseInt(model.getClassDef().getClassId());
		
		MongoCollection<BsonDocument> collection = MongoDBUtil.instance.getCollection(classID);
		if(collection == null){
				System.out.println("no data with modelid " + classID +"!");
				return null;
		}
		
		MongoCursor<BsonDocument> cursor = null;
		String fieldName = relation.getRelationField();
		if(Long.parseLong(oid)> Integer.MAX_VALUE){
			cursor = collection.find(eq(fieldName, Long.parseLong(oid))).iterator();
		}else{
			cursor = collection.find(eq(fieldName, Integer.parseInt(oid))).iterator();
		}
		
		List<QueryRecord> records = new ArrayList<QueryRecord>(500);
		
		while(cursor.hasNext()){
			BsonDocument dev = cursor.next();
			QueryRecord record = new QueryRecord();
			
			QueryField[] fields = getFields(dev,false);
			record.setFields(fields);
			
			// return shape
			String geojson = dev.get("Geometry").toString();
			record.setGeoJson(geojson);
			
			SymbolDef symdef = null;	
			String devModelId = MongoDBUtil.bsonToString(dev.get("SBZLX")); 
			Iterator<SymbolDef> itertor = null;
			try {
				itertor = sym.search(devModelId);
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
//				symbol.setDevtypeId(symdef.getDevTypeId());
				record.setSymbol(symbol);
			}
			records.add(record);
		}
		cursor.close();
		
		if(records.size() > 0){
			QueryResult result = new QueryResult();
			result.setRecords(records.toArray(new QueryRecord[records.size()]));
			result.setCount(records.size());
			return result;
		}
		else{
			return null;
		}
	}

	
	/**
	 * 
	 * */
	private QueryField[] getFields(BsonDocument doc,boolean isTopu){
		List<QueryField> fieldList = new ArrayList<QueryField>();
		
		String value = MongoDBUtil.bsonToString(doc.get("OID"));
		QueryField field = new QueryField();
		field.setFieldName("OID");
		field.setFieldValue(value);
		fieldList.add(field);
		
		value = MongoDBUtil.bsonToString(doc.get("SBMC"));
		field = new QueryField();
		field.setFieldName("SBMC");
		field.setFieldValue(value);
		fieldList.add(field);
		
		value = MongoDBUtil.bsonToString(doc.get("DYDJ"));
		field = new QueryField();
		field.setFieldName("DYDJ");
		field.setFieldValue(value);
		fieldList.add(field);
		
		value = MongoDBUtil.bsonToString(doc.get("FHJD"));
		field = new QueryField();
		field.setFieldName("FHJD");
		field.setFieldValue(value);
		fieldList.add(field);
		
		value = MongoDBUtil.bsonToString(doc.get("SBZLX"));
		field = new QueryField();
		field.setFieldName("SBZLX");
		field.setFieldValue(value);
		fieldList.add(field);
		
		value =  MongoDBUtil.bsonToString(doc.get("SBID"));
		field = new QueryField();
		field.setFieldName("SBID");
		field.setFieldValue(value);
		fieldList.add(field);
		
		value =  MongoDBUtil.bsonToString(doc.get("FHDX"));
		field = new QueryField();
		field.setFieldName("FHDX");
		field.setFieldValue(value);
		fieldList.add(field);
		
		String DYDJ = BsonUtil.BsonToString(doc.get("DYDJ"));
		
		String dyz ="";
		if(DYDJ.equalsIgnoreCase("0")){
			dyz = "0";
		}else{
			String sql = "select * from " + TableName.CONF_CODEDEFINITION + " where codeid = 10401 and codedefid = " + value;
			try {
				Map<String, Object> codeFieldMap = MongoQueryActivator.dbAdaptor.findMap(sql);
				if(codeFieldMap == null){
					dyz = "0";
				}else{
					if(codeFieldMap.get("codename") == null){
						dyz = "0";
					}else{
						dyz = String.valueOf(codeFieldMap.get("codename"));
					}
				}	
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		field = new QueryField();
		field.setFieldName("DYZ");
		field.setFieldValue(dyz);
		fieldList.add(field);
		
		if(isTopu){
			field = new QueryField();
			field.setFieldName("CONNECTION");
			field.setFieldValue(MongoDBUtil.bsonToString(doc.get("CONNECTION")));
			fieldList.add(field);
		}
		
		QueryField[] fields = new QueryField[fieldList.size()];
		fields = fieldList.toArray(fields);
		return fields;
	}
	

	/**
	 * 修复值中的特殊字符
	 * @param value
	 * @return
	 */
	private String repairSpacialChar(String value) {
		if (value.contains(":") || value.contains("") || value.contains("'")
				|| value.contains("{") || value.contains("}")
				|| value.contains("\"") || value.contains(",")
				|| value.contains("[") || value.contains("]")
				|| value.contains("\t") || value.contains("\r")
				|| value.contains("\f") || value.contains("\b")
				|| value.contains("\n") || value.contains("\"")
				|| value.contains(" ") || value.contains("～")) {

			value = value.replaceAll(":", "；");
			value = value.replaceAll("\\\\", "");
			value = value.replaceAll("'", "");
			value = value.replaceAll("\"", "");
			value = value.replaceAll("\t", "");
			value = value.replaceAll("\r", "");
			value = value.replaceAll("\f", "");
			value = value.replaceAll("\n", "");
			value = value.replaceAll("～", "_");
		}
		return value;
	}

	private int getRelationId(String relationType) {
		if (relationType.equalsIgnoreCase("CONTAIN")) {
			return 3002;
		} else if (relationType.equalsIgnoreCase("PARENT")) {
			return 3001;
		}
		return 0;
	}
}
