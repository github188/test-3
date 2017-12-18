package nari.MongoQuery.Util;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import nari.MongoQuery.MongoConfig;
import nari.MongoQuery.MongoQueryActivator;

import nari.parameter.bean.Link;
import nari.parameter.bean.Operator;
import nari.parameter.bean.Pair;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonRegularExpression;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;



import static com.mongodb.client.model.Filters.*;
/**
 * Singleton of the database operator, include get database, collection and someother functions
 * */
public enum MongoDBUtil {
	/*
	 * define a enum element, represent instance of the class*/
	instance;
	
	private MongoClient mongoClient;
	private MongoDatabase dwzy;
	static{
		System.out.println("====init MongoDBUtil========");
		
		Builder options = new MongoClientOptions.Builder();
		options.connectionsPerHost(Integer.parseInt(MongoQueryActivator.mongoConfig.getConnectionsPerHost()));
		options.connectTimeout(15000);
		options.maxWaitTime(5000);
		options.socketTimeout(0);
		options.threadsAllowedToBlockForConnectionMultiplier(5);
		options.socketKeepAlive(true);
		
//		options.writeConcern(WriteConcern.SAFE);
		MongoConfig mongoConfig = MongoQueryActivator.mongoConfig;
		ServerAddress serverAddress = new ServerAddress(mongoConfig.getMongoAddress(), Integer.parseInt(mongoConfig.getMongoPort()));
		
		if (mongoConfig.isEncryption()) {
			// 建立加密连接
			MongoCredential credential = MongoCredential.createCredential("root", "admin", "P@ssw0rd".toCharArray());
			instance.mongoClient = new MongoClient(serverAddress, Arrays.asList(credential), options.build());
		} else {
			instance.mongoClient = new MongoClient(serverAddress, options.build());
		}
		
		instance.dwzy = instance.mongoClient.getDatabase(mongoConfig.getMongoDbName());
	}
	/**
	 * Get db -certain database named dbName
	 * @param dbname
	 * @return
	 * */
	public MongoDatabase getDB(String dbName){
		if(dbName != null && !"".equals(dbName)){
			if(!dbName.equals("jsdwzy")){
				MongoDatabase database  = mongoClient.getDatabase(dbName);
				return database;
			}else{
				if(dwzy == null){
					return dwzy = mongoClient.getDatabase(dbName);
				}else{
					return dwzy;
				}
				
			}
		}
		return null;
	}
	/**
	 * Get collection - certained collection named collName in 
	 * @param dbName name of the database
	 * @param collection name of the collection
	 * */
	public MongoCollection<BsonDocument> getCollection(String dbName, String collection){
		if(null == collection || "".equals(collection)){
			return null;
		}
		if(null == dbName || "".equals(dbName)){
			return null;
		}		
		MongoCollection<BsonDocument> result = mongoClient.getDatabase(dbName).getCollection(collection, BsonDocument.class);
		return result;
	}
	
	/**
	 * Get collection with collection name
	 * @param collection name of the collection in database(default jsdwzy)
	 * */
	public MongoCollection<BsonDocument> getCollection(String collection){
		if(null == collection || "".equals(collection)){
			return null;
		}
		if(dwzy == null){
			dwzy = instance.mongoClient.getDatabase("jsdwzy");
		}
		MongoCollection<BsonDocument> result = dwzy.getCollection(collection, BsonDocument.class);
		return result;
	}
	/**
	 * Get collection with modelid
	 * @param modelid modelid of the collection
	 * */
	public MongoCollection<BsonDocument> getCollectionByModelID(int modelid){
		int classId = MongoQueryActivator.RELATIONS.get(modelid);
		return getCollection(classId);
		
	}
	
	/**
	 * Get colleciton with database name and classid
	 * @param dbName name of the database
	 * @param classId classid of the collection
	 * */
	public MongoCollection<BsonDocument> getCollection(String dbName, int classId){
		String modelname = MongoQueryActivator.tables.get(String.valueOf(classId));
		return getCollection(dbName, modelname);
	}
	
	/**
	 * Get colleciton with database name and classid, default database is jsdwzy
	 * @param classId  classid of the collection 
	 * */
	public MongoCollection<BsonDocument> getCollection(int classId){
		String modelname = MongoQueryActivator.tables.get(String.valueOf(classId));
		return getCollection(modelname);
	}
	
//	/**
//	 * Get one doc with 
//	 * @param dbName
//	 * @param collection
//	 * @param pairs
//	 * @param link
//	 * */
//	public BsonDocument getOneDoc(String dbName, String collection, Pair[] pairs, Link link){
//		BsonDocument comExp = null;
//		if(pairs != null && pairs.length != 0){
//			int pairLength = pairs.length;
//			BsonDocument[] exps = new BsonDocument[pairLength];
//			
//			for (int j = 0; j < pairLength; ++j){
//				String key = pairs[j].getKey();
//				String value = pairs[j].getValue();
//				Operator op = pairs[j].getOp();
//				exps[j] = getExp(key, value, op);
//			}
//			
//			if(link != null)
//				comExp = combinaExps(exps, link);
//			else
//				comExp = exps[0];	
//		}//if
//		MongoCollection<BsonDocument> collect = getCollection(dbName, collection);
//		BsonDocument result = collect.find(comExp).iterator().next().asDocument();
//		return result;
//	}
//	/**
//	 * 
//	 * @param collection
//	 * @param pairs
//	 * @param link
//	 * */
//	public BsonDocument getOneDoc(MongoCollection<BsonDocument> collection, Pair[] pairs, Link link){
//		BsonDocument comExp = null;
//		if(pairs != null && pairs.length != 0){
//			int pairLength = pairs.length;
//			BsonDocument[] exps = new BsonDocument[pairLength];
//			
//			for (int j = 0; j < pairLength; ++j){
//				String key = pairs[j].getKey();
//				String value = pairs[j].getValue();
//				Operator op = pairs[j].getOp();
//				exps[j] = getExp(key, value, op);
//			}
//			
//			if(link != null)
//				comExp = combinaExps(exps, link);
//			else
//				comExp = exps[0];	
//		}//if
//		BsonDocument result = collection.find(comExp).iterator().next().asDocument();
//		return result;
//	}
	/*
	 * get device symbol info
	 * @param device 
	 * @return cursor  
	 * */
//	public MongoCursor<BsonDocument> getCousor(BsonDocument device, String collection, String key, Operator op, String value){
//		String SBZLX = device.get("SBZLX").toString();
//		MongoCollection<BsonDocument> symbol = MongoDBUtil.instance.getCollection("jsdwzy", "modelsymbol");  
//		MongoCursor<BsonDocument> result = symbol.find(eq("modelid",SBZLX)).iterator(); 
//		return result;
//	}

	/**
	 * 
	 * */
	public List<String> getDefaultCollectionNames() {
		List<String> tableNames = new ArrayList<String>();
		if(dwzy != null){
			MongoCursor<String> cursor = dwzy.listCollectionNames().iterator();
			while(cursor.hasNext()){
				String name = cursor.next();
				tableNames.add(name);
			}
			cursor.close();
		}
		
		return tableNames;
	}
	
	/**
	 * 
	 * */
	public Map<String, Map<String, Integer>>getSchemas(List<String> collections){
		Iterator<String> iter = collections.iterator();
		Map<String, Map<String, Integer>> schemas = new HashMap<String, Map<String, Integer>>();
		while(iter.hasNext()){
			String name = iter.next();
			MongoCollection<BsonDocument> collection = getCollection(name);
			BsonDocument doc = collection.find().batchSize(1).first();
			if(doc != null){
				Map<String, Integer> schema = new HashMap<String, Integer>();
				for(Entry<String, BsonValue> entry: doc.entrySet()){
					String field = entry.getKey();
					int type = entry.getValue().getBsonType().ordinal();
					schema.put(field, type);
				}
				schemas.put(name, schema);
			}
		}
		return schemas;
	}
	
	
	/**
	 * 
	 */
	public MongoCollection<BsonDocument> creatCollection(String tableName){
		if(dwzy == null){
			dwzy = instance.mongoClient.getDatabase("jsdwzy");
		}
		dwzy.createCollection(tableName);
		return dwzy.getCollection(tableName, BsonDocument.class);
	}
	
	/**
	 * 
	 * @param pairs conditions 
	 * @param link  operation mark
	 * @param collection
	 * @return cursor mongocursor of search result in collection
	 * */
	public MongoCursor<BsonDocument> find(MongoCollection<BsonDocument> collection, Map<String, Integer> fieldTypes, Pair[] pairs, Link link){
		BsonDocument comExp = null;
		if(pairs != null && pairs.length != 0){
			int pairLength = pairs.length;
			BsonDocument[] exps = new BsonDocument[pairLength];
			
			for (int j = 0; j < pairLength; ++j){
				String key = pairs[j].getKey();
				String value = pairs[j].getValue();
				Operator op = pairs[j].getOp();
				exps[j] = getExp(fieldTypes, key, value, op);
			}
			
			if(link != null)
				comExp = combinaExps(exps, link);
			else
				comExp = exps[0];	
		}else{
			return null;
		}
		return collection.find(comExp).limit(100).iterator();
	}
	
	/**
	 * get query expressions 
	 * @param pairs
	 * @link link
	 * */
	public static BsonDocument getExps(Map<String, Integer> fieldTypes, Pair[] pairs, Link link){
		BsonDocument comExp = null;
		if(pairs != null && pairs.length != 0){
			int pairLength = pairs.length;
			BsonDocument[] exps = new BsonDocument[pairLength];
			
			for (int j = 0; j < pairLength; ++j){
				String key = pairs[j].getKey();
				String value = pairs[j].getValue();
				Operator op = pairs[j].getOp();
				exps[j] = getExp(fieldTypes, key, value, op);
			}
			
			if(link != null){
				comExp = combinaExps(exps, link);
			}			
			else{
				comExp = exps[0];
			}
			return comExp;
					
		}else{
			return null;
		}
	}
	/**
	 * @param key
	 * @param value
	 * @param op
	 * */ 
		public static BsonDocument getExp(Map<String, Integer> fieldTypes, String key, String value, Operator op){
			
			BsonDocument exp = new BsonDocument();
			BsonValue bsonValue = BsonUtil.getRealBsonValue(key, value, fieldTypes.get(key));
			switch(op){
			case EQ:
				exp.append(key, bsonValue);
				break;
			case LT:
				exp.append(key, new BsonDocument().append("$lt", bsonValue));
				break;
			case LTE:
				exp.append(key, new BsonDocument().append("$lte", bsonValue));
			case MT:
				exp.append(key, new BsonDocument().append("$gte", bsonValue));
				break;
			case LIKE:
				if(value.equals("")){
					return exp;
				}else{
					Pattern pattern = Pattern.compile("^.*" + value + ".*$", Pattern.CASE_INSENSITIVE);
					exp.put(key, new BsonRegularExpression(pattern.toString()));
				}
				//exp.append(key, new BsonString("/A/"));
				break;
			}
			return exp;
		}
	/**
	 * @param exps
	 * @param link
	 * */
		public static BsonDocument combinaExps(BsonDocument[] exps, Link link){
			BsonArray expArray = new BsonArray();
			BsonDocument comExp = new BsonDocument();
			int length = exps.length;
			if(length < 1){
				return comExp;
			}
			for(int i = 0; i < length; ++i){
				expArray.add(exps[i]);
			}
			switch(link){
			case AND:
				comExp.append("$and", expArray);
				break;
			case OR:
				if(expArray.size() == 1){
					expArray.add(new BsonDocument("SBZLX", new BsonInt32(0)));
				}
				comExp.append("$or", expArray);
				break;
			}
			return comExp;
		}
		
		/**
		 * 
		 * */
		public void createIndex(List<String> collections, List<String> indexFields){
			
		}
		
		/**
		 * 
		 * */
		public void createIndex(String dbName, List<String> indexFields){
			
		}
		
		/**
		 * */
		public void createIndex(List<String> indexFields){
			
		}
		/***
		 * convert one bsonvalue to String
		 * 
		 * @param bValue
		 * */
		public static String bsonToString(BsonValue bValue) {
			String value = null;
			int bType = bValue.getBsonType().ordinal();
			if (1 == bType) {// double
				value = String.valueOf(bValue.asDouble().getValue());
			} else if (2 == bType) {// string
				value = bValue.asString().getValue();
			} else if (10 == bType) {// null
				return null;
			} else if (16 == bType) {// int32
				value = String.valueOf(bValue.asInt32().getValue());
			} else if (18 == bType) {// int64
				value = String.valueOf(bValue.asInt64().getValue());
			} else {
				return null;
			}
			return value;
		}
		
	/**
	 * close MongoClient 
	 * */
	public void close(){
		if(mongoClient != null){
			mongoClient.close();	
		}			
		mongoClient = null;
		System.out.println("Close MongoClient...");
	}
}
