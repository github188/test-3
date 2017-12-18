package nari.MongoQuery.Adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bson.BsonDocument;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import nari.MongoQuery.Util.BsonUtil;
import nari.MongoQuery.Util.MongoDBUtil;
import nari.model.TableName;
import nari.model.bean.DefaultSymbolDef;
import nari.model.bean.SymbolDef;

import static com.mongodb.client.model.Filters.*;

public class SymbolAdapterImpl{
	private static MongoCollection<BsonDocument> modelsymbol = null;
	private static MongoCollection<BsonDocument> conf_modelsymbol = null;
	
	private final static String database = "shdwzy";
	private final static String SYMBOLVALUE = "SYMBOLVALUE";
	private final static String MODELID = "modelid";
	private final static String SYMBOLID = "symbolid";
	private final static String DEVTYPEID = "devtypeid";
	
	public SymbolAdapterImpl(){
		
	}
	/*
	 * @param cursor, cursor of first doc in modelsymbol with certained classid/modelid
	 * */
	public static SymbolDef search(String classID){
		
		if(modelsymbol == null)
			modelsymbol = MongoDBUtil.instance.getCollection(database, "modelsymbol");
		if(conf_modelsymbol == null)
			conf_modelsymbol = MongoDBUtil.instance.getCollection(database, TableName.CONF_MODELSYMBOL);
		
		MongoCursor<BsonDocument> cursor = modelsymbol.find(eq(MODELID, classID)).iterator();
		if(null == cursor){
			return null;
		}else{
			while(cursor.hasNext()){
				BsonDocument doc = cursor.next();
				DefaultSymbolDef def = null;
				String symbolValue = doc.get(SYMBOLVALUE).toString();
				if(symbolValue == null || symbolValue.equals("")){
					continue; // to be continued
				}
				if(symbolValue.endsWith(";")){
					symbolValue = symbolValue.substring(0, symbolValue.length() - 2);
				}
				boolean flagEqual = true;
				String conditions[] = symbolValue.split("=");
				for(String condition : conditions){
					String devVal = null;
					String cval[] = condition.split("=");
					String bValue = doc.get(cval[0]).toString();
					if(bValue == null)
						devVal = "";
					else
						devVal = bValue;
					if("".equals(devVal)){
						continue;
					}
					String cValue = cval[1];
					if(cValue.equalsIgnoreCase(bValue)){
						flagEqual = flagEqual && true;
					}else{
						flagEqual = flagEqual && false;
						break;
					}
				}
				if(flagEqual){
					String devtypeid = conf_modelsymbol.find(and(eq(SYMBOLID, doc.get(SYMBOLID)))).first().toString();
					def = getDefualtSymbolDef(doc, devtypeid);
					return def;
				}
				
			}
		}
		return null;
	}
	/*
	 * get defaultsymboldef with symbol bsondocument contains (modelid,symbolid,symbolvalue) and devTypeid 
	 * */
	public static DefaultSymbolDef getDefualtSymbolDef(BsonDocument symbolDoc, String devTypeId){
		if(symbolDoc.isNull())
			return null;
		else{
			Map< String, Object> symbol = new HashMap< String, Object >();
			String modelid = symbolDoc.get("modelid").toString();
			String symbolid = symbolDoc.get("symbolid").toString();
			String symbolvalue = symbolDoc.get("symbolvalue").toString();
			
			symbol.put(MODELID, modelid);
			symbol.put(SYMBOLID, symbolid);
			symbol.put(SYMBOLVALUE, symbolvalue);
			symbol.put(DEVTYPEID, devTypeId);
			
			DefaultSymbolDef result = new DefaultSymbolDef(symbol);
			return result;
		}
	}
	
	/**
	 * get symbol iterator from oracle and deivce from mongodb 
	 * @param it the itertor of symbol in oracle
	 * @param device device form mongodb
	 * */
	public static SymbolDef searchSymbol(Iterator<SymbolDef> it, BsonDocument device) {
		
		SymbolDef defaultSymbol = null, symbol = null;
		if (it == null) {
			return null;
		}
		
		while (it.hasNext()) {
			SymbolDef def = it.next();
			
			if (null == defaultSymbol) {
				defaultSymbol = def;
			}
			
			it.remove();
			String value = def.getSymbolValue();
			if(value == null || "".equals(value) || value.equals("null")){
				defaultSymbol = def;
				continue;
			}
			
			if(value.endsWith(";")){
				value = value.substring(0, value.length()-2);
			}
			String[] conditions = value.split(";");
			boolean b = true;
			for (String condition : conditions) {
				//String devVal = device.getValue(condition.split("=")[0])==null?"":String.valueOf(device.getValue(condition.split("=")[0]));
				String cond = condition.split("=")[0];
				
				String condvalue = device.containsKey(cond) ? BsonUtil.BsonToString(device.get(cond)): null;
				String devVal = condvalue == null?"":condvalue;
				if("".equals(devVal)){
					continue;
				}

			String val = condition.split("=")[1];
			if(devVal.equalsIgnoreCase(val)){
				b = b && true;
			}else{
				b = b && false;
				break;
			}
			}
			
			if(b){
				symbol = def;
				break;
			}
		}
		
		return symbol == null ? defaultSymbol : symbol;
	}
}
