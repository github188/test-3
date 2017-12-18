package nari.MongoDBUpdate.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.MongoQuery.MongoQueryActivator;
import nari.MongoQuery.Util.MongoDBUtil;
import nari.model.TableName;
import nari.MongoDBUpdate.Util.SwitchMongoOracleUtil;
import nari.MongoDBUpdate.bean.UpdateThematicRequest;
import nari.MongoDBUpdate.bean.UpdateThematicResponse;
import nari.parameter.bean.QueryResult;
import nari.parameter.code.ReturnCode;

import oracle.jdbc.rowset.OracleFilteredRowSet;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

public class UpdateThematicHandler {
	
	private Logger logger = LoggerManager.getLogger(this.getClass());
	private DbAdaptor db = MongoQueryActivator.dbAdaptor;
	
	private MongoCollection<BsonDocument> collectionThematicDev = null;	//专题图设备集合
	private BsonDocument thematicDevQuery = null;	//MongoSSTSL条件
	private String thematicDevCondSql = "";			//oracleSSTSL条件

	public UpdateThematicResponse updateThematic(UpdateThematicRequest req) throws Exception{
		UpdateThematicResponse resp = new UpdateThematicResponse();
		
		String mapId = req.getMapId();
		if(mapId == null || "".equalsIgnoreCase(mapId)){
			resp.setCode(ReturnCode.NULL);
			System.out.println("输入参数缺少必填项");
			return resp;
		}
		
		//为空更新所有mapid的
		String[] documentId = req.getDocumentId();
		BsonArray SSTSLBson = new BsonArray();
		if(documentId != null && documentId.length != 0){
			thematicDevCondSql = "where sstsl in (" + documentId[0];
			SSTSLBson.add(new BsonInt32(Integer.valueOf(documentId[0])));
			for(int i=1;i<documentId.length;i++){
				thematicDevCondSql = thematicDevCondSql + "," + documentId[i];
				SSTSLBson.add(new BsonInt32(Integer.valueOf(documentId[i])));
			}
			thematicDevCondSql = thematicDevCondSql + ")";
			thematicDevQuery = new BsonDocument();
			thematicDevQuery.append("SSTSL", new BsonDocument().append("$in", SSTSLBson));
		}
		
		//专题图每种设备
		String thematicSql = "select * from " + TableName.CONF_DOCUMENTMODEL + " where mapId = "
				+ mapId;
		List<Map<String, Object>> thematicList = new ArrayList<Map<String, Object>>();
		try {
			thematicList = db.findAllMap(thematicSql);
		} catch (SQLException e) {
			logger.error("数据库查询出错");
			resp.setCode(ReturnCode.SQLERROR);
			return resp;
		}
		if(thematicList == null || thematicList.size() == 0){
			logger.error("无数据,传入值可能有误");
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		
		List<String> updatedTableList = new ArrayList<String>();
		for (int i = 0; i < thematicList.size(); i++) {		//mapid对应的每种设备类型层
			Map<String, Object> recordMap = thematicList.get(i);
			
			String tableName = String.valueOf(recordMap.get("geotablename"));
			if(tableName.startsWith("T_TX")){
				continue;
			}
			
			//删除mongo专题图表内对应设备记录
			//得到每个专题图所有数据
			collectionThematicDev = MongoDBUtil.instance.getCollection(tableName);
			//删除Mongo对应数据记录
			if(thematicDevQuery != null){
				collectionThematicDev.deleteMany(thematicDevQuery);
			}else{	//若未传documentId则删表
				collectionThematicDev.drop();
			}
			
			//从oracle取出对应记录
			String devSql = "select * from " + tableName + " " + thematicDevCondSql;
			List<Map<String, Object>> thematicDevList = new ArrayList<Map<String, Object>>();
			try {
				thematicDevList = db.findAllMap(devSql);
			} catch (SQLException e) {
				logger.error("数据库查询出错");
				resp.setCode(ReturnCode.SQLERROR);
				return resp;
			}
			if(thematicDevList == null || thematicDevList.size() == 0){
				continue;
			}
			
			for (int j = 0; j < thematicDevList.size(); j++) {		//每种专题图对应的每条记录
				Map<String, Object> eachThematicDev = thematicDevList.get(j);
				//将每条oracle记录变成Mongo记录
				BsonDocument mongoRecord = SwitchMongoOracleUtil.OracleRecord2MongoRecord(tableName, eachThematicDev);
				//将该记录加入mongo
				SwitchMongoOracleUtil.insertMongoFromBsonRecord(tableName, mongoRecord);
			}
			//更新完mongo一张表所有对应，将这张表返回至结果
			updatedTableList.add(tableName);
		}
		
		String[] updatedTable = new String[updatedTableList.size()];
		updatedTable = updatedTableList.toArray(updatedTable);
		resp.setUpdatedTable(updatedTable);
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}
}
