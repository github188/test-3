package nari.MongoDBUpdate.Util;

import static com.mongodb.client.model.Filters.eq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nari.Dao.bundle.bean.ResultHandler;
import nari.Dao.interfaces.DbAdaptor;
import nari.MongoDBUpdate.UpdateServiceActivator;
import nari.MongoQuery.Util.BsonUtil;
import nari.MongoQuery.Util.MongoDBUtil;

import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNull;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.conversions.Bson;
import org.geotools.geojson.geom.GeometryJSON;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.vividsolutions.jts.geom.Geometry;

public class SwitchMongoOracleUtil {

	private static final BigDecimal MAX_INTEGER = BigDecimal.valueOf(Integer.MAX_VALUE);
	private static final String CONNECTION_FIELD_NAME = "CONNECTION";
	private static final String ZCONNECTION_FIELD_NAME = "ZCONNECTION";
	
	public static BsonDocument OracleRecord2MongoRecord(String TableName, Map<String, Object> oracleRecord) throws Exception{
		BsonDocument doc = new BsonDocument();
		
		Map<String, FieldDef> fieldDefs = getFieldDefSet(TableName);
		
		Iterator<String> keyIt = oracleRecord.keySet().iterator();
		while(keyIt.hasNext()){
			String key = keyIt.next().toUpperCase();
			
			FieldDef fieldDef = fieldDefs.get(key);
			if(fieldDef == null){
				continue;
			}
			String columnLabel = fieldDef.getName();
			
			if(columnLabel == null){
				continue;
			}
			
			Object value = oracleRecord.get(columnLabel.toLowerCase());
						
			if(value == null){
				doc.append(columnLabel, new BsonNull());
				continue;
			}
			
			if (fieldDef.getType() == FieldDef.INTEGER) {
				doc.append(columnLabel, new BsonInt32(Integer.valueOf(value.toString())));
			} else if (fieldDef.getType() == FieldDef.NUMBER) {
				BsonValue bValue = parseNumberField((BigDecimal)value);
				doc.append(columnLabel, bValue);
				
			} else if (fieldDef.getType() == FieldDef.BIGINTEGER) {
				doc.append(columnLabel, new BsonInt64(Long.valueOf(value.toString())));
			} else if (fieldDef.getType() == FieldDef.DOUBLE) {
				doc.append(columnLabel, new BsonDouble(Double.valueOf(value.toString())));
			} else if (fieldDef.getType() == FieldDef.STRING) {
				doc.append(columnLabel, new BsonString(value.toString()));
			} else if (fieldDef.getType() == FieldDef.GEOMETRY) {
				STRUCT struct = (STRUCT) value;
				JGeometry jGeometry = null;
				try {
					jGeometry = JGeometry.load(struct);
				} catch (SQLException e) {
					continue;
				}
				if (jGeometry == null) {
					continue;
				}
				Geometry geometry = OracleSpatialHelper.getGeometry(jGeometry);
				if (geometry == null) {
					continue;
				}
				doc.append("Geometry", parseGeometryField(geometry, false));
				
			} else if (fieldDef.getType() == FieldDef.BLOB) {
				if (CONNECTION_FIELD_NAME.equalsIgnoreCase(columnLabel) ||
						ZCONNECTION_FIELD_NAME.equalsIgnoreCase(columnLabel)) {
					BsonValue bVaue = parseConnectionField((Blob)value);
					if(bVaue != null){
						doc.append(columnLabel, bVaue);
					}
					else{
						doc.append(columnLabel, BsonNull.VALUE);
					}
				}
				else{
					
				}
			}
			
		}
		
		return doc;
	}
	

	private static BsonValue parseNumberField(BigDecimal fieldValue) {
		if (fieldValue.scale() > 0) {
			// 转化为double
			return new BsonDouble(fieldValue.doubleValue());
		} else if (fieldValue.compareTo(MAX_INTEGER) > 0) {
			// 转化为long
			return new BsonInt64(fieldValue.longValue());
		} else {
			return new BsonInt32(fieldValue.intValue());
		}
	}
	
	
	
	public static boolean insertMongoFromBsonRecord(String tableName,BsonDocument mongoRecord){
		MongoCollection<BsonDocument> MongoCollection = MongoDBUtil.instance.getCollection(tableName);
		//将该记录加入mongo
		if(MongoCollection == null){//若表被删了先建表
			MongoCollection = MongoDBUtil.instance.creatCollection(tableName);
		}
		//查询mongo是否已存在若存在则添加
		
		int oid = mongoRecord.get("OID").asInt32().getValue();
		MongoCursor<BsonDocument> thematicDevCursor = MongoCollection.find(eq("OID", oid)).iterator();
		if(!thematicDevCursor.hasNext()){//若mongo无对应记录则添加否则不做操作
			MongoCollection.deleteOne(eq("OID", oid));
		}
		
		MongoCollection.insertOne(mongoRecord);
		
		thematicDevCursor.close();
		return true;
	}

	private static BsonValue parseGeometryField(Geometry geometry, boolean simplify) {
		
		if (null != geometry) {
			GeometryJSON gjson = null;
			if (simplify) {
				gjson = new GeometryJSON(8);
			} else {
				gjson = new GeometryJSON(12);
			}
			OutputStream os = new ByteArrayOutputStream();
			try {
				gjson.write(geometry, os);
			} catch (IOException e) {
				return BsonNull.VALUE;
			}
			return BsonDocument.parse(os.toString());
		}
		return BsonNull.VALUE;
	}
	
	private static BsonValue parseConnectionField(Blob blob) throws SQLException {
		
		if (null == blob || blob.length() <= 0) {
			return null;
		}
		InputStream is = blob.getBinaryStream();
		int terminalCount = 0;
		try {
			terminalCount = is.read();	//第一个byte代表拓扑点个数
		} catch (IOException e) {
			return null;
		}
		long blength = blob.length();
		if (terminalCount == 0 || blength != (8 * terminalCount + 1) ) {	//拓扑点个数*8 + 1即为blob的长度（8byte表示一个拓扑点id）
			return null;
		}
		
		byte[] bytes = new byte[8 * terminalCount];
		try {
			if (8 * terminalCount != is.read(bytes, 0, 8 * terminalCount)) {
				return null;
			}
		} catch (IOException ex) {
			return null;
		}
		
		int offset = 0;
		BsonArray connections = new BsonArray();
		for (int i = 0; i < terminalCount; i++) {
			long connectionNodeId = bytesToLong(bytes, offset);
			connections.add(new BsonInt64(connectionNodeId));
			offset += 8;
		}
		return connections;
	}

	/**
	 * byte数组转换成长整形
	 * @param bytes
	 * @param off
	 * @return
	 */
	private static long bytesToLong(byte[] bytes, int off) {
		int b0 = bytes[off + 0] & 0xFF;  
	    int b1 = bytes[off + 1] & 0xFF;  
	    int b2 = bytes[off + 2] & 0xFF;  
	    int b3 = bytes[off + 3] & 0xFF; 
	    int b4 = bytes[off + 4] & 0xFF;  
	    int b5 = bytes[off + 5] & 0xFF; 
	    int b6 = bytes[off + 6] & 0xFF; 
	    int b7 = bytes[off + 7] & 0xFF; 
	    return (b7 << 56) | (b6 << 48) | (b5 << 40) | (b4 << 32) |
	    		(b3 << 24) | (b2 << 16) | (b1 << 8) | b0;  
	}
	
	public static Map<String, FieldDef> getFieldDefSet(String tableName){
		if(UpdateServiceActivator.fieldDefMaps.containsKey(tableName)){
			return UpdateServiceActivator.fieldDefMaps.get(tableName);
		}
		
		DbAdaptor dbAdaptor = UpdateServiceActivator.dbAdaptor;
		
		final Map<String, FieldDef> fieldDefSet = new HashMap<String, FieldDef>();
		
		try {
			dbAdaptor.query("select * from dwzy." + tableName + " where 1=0", 
					new ResultHandler<Object>() {

						@Override
						public Object handle(ResultSet resultSet)
								throws SQLException {
							ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
							for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
								
								String columnName = resultSetMetaData.getColumnName(i).toUpperCase();
								String columnClassName = resultSetMetaData.getColumnClassName(i).toUpperCase();
								String columnTypeName = resultSetMetaData.getColumnTypeName(i).toUpperCase();
								int scale = resultSetMetaData.getScale(i);
								int type = resultSetMetaData.getColumnType(i);
								
								
								FieldDef fieldDef = new FieldDef();
								fieldDef.setName(columnName);
								fieldDef.setClassName(columnClassName);
								fieldDef.setTypeName(columnTypeName);
								fieldDef.setScale(scale);
								
								if (type == Types.NUMERIC) {
									
									fieldDef.setType(FieldDef.NUMBER);
									
								}else if (type == Types.INTEGER) {
									
									fieldDef.setType(FieldDef.INTEGER);
									
								}
								else if (type == Types.FLOAT || type == Types.DOUBLE) {
									
									fieldDef.setType(FieldDef.DOUBLE);
									
								} else if (type == Types.BIGINT) {
									
									fieldDef.setType(FieldDef.BIGINTEGER);
									
								} else if ("VARCHAR2".equalsIgnoreCase(columnTypeName)) {
									
									fieldDef.setType(FieldDef.STRING);
									
								} else if ("MDSYS.SDO_GEOMETRY".equalsIgnoreCase(columnTypeName)) {
									
									fieldDef.setType(FieldDef.GEOMETRY);
									
								} else if ("BLOB".equalsIgnoreCase(columnTypeName)) {
									
									fieldDef.setType(FieldDef.BLOB);
									
								}
								
								fieldDefSet.put(columnName, fieldDef);
							}
							return null;
						}
				
			});
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		UpdateServiceActivator.fieldDefMaps.put(tableName, fieldDefSet);
		
		return fieldDefSet;
	}
}
