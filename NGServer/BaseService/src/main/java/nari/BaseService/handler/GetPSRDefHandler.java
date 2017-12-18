package nari.BaseService.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nari.BaseService.BaseServiceActivator;
import nari.Dao.interfaces.DbAdaptor;
import nari.model.TableName;
import nari.parameter.BaseService.GetPSRDef.GetPSRDefRequest;
import nari.parameter.BaseService.GetPSRDef.GetPSRDefResponse;
import nari.parameter.bean.PSRDef;
import nari.parameter.code.ReturnCode;

public class GetPSRDefHandler {
	public GetPSRDefResponse getPSRDef(GetPSRDefRequest request){
		GetPSRDefResponse resp = new GetPSRDefResponse();
		DbAdaptor dbh = BaseServiceActivator.dbAdaptor;
		String type = request.getType();
		//根据所属类型写查询条件
		String where = "";
		if("zwyc".equals(type)){
			where = "where (ELECTRICLOGIC = 9 or ELECTRICLOGIC = 25) and ISGEOMETRY = 1";
		}else if("dysb".equals(type)){
			where = "where (ELECTRICLOGIC = 26 or ELECTRICLOGIC = 10 or ELECTRICLOGIC = 6) and not(CLASSNAME like '%YXSB%') and ISGEOMETRY = 1";
		}else if("yxsb".equals(type)){
			where = "where (ELECTRICLOGIC = 26 or ELECTRICLOGIC = 10) and CLASSNAME like '%YXSB%' and ISGEOMETRY = 1";
		}else if("znyc".equals(type)){
			where = "where ELECTRICLOGIC = 5 and ISGEOMETRY = 1";
		}else if("ggsb".equals(type)){
			where = "where ELECTRICLOGIC = 0 and ISGEOMETRY = 1";
		}else if("xnrq".equals(type)){
			where = "where ISGEOMETRY = 0";
		}else{
			resp.setCode(ReturnCode.MISSTYPE);
			return resp;
		}
		//查询得到对应类型的设备类型查询
		String sql = "select CLASSID, CLASSALIAS, ELECTRICLOGIC, ISGEOMETRY, CLASSNAME from " + TableName.CONF_OBJECTMETA + " "+where;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = dbh.findAllMap(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		PSRDef[] psrDefs = new PSRDef[list.size()];
		for(int i=0;i<list.size();i++){
			Map<String, Object> map = list.get(i);
			String a = String.valueOf(map.get("classid"));
//			String a = (String)(list.get(i).get("classid"));
			psrDefs[i] = new PSRDef();
			psrDefs[i].setPsrType(a);
			psrDefs[i].setPsrName(String.valueOf(list.get(i).get("classalias")));
		}
		resp.setPsrDefs(psrDefs);
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}
}
