package nari.MongoQuery.MapService.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.Logger.LoggerManager;
import nari.MongoQuery.MongoQueryActivator;
import nari.model.TableName;
import nari.parameter.BaseService.GroupTable.GroupTableRequest;
import nari.parameter.BaseService.GroupTable.GroupTableResponse;
import nari.parameter.bean.GroupTableCondition;
import nari.parameter.bean.TableCondition;
import nari.parameter.code.ReturnCode;

public class GroupTableHandler {

	DbAdaptor db = MongoQueryActivator.dbAdaptor;
	
	private nari.Logger.Logger logger = LoggerManager.getLogger(this.getClass());

	public GroupTableResponse groupTable(GroupTableRequest req) {
		GroupTableResponse resp = new GroupTableResponse();

		int groupCount = Integer.valueOf(req.getGroupCount());	//分组数
		String getTableNameSql = "select geotablename from " + TableName.CONF_DOCUMENTMODEL + " t where t.mapid=1001";
		// 获取所有的classId
		List<Map<String, Object>> tableNameList = new ArrayList<Map<String, Object>>();
		try {
			tableNameList = db.findAllMap(getTableNameSql);
		} catch (SQLException e) {
			logger.info("数据库查询出错");
			resp.setCode(ReturnCode.SQLERROR);
			return resp;
		}
		if (tableNameList == null || tableNameList.size() == 0) {
			logger.info("查询结果无数据");
			resp.setCode(ReturnCode.NODATA);
			return resp;
		}

		//对每张表进行查询
		int tableCount = tableNameList.size();
		int totalCount = 0;
		Map<String,Integer> tableRecordCountMap = new HashMap<String,Integer>();
		for(int i=0;i<tableCount;i++){
			String eachTableName = String.valueOf(tableNameList.get(i).get("geotablename"));
			String getRecordSql = "select count(*) from "+eachTableName;
			List<Map<String, Object>> RecordCountList = new ArrayList<Map<String, Object>>();
			try {
				RecordCountList = db.findAllMap(getRecordSql);
			} catch (SQLException e) {
				logger.info("数据库查询出错");
				resp.setCode(ReturnCode.SQLERROR);
				return resp;
			}
			if(RecordCountList == null || RecordCountList.size() == 0){
				logger.info("查询结果无数据");
				resp.setCode(ReturnCode.NODATA);
				return resp;
			}
			String recordCount = String.valueOf(RecordCountList.get(0).get("count(*)"));
			Integer eachTableCount = Integer.valueOf(recordCount);
			totalCount = totalCount+eachTableCount;
			tableRecordCountMap.put(eachTableName, eachTableCount);
		}	//每张表查询完毕
		
		//对每个map进行判断分组
		int eachGroupCount = totalCount/groupCount;	//每组计算的记录数
		GroupTableCondition[] groupTableConditions = new GroupTableCondition[groupCount];
	
		int tableNum=0;	//表序号
		for(int i=0;i<groupCount;i++){
			int eachGroupTotalCount = 0;	//每张表记录总和数
			List<TableCondition> TableConditionList = new ArrayList<TableCondition>();
		for(int j=tableNum;j<tableCount;j++){	//每张表的循环
			
			
			String eachTableName = String.valueOf(tableNameList.get(j).get("geotablename"));
			int eachtableCount = tableRecordCountMap.get(eachTableName);
			
			TableCondition tableCondition = new TableCondition();
			tableCondition.setName(eachTableName);
			tableCondition.setRecordCount(String.valueOf(eachtableCount));
			TableConditionList.add(tableCondition);
			eachGroupTotalCount = eachGroupTotalCount + eachtableCount;
			if(eachGroupTotalCount>=eachGroupCount){	//若表的记录数大于每组数,组成一组
				TableCondition[] tableConditions = new TableCondition[TableConditionList.size()];
				tableConditions = TableConditionList.toArray(tableConditions);
				groupTableConditions[i] = new GroupTableCondition();
				groupTableConditions[i].setTableConditions(tableConditions);
				groupTableConditions[i].setGroupNum(String.valueOf(i));
				groupTableConditions[i].setRecordsCount(String.valueOf(eachGroupTotalCount));
				
				tableNum = j+1;	//下张表开始循环
				break;	//跳出循环到第二组
			}
		}
		}
		resp.setGroupTableConditions(groupTableConditions);
		resp.setCode(ReturnCode.SUCCESS);
		return resp;
	}

}
