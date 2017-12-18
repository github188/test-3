package nari.MainGridService.BaseHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.MainGridService.MainGridServiceActivator;
import nari.MainGridService.Service.interfaces.MainGridService;
import nari.parameter.BaseService.GetClassMeta.GetClassMetaResponse;
import nari.parameter.MainGridService.getIpAdress.GetIpAdressRequest;
import nari.parameter.MainGridService.getIpAdress.GetIpAdressResponse;
import nari.parameter.code.ReturnCode;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetIpAdressHandler {

	public GetIpAdressResponse getIpAdress(GetIpAdressRequest req){
		GetIpAdressResponse resp = new GetIpAdressResponse();
		DbAdaptor db = MainGridServiceActivator.dbAdaptor;

		String whereCondition = ""; // 条件语句
		// ip条件
		String[] ipTypes = req.getIpType();
		if (ipTypes != null && ipTypes.length != 0) {
			StringBuffer ipTypesCondition = new StringBuffer();
			ipTypesCondition.append("((bz like '%" + ipTypes[0] + "%')");
			for (int i = 1; i < ipTypes.length; i++) {
				ipTypesCondition.append(" or (bz like '%" + ipTypes[i] + "%')");
			}
			ipTypesCondition.append(")");
			whereCondition = "where " + ipTypesCondition.toString();
		}

		String selectCondition = "select *"; // 查询字段语句
//		// 查询字段条件
//		String[] returnFields = req.getReturnField();
//		if (returnFields != null && returnFields.length != 0) {
//			StringBuffer fieldCondition = new StringBuffer();
//			fieldCondition.append("( " + returnFields[0]);
//			for (int i = 1; i < returnFields.length; i++) {
//				fieldCondition.append("," + returnFields[i]);
//			}
//			fieldCondition.append(")");
//			selectCondition = "select " + fieldCondition;
//		}

		String sql = ""; // 总sql语句
		sql = selectCondition + " from zwgk_pz_ggdmb@dblink_mcsas " + whereCondition;
		List<Map<String, Object>> listMap = null;
		try {
			listMap = db.findAllMap(sql);
		} catch (SQLException e) {
			System.out.println("数据库查询出错");
			resp.setCode(ReturnCode.SQLERROR);
			return resp;
		}

		if (listMap == null || listMap.size() == 0) {
			System.out.println("查询无数据");
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}

		// 若returnField为空,返回全部
		String[] returnFields = null;
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
