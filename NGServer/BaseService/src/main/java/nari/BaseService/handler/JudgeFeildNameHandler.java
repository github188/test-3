package nari.BaseService.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nari.BaseService.BaseServiceActivator;
import nari.Dao.interfaces.DbAdaptor;
import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.model.TableName;
import nari.parameter.BaseService.JudgeFeildName.JudgeFeildNameRequest;
import nari.parameter.BaseService.JudgeFeildName.JudgeFeildNameResponse;
import nari.parameter.code.ReturnCode;

public class JudgeFeildNameHandler {
	DbAdaptor dbAdaptor = BaseServiceActivator.dbAdaptor;
	
	private Logger logger = LoggerManager.getLogger(this.getClass());
	
	public JudgeFeildNameResponse judgeFeildName(JudgeFeildNameRequest request){
		JudgeFeildNameResponse resp = new JudgeFeildNameResponse();
		List<Map<String,Object>> tableList = new ArrayList<Map<String,Object>>();
		String [] feildName = request.getFieldName();
		String allTableSql = "select classid, classname from " + TableName.CONF_OBJECTMETA;
		try {
			tableList = dbAdaptor.findAllMap(allTableSql);
		} catch (SQLException e) {
			logger.info("数据库查询出错");
			resp.setCode(ReturnCode.SQLERROR);
			return resp;
		}
		if(tableList == null || tableList.size() == 0){
			logger.info("查无数据");
			resp.setCode(ReturnCode.SQLERROR);
			return resp;
		}
		//包含该字段的classid的集合
		List<String> containClassIdList = new ArrayList<String>(); 
		List<String> notContainClassIdList = new ArrayList<String>(); 
		List<String> noDataClassIdList = new ArrayList<String>();
		//对每个表进行查询
		for(int i=0;i<tableList.size();i++){
			List<Map<String,Object>> recordList = new ArrayList<Map<String,Object>>();
			String tableName = String.valueOf(tableList.get(i).get("classname"));
			String eachTableSql = "select * from "+tableName+" where rownum<2";
			try {
				recordList = dbAdaptor.findAllMap(eachTableSql);
			} catch (SQLException e) {
				logger.info("数据库查询出错");
				resp.setCode(ReturnCode.SQLERROR);
				return resp;
			}
			if(recordList == null || recordList.size() == 0){
				noDataClassIdList.add(String.valueOf(tableList.get(i).get("classid")));
				continue;
			}
			boolean containFlag = true;
			for(int j=0;j<feildName.length;j++){
				//判断是否包含此字段
				if(!recordList.get(0).containsKey(feildName[j].toLowerCase())){
					containFlag = false;
					break;
				}
			}
				if(containFlag){
					containClassIdList.add(String.valueOf(tableList.get(i).get("classid")));
				}else{
					notContainClassIdList.add(String.valueOf(tableList.get(i).get("classid")));
				}
		}
		//返回classId集合
		String[] containClassIds = new String[containClassIdList.size()];
		containClassIds = containClassIdList.toArray(containClassIds);
		String[] notContainClassIds = new String[notContainClassIdList.size()];
		notContainClassIds = notContainClassIdList.toArray(notContainClassIds);
		String[] noDataClassIds = new String[noDataClassIdList.size()];
		noDataClassIds = noDataClassIdList.toArray(noDataClassIds);
		resp.setNoDataClassIds(noDataClassIds);
		resp.setContainClassIds(containClassIds);
		resp.setNotContainClassIds(notContainClassIds);
		
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}
}
