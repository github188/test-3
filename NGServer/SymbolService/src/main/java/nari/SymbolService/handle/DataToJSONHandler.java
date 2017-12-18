package nari.SymbolService.handle;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.Logger.Logger;
import nari.Logger.LoggerManager;
import nari.SymbolService.SymbolServiceActivator;
import nari.model.TableName;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DataToJSONHandler {
	private Logger logger = LoggerManager.getLogger(this.getClass());
	
	public JSONArray dataToJSON(int sqlNum,String[] returnField){
	DbAdaptor db = SymbolServiceActivator.db;
	List<Map<String, Object>> listMap = null;;
	String sql = "";
	//各种配置的sql语句
	try {
		switch(sqlNum){
			case 1:
				sql = "select distinct t.modelid,t.minscale,t.maxscale,t.condition,t.filter,m.labelcolor,m.labelsize,m.isvisible,m.ischangebyscale,m.align,m.arrange,m.rowcharnum,m.displayfield,m.offsety "
						+ "from " + TableName.CONF_LABELSTYLE + " t left join conf_labelstyle m on t.oid = m.ruleid where t.schemaid = 1 and t.ruletype = 2";break;
		case 2:sql = "select codedefid,codename from " + TableName.CONF_CODEDEFINITION + " where codeid = 10401 ";break;	//电压值与中文对应关系
		default:logger.info("传输有误");
		return null;
		}
		listMap = db.findAllMap(sql);
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	if(listMap == null || listMap.size() == 0){
		return null;
	}
	
	//若returnField为空,返回全部
	if(returnField == null){
		Object[] keysName = listMap.get(0).keySet().toArray();
		returnField = new String[keysName.length];
		for(int j=0;j<keysName.length;j++){
			returnField[j] = String.valueOf(keysName[j]);
		}
	}
	
	JSONArray resultArray = new JSONArray();
//	List<Map<String,Object>> JudgeSame = new ArrayList<Map<String,Object>>();
	for(int i = 0;i<listMap.size();i++){
			JSONObject resultObject = new JSONObject();
			Map<String, Object> map = listMap.get(i);
			//给返回字段赋值
			for(int j=0;j<returnField.length;j++){
				Object value = map.get(returnField[j].toLowerCase());
				resultObject.put(returnField[j].toUpperCase(), value);
			}
			if(resultObject == null || resultObject.isEmpty()){
				return null;
			}
			resultArray.add(resultObject);
		}
	return resultArray;
	}

}
