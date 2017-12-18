package nari.MongoQuery.Util;

import java.util.HashMap;
import java.util.Map;

import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNull;
import org.bson.BsonString;
import org.bson.BsonValue;

public final class BsonUtil {
	/***
	 * convert one bsonvalue to String
	 * @param bValue 
	 * @return String
	 * */
	public static String BsonToString(BsonValue bValue){
		if(bValue.isNull()){
			return null;
		}
		String value = null;
		int bType = bValue.getBsonType().ordinal();
		if(1 == bType){//double
			value = String.valueOf(bValue.asDouble().getValue());
		}else if(2 == bType){//string
			value = bValue.asString().getValue();
		}else if(10 == bType){//null
			return null;
		}else if(16 == bType){//int32
			value = String.valueOf(bValue.asInt32().getValue());
		}else if(18 == bType){//int64
			value = String.valueOf(bValue.asInt64().getValue());
		}else{
			return null;
		}
		return value;
	}
	/***
	 * get real bsonvalue
	 * @param bValue 
	 * @return String
	 * */
	public static BsonValue getRealBsonValue(String key, String value, int bsonType){
		BsonValue bsonValue;
		if(1 == bsonType){//double
			bsonValue = new BsonDouble(Double.parseDouble(value));
		}else if(2 == bsonType){//string
			bsonValue = new BsonString(value);
		}else if(10 == bsonType){//null
			bsonValue = new BsonNull();
		}else if(16 == bsonType){//int32
			bsonValue = new BsonInt32(Integer.parseInt(value));
		}else if(18 == bsonType){//int64
			bsonValue = new BsonInt64(Long.parseLong(value));
		}else{
			bsonValue = new BsonNull();
		}
		return bsonValue;
	}
}
