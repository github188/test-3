package nari.SymbolService.handle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.SymbolService.SymbolServiceActivator;
import nari.model.TableName;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetRenderRuleObjHandler {
	
	public JSONArray getRenderRuleObj(){
		JSONArray ruleArray = new JSONArray();
		
		
		//查询数据库得到所有所需数据
		DbAdaptor db = SymbolServiceActivator.db;
//		String sql = "select * from" +
//		" (select * from " + TableName.CONF_RENDERRULE + " t  left join " 
//				+ TableName.CONF_SYMBOLSTYLE + " m on t.oid = m.ruleid where t.schemaid=1 and (t.ruletype=1) ) a, " 
//		+ TableName.CONF_MODELMETA + " b where a.modelid = b.modelid";
		
		String sql = "select * from" +
				" (select * from " + TableName.CONF_RENDERRULE + " t  left join " 
						+ TableName.CONF_SYMBOLSTYLE + " m on t.oid = m.ruleid where t.schemaid=5 and (t.ruletype=1) ) a, " 
				+ TableName.CONF_MODELMETA + " b where a.modelid = b.modelid";
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		try {
			resultList = db.findAllMap(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//判断是否合并的参数
		List<Map<String,String>> judgeSameList = new ArrayList<Map<String,String>>();
		//对每一条记录做操作
		for(int i=0;i<resultList.size();i++){
			//一个画图规则
			JSONObject ruleObj = new JSONObject();
			Map<String,String> judgeSameMap = new HashMap<String,String>();
			//一个ruleList属性数组
			JSONArray ruleListArray = new JSONArray();
			//其中的一个ruleList
			JSONObject ruleListObject = new JSONObject();
			Map<String,Object> recordMap = resultList.get(i);
			String modelIdValue = String.valueOf(recordMap.get("modelid"));	//取出其中的modelId
			String classIdValue = String.valueOf(recordMap.get("classid"));	//取出其中的classId
			String symbolIdValue = String.valueOf(recordMap.get("symbolid"));	//取出其中的symbolId
			String minScale = String.valueOf(recordMap.get("minscale"));	//取出其中的minScale
			String maxScale = String.valueOf(recordMap.get("maxscale"));	//取出maxScale
			String symbolColorValue = String.valueOf(recordMap.get("symbolcolor"));	//取出symbolColor
			String symSize = String.valueOf(recordMap.get("symbolsize"));	//取出其中的minScale
			String condition = String.valueOf(recordMap.get("condition"));	//取出condition
			String filter = String.valueOf(recordMap.get("filter"));	//取出filter
			
			//添加字段DYDJ
			String dydj = "";
			if(condition != null && !(condition.equals("")) && !(condition.equals("null"))){
				if(condition.indexOf("DYDJ") != -1){
					//取出condition里面DYDJ
					String containDydj = condition.substring(condition.indexOf("DYDJ")+6);
					dydj = containDydj.substring(0,containDydj.indexOf("'")).trim();
					filter = "";
				}else{
					//condition内没有DYDJ
					dydj = "";
					filter = "";
				}
				
			}else if(filter != null && !(filter.equals("")) && !(filter.equals("null"))){
				String containDydj = filter.substring(filter.indexOf("DYDJ"));
				String containDydjNumber = containDydj.substring(containDydj.indexOf("'")+1);
				dydj = containDydjNumber.substring(0,containDydjNumber.indexOf("'")).trim();
				condition = "";
			}else{
				condition = "";
				filter = "";
			}
			
			//将颜色转换为对应rgb格式
			String symbolColor = "";
			if(symbolColorValue != null && !(symbolColorValue.equals("")) && !(symbolColorValue.equals("null"))){
				String[] ss = new String[]{"000000","00000","0000","000","00","0",""};
				String hexSymbolColorValue = Integer.toHexString(Integer.valueOf(symbolColorValue)).toUpperCase();
				String symbolColorHex = ss[hexSymbolColorValue.length()]+hexSymbolColorValue;
				String [] rgb = new String[3];
				for(int j=0;j<rgb.length;j++){
					rgb[j] = symbolColorHex.substring(2*j,2*j+2);
					rgb[j] = String.valueOf(Integer.valueOf(rgb[j], 16));
				}
				//翻转
				symbolColor = rgb[2]+","+rgb[1]+","+rgb[0];
			}
			
			//得到一个ruleListObject
			ruleListObject.put("minScale".toUpperCase(), minScale);
			ruleListObject.put("maxScale".toUpperCase(), maxScale);
			ruleListObject.put("symbolColor".toUpperCase(), symbolColor);
			ruleListObject.put("symbolsize".toUpperCase(), symSize);
			ruleListObject.put("condition".toUpperCase(), condition);
			ruleListObject.put("filter".toUpperCase(), filter);
			ruleListObject.put("dydj".toUpperCase(), dydj);
			
			//将modelIdValue,symbolIdValue放入判断map中
			judgeSameMap.put(modelIdValue, symbolIdValue);
			//若取出的这两个值与原来的均相同
			if(judgeSameList.contains(judgeSameMap)){
				int sameIndex = judgeSameList.indexOf(judgeSameMap);
				//得到相应ruleList
				ruleListArray = ruleArray.getJSONObject(sameIndex).getJSONArray("ruleList".toUpperCase());
				ruleListArray.add(ruleListObject);
			}
			else{
				judgeSameList.add(judgeSameMap);
				ruleObj.put("modelId".toUpperCase(),modelIdValue);
				ruleObj.put("symbolId".toUpperCase(), symbolIdValue);
				ruleObj.put("classId".toUpperCase(), classIdValue);
				ruleListArray.add(ruleListObject);
				ruleObj.put("ruleList".toUpperCase(), ruleListArray);
				ruleArray.add(ruleObj);
			}
		}
		return ruleArray;
	}
	
	public static void main(String[] args) {
		String a = "ff";
		System.out.println(Integer.valueOf(a,16));
	}
}
