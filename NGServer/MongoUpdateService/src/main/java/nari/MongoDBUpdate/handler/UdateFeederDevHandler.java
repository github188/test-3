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
import nari.MongoDBUpdate.bean.UdateFeederDevRequest;
import nari.MongoDBUpdate.bean.UdateFeederDevResponse;
import nari.MongoDBUpdate.bean.UpdateStationDevRequest;
import nari.parameter.code.ReturnCode;

import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.geotools.geojson.GeoJSON;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class UdateFeederDevHandler {

	private Logger logger = LoggerManager.getLogger(this.getClass());
	private DbAdaptor db = MongoQueryActivator.dbAdaptor;

	private UpdateStationDevHandler updateStation = new UpdateStationDevHandler();
	private UpdateStationDevRequest updateStationReq = new UpdateStationDevRequest();

	private String feederClassId = "100001";
	private String RelationField = "SSDKX";
	private BsonDocument mongoRelationQuery = new BsonDocument();
	private String OIDCondSql = "";
	private String QSDZ = "";

	private MongoCollection<BsonDocument> pModelCollection = null;
	private List<Map<String, Object>> oraclePDevRs = null;

	public UdateFeederDevResponse udateFeederDev(UdateFeederDevRequest req) throws Exception {
		UdateFeederDevResponse resp = new UdateFeederDevResponse();
		List<String> updatedTableList = new ArrayList<String>();

		String oraclePOID = "";
		// 条件组合
		// SSDZ条件
		String SBID = req.getSBID();
		String OID = req.getOID();
		String pmodelId = req.getPmodelId();
		if (OID != null && !"".equalsIgnoreCase(OID)) { // 以oid为准
			mongoRelationQuery.put(RelationField,
					new BsonInt32(Integer.parseInt(OID)));
			OIDCondSql = "where " + RelationField + " = " + OID;
		} else if (SBID != null && !"".equalsIgnoreCase(SBID)) { // 若无OID,有SBID,得到OID
			// 将p设备的sbid转为oid
			// Mongo中的
			pModelCollection = MongoDBUtil.instance.getCollection(Integer.parseInt(feederClassId));
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

			// oracle中的
			String pDevSql = "select oid from " + TableName.T_TX_ZNYC_DZ
					+ "where SBID = " + SBID;
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
			// 从第一个得到OID
			oraclePOID = String.valueOf(oraclePDevRs.get(0).get("oid"));
			if (OID == null || "".equalsIgnoreCase(OID) || oraclePOID == null
					|| "".equalsIgnoreCase(oraclePOID)) {
				resp.setCode(ReturnCode.VALUEWRONG);
				System.out.println("输入参数SBID有误");
				return resp;
			}

			mongoRelationQuery.put(RelationField,
					new BsonInt32(Integer.parseInt(OID)));
			OIDCondSql = "where " + RelationField + " = " + oraclePOID;
		} else {
			resp.setCode(ReturnCode.NULL);
			System.out.println("输入参数缺少必须项");
			return resp;
		}

		// 得到feeder表格本体数据
		if (pModelCollection == null) {
			pModelCollection = MongoDBUtil.instance.getCollectionByModelID(Integer.parseInt(pmodelId));
		}
		BsonDocument pmodelQuery = new BsonDocument();
		pmodelQuery.put("OID", new BsonInt32(Integer.parseInt(OID)));
		// 从oracle取出更新mongo
		if (oraclePDevRs == null) {
			String pDevSql = "select oid from " + TableName.T_TX_ZNYC_DZ
					+ "where OID = " + OID;
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

		// 1:更新起始电站（根据oracle的QSDZ电站）
		// 获得oraclePDev
		for (int i = 0; i < oraclePDevRs.size(); i++) {
			Map<String, Object> eachPDev = oraclePDevRs.get(i);
			String qsdz = String.valueOf(eachPDev.get("qsdz"));
			// 更新起始电站
			updateStationReq.setOID(qsdz);
			updateStation.updateStationDev(updateStationReq);
		}
		updatedTableList.add("T_TX_ZNYC_DZ");

		// 2:更新关系设备（电站，其他）
		// 获取p设备下所有r设备类型
		String relationSql = "select a.*,b.classid,b.modelname from "
				+ TableName.CONF_MODELRELATION + " a,"
				+ TableName.CONF_MODELMETA
				+ " b where a.rmodelid = b.moelname and a.pmodelid = "
				+ pmodelId + " and a.relationId = 3002";
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

		for (int i = 0; i < relationRs.size(); i++) { // 每一种R设备类型的更新

			String rmodelid = String.valueOf(relationRs.get(i).get("rmodelid"));
			String rclassid = String.valueOf(relationRs.get(i).get("classid"));
			String rtableName = String.valueOf(relationRs.get(i).get("tablename"));
			// 得到oracle设备类型表
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

			if ("300000".equalsIgnoreCase(rclassid)) { // 2.1:更新电站
				for (int j = 0; j < rDevRs.size(); j++) {
					String stationOID = String.valueOf(rDevRs.get(j).get("oid"));
					updateStationReq.setOID(stationOID);
					updateStation.updateStationDev(updateStationReq);
				}
			} else { // 2.2:跟新站外
				// 删除mongo
				MongoCollection<BsonDocument> rModelCollection = null;
				rModelCollection = MongoDBUtil.instance.getCollectionByModelID(Integer.parseInt(rmodelid));
				rModelCollection.deleteMany(mongoRelationQuery);

				// 从oracle跟新数据
				for (int j = 0; j < rDevRs.size(); j++) { // 每一条rDevOracle记录添加到mongo更新
					// 得到oracle记录
					Map<String, Object> eachDev = rDevRs.get(j);
					// 将oracle记录变成mongo记录
					BsonDocument eachDevBson = SwitchMongoOracleUtil.OracleRecord2MongoRecord(rtableName, eachDev);
					// 将该记录加入mongo
					SwitchMongoOracleUtil.insertMongoFromBsonRecord(rtableName, eachDevBson);
				}
			}
			updatedTableList.add(rtableName);
		}// 每个rmodel更新完成

		// 3:跟新本体馈线表
		// 3.1:T_TX_ZWYC_SBSSDKX
		// 删除mongo
		MongoCollection<BsonDocument> SBSSDKXCollection1 = null;
		BsonDocument selfQuery1 = new BsonDocument();
		String selfField1 = "DKXID";
		selfQuery1.put(selfField1, new BsonInt32(Integer.parseInt(OID)));
		SBSSDKXCollection1 = MongoDBUtil.instance.getCollection("T_TX_ZWYC_SBSSDKX");
		SBSSDKXCollection1.deleteMany(selfQuery1);

		// 从oracle取数据
		List<Map<String, Object>> selfRs1 = new ArrayList<Map<String, Object>>();
		String selfSql1 = "select * from T_TX_ZWYC_SBSSDKX where " + selfField1
				+ " = " + OID;
		try {
			selfRs1 = db.findAllMap(selfSql1);
		} catch (SQLException e) {
			resp.setCode(ReturnCode.SQLERROR);
			System.out.println("数据库查询出错");
			return resp;
		}
		if (selfRs1 == null || selfRs1.size() == 0) {
			resp.setCode(ReturnCode.NODATA);
			System.out.println("数据库查询无数据");
			return resp;
		}
		// 从oracle跟新数据
		for (int j = 0; j < selfRs1.size(); j++) { // 每一条rDevOracle记录添加到mongo更新
			// 得到oracle记录
			Map<String, Object> eachDev = selfRs1.get(j);
			// 将oracle记录变成mongo记录
			BsonDocument eachDevBson = SwitchMongoOracleUtil.OracleRecord2MongoRecord("T_TX_ZWYC_SBSSDKX", eachDev);
			// 将该记录加入mongo
			SwitchMongoOracleUtil.insertMongoFromBsonRecord("T_TX_ZWYC_SBSSDKX", eachDevBson);
		}
		updatedTableList.add("T_TX_ZWYC_SBSSDKX");

		// 3.2:T_TX_ZWYC_DKXZX
		// 删除mongo
		MongoCollection<BsonDocument> SBSSDKXCollection2 = null;
		BsonDocument selfQuery2 = new BsonDocument();
		String selfField2 = "SSDKX";
		selfQuery2.put(selfField2, new BsonInt32(Integer.parseInt(OID)));
		SBSSDKXCollection2 = MongoDBUtil.instance
				.getCollection("T_TX_ZWYC_DKXZX");
		SBSSDKXCollection2.deleteMany(selfQuery2);

		// 从oracle取数据
		List<Map<String, Object>> selfRs2 = new ArrayList<Map<String, Object>>();
		String selfSql2 = "select * from T_TX_ZWYC_DKXZX where " + selfField2
				+ " = " + OID;
		try {
			selfRs2 = db.findAllMap(selfSql2);
		} catch (SQLException e) {
			resp.setCode(ReturnCode.SQLERROR);
			System.out.println("数据库查询出错");
			return resp;
		}
		if (selfRs2 == null || selfRs2.size() == 0) {
			resp.setCode(ReturnCode.NODATA);
			System.out.println("数据库查询无数据");
			return resp;
		}
		// 从oracle跟新数据
		for (int j = 0; j < selfRs2.size(); j++) { // 每一条rDevOracle记录添加到mongo更新
			// 得到oracle记录
			Map<String, Object> eachDev = selfRs2.get(j);
			// 将oracle记录变成mongo记录
			BsonDocument eachDevBson = SwitchMongoOracleUtil
					.OracleRecord2MongoRecord("T_TX_ZWYC_DKXZX", eachDev);
			// 将该记录加入mongo
			SwitchMongoOracleUtil.insertMongoFromBsonRecord("T_TX_ZWYC_DKXZX",
					eachDevBson);
		}
		updatedTableList.add("T_TX_ZWYC_DKXZX");

		// 3.3:T_TX_ZWYC_DKX
		// 删除mongo
		MongoCollection<BsonDocument> SBSSDKXCollection3 = null;
		BsonDocument selfQuery3 = new BsonDocument();
		String selfField3 = "DKXID";
		selfQuery3.put(selfField3, new BsonInt32(Integer.parseInt(OID)));
		SBSSDKXCollection3 = MongoDBUtil.instance
				.getCollection("T_TX_ZWYC_DKX");
		SBSSDKXCollection3.deleteMany(selfQuery3);

		// 从oracle取数据
		List<Map<String, Object>> selfRs3 = new ArrayList<Map<String, Object>>();
		String selfSql3 = "select * from T_TX_ZWYC_DKX where " + selfField3
				+ " = " + OID;
		try {
			selfRs3 = db.findAllMap(selfSql3);
		} catch (SQLException e) {
			resp.setCode(ReturnCode.SQLERROR);
			System.out.println("数据库查询出错");
			return resp;
		}
		if (selfRs3 == null || selfRs3.size() == 0) {
			resp.setCode(ReturnCode.NODATA);
			System.out.println("数据库查询无数据");
			return resp;
		}
		// 从oracle跟新数据
		for (int j = 0; j < selfRs3.size(); j++) { // 每一条rDevOracle记录添加到mongo更新
			// 得到oracle记录
			Map<String, Object> eachDev = selfRs3.get(j);
			// 将oracle记录变成mongo记录
			BsonDocument eachDevBson = SwitchMongoOracleUtil.OracleRecord2MongoRecord("T_TX_ZWYC_DKX", eachDev);
			// 将该记录加入mongo
			SwitchMongoOracleUtil.insertMongoFromBsonRecord("T_TX_ZWYC_DKX", eachDevBson);
		}
		updatedTableList.add("T_TX_ZWYC_DKX");

		updatedTableList.add("T_TX_ZNYC_DZ");
		String[] updatedTable = new String[updatedTableList.size()];
		updatedTable = updatedTableList.toArray(updatedTable);
		resp.setUpdatedTable(updatedTable);
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}

}
