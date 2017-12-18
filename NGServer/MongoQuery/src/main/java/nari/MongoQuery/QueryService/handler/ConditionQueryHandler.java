package nari.MongoQuery.QueryService.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.BsonDocument;
import org.bson.BsonValue;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
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
import nari.parameter.QueryService.ConditionQuery.ConditionQueryRequest;
import nari.parameter.QueryService.ConditionQuery.ConditionQueryResponse;
import nari.parameter.bean.Link;
import nari.parameter.bean.Pair;
import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.SymbolPair;
import nari.parameter.bean.TypeCondition;
import nari.parameter.code.ReturnCode;
/**
 * 条件查询执行类，查询过程中考虑字段类型，不都是String类型
 * */
public class ConditionQueryHandler {
	
	private static final int MAX_RESULT_COUNT = 100; // 最大记录数量
	private SymbolAdapter symAdapter = null;
	
	/**
	 * 
	 * */
	public ConditionQueryHandler(){
		symAdapter = MongoQueryActivator.symbolAdapter;
	}	
	
	/**
	 * 根据条件查询 
	 * @throws Exception 
	 * */
	public ConditionQueryResponse queryByCondition(ConditionQueryRequest request) throws Exception{
		
		ConditionQueryResponse resp = new ConditionQueryResponse();
		TypeCondition[] conditions = request.getCondition();
		List<QueryResult> results = new ArrayList<QueryResult>(conditions.length);
		Integer maxSize = Integer.valueOf(0);
		for (int i = 0; i < conditions.length; ++i){
			
			if(maxSize >= MAX_RESULT_COUNT){
				continue;
			}
			
			if (conditions[i].getPsrTypeSys().equals("classId")) {
				int classId = Integer.parseInt(conditions[i].getPsrType());
				QueryResult result = getQueryResult(classId, conditions[i], request, maxSize);
				if (null != result && result.getCount() > 0) {
					results.add(result);
				}	
			} else {
				String equId = conditions[i].getPsrType();
				List<Integer> classIds = ModelActivator.getClassIdByEquId(equId);
				for (Integer classId : classIds) {
					QueryResult result = getQueryResult(classId, conditions[i], request, maxSize);
					if (null != result && result.getCount() > 0) {
						results.add(result);
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
	 * 获取一个QueryResult
	 * @param classID
	 * @param cond
	 * @param request
	 * @param maxSize
	 * @return
	 */
	private QueryResult getQueryResult(int classID, TypeCondition cond, ConditionQueryRequest request, Integer maxSize) {
		
		MongoCollection<BsonDocument> collection = MongoDBUtil.instance.getCollection(classID);
		
		// combination of condition
		Pair[] pairs = cond.getPairs();
		MongoCursor<BsonDocument> cursor = null;
		if(pairs != null && pairs.length != 0){
			Link link = cond.getLink();	
			String collectionName = collection.getNamespace().getCollectionName();
			Map<String, Integer> fieldTypes = MongoQueryActivator.FIELDTYPES.get(collectionName);
			if(fieldTypes != null) {
				cursor = collection.find(MongoDBUtil.getExps(fieldTypes, pairs, link)).iterator();
				if(cursor == null) {
					return null;
				}
			} else {
				return null;
			}
		}

		String[] queryField = cond.getReturnField();
		if(queryField == null || queryField.length == 0){
			ModelService ms = MongoQueryActivator.modelService;
			DeviceModel model = DeviceModel.NONE;
			try {
				model = ms.fromClass(String.valueOf(classID), false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			FieldDef fieldDef = model.getFieldDef();
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
		
		boolean isQueryGeometry = request.isQueryGeometry();
		boolean isQueryTopo = request.isQueryTopo();
		
		List<QueryRecord> records = getQueryRecords(classID, cursor, queryField, isQueryTopo, true);

		QueryResult result = new QueryResult();
		result.setRecords(records.toArray(new QueryRecord[records.size()]));
		result.setCount(records.size());
		maxSize += records.size();
		return result;
	}

	/**
	 * 获取QueryResult中的记录
	 * @param classId
	 * @param cursor
	 * @param queryField
	 * @param isQueryTopo
	 * @param isQueryGeometry
	 * @return
	 */
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
				if (isQueryGeometry) {
					BsonValue geoValue = dev.get("Geometry");
					if (null !=  geoValue) {
						String geoString = geoValue.toString();
						record.setGeoJson(geoString);
					}
				}
				
				if (isQueryTopo) {
					// 拓扑字段，暂不处理
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
					itertor = symAdapter.search(devModelId);
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
