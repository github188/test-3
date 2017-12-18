package nari.MongoClient.interfaces;

import java.util.List;
import java.util.Map;

import org.bson.BsonDocument;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public interface MongoAdaptor {

	public static final MongoAdaptor NONE = new MongoAdaptor(){

		@Override
		public MongoCollection<BsonDocument> getMongoCollection(String collectionName) throws Exception {
			return null;
		}

		@Override
		public FindIterable<BsonDocument> find(String collectionName,BsonDocument filter,String[] returnField) throws Exception {
			return null;
		}

		@Override
		public FindIterable<BsonDocument> find(MongoCollection<BsonDocument> mongoColl, BsonDocument filter,String[] returnField) throws Exception {
			return null;
		}

		@Override
		public FindIterable<BsonDocument> find(String collectionName,String[] returnField) throws Exception {
			return null;
		}

		@Override
		public FindIterable<BsonDocument> find(MongoCollection<BsonDocument> mongoColl,String[] returnField) throws Exception {
			return null;
		}

		@Override
		public <TResult> FindIterable<TResult> find(String collectionName, Class<TResult> result,String[] returnField) throws Exception {
			return null;
		}

		@Override
		public <TResult> FindIterable<TResult> find(String collectionName, BsonDocument filter, Class<TResult> result,String[] returnField) throws Exception {
			return null;
		}

		@Override
		public <TResult> FindIterable<TResult> find(MongoCollection<BsonDocument> mongoColl, Class<TResult> result,String[] returnField) throws Exception {
			return null;
		}

		@Override
		public <TResult> FindIterable<TResult> find(MongoCollection<BsonDocument> mongoColl, BsonDocument filter, Class<TResult> result,String[] returnField) throws Exception {
			return null;
		}

		@Override
		public void insert(String collectionName, BsonDocument document) throws Exception {
			
		}

		@Override
		public void insert(MongoCollection<BsonDocument> mongoColl, BsonDocument document) throws Exception {
			
		}

		@Override
		public void insert(String collectionName, List<BsonDocument> documents) throws Exception {
			
		}

		@Override
		public void insert(MongoCollection<BsonDocument> mongoColl, List<BsonDocument> documents) throws Exception {
			
		}

		@Override
		public void update(String collectionName, BsonDocument filter, BsonDocument document) throws Exception {
			
		}

		@Override
		public void update(MongoCollection<BsonDocument> mongoColl, BsonDocument filter, BsonDocument document) throws Exception {
			
		}

		@Override
		public void delete(String collectionName, BsonDocument document) throws Exception {
			
		}

		@Override
		public void delete(MongoCollection<BsonDocument> mongoColl, BsonDocument document) throws Exception {
			
		}

		@Override
		public int count(String collectionName) throws Exception {
			return 0;
		}

		@Override
		public int count(String collectionName, BsonDocument filter) throws Exception {
			return 0;
		}

		@Override
		public int count(MongoCollection<BsonDocument> mongoColl) throws Exception {
			return 0;
		}

		@Override
		public int count(MongoCollection<BsonDocument> mongoColl, BsonDocument filter) throws Exception {
			return 0;
		}

		@Override
		public <TResult> List<TResult> findList(MongoCollection<BsonDocument> mongoColl, BsonDocument filter, Class<TResult> result,String[] returnField) throws Exception {
			return null;
		}

		@Override
		public <TResult> TResult findFirst(MongoCollection<BsonDocument> mongoColl, BsonDocument filter, Class<TResult> result,String[] returnField) throws Exception {
			return null;
		}

		@Override
		public List<Map<String, Object>> findList(MongoCollection<BsonDocument> mongoColl, BsonDocument filter, String[] returnField) throws Exception {
			return null;
		}

		@Override
		public Map<String, Object> findFirst(MongoCollection<BsonDocument> mongoColl, BsonDocument filter, String[] returnField) throws Exception {
			return null;
		}

		@Override
		public boolean isActive() {
			return false;
		}

	};
	
	public int count(String collectionName) throws Exception;
	
	public int count(String collectionName,BsonDocument filter) throws Exception;
	
	public int count(MongoCollection<BsonDocument> mongoColl) throws Exception;
	
	public int count(MongoCollection<BsonDocument> mongoColl,BsonDocument filter) throws Exception;
	
	public MongoCollection<BsonDocument> getMongoCollection(String collectionName) throws Exception;
	
	public FindIterable<BsonDocument> find(String collectionName,BsonDocument filter,String[] returnField) throws Exception;
	
	public FindIterable<BsonDocument> find(MongoCollection<BsonDocument> mongoColl,BsonDocument filter,String[] returnField) throws Exception;
	
	public FindIterable<BsonDocument> find(String collectionName,String[] returnField) throws Exception;
	
	public FindIterable<BsonDocument> find(MongoCollection<BsonDocument> mongoColl,String[] returnField) throws Exception;
	
	public <TResult> FindIterable<TResult> find(String collectionName,Class<TResult> result,String[] returnField) throws Exception;
	
	public <TResult> FindIterable<TResult> find(String collectionName,BsonDocument filter,Class<TResult> result,String[] returnField) throws Exception;
	
	public <TResult> FindIterable<TResult> find(MongoCollection<BsonDocument> mongoColl,Class<TResult> result,String[] returnField) throws Exception;
	
	public <TResult> FindIterable<TResult> find(MongoCollection<BsonDocument> mongoColl,BsonDocument filter,Class<TResult> result,String[] returnField) throws Exception;
	
	public <TResult> List<TResult> findList(MongoCollection<BsonDocument> mongoColl,BsonDocument filter,Class<TResult> result,String[] returnField) throws Exception;
	
	public <TResult> TResult findFirst(MongoCollection<BsonDocument> mongoColl,BsonDocument filter,Class<TResult> result,String[] returnField) throws Exception;
	
	public List<Map<String,Object>> findList(MongoCollection<BsonDocument> mongoColl,BsonDocument filter,String[] returnField) throws Exception;
	
	public Map<String,Object> findFirst(MongoCollection<BsonDocument> mongoColl,BsonDocument filter,String[] returnField) throws Exception;
	
	public void insert(String collectionName,BsonDocument document) throws Exception;
	
	public void insert(MongoCollection<BsonDocument> mongoColl,BsonDocument document) throws Exception;
	
	public void insert(String collectionName,List<BsonDocument> documents) throws Exception;
	
	public void insert(MongoCollection<BsonDocument> mongoColl,List<BsonDocument> documents) throws Exception;
	
	public void update(String collectionName,BsonDocument filter,BsonDocument document) throws Exception;
	
	public void update(MongoCollection<BsonDocument> mongoColl,BsonDocument filter,BsonDocument document) throws Exception;
	
	public void delete(String collectionName,BsonDocument document) throws Exception;
	
	public void delete(MongoCollection<BsonDocument> mongoColl,BsonDocument document) throws Exception;
	
	public boolean isActive();
}
