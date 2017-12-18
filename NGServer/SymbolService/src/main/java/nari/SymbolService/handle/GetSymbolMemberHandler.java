package nari.SymbolService.handle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nari.Dao.interfaces.DbAdaptor;
import nari.SymbolService.SymbolServiceActivator;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetSymbolMemberHandler {

	DbAdaptor db = SymbolServiceActivator.db;
	
//	String[] identityFields = new String[] { "symbolid", "type", "width", "height","origin", "extent" };
	String[] identityFields = new String[] { "symbolid", "type", "width", "height","anchorX","anchorY" };
//	String[] elementFields = new String[]{"draworder","graphicstype", "graphicsposition", "fillopacity", "textcontent", "style", "transform"};

	public JSONObject getSymbolMember() {
		// 新版符号
		JSONObject ResultObject = new JSONObject();			//{SYMBOL:symbolArray}
		JSONArray symbolArray = new JSONArray();			//[eachSymbolObject,eachSymbolObject,,,]
		

		String sql = "select * from sys_symbolelement t left join sys_symbol m on m.oid = t.symbolid";
		//String sql = "select * from sys_symbolelement t left join sys_symbol m on m.oid = t.symbolid where m.alias like '%二次%' order by symbolid, draworder";	//只需要二次的时候
		List<Map<String, Object>> rs = null;
		try {
			rs = db.findAllMap(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if(rs == null || rs.isEmpty()){
			return ResultObject;
		}
		
		
		Set<String> allFieldSet = rs.get(0).keySet();
		for(String field:identityFields){
			allFieldSet.remove(field);
		}
		String[] elementFields = new String[allFieldSet.size()];
		elementFields = allFieldSet.toArray(elementFields);
		//合并标识字段一个相同其他的都相同(symbolId)
		Map<String,JSONObject> symbolIdMap = new HashMap<String,JSONObject>();
		for(int i=0;i<rs.size();i++){
			Map<String, Object> eachRs = rs.get(i);
			
			String symbolId = String.valueOf(eachRs.get("symbolid"));
			JSONObject eachElementObject = new JSONObject();	//存储除了elementFields的信息
			for(int j=0;j<elementFields.length;j++){	//组装elementArray
				String elementField = elementFields[j];
				if("transform".equalsIgnoreCase(elementField)){	//对transform,textcontent特殊处理
					String transform = String.valueOf(eachRs.get(elementField.toLowerCase()));
					if(transform == null || "".equalsIgnoreCase(transform) || "null".equalsIgnoreCase(transform)){
						eachElementObject.put("rotate".toUpperCase(), "0");
					}else{
						int begin = transform.indexOf("(")+1;
						int end = transform.indexOf(")");
						String value = transform.substring(begin, end);
						eachElementObject.put("rotate".toUpperCase(), value);
					}
				}else if("style".equalsIgnoreCase(elementField)){
					String style = String.valueOf(eachRs.get(elementField.toLowerCase()));
					if(style == null || "".equalsIgnoreCase(style) || "null".equalsIgnoreCase(style)){
						eachElementObject.put("fontsize".toUpperCase(), null);
					}else{
						int begin = style.indexOf("font-size:")+10;
						int end = style.indexOf("text")-1;
						String value = style.substring(begin, end);
						eachElementObject.put("fontsize".toUpperCase(), value);
					}
				}
				else{
				//symbol判断完取出elementArray
				eachElementObject.put(elementField.toUpperCase(), String.valueOf(eachRs.get(elementField.toLowerCase())));
				}
			}
				
			if(symbolIdMap.containsKey(symbolId)){	//若存在相同
				JSONObject eachSymbolObject = symbolIdMap.get(symbolId);	
				JSONArray elementArray = eachSymbolObject.getJSONArray("ELEMENTS");
				elementArray.add(eachElementObject);
				eachSymbolObject.put("ELEMENTS", elementArray);
				symbolIdMap.put(symbolId, eachSymbolObject);
			}else{
				JSONObject eachSymbolObject = new JSONObject();		//{SYMBOLID:***,ELEMENT:elementArray}
				JSONArray elementArray = new JSONArray();
				String origin = String.valueOf(eachRs.get("origin")).toString();
				String exetent = String.valueOf(eachRs.get("extent")).toString();
				boolean offSetFlag = false;
				if("null".equalsIgnoreCase(origin) || origin == null || "null".equalsIgnoreCase(exetent) || exetent == null){
					offSetFlag = true;
				}
				for(int j=0;j < identityFields.length;j++){
					String identityField = identityFields[j];
					String[] origins = String.valueOf(eachRs.get("origin")).toString().split(",");
					String[] exetents = String.valueOf(eachRs.get("extent")).toString().split(" ");
					if("anchorX".equalsIgnoreCase(identityField)){		//对offsetX,offsetY特殊处理
						if(offSetFlag){
							eachSymbolObject.put(identityField.toUpperCase(),0.0);
						}else{
							double x = Double.valueOf(origins[0]);
							double xmin = Double.valueOf(exetents[0]);
							double xmax = Double.valueOf(exetents[2]);
							String anchorX = String.valueOf((x-xmin)/(xmax-xmin));
							eachSymbolObject.put(identityField.toUpperCase(),anchorX);
						}
					}else if("anchorY".equalsIgnoreCase(identityField)){
						if(offSetFlag){
							eachSymbolObject.put(identityField.toUpperCase(),0.0);
						}else{
							double y = Double.valueOf(origins[1]);
							double ymin = Double.valueOf(exetents[1]);
							double ymax = Double.valueOf(exetents[3]);
							String anchorY = String.valueOf((y-ymin)/(ymax-ymin));
							eachSymbolObject.put(identityField.toUpperCase(),anchorY);
						}
					}
					else{
						
						String	fieldValue = String.valueOf(eachRs.get(identityField.toLowerCase()));
						eachSymbolObject.put(identityField.toUpperCase(), fieldValue);
					}
				}	//identityFields处理结束
				elementArray.add(eachElementObject);
				eachSymbolObject.put("ELEMENTS", elementArray);
				symbolIdMap.put(symbolId, eachSymbolObject);
			}
		}	//所有数据得到
		
		Iterator<JSONObject> eachSymbolObjectIt = symbolIdMap.values().iterator();
		while(eachSymbolObjectIt.hasNext()){
			symbolArray.add(eachSymbolObjectIt.next());
		}
		
		
		ResultObject.put("SYMBOLS", symbolArray);

		return ResultObject;
	}
}
