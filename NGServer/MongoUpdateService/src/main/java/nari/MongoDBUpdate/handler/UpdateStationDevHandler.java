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
import nari.MongoDBUpdate.UpdateServiceActivator;
import nari.MongoDBUpdate.Util.SwitchMongoOracleUtil;
import nari.MongoDBUpdate.bean.UpdateStationDevRequest;
import nari.MongoDBUpdate.bean.UpdateStationDevResponse;
import nari.parameter.bean.QueryResult;
import nari.parameter.code.ReturnCode;

import oracle.spatial.util.WKT;

import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.geotools.geojson.geom.GeometryJSON;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class UpdateStationDevHandler {
	
	private Logger logger = LoggerManager.getLogger(this.getClass());
	private DbAdaptor db = UpdateServiceActivator.dbAdaptor;

	private String StationClassId = "300000";
	private String RelationField = "SSDZ";
	private BsonDocument mongoRelationQuery = new BsonDocument();
	private String OIDCondSql = "";
	
	MongoCollection<BsonDocument> pModelCollection = null;
	List<Map<String, Object>> oraclePDevRs = null;

	public UpdateStationDevResponse updateStationDev(UpdateStationDevRequest req) throws Exception {
		UpdateStationDevResponse resp = new UpdateStationDevResponse();
		List<String> updatedTableList = new ArrayList<String>();
		
		String oraclePOID = "";
		// 条件组合
		// SSDZ条件
		String SBID = req.getSBID();
		String OID = req.getOID();
		String pmodelId = req.getPmodelId();
		if (OID != null && !"".equalsIgnoreCase(OID)) { // 以oid为准
			mongoRelationQuery.put(RelationField, new BsonInt32(Integer.parseInt(OID)));
			OIDCondSql = "where " + RelationField + " = " + OID;
		} else if (SBID != null && !"".equalsIgnoreCase(SBID)) {	//若无OID,有SBID,得到OID
			// 将p设备的sbid转为oid
			//Mongo中的
			pModelCollection = MongoDBUtil.instance.getCollection(Integer
					.parseInt(StationClassId));
			BsonDocument pmodelQuery = new BsonDocument();
			pmodelQuery.put("SBID", new BsonString(SBID));
			// 找到sbid对应那条电站数据
			MongoCursor<BsonDocument> stationCursor = pModelCollection.find(
					pmodelQuery).iterator();
			while (stationCursor.hasNext()) {
				// 获得第一个
				BsonDocument stationdoc = stationCursor.next();
				BsonValue bsonValue = stationdoc.getString("OID");
				OID = bsonValue.toString();
				BsonValue bsonModelValue = stationdoc.getString("SBZLX");
				break;
			}
			
			//oracle中的
			String pDevSql = "select oid from " + TableName.T_TX_ZNYC_DZ + "where SBID = " + SBID;
			try {
				oraclePDevRs = db.findAllMap(pDevSql);
			} catch (SQLException e) {
				resp.setCode(ReturnCode.SQLERROR);
				System.out.println("数据库查询出错");
				return resp;
			}
			if (oraclePDevRs == null || oraclePDevRs.size() == 0) {
				resp.setCode(ReturnCode.NODATA);
				System.out.println("数据库查询无数据");
				return resp;
			}
			//从第一个得到OID
			oraclePOID = String.valueOf(oraclePDevRs.get(0).get("oid"));
			if (OID == null || "".equalsIgnoreCase(OID) || oraclePOID == null || "".equalsIgnoreCase(oraclePOID)) {
				resp.setCode(ReturnCode.VALUEWRONG);
				System.out.println("输入参数SBID有误");
				return resp;
			}
			
			mongoRelationQuery.put(RelationField, new BsonInt32(Integer.parseInt(OID)));
			OIDCondSql = "where " + RelationField + " = " + oraclePOID;
		} else {
			resp.setCode(ReturnCode.NULL);
			System.out.println("输入参数缺少必须项");
			return resp;
		}
		
		//获取p设备下所有r设备类型
		String relationSql = "select a.*,b.classid,b.modelname from " + TableName.CONF_MODELRELATION + " a," 
		+ TableName.CONF_MODELMETA + " b where a.rmodelid = b.moelname and a.pmodelid = " + pmodelId + " and a.relationId = 3002";
		List<Map<String, Object>> relationRs = new ArrayList<Map<String, Object>>();
		try {
			relationRs = db.findAllMap(relationSql);
		} catch (SQLException e) {
			resp.setCode(ReturnCode.SQLERROR);
			System.out.println("数据库查询出错");
			return resp;
		}
		if (relationRs == null || relationRs.size() == 0) {
			resp.setCode(ReturnCode.NODATA);
			System.out.println("数据库查询无数据");
			return resp;
		}

		for (int i = 0; i < relationRs.size(); i++) {	//每一种R设备类型的更新
			String rmodelid = String.valueOf(relationRs.get(i).get("rmodelid"));
			String rtableName = String.valueOf(relationRs.get(i).get("tablename"));
			
			//删除mongo
			MongoCollection<BsonDocument> rModelCollection = null;
			rModelCollection = MongoDBUtil.instance.getCollectionByModelID(Integer.parseInt(rmodelid));
			rModelCollection.deleteMany(mongoRelationQuery);
			
			//查询oracle
			String rDevSql = "select * from DWZY." + rtableName + OIDCondSql;
			List<Map<String, Object>> rDevRs = new ArrayList<Map<String, Object>>();
			try {
				rDevRs = db.findAllMap(rDevSql);
			} catch (SQLException e) {
				resp.setCode(ReturnCode.SQLERROR);
				System.out.println("数据库查询出错");
				return resp;
			}
			if (rDevRs == null || rDevRs.size() == 0) {
				resp.setCode(ReturnCode.NODATA);
				System.out.println("数据库查询无数据");
				return resp;
			}
			
			for(int j=0;j<rDevRs.size();j++){	//每一条rDevOracle记录添加到mongo更新
				//得到oracle记录
				Map<String, Object> eachDev = rDevRs.get(j);
				//将oracle记录变成mongo记录
				BsonDocument eachDevBson = SwitchMongoOracleUtil.OracleRecord2MongoRecord(rtableName, eachDev);
				//将该记录加入mongo
				SwitchMongoOracleUtil.insertMongoFromBsonRecord(rtableName, eachDevBson);
			}	//一个rmodel更新完成
			
			updatedTableList.add(rtableName);
		}//每个rmodel更新完成
		
		//更新pmodel表对应记录
		if(pModelCollection == null){
			pModelCollection = MongoDBUtil.instance.getCollectionByModelID(Integer.parseInt(pmodelId));
		}
		BsonDocument pmodelQuery = new BsonDocument();
		pmodelQuery.put("OID", new BsonInt32(Integer.parseInt(OID)));
		//删除mongo
		pModelCollection.deleteMany(pmodelQuery);
		//从oracle取出更新mongo
		if(oraclePDevRs == null){
			String pDevSql = "select oid from " + TableName.T_TX_ZNYC_DZ + "where OID = " + OID;
			try {
				oraclePDevRs = db.findAllMap(pDevSql);
			} catch (SQLException e) {
				resp.setCode(ReturnCode.SQLERROR);
				System.out.println("数据库查询出错");
				return resp;
			}
			if (oraclePDevRs == null || oraclePDevRs.size() == 0) {
				resp.setCode(ReturnCode.NODATA);
				System.out.println("数据库查询无数据");
				return resp;
			}
		}
		for(int i=0;i<oraclePDevRs.size();i++){
			Map<String, Object> eachPDev = oraclePDevRs.get(i);
			//将每条oracle记录变成Mongo记录
			BsonDocument mongoRecord = SwitchMongoOracleUtil.OracleRecord2MongoRecord("T_TX_ZNYC_DZ", eachPDev);
			//将该记录加入mongo
			SwitchMongoOracleUtil.insertMongoFromBsonRecord("T_TX_ZNYC_DZ", mongoRecord);
		}//电站更新完成
		updatedTableList.add("T_TX_ZNYC_DZ");
		String[] updatedTable = new String[updatedTableList.size()];
		updatedTable = updatedTableList.toArray(updatedTable);
		resp.setUpdatedTable(updatedTable);
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}

}
