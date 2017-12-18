package nari.QueryService.handler;

import java.util.ArrayList;
import java.util.List;

import nari.QueryService.bean.GetAllSubSetRequest;
import nari.QueryService.bean.GetAllSubSetResponse;
import nari.QueryService.bean.SpatialqueryVirtualRequest;
import nari.QueryService.bean.SpatialqueryVirtualResponse;
import nari.parameter.QueryService.SpatialQuery.SpatialQueryRequest;
import nari.parameter.QueryService.SpatialQuery.SpatialQueryResponse;
import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.TypeCondition;
import nari.parameter.code.ReturnCode;

public class SpatialqueryVirtualHandler {

	public SpatialqueryVirtualResponse spatialqueryVirtual(SpatialqueryVirtualRequest req){
		SpatialqueryVirtualResponse resp = new SpatialqueryVirtualResponse();
		
		//先调空间查询
		SpatialQueryHandler spatialHandler = new SpatialQueryHandler();
		SpatialQueryRequest spatialReq = new SpatialQueryRequest();
		spatialReq.setGeom(req.getGeom());
		//修改一下其中的condition,是返回字段必须有接下来要用的classid,sbid
		TypeCondition[] conds = req.getConds();
		
		for(TypeCondition con : conds){
			String[] returnField = con.getReturnField();
			List<String> fieldList = new ArrayList<String>();
			boolean sbidFlag = false;
			boolean classidFlag = false;
			for(String field : returnField){
				fieldList.add(field);
				if("SBID".equalsIgnoreCase(field)){
					sbidFlag = true;
				}
				if("classid".equalsIgnoreCase(field)){
					classidFlag = true;
				}
			}
			if(!sbidFlag){
				fieldList.add("SBID");
			}
			if(!sbidFlag){
				fieldList.add("CLASSID");
			}
			String[] returnFields = new String[fieldList.size()];
			returnFields = fieldList.toArray(returnFields);
			con.setReturnField(returnFields);
		}
		spatialReq.setConds(req.getConds());
		spatialReq.setToken(req.getToken());
		SpatialQueryResponse spatialResp = spatialHandler.spatialQuery(spatialReq);
		
		
		for(QueryResult spatialResult : spatialResp.getResult()){
			List<QueryResult> QueryResultList = new ArrayList<QueryResult>();
			for(QueryRecord record : spatialResult.getRecords()){
				//得到所需sbid,classid
				String classid = "";
				String sbid = "";
				QueryField[] fields = record.getFields();
				for(QueryField field : record.getFields()){
					if("sbid".equalsIgnoreCase(field.getFieldName())){
						sbid = field.getFieldValue();
					}else if("classid".equalsIgnoreCase(field.getFieldName())){
						classid = field.getFieldValue();
					}else{
						continue;
					}
				}
				//再调虚拟设备下所有设备的查询
				GetAllSubSet subSetHandler = new GetAllSubSet();
				GetAllSubSetRequest subSetReq= new GetAllSubSetRequest();
				subSetReq.setSBID(sbid);
				subSetReq.setPclassId(classid);
				GetAllSubSetResponse subSetResp = subSetHandler.getAllSubSet(subSetReq);
				QueryResult[] subResults = subSetResp.getResult();
				if(subResults == null || subResults.length == 0){
					continue;
				}
				for(QueryResult subResult : subResults){
					QueryResultList.add(subResult);
				}
			}
			
			QueryResult[] result = new QueryResult[QueryResultList.size()];
			result = QueryResultList.toArray(result);
			resp.setResult(result);
			resp.setCode(ReturnCode.SUCCESS);
		}
		
		return resp;
	}
}
