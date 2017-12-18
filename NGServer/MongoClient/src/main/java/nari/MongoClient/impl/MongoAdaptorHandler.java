package nari.MongoClient.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nari.Geometry.Geometry;
import nari.MongoClient.interfaces.MongoAdaptor;

import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoAdaptorHandler implements MongoAdaptor {

	private MongoDatabase dataBase = null;

	public MongoAdaptorHandler(MongoClient mongoClient,MongoDatabase dataBase) {
		this.dataBase = dataBase;
	}

	@Override
	public MongoCollection<BsonDocument> getMongoCollection(String collectionName) throws Exception {
		if(collectionName==null || "".equals(collectionName)){
			return null;
		}
		
		return dataBase.getCollection(collectionName.toUpperCase(), BsonDocument.class);
	}

	@Override
	public FindIterable<BsonDocument> find(String collectionName,BsonDocument filter,String[] returnField) throws Exception {
		if(filter==null){
			return null;
		}
		BsonDocument proj = null;
		if(returnField!=null && returnField.length>0){
			proj = new BsonDocument();
			for(String field:returnField){
				proj.append(field.toUpperCase(), new BsonInt32(1));
			}
		}
		MongoCollection<BsonDocument> coll = getMongoCollection(collectionName);
		if(proj!=null){
			return coll.find(filter).projection(proj);
		}
		return coll.find(filter);
	}

	@Override
	public FindIterable<BsonDocument> find(MongoCollection<BsonDocument> mongoColl, BsonDocument filter,String[] returnField) throws Exception {
		BsonDocument proj = null;
		if(returnField!=null && returnField.length>0){
			proj = new BsonDocument();
			for(String field:returnField){
				proj.append(field.toUpperCase(), new BsonInt32(1));
			}
		}
		if(proj!=null){
			return mongoColl.find(filter).projection(proj);
		}
		return mongoColl.find(filter);
	}

	@Override
	public FindIterable<BsonDocument> find(String collectionName,String[] returnField) throws Exception {
		MongoCollection<BsonDocument> coll = getMongoCollection(collectionName);
		return find(coll,returnField);
	}

	@Override
	public FindIterable<BsonDocument> find(MongoCollection<BsonDocument> mongoColl,String[] returnField) throws Exception {
	
		BsonDocument proj = null;
		if(returnField!=null && returnField.length>0){
			proj = new BsonDocument();
			for(String field:returnField){
				proj.append(field.toUpperCase(), new BsonInt32(1));
			}
		}
		if(proj!=null){
			return mongoColl.find().projection(proj);
		}
		return mongoColl.find();
	}

	@Override
	public <TResult> FindIterable<TResult> find(String collectionName, Class<TResult> result,String[] returnField) throws Exception {
		MongoCollection<BsonDocument> coll = getMongoCollection(collectionName);
		return find(coll,result,returnField);
	}

	@Override
	public <TResult> FindIterable<TResult> find(String collectionName, BsonDocument filter, Class<TResult> result,String[] returnField) throws Exception {
		MongoCollection<BsonDocument> coll = getMongoCollection(collectionName);
		return find(coll,filter, result,returnField);
	}

	@Override
	public <TResult> FindIterable<TResult> find(MongoCollection<BsonDocument> mongoColl, Class<TResult> result,String[] returnField) throws Exception {
		BsonDocument proj = null;
		if(returnField!=null && returnField.length>0){
			proj = new BsonDocument();
			for(String field:returnField){
				proj.append(field.toUpperCase(), new BsonInt32(1));
			}
		}
		if(proj!=null){
			return mongoColl.find(result).projection(proj);
		}
		return mongoColl.find(result);
	}

	@Override
	public <TResult> FindIterable<TResult> find(MongoCollection<BsonDocument> mongoColl, BsonDocument filter, Class<TResult> result,String[] returnField) throws Exception {
		BsonDocument proj = null;
		if(returnField!=null && returnField.length>0){
			proj = new BsonDocument();
			for(String field:returnField){
				proj.append(field.toUpperCase(), new BsonInt32(1));
			}
		}
		if(proj!=null){
			return mongoColl.find(filter, result).projection(proj);
		}
		return mongoColl.find(filter, result);
	}

	@Override
	public void insert(String collectionName, BsonDocument document) throws Exception {
		MongoCollection<BsonDocument> coll = getMongoCollection(collectionName);
		coll.insertOne(document);
	}

	@Override
	public void insert(MongoCollection<BsonDocument> mongoColl, BsonDocument document) throws Exception {
		mongoColl.insertOne(document);
	}

	@Override
	public void insert(String collectionName, List<BsonDocument> documents) throws Exception {
		MongoCollection<BsonDocument> coll = getMongoCollection(collectionName);
		coll.insertMany(documents);
	}

	@Override
	public void insert(MongoCollection<BsonDocument> mongoColl, List<BsonDocument> documents) throws Exception {
		mongoColl.insertMany(documents);
	}

	@Override
	public void update(String collectionName, BsonDocument filter, BsonDocument document) throws Exception {
		MongoCollection<BsonDocument> coll = getMongoCollection(collectionName);
		coll.updateOne(filter, document);
	}

	@Override
	public void update(MongoCollection<BsonDocument> mongoColl, BsonDocument filter, BsonDocument document) throws Exception {
		mongoColl.updateOne(filter, document);
	}

	@Override
	public void delete(String collectionName, BsonDocument document) throws Exception {
		MongoCollection<BsonDocument> coll = getMongoCollection(collectionName);
		coll.deleteOne(document);
	}

	@Override
	public void delete(MongoCollection<BsonDocument> mongoColl, BsonDocument document) throws Exception {
		mongoColl.deleteOne(document);
	}

	@Override
	public int count(String collectionName) throws Exception {
		MongoCollection<BsonDocument> coll = getMongoCollection(collectionName);
		return (int)coll.count();
	}

	@Override
	public int count(String collectionName, BsonDocument filter) throws Exception {
		MongoCollection<BsonDocument> coll = getMongoCollection(collectionName);
		return (int)coll.count(filter);
	}

	@Override
	public int count(MongoCollection<BsonDocument> mongoColl) throws Exception {
		return (int)mongoColl.count();
	}

	@Override
	public int count(MongoCollection<BsonDocument> mongoColl, BsonDocument filter) throws Exception {
		return (int)mongoColl.count(filter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <TResult> List<TResult> findList(MongoCollection<BsonDocument> mongoColl, BsonDocument filter, Class<TResult> result,String[] returnField) throws Exception {
		if(mongoColl==null){
			return null;
		}
		MongoCursor<BsonDocument> cursor = null;
		
		BsonDocument proj = null;
		if(returnField!=null && returnField.length>0){
			proj = new BsonDocument();
			for(String field:returnField){
				proj.append(field.toUpperCase(), new BsonInt32(1));
			}
		}
		
		FindIterable<BsonDocument> ft = null;
		if(filter==null){
			ft = mongoColl.find();
		}else{
			ft = mongoColl.find(filter);
		}
		
		if(proj!=null){
			ft = ft.projection(proj);
		}
		cursor = ft.iterator();
		
		List<TResult> results = new ArrayList<TResult>();
		BsonDocument document = null;
		if(cursor!=null){
			
			while(cursor.hasNext()){
				document = cursor.next();
				TResult r = null;
				if(result==Map.class || result==HashMap.class){
					r = (TResult)makeMap(document);
				}else{
					r = makeResult(document,result);
				}
				
				if(r==null){
					continue;
				}
				results.add(r);
			}
			cursor.close();
			
		}
		return results;
	}

	@Override
	public <TResult> TResult findFirst(MongoCollection<BsonDocument> mongoColl, BsonDocument filter, Class<TResult> result,String[] returnField) throws Exception {
		if(mongoColl==null){
			return null;
		}
		
		BsonDocument proj = null;
		if(returnField!=null && returnField.length>0){
			proj = new BsonDocument();
			for(String field:returnField){
				proj.append(field.toUpperCase(), new BsonInt32(1));
			}
		}
		
		FindIterable<BsonDocument> ft = null;
		
		BsonDocument document = null;
		if(filter==null){
			ft = mongoColl.find();
		}else{
			ft = mongoColl.find(filter);
		}
		if(proj!=null){
			ft = ft.projection(proj);
		}
		document = ft.first();
		
		TResult r = makeResult(document,result);
		
		return r;
	}

	private Map<String,Object> makeMap(BsonDocument document){
		if(document==null){
			return null;
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		Set<String> keySet = document.keySet();
		Iterator<String> it = keySet.iterator();
		String key = "";
		Object value = null;
		while(it.hasNext()){
			key = it.next();
			value = document.getString(key.toUpperCase());
			map.put(key, value);
		}
		return map;
	}
	
	private <T> T makeResult(BsonDocument document, Class<T> result) throws Exception{
		if(document==null){
			return null;
		}
		
		Field[] fields = result.getDeclaredFields();
		
		String fieldName = "";
		Class<?> fieldType = null;
		
		Object value = null;
		String bsonValue = "";
		
		T r = result.newInstance();
		for(Field f:fields){
			f.setAccessible(true);
			fieldType = f.getType();
			
			fieldName = f.getName();
			
			if(fieldType==int.class || fieldType==Integer.class){
				bsonValue = document.getString(fieldName.toUpperCase(), new BsonString("0")).getValue();
				value = Integer.parseInt(bsonValue);
			}else if(fieldType==long.class || fieldType==Long.class){
				bsonValue = document.getString(fieldName.toUpperCase(), new BsonString("0")).getValue();
				value = Long.parseLong(bsonValue);
			}else if(fieldType==double.class || fieldType==Double.class){
				bsonValue = document.getString(fieldName.toUpperCase(), new BsonString("0")).getValue();
				value = Double.parseDouble(bsonValue);
			}else if(fieldType==short.class || fieldType==Short.class){
				bsonValue = document.getString(fieldName.toUpperCase(), new BsonString("0")).getValue();
				value = Short.parseShort(bsonValue);
			}else if(fieldType==float.class || fieldType==Float.class){
				bsonValue = document.getString(fieldName.toUpperCase(), new BsonString("0")).getValue();
				value = Float.parseFloat(bsonValue);
			}else if(fieldType==byte.class || fieldType==Byte.class){
				bsonValue = document.getString(fieldName.toUpperCase(), new BsonString("0")).getValue();
				value = Byte.parseByte(bsonValue);
			}else if(fieldType==String.class){
				bsonValue = document.getString(fieldName.toUpperCase(), new BsonString("")).getValue();
				value = bsonValue;
			}else if(fieldType==Geometry.class){
//				bsonValue = document.getString(fieldName.toUpperCase(), new BsonString("")).getValue();
//				value = bsonValue;
			}
			
			f.set(r, value);
		}
		
		return r;
	}

	@Override
	public List<Map<String, Object>> findList(MongoCollection<BsonDocument> mongoColl, BsonDocument filter, String[] returnField) throws Exception {
		if(mongoColl==null){
			return null;
		}
		MongoCursor<BsonDocument> cursor = null;
		
		if(filter==null){
			cursor = mongoColl.find().iterator();
		}else{
			cursor = mongoColl.find(filter).iterator();
		}
		List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
		BsonDocument document = null;
		if(cursor!=null){
			while(cursor.hasNext()){
				document = cursor.next();
				Map<String,Object> r = makeMap(document);
				
				if(r==null){
					continue;
				}
				results.add(r);
			}
			cursor.close();
			
		}
		return results;
	}

	@Override
	public Map<String, Object> findFirst(MongoCollection<BsonDocument> mongoColl, BsonDocument filter, String[] returnField) throws Exception {
		if(mongoColl==null){
			return null;
		}
		
		BsonDocument document = null;
		if(filter==null){
			document = mongoColl.find().first();
		}else{
			document = mongoColl.find(filter).first();
		}
		
		return makeMap(document);
		
	}

	@Override
	public boolean isActive() {
		return true;
	}
	
}
