package nari.MainGridService.handler;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.MainGridService.MainGridServiceActivator;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetHistoryMessageHandler {
	private DbAdaptor db215 = MainGridServiceActivator.dbAdaptor215;
	
	public JSONObject getHistoryMessage(String TFBH,String startTime,String endTime){
		JSONObject responseObject = new JSONObject();
		JSONArray responseArray = new JSONArray();
		//sql条件
		String TFCondition = "";
		String fbsjCondition = "";
		if(startTime != null && endTime != null){
		fbsjCondition = " fbsj >= to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') " +
				"and fbsj <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss')";	//时间条件
		}
		if(TFCondition.equalsIgnoreCase("")){
			TFCondition = fbsjCondition;
		}else{
			TFCondition = TFCondition + " and " + fbsjCondition;
		}
		String tfNameCondition = "tfbh = "+TFBH;	//台风编号条件
		if(TFCondition.equalsIgnoreCase("")){
			TFCondition = tfNameCondition;
		}else{
			TFCondition = TFCondition + " and " + tfNameCondition;
		}
		if(!(TFCondition.equalsIgnoreCase(""))){
			TFCondition = " where " + TFCondition;	//总条件
		}
		
		String forecastSql = "select * from ZWGK_SJJC_TFJK_LSTF "+TFCondition+" order by fbsj";
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
				String value = "";
				//对时间类型特殊处理
				if(fieldNames[j].equalsIgnoreCase("fbsj")){
					Date fbsj = (Date)recordMap.get(fieldNames[j]);
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					value = format.format(fbsj);
				}else{
					value = String.valueOf(recordMap.get(fieldNames[j]));
				}
				recordObject.put(fieldNames[j], value);
			}
			responseArray.put(i,recordObject);
		}
		
		String forecastTime = responseArray.getJSONObject(resultList.size()-1).getString("fbsj");
		responseObject.put("result", responseArray);
		responseObject.put("lastTime", forecastTime);
		return responseObject;
	}
}

