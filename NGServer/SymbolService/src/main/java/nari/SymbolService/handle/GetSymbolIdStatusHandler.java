package nari.SymbolService.handle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nari.Dao.interfaces.DbAdaptor;
import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.SymbolService.SymbolServiceActivator;
import nari.SymbolService.bean.GetSymbolIdStatusRequest;
import nari.SymbolService.bean.GetSymbolIdStatusResponse;
import nari.model.TableName;
import nari.parameter.code.ReturnCode;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetSymbolIdStatusHandler {
	
	private Logger logger = LoggerManager.getLogger(this.getClass());
	DbAdaptor db = SymbolServiceActivator.db;

	public GetSymbolIdStatusResponse getSymbolIdStatus(GetSymbolIdStatusRequest req){
		GetSymbolIdStatusResponse resp = new GetSymbolIdStatusResponse();
		
		String[] modelId = req.getModelId();
		String modelCondSql = "";
		if(modelId != null && modelId.length !=0){
			modelCondSql = " where modelid in (" + modelId[0];
			for(int i=1;i<modelId.length;i++){
				modelCondSql = modelCondSql + "," + modelId[i];
			}
		}
		
		String SymbolSql = "select * from " + TableName.CONF_MODELSYMBOL + modelCondSql;
		
		List<Map<String, Object>> modelSymbolList = new ArrayList<Map<String, Object>>();
		try {
			modelSymbolList = db.findAllMap(SymbolSql);
		} catch (SQLException e) {
			logger.error("数据库查询出错");
			return resp;
		}
		if(modelSymbolList == null || modelSymbolList.size() == 0){
			logger.error("无数据,传入值可能有误");
			return resp;
		}
		
		
		Map<String,JSONArray> modelMap = new HashMap<String,JSONArray>();
		//modelsymbol:{modelid:symbolArray};
		for (int i = 0; i < modelSymbolList.size(); i++) {	
			Map<String, Object> modelSymbolRecord = modelSymbolList.get(i);
			Iterator<String> keyIt = modelSymbolRecord.keySet().iterator();
			
			JSONArray symbolArray = new JSONArray();
			//先处理modelid
			String modelid = String.valueOf(modelSymbolRecord.get("modelid"));
			if(modelMap.containsKey(modelId)){	//
				symbolArray = modelMap.get("modelid");
			}else{
				modelMap.put("modelid", symbolArray);
			}
			
			JSONObject eachsymbolJSON = new JSONObject();
			while(keyIt.hasNext()){
				String key = keyIt.next();
				String value = String.valueOf(modelSymbolRecord.get(key));
				if(value == null || "null".equalsIgnoreCase(value)){
					continue;
				}
				if("modelid".equalsIgnoreCase(key)){	//将modelid提出来分组
					continue;
				}
				eachsymbolJSON.put(key.toUpperCase(), value);
				
				//将symbolValue拆开添加一次
				if("symbolvalue".equalsIgnoreCase(key)){
					String[] values = value.split(";");
					for(int j=0;j<values.length;j++){
						String eachCondition = values[j];
						String[] Condition = eachCondition.split("=");
						eachsymbolJSON.put(Condition[0].toUpperCase(), Condition[1].toUpperCase());
					}
				}
			}//所有字段组装至eachsymbolJSON	
			symbolArray.add(eachsymbolJSON);
			modelMap.put(modelid, symbolArray);					//map组装完成
		}
		
		JSONObject modelSymbolObject = new JSONObject();
		for(Entry<String, JSONArray> enrty : modelMap.entrySet()){
			modelSymbolObject.put(enrty.getKey(), enrty.getValue());
		}
		resp.setModelSymbol(modelSymbolObject);
		return resp;
	}
}
