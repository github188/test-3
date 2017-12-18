package nari.MongoQuery.QueryService.handler;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nari.Dao.interfaces.DbAdaptor;
import nari.MongoQuery.MongoQueryActivator;
import nari.MongoQuery.Adapter.SymbolAdapterImpl;
import nari.MongoQuery.Util.BsonUtil;
import nari.MongoQuery.Util.MongoDBUtil;
import nari.model.TableName;
import nari.model.bean.SymbolDef;
import nari.model.symbol.SymbolAdapter;
import nari.parameter.QueryService.StationIdQuery.QueryByStationIdRequest;
import nari.parameter.QueryService.StationIdQuery.QueryByStationIdResponse;
import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.SymbolPair;
import nari.parameter.code.ReturnCode;

import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import static com.mongodb.client.model.Filters.*;

/**
 * 查询过程中考虑字段类型，不都是String类型
 * */
public class QueryByStationIdHandler {
	
	private MongoCollection<BsonDocument> collectionDZ = null;
	
	private MongoCollection<BsonDocument> conf_modelrealtion = null;
	private SymbolAdapter symAdapter = null;
	private boolean isTopu = false;

	public QueryByStationIdHandler(){
		symAdapter = MongoQueryActivator.symbolAdapter;
		
	}
	/**
	 * */
	public QueryByStationIdResponse queryByStationId(QueryByStationIdRequest request){
		QueryByStationIdResponse resp = new QueryByStationIdResponse();
		
		isTopu = "true".equalsIgnoreCase(request.getIsTopu());
		
		String classId = "300000";	
		String OID = request.getSBID();
		
		if(collectionDZ == null){
			collectionDZ = MongoDBUtil.instance.getCollection(Integer.parseInt(classId));
		}
		
		String sbzlx = BsonUtil.BsonToString(collectionDZ.find(eq("OID", Integer.parseInt(OID))).first().get("SBZLX"));
		System.out.println("sbzlx:" + sbzlx);		
		if(conf_modelrealtion == null){
			conf_modelrealtion = MongoDBUtil.instance.getCollection(TableName.CONF_MODELRELATION);
		}
		// 查询所属电站，过滤设备子类型
		BsonDocument query = new BsonDocument();
		query.put("RELATIONFIELD", new BsonString("SSDZ"));
		query.put("PMODELID", new BsonInt32(Integer.parseInt(sbzlx)));
		MongoCursor<BsonDocument> cursor = conf_modelrealtion.find(query).batchSize(400).iterator();		
		List<Integer> filter_classId = new ArrayList<Integer>(300);	
		while(cursor.hasNext()){
			BsonDocument modeldoc = cursor.next();
			
			int rModelId = modeldoc.getInt32("RMODELID").getValue();
			int classid = MongoQueryActivator.RELATIONS.get(rModelId);
			
			if(filter_classId.contains(classid)){
				continue;
			}else{
				filter_classId.add(classid);
			}
		}
		cursor.close();
		
		int size = filter_classId.size();
		List<QueryResult> results = new ArrayList<QueryResult>(size);	
		try {
			int threadNum = 1;
			if(threadNum > 1){
				ExecutorService executor = Executors.newFixedThreadPool(threadNum);
				CountDownLatch threadSignal = new CountDownLatch(threadNum);
				
				GetDeviceInStation[] getDev = new GetDeviceInStation[threadNum];
				for(int i = 0; i < threadNum; ++i){
					getDev[i] = new GetDeviceInStation(threadSignal, threadNum, size, i, OID, filter_classId);
					executor.execute(getDev[i]);
				}
				
				threadSignal.await();
				executor.shutdown();
				for(int i = 0; i< threadNum; ++i){
					results.addAll(getDev[i].getResults());
				}
			}else{
				for(int i = 0; i < size; ++ i){
					int classID = filter_classId.get(i);
					System.out.println("classid " + classID);
					List<QueryRecord> records = new ArrayList<QueryRecord>(500);
				
					MongoCollection<BsonDocument> collection = MongoDBUtil.instance.getCollection(classID);
					if(collection == null){
							System.out.println("no data with modelid " + classID +"!");
							continue;
					}	
					MongoCursor<BsonDocument> cursor_ssdz = null;
					if(Long.parseLong(OID)> Integer.MAX_VALUE){
						cursor_ssdz = collection.find(eq("SSDZ", Long.parseLong(OID))).batchSize(100).iterator();
					}else{
						cursor_ssdz = collection.find(eq("SSDZ", Integer.parseInt(OID))).batchSize(100).iterator();
					}
					
					while(cursor_ssdz.hasNext()){
						BsonDocument dev = cursor_ssdz.next();
						QueryRecord record = new QueryRecord();
						
						QueryField[] fields = getFields(dev,isTopu);
						record.setFields(fields);
						
						// return shape
						String geojson = dev.get("Geometry").toString();
						record.setGeoJson(geojson);
						
						SymbolDef symdef = null;	
						String devModelId = BsonUtil.BsonToString(dev.get("SBZLX")); 
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
//							symbol.setDevtypeId(symdef.getDevTypeId());
							record.setSymbol(symbol);
						}
						records.add(record);
					}
					cursor_ssdz.close();
					
					if(records.size() > 0){
						QueryResult result = new QueryResult();
						result.setRecords(records.toArray(new QueryRecord[records.size()]));
						result.setCount(records.size());
						results.add(result);
					}	
				}
			}		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resp.setResult(results.toArray(new QueryResult[results.size()]));
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
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
		field.setFieldName("SBMC");
		field.setFieldValue(value);
		fieldList.add(field);
		
		value = MongoDBUtil.bsonToString(doc.get("DYDJ"));
		field.setFieldName("DYDJ");
		field.setFieldValue(value);
		fieldList.add(field);
		
		value = MongoDBUtil.bsonToString(doc.get("FHJD"));
		field.setFieldName("FHJD");
		field.setFieldValue(value);
		fieldList.add(field);
		
		value = MongoDBUtil.bsonToString(doc.get("SBZLX"));
		field.setFieldName("SBZLX");
		field.setFieldValue(value);
		fieldList.add(field);
		
		value =  MongoDBUtil.bsonToString(doc.get("SBID"));
		field.setFieldName("SBID");
		field.setFieldValue(value);
		fieldList.add(field);
		
		value =  MongoDBUtil.bsonToString(doc.get("FHDX"));
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
		field.setFieldName("DYZ");
		field.setFieldValue(dyz);
		fieldList.add(field);
		
		if(isTopu){
		field.setFieldName("CONNECTION");
		field.setFieldValue(MongoDBUtil.bsonToString(doc.get("CONNECTION")));
		fieldList.add(field);
		}
		
		QueryField[] fields = new QueryField[fieldList.size()];
		fields = fieldList.toArray(fields);
		return fields;
	}
	
	
	/**
	 * 
	 * */
	class GetDeviceInStation implements Runnable{
		
		private CountDownLatch threadsSignal; 
		
		private int tableStart;
		private int tableEnd;
		
		private String OID;
		private List<Integer> filterClassID;
		
		private List<QueryResult> results;
		/**
		 * */
		public GetDeviceInStation(CountDownLatch threadsSignal, int threadCount,int tableCount, int threadOrder, String OID,List<Integer> filterClassID){
			this.threadsSignal = threadsSignal;
			if(threadCount == 0){
				tableStart = 0;
				tableEnd = 0;
			}else{
				this.OID = OID;
				this.filterClassID = filterClassID;
				
				int mod = tableCount / threadCount;
				int remain = tableCount % threadCount;
				
				if(remain == 0){
					tableStart = mod * threadOrder;
					tableEnd = tableStart + mod - 1;
				}else{
					if(threadOrder > mod){
						if(mod != 0){
							tableStart = (mod + 1) * threadOrder + (threadOrder - remain) * mod;
							tableEnd = tableStart + mod - 1;
						}
						
					}else if(threadOrder == remain){
						if(mod != 0){
							tableStart = (mod + 1) * threadOrder;
							tableEnd = tableStart + mod - 1;
						}						
					}else{// threadOrder < remain
						tableStart = (mod + 1) * threadOrder;
						tableEnd = tableStart + mod;
					}
				}
				if(tableEnd >= tableCount){
					tableEnd = tableCount - 1;
				}
			}
			
		}
		
		@Override
		public  void run() {
			// TODO Auto-generated method stub
			results = new ArrayList<QueryResult>(tableEnd - tableStart);
			
			for(int i = tableStart; i <= tableEnd; ++ i){
				int classID = filterClassID.get(i);
				System.out.println("classid " + classID);
				List<QueryRecord> records = new ArrayList<QueryRecord>(500);
			
				MongoCollection<BsonDocument> collection = MongoDBUtil.instance.getCollection(classID);
				if(collection == null){
						System.out.println("no data with modelid " + classID +"!");
						continue;
				}	
				MongoCursor<BsonDocument> cursor_ssdz = collection.find(eq("SSDZ", OID)).batchSize(100).iterator();
				
				while(cursor_ssdz.hasNext()){
					BsonDocument dev = cursor_ssdz.next();
					QueryRecord record = new QueryRecord();
					
					// returnfields(OID,SBMC,SHAPE,symbol,DYDJ)
					QueryField[] fields = getFields(dev,isTopu);
					record.setFields(fields);
					
					// return shape
					String geojson = dev.get("Geometry").toString();
					record.setGeoJson(geojson);
					
					SymbolDef symdef = null;	
					String devModelId = dev.getString("SBZLX").getValue(); 
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
//						symbol.setDevtypeId(symdef.getDevTypeId());
						record.setSymbol(symbol);
					}
					records.add(record);
				}
				cursor_ssdz.close();
				
				if(records.size() > 0){
					QueryResult result = new QueryResult();
					result.setRecords(records.toArray(new QueryRecord[records.size()]));
					result.setCount(records.size());
					results.add(result);
				}	
			}
			threadsSignal.countDown();
		}//run
		
		/**
		 * */
		public List<QueryResult> getResults(){
			return results;
		}
	}
}


