package nari.BaseService.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nari.BaseService.BaseServiceActivator;
import nari.BaseService.bean.GetToolBoxRequest;
import nari.Dao.interfaces.DbAdaptor;

import org.json.JSONArray;
import org.json.JSONObject;

 
public class GetToolBoxConfigHandler {
	
	DbAdaptor db = BaseServiceActivator.dbAdaptor;
	Map<String,JSONObject> BaseParentMap = new HashMap<String,JSONObject>();	//oid--BaseJSON
	Map<String,JSONObject> typeParentMap = new HashMap<String,JSONObject>();	//oid--typeJSON(缺nextLevel)
	Map<String,JSONObject> modelSymbolMap = new HashMap<String,JSONObject>();	//modelid--modelJSON

	public JSONArray gettoolBoxConfig(GetToolBoxRequest req){
		
		String[] mapids = req.getMapids();
		
		if(!initMap(mapids)){//"3015","3014","3001","1001"
			return new JSONArray().put((JSONObject)new JSONObject().put("Wrong", "数据库查询发生错误"));
		}
		
		JSONArray baseParentJSONArray = new JSONArray();
		JSONObject firstBaseParentJSON = new JSONObject();
		firstBaseParentJSON.put("name", "常用工具");
		firstBaseParentJSON.put("displayorder", "0");
		baseParentJSONArray.put(firstBaseParentJSON);
		
		
		Map<String,JSONArray> typeParentJSONArrayMap = new HashMap<String,JSONArray>();	//BaseParentid--typeParentJSONArray
		Map<String,JSONArray> modelJSONArrayMap = new HashMap<String,JSONArray>();	//typeParentid--modelJSONArray
		
		//modelsmbolLevel(typeParentLevel每层对应modelArray组装完成)
		int count = modelSymbolMap.size();
		String[] modelSymbolMapKey = new String[count];
		modelSymbolMapKey = modelSymbolMap.keySet().toArray(modelSymbolMapKey);
		for(int i=0;i<count;i++){
			JSONObject eachModelJSON = modelSymbolMap.get(modelSymbolMapKey[i]);
			String typeParentOid = eachModelJSON.getString("typeParentid");
			if(typeParentMap.containsKey(typeParentOid)){
				JSONArray modelJSONArray = new JSONArray();
				if(modelJSONArrayMap.containsKey(typeParentOid)){
					modelJSONArray = modelJSONArrayMap.get(typeParentOid);
				}
				modelJSONArray = modelJSONArray.put(eachModelJSON);
				modelJSONArrayMap.put(typeParentOid, modelJSONArray);
			}else{
				System.out.println("can not matching parentId with oid in database");
				continue;
			}
		}
		
		//typeParentLevel(BaseParentLevel每层对应typeParentArray组装完成)
		int typeParentCount = modelJSONArrayMap.size();
		String[] typeParentId = new String[count];
		typeParentId = modelJSONArrayMap.keySet().toArray(typeParentId);
		for(int i=0;i<typeParentCount;i++){
			JSONObject eachTypeParentJSON = typeParentMap.get(typeParentId[i]);
			//组装每一个TypeParentJSON
			eachTypeParentJSON.put("modelLevel", modelJSONArrayMap.get(typeParentId[i]));
			//组装每层对应typeParentArray
			String baseParentid = eachTypeParentJSON.getString("baseParentid");
			if(BaseParentMap.containsKey(baseParentid)){
				JSONArray typeParentJSONArray = new JSONArray();
				if(typeParentJSONArrayMap.containsKey(baseParentid)){
					typeParentJSONArray = typeParentJSONArrayMap.get(baseParentid);
				}
				typeParentJSONArray = typeParentJSONArray.put(eachTypeParentJSON);
				typeParentJSONArrayMap.put(baseParentid, typeParentJSONArray);
			}else{
				System.out.println("can not matching parentId with oid in database");
				continue;
			}
		}
		
		//baseParentLevel
		int baseCount = BaseParentMap.size();
		String[] BaseParentid = new String[baseCount];
		BaseParentid = BaseParentMap.keySet().toArray(BaseParentid);
		for(int i=0;i<BaseParentMap.size();i++){
			JSONObject eachBaseParentJSON = BaseParentMap.get(BaseParentid[i]);
			eachBaseParentJSON.put("typeLevel", typeParentJSONArrayMap.get(BaseParentid[i]));
			baseParentJSONArray.put(eachBaseParentJSON);
		}
		
		return baseParentJSONArray ;
	}
	
	
	private boolean initMap(String[] mapids){
		String sql = "select a.*,b.modelid,b.symbolid,b.symbolvalue from conf_toolbox a left join conf_modelsymbol b on a.modelid = b.modelid";
		String orderSql = " order by parentid,displayorder";
		String mapCondition = "";
		
		if(mapids != null && mapids.length != 0){
			mapCondition = " where a.mapid = " + mapids[0];
			for(int i=1;i<mapids.length;i++){
				mapCondition = mapCondition + " and a.mapid = " + mapids[i];
			}
		}
		sql = sql + mapCondition + orderSql;
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		try {
			resultList = db.findAllMap(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(resultList == null || resultList.size() == 0){
			return false;
		}
		
		for(int i=0;i<resultList.size();i++){
			Map<String,Object> result = resultList.get(i);
			String oid = String.valueOf(result.get("oid"));
			String name = String.valueOf(result.get("name"));
			String displayOrder = String.valueOf(result.get("displayorder"));
			
//			//mapidLevel
//			String mapid =  String.valueOf(result.get("mapid"));
//			if(mapid != null && !"null".equalsIgnoreCase(mapid)){
//				mapIdMap.put(mapid, result);
//			}
			
			//BaseParentLevel
			String parentid = String.valueOf(result.get("parentid"));
			if(parentid == null || "null".equalsIgnoreCase(parentid)){
				JSONObject eachBaseParentJSON = new JSONObject();
				//loadEachBaseParentJSON
				eachBaseParentJSON.put("name", name);
				eachBaseParentJSON.put("displayorder", displayOrder);
				String mapid =  String.valueOf(result.get("mapid"));
				eachBaseParentJSON.put("mapid", mapid);
				BaseParentMap.put(oid, eachBaseParentJSON);
				continue;
			}
			
			//typeParentLevel
			String modelid = String.valueOf(result.get("modelid"));
			if(modelid == null || "null".equalsIgnoreCase(modelid)){
				JSONObject eachTypeParentJSON = new JSONObject();
				eachTypeParentJSON.put("name", name);
				eachTypeParentJSON.put("displayorder", displayOrder);
				eachTypeParentJSON.put("baseParentid", parentid);
				typeParentMap.put(oid, eachTypeParentJSON);
				continue;
			}
			
			//smbolLevel
			JSONObject eachModelJSON = new JSONObject();
			if(modelSymbolMap.containsKey(modelid)){
				eachModelJSON = modelSymbolMap.get(modelid);
			}else{
				eachModelJSON.put("name", name);
				eachModelJSON.put("displayorder", displayOrder);
				eachModelJSON.put("modelid", modelid);
				eachModelJSON.put("typeParentid", parentid);
				
			}
			JSONObject symbolJSON = new JSONObject();
			String symbolid = String.valueOf(result.get("symbolid"));
			String symbolvalue = String.valueOf(result.get("symbolvalue"));
			symbolJSON.put("symbolid", symbolid);
			symbolJSON.put("symbolvalue", symbolvalue);
			eachModelJSON.put("symbol", symbolJSON);
			
			modelSymbolMap.put(modelid, eachModelJSON);
		}
		
		return true;
	}
}
