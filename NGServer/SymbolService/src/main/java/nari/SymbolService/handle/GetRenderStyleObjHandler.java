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

public class GetRenderStyleObjHandler {
	public JSONArray getRenderStyleObj(){
		JSONArray styleArray = new JSONArray();
		
		//查询数据库得到所有所需数据
		DbAdaptor db = SymbolServiceActivator.db;
		String sql = "select distinct t.modelid,t.minscale,t.maxscale,t.condition,t.filter,m.labelcolor,m.labelsize,m.isvisible,m.ischangebyscale,m.align,m.arrange,m.rowcharnum,m.displayfield,m.offsety " +
				"from " + TableName.CONF_RENDERRULE + " t left join " + TableName.CONF_LABELSTYLE + " m on t.oid = m.ruleid where t.schemaid = 1 and t.ruletype = 2";
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
			//一个画图样式
			JSONObject styleObj = new JSONObject();
			Map<String,String> judgeSameMap = new HashMap<String,String>();
			//一个styleList属性数组
			JSONArray styleListArray = new JSONArray();
			//其中的一个styleList
			JSONObject styleListObject = new JSONObject();
			Map<String,Object> recordMap = resultList.get(i);
			String modelIdValue = String.valueOf(recordMap.get("modelid"));	//取出其中的modelId
			String minScale = String.valueOf(recordMap.get("minscale"));	//取出其中的minScale
			String maxScale = String.valueOf(recordMap.get("maxscale"));	//取出maxScale
			String condition = String.valueOf(recordMap.get("condition"));	//取出condition
			String filter = String.valueOf(recordMap.get("filter"));	//取出filter
			String lableColorValue = String.valueOf(recordMap.get("labelcolor"));	//取出lableColor
			String lableSizeValue = String.valueOf(recordMap.get("labelsize"));	//取出其中的lableSize
			String isVisibleValue = String.valueOf(recordMap.get("isvisible"));	//取出其中的isVisible
			String isChangeByScaleValue = String.valueOf(recordMap.get("ischangebyscale"));	//取出其中的isChangeByScale
			String alignValue = String.valueOf(recordMap.get("align"));	//取出其中的align
			String arrangeValue = String.valueOf(recordMap.get("arrange"));	//取出其中的arrange
			String rowCharNumValue = String.valueOf(recordMap.get("rowcharnum"));	//取出其中的rowCharNum
			String displayFieldValue = String.valueOf(recordMap.get("displayfield"));	//取出其中的displayField
			String offSetyValue = String.valueOf(recordMap.get("offsety"));	//取出其中的offSety
			
//			//添加字段DYDJ
//			String dydj = "";
//			if(condition != null && !(condition.equals("")) && !(condition.equals("null"))){
//				if(condition.indexOf("DYDJ") != -1){
//					//取出condition里面DYDJ
//					String containDydj = condition.substring(condition.indexOf("DYDJ")+6);
//					dydj = containDydj.substring(0,containDydj.indexOf("'")).trim();
//					filter = "";
//				}else{
//					//condition内没有DYDJ
//					dydj = "";
//					filter = "";
//				}
//				
//			}else if(filter != null && !(filter.equals("")) && !(filter.equals("null"))){
//				String containDydj = filter.substring(filter.indexOf("DYDJ"));
//				String containDydjNumber = containDydj.substring(containDydj.indexOf("'")+1);
//				dydj = containDydjNumber.substring(0,containDydjNumber.indexOf("'")).trim();
//				condition = "";
//			}else{
//				condition = "";
//				filter = "";
//			}
			
			//将颜色转换为对应rgb格式
			String lableColor = "";
			if(lableColorValue != null && !(lableColorValue.equals("")) && !(lableColorValue.equals("null"))){
				String[] ss = new String[]{"000000","00000","0000","000","00","0",""};
				String hexLableColorValue = Integer.toHexString(Integer.valueOf(lableColorValue)).toUpperCase();
				String lableColorColorHex = ss[hexLableColorValue.length()]+hexLableColorValue;
				String [] rgb = new String[3];
				for(int j=0;j<rgb.length;j++){
					rgb[j] = lableColorColorHex.substring(2*j,2*j+2);
					rgb[j] = String.valueOf(Integer.valueOf(rgb[j], 16));
				}
				//翻转
				lableColor = rgb[2]+","+rgb[1]+","+rgb[0];
			}
			
			
			//得到一个styleListObject
			styleListObject.put("minScale", minScale);
			styleListObject.put("maxScale", maxScale);
			styleListObject.put("condition", condition);
			styleListObject.put("filter", filter);
			styleListObject.put("lableColor", lableColor);
			styleListObject.put("lableSize", lableSizeValue);
			styleListObject.put("isVisible", isVisibleValue);
			styleListObject.put("isChangeByScale", isChangeByScaleValue);
			styleListObject.put("align", alignValue);
			styleListObject.put("arrange", arrangeValue);
			styleListObject.put("rowCharNum", rowCharNumValue);
			styleListObject.put("displayField", displayFieldValue);
			styleListObject.put("offSety", offSetyValue);
			
			//将modelIdValue放入判断map中
			judgeSameMap.put("modelId", modelIdValue);
			//若取出的这两个值与原来的均相同
			if(judgeSameList.contains(judgeSameMap)){
				int sameIndex = judgeSameList.indexOf(judgeSameMap);
				//得到相应ruleList
				styleListArray = styleArray.getJSONObject(sameIndex).getJSONArray("styleList");
				styleListArray.add(styleListObject);
			}
			else{
				judgeSameList.add(judgeSameMap);
				styleObj.put("modelId",modelIdValue);
				styleListArray.add(styleListObject);
				styleObj.put("styleList", styleListArray);
				styleArray.add(styleObj);
			}
		}
		return styleArray;
	}
	
}
