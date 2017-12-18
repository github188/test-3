package nari.MainGridService.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

import nari.Dao.interfaces.DbAdaptor;
import nari.MainGridService.MainGridServiceActivator;
import nari.model.device.ModelService;
import nari.model.geometry.GeometryService;
import nari.parameter.MainGridService.XLGeoQuery.XLGeoQueryRequest;
import nari.parameter.MainGridService.XLGeoQuery.XLGeoQueryResponse;
import nari.parameter.bean.GeometryPair;
import nari.parameter.bean.QueryField;
import nari.parameter.bean.QueryRecord;
import nari.parameter.bean.QueryResult;
import nari.parameter.bean.SelfDefField;
import nari.parameter.code.ReturnCode;

public class XLGeoQueryHandler {
	GeometryService geoService = MainGridServiceActivator.geoService;
	ModelService ms = MainGridServiceActivator.modelService;
	DbAdaptor db = MainGridServiceActivator.dbAdaptor;
	Map<String, String> DYDJCodeMap = MainGridServiceActivator.DYDJCodeMap;

	public XLGeoQueryResponse xLGeoQuery(
			XLGeoQueryRequest request) {
		XLGeoQueryResponse resp = new XLGeoQueryResponse();
		//线路搜索条件
		String[] SBID = request.getSBID();
		String[] OID = request.getOID();
		String[] DYDJ = request.getDYDJ();
		String xlCondition = "";
		StringBuffer SBIDCondition = new StringBuffer();
		StringBuffer DYDJCondition = new StringBuffer();
		StringBuffer OIDCondition = new StringBuffer();
		if (SBID != null && SBID.length != 0) {	//若sbId条件不为空
			SBIDCondition.append("sbid in (");
			SBIDCondition.append("'"+SBID[0]);
			for (int i = 1; i < SBID.length; i++) {
				SBIDCondition.append("','" + SBID[i]);
			}
			SBIDCondition.append("')");
			if("".equalsIgnoreCase(xlCondition)){
				xlCondition = xlCondition + SBIDCondition;
			}else{
				xlCondition = xlCondition+"and"+SBIDCondition;
			}
		}
		if(DYDJ != null && DYDJ.length != 0){	//若电压等级条件不为空
			DYDJCondition.append("dydj in (");
			DYDJCondition.append("'"+DYDJ[0]);
			for (int i = 1; i < DYDJ.length; i++) {
				DYDJCondition.append("','" + DYDJ[i]);
			}
			DYDJCondition.append("')");
			if("".equalsIgnoreCase(xlCondition)){
				xlCondition = xlCondition + DYDJCondition;
			}else{
				xlCondition = xlCondition+"and"+DYDJCondition;
			}
		}
		if(OID != null && OID.length != 0){	//若OID条件不为空
			OIDCondition.append("OID in (");
			OIDCondition.append("'"+OID[0]);
			for (int i = 1; i < OID.length; i++) {
				OIDCondition.append("','" + OID[i]);
			}
			OIDCondition.append("')");
			if("".equalsIgnoreCase(xlCondition)){
				xlCondition = xlCondition + OIDCondition;
			}else{
				xlCondition = xlCondition+"and"+OIDCondition;
			}
		}
		
		//最后对xlCondition作出判断
		if(!("".equalsIgnoreCase(xlCondition))){
			xlCondition = "where "+xlCondition;
		}
		
		//线路查询返回字段条件
		boolean shapeFlag = false;	//由于此字段需特殊处理，故要做判断
		StringBuffer FeildCondition = new StringBuffer();
		String[] returnFields = request.getReturnFeilds();
		if(returnFields == null || returnFields.length == 0){
			returnFields = new String[]{
				"OID","SBID","SBMC","SHAPE"
			};
			shapeFlag = true;
		}
		FeildCondition.append(returnFields[0]);
		if("SHAPE".equalsIgnoreCase(returnFields[0])){
			shapeFlag = true;
		}
		for(int i=1;i<returnFields.length;i++){
			FeildCondition.append(","+returnFields[i]);
			if("SHAPE".equalsIgnoreCase(returnFields[i])){
				shapeFlag = true;
			}
		}
		String xlSql = "select "+FeildCondition+" from t_tx_zwyc_xlgeo " + xlCondition;
		//查询得到结果
		List<Map<String,Object>> XLList = new ArrayList<Map<String,Object>>();
		try {
			XLList = db.findAllMap(xlSql);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("数据库查询出错");
			resp.setCode(ReturnCode.SQLERROR);
			return resp;
		}
		if(XLList == null || XLList.size() == 0){
			System.out.println("查询结果无数据");
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}
		
		//对返回值的处理
		QueryResult[] results = new QueryResult[1];
		results[0] = new QueryResult();
		int recordCount = XLList.size();
		results[0].setCount(recordCount);
		QueryRecord[] records = new QueryRecord[recordCount];
		int fieldsNum = returnFields.length;
		
		//每条记录的处理
		for(int i=0;i<recordCount;i++){
			Map<String,Object> eachRecordMap = XLList.get(i);
			records[i] = new QueryRecord();
			
			List<QueryField> fieldList = new ArrayList<QueryField>();
			//返回每个字段对应的值
			for(int j=0;j<fieldsNum;j++){
				QueryField field = new QueryField();
				
				field.setFieldName(returnFields[j]);
				String fieldValue = String.valueOf(eachRecordMap.get(returnFields[j].toLowerCase()));
				if("sbmc".equalsIgnoreCase(returnFields[j])){
					fieldValue = modifySBMC(fieldValue);
				}
				field.setFieldValue(fieldValue);
				fieldList.add(field);
			}	//添加feild结束
			
			//添加自定义feild
			SelfDefField[] selfDefFields = request.getSelfDefFields();
			if(selfDefFields != null && selfDefFields.length != 0){
				for(SelfDefField selfDefField:selfDefFields){
					QueryField fields2 = new QueryField();
					fields2.setFieldName(selfDefField.getSelfFieldName());
					fields2.setFieldValue(selfDefField.getSelfFieldValue());
					fields2.setFieldAlias(selfDefField.getSelfFieldAlias());
					fieldList.add(fields2);
				}
			}	//自定义添加feild结束
			QueryField[] fields = new QueryField[fieldList.size()];
			fields = fieldList.toArray(fields);
			records[i].setFields(fields);
			
			//返回shape信息
			if(shapeFlag){
				GeometryPair geom = new GeometryPair();
				Object shapeObject = eachRecordMap.get("shape");
				if ("oracle.sql.STRUCT".equals(shapeObject.getClass().getName())){
					try {
						STRUCT struct = (STRUCT)shapeObject;
						JGeometry jGeometry = JGeometry.load(struct);
						int[] elemInfo = jGeometry.getElemInfo();
						int[] startDouble = new int[elemInfo.length/3];
						for(int j=0;j<elemInfo.length/3;j++){
							startDouble[j] = elemInfo[3*j];
						}
						geom.setStartDouble(startDouble);
						geom.setCoords(jGeometry.getOrdinatesArray());
						int typeNum = jGeometry.getType();
						String geoType = "";
						switch(typeNum){
						case 1:geoType = "POINT";break;
						case 2:geoType = "LineString";break;
						case 3:geoType = "POLYGON";break;
						case 6:geoType = "MultiLineString";break;
						}
						geom.setGeometryType(geoType);
						int geoNum = jGeometry.getOrdinatesOfElements().length;
						geom.setOther(geoNum);
						}catch(Exception e){
							e.printStackTrace();
						}
				}
				records[i].setGeom(geom);
			}
		}
		
		//返回结果
		results[0].setRecords(records);
		resp.setResult(results);
		resp.setCode(ReturnCode.SUCCESS);
		return resp;

			}
	
	//将设备名称包含特殊符号的去掉
	public String modifySBMC(String value){
		if(value.contains(":") || 
				value.contains("") || 
				value.contains("'") || 
				value.contains("{") || 
				value.contains("}") || 
				value.contains("\"") || 
				value.contains(",") || 
				value.contains("[") || 
				value.contains("]") || 
				value.contains("\t") || 
				value.contains("\r") || 
				value.contains("\f") || 
				value.contains("\b") || 
				value.contains("\n") || 
				value.contains("\"") || 
				value.contains(" ")||
				value.contains("～")){
			
			value = value.replaceAll(":", "；");
			value = value.replaceAll("\\\\", "");
			value = value.replaceAll("'", "");
			value = value.replaceAll("\"", "");
			value = value.replaceAll("\t","");
			value = value.replaceAll("\r","");
			value = value.replaceAll("\f","");
			value = value.replaceAll("\n","");
			value = value.replaceAll("～", "_");
		}
		return value;
	}
}
