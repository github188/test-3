package nari.BaseService.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import nari.BaseService.BaseServiceActivator;
import nari.Dao.interfaces.DbAdaptor;
import nari.model.TableName;
import nari.parameter.BaseService.GetMapClassList.GetMapClassListRequest;
import nari.parameter.BaseService.GetMapClassList.GetMapClassListResponse;
import nari.parameter.bean.PsrTypeInfo;
import nari.parameter.bean.PsrTypeInfoList;
import nari.parameter.bean.VoltageLevel;
import nari.parameter.code.ReturnCode;

public class GetMapClassListHandler {
	
	private static final PsrTypeInfo[] classInfos = null;

	public GetMapClassListResponse getMapClassList(GetMapClassListRequest request) {
		int mapId = request.getMapId();
		String sql = "select t1.classid, t2.classname, t2.classalias from " + TableName.CONF_DOCUMENTMODEL + 
				" t1, " + TableName.CONF_OBJECTMETA + " t2 where t1.classid = t2.classid and t1.mapid = " + String.valueOf(mapId);
				
		GetMapClassListResponse resp = new GetMapClassListResponse();
		DbAdaptor db = BaseServiceActivator.dbAdaptor;
		List<Map<String, Object>> listMap = null;
		
		try {
			listMap = db.findAllMap(sql);
		} catch (SQLException e) {
			System.out.println("Error");
			resp.setCode(ReturnCode.SQLERROR);
			return resp;
		}
		
		if (listMap == null || listMap.size() == 0) {
			System.out.println("NoData");
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		
		PsrTypeInfo[] psrTypeInfos = new PsrTypeInfo[listMap.size()];
		for(int i = 0; i < listMap.size(); i++) {
			psrTypeInfos[i] = new PsrTypeInfo();
			psrTypeInfos[i].setPsrTypeId(String.valueOf(listMap.get(i).get("classid")));
			psrTypeInfos[i].setPsrTypeName(String.valueOf(listMap.get(i).get("classname")));
			psrTypeInfos[i].setPsrTypeAlias(String.valueOf(listMap.get(i).get("classalias")));
		}
		
		PsrTypeInfoList psrTypeInfoList = new PsrTypeInfoList();
		psrTypeInfoList.setMapId(mapId);
		psrTypeInfoList.setPsrTypeInfos(psrTypeInfos);
		
		resp.setCode(ReturnCode.SUCCESS);
		resp.setPsrTypeInfoList(psrTypeInfoList);
		return resp;
	}

}
