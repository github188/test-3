package nari.MainGridService.handler;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.MainGridService.MainGridServiceActivator;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetForecastMessageHandler {

	private DbAdaptor db215 = MainGridServiceActivator.dbAdaptor215;
	
	public JSONObject getForecastMessage(String TFBH,String forecastTime){
		JSONObject responseObject = new JSONObject();
		JSONArray responseArray = new JSONArray();
		//根据传的当前时间得到数据库里有的发布时间
		String getFbsJSql = " select fbsj from ZWGK_SJJC_TFJK_YB where fbsj >= to_date('"+forecastTime+"','yyyy-mm-dd hh24:mi:ss') " +
				" and rownum <=1 and TFBH = "+TFBH+" group by fbsj order by fbsj desc";
		List<Map<String,Object>> getFbsJList = new ArrayList<Map<String,Object>>();
		try {
			getFbsJList = db215.findAllMap(getFbsJSql);
		} catch (SQLException e) {
			System.out.println("数据库查询错误");
			e.printStackTrace();
		}
		if(getFbsJList == null || getFbsJList.size()==0){
			return responseObject.put("noData", "无预测数据");
		}
		//关于date的相互转换
		Object obj = getFbsJList.get(0).get("fbsj");
		java.util.Date formSqlDate = (java.util.Date)obj;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	//H24小时制
		String fbsj = format.format(formSqlDate);
		
		String fbsjCondition = "(fbsj = to_date('"+fbsj+"','yyyy-mm-dd hh24:mi:ss'))";	//发布时间条件
		String tfNameCondition = "tfbh = "+TFBH;	//台风编号条件
		String TFCondition = " where "+fbsjCondition+" and "+tfNameCondition;
		String forecastSql = "select * from ZWGK_SJJC_TFJK_YB "+TFCondition;	//查询sql
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		try {
			resultList = db215.findAllMap(forecastSql);
		} catch (SQLException e) {
			System.out.println("数据库查询错误");
			e.printStackTrace();
		}
		Map<String,String> jageSameMap = new HashMap<String,String>();
		if(resultList == null || resultList.size() == 0 || resultList.get(0) == null){
			return responseObject.put("wrong", "该表无数据");
		}
		//查出表有哪些字段
		String[] fieldNames = new String[resultList.get(0).size()];
		fieldNames = resultList.get(0).keySet().toArray(fieldNames);
		for(int i=0;i<resultList.size();i++){
			Map<String,Object> recordMap = resultList.get(i);
			JSONObject recordObject = new JSONObject();
			for(int j=0;j<fieldNames.length;j++){
				String value = String.valueOf(recordMap.get(fieldNames[j]));
				recordObject.put(fieldNames[j], value);
			}
			responseArray.put(i,recordObject);
		}
		responseObject.put("result", responseArray);
		return responseObject;
	}
}
