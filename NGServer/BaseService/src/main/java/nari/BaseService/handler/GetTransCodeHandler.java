package nari.BaseService.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import nari.BaseService.BaseServiceActivator;
import nari.Dao.interfaces.DbAdaptor;
import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.model.TableName;
import nari.parameter.BaseService.getTransCode.GetTransCodeRequest;
import nari.parameter.BaseService.getTransCode.GetTransCodeResponse;
import nari.parameter.code.ReturnCode;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 10.得到转义表码值
 */
public class GetTransCodeHandler {

	private Logger logger = LoggerManager.getLogger(this.getClass());
	
	public GetTransCodeResponse getTransCode(GetTransCodeRequest request) {
		DbAdaptor db = BaseServiceActivator.dbAdaptor;
		GetTransCodeResponse resp = new GetTransCodeResponse();

		String whereCondition = ""; // 条件语句
		String[] CodeDefId = request.getCodeDefId();
		if (CodeDefId != null && CodeDefId.length != 0) {	//CodeDefId条件
			StringBuffer CodeDefIdCondition = new StringBuffer();
			CodeDefIdCondition.append("CodeDefId in ( " + CodeDefId[0]);
			for (int i = 1; i < CodeDefId.length; i++) {
				CodeDefIdCondition.append("," + CodeDefId[i]);
			}
			CodeDefIdCondition.append(")");
			if(whereCondition.equalsIgnoreCase("")){
				whereCondition = "where " + CodeDefIdCondition.toString();
			}else{
				whereCondition = " and " + CodeDefIdCondition.toString();
			}
		}
		
		String[] CodeId = request.getCodeId();
		if (CodeId != null && CodeId.length != 0) {	//CodeId条件
			StringBuffer CodeIdCondition = new StringBuffer();
			CodeIdCondition.append("CodeDefId in ( " + CodeId[0]);
			for (int i = 1; i < CodeId.length; i++) {
				CodeIdCondition.append("," + CodeId[i]);
			}
			CodeIdCondition.append(")");
			if(whereCondition.equalsIgnoreCase("")){
				whereCondition = "where " + CodeIdCondition.toString();
			}else{
				whereCondition = " and " + CodeIdCondition.toString();
			}
		}

		// 查询字段条件
		String[] returnFields = request.getReturnField();
		String selectCondition = "select *"; // 查询字段语句
		if (returnFields != null && returnFields.length != 0) {
			StringBuffer fieldCondition = new StringBuffer();
			fieldCondition.append("( " + returnFields[0]);
			for (int i = 1; i < returnFields.length; i++) {
				fieldCondition.append("," + returnFields[i]);
			}
			fieldCondition.append(")");
			selectCondition = "select " + fieldCondition;
		}

		String sql = ""; // 总sql语句
		sql = selectCondition + " from " + TableName.CONF_OBJECTMETA + " " + whereCondition;
		List<Map<String, Object>> listMap = null;
		try {
			listMap = db.findAllMap(sql);
		} catch (SQLException e) {
			logger.info("数据库查询出错");
			resp.setCode(ReturnCode.SQLERROR);
			return resp;
		}

		if (listMap == null || listMap.size() == 0) {
			logger.info("查询无数据");
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}

		// 若returnField为空,返回全部
		if (returnFields == null || returnFields.length == 0) {
			Object[] keysName = listMap.get(0).keySet().toArray();
			returnFields = new String[keysName.length];
			for (int j = 0; j < keysName.length; j++) {
				returnFields[j] = String.valueOf(keysName[j]);
			}
		}

		JSONArray resultArray = new JSONArray();
//		List<Map<String, Object>> JudgeSame = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < listMap.size(); i++) {
			JSONObject resultObject = new JSONObject();
			Map<String, Object> map = listMap.get(i);
			// 给返回字段赋值
			for (int j = 0; j < returnFields.length; j++) {
				Object value = map.get(returnFields[j].toLowerCase());
				resultObject.put(returnFields[j].toUpperCase(), value);
			}
			if (resultObject == null || resultObject.isEmpty()) {
				return null;
			}
			resultArray.add(resultObject);
		}

		resp.setCode(ReturnCode.SUCCESS);
		resp.setResultJSONArray(resultArray);
		return resp;
	}

}
