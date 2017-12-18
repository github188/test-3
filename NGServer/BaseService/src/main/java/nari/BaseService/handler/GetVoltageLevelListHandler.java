package nari.BaseService.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import nari.BaseService.BaseServiceActivator;
import nari.Dao.interfaces.DbAdaptor;
import nari.model.TableName;
import nari.parameter.BaseService.GetVoltageLevelList.GetVoltageLevelListResponse;
import nari.parameter.bean.VoltageLevel;
import nari.parameter.code.ReturnCode;

public class GetVoltageLevelListHandler {

	public GetVoltageLevelListResponse getVoltageLevelList() {
		DbAdaptor db = BaseServiceActivator.dbAdaptor;
		GetVoltageLevelListResponse resp = new GetVoltageLevelListResponse();
		
		String sql = "select * from " + TableName.CONF_CODEDEFINITION + " where codeid = 10401";
		List<Map<String, Object>> listMap = null;
		
		try {
			listMap = db.findAllMap(sql);
		} catch (SQLException e) {
			System.out.printf("数据库查询出错");
			resp.setCode(ReturnCode.SQLERROR);
			return resp;
		}
		
		if(listMap == null || listMap.size() == 0) {
			System.out.printf("查询无数据");
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		
		VoltageLevel[] voltageLevels = new VoltageLevel[listMap.size()];
		for(int i = 0; i < listMap.size(); i++) {
			voltageLevels[i] = new VoltageLevel();
			voltageLevels[i].setVoltageLevelID(String.valueOf(listMap.get(i).get("codedefid")));
			voltageLevels[i].setVoltageLevelName(String.valueOf(listMap.get(i).get("codename")));
		}
		
		resp.setCode(ReturnCode.SUCCESS);
		resp.setVoltageLevels(voltageLevels);
		return resp;
	}
}
