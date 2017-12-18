package nari.model.bean;

import java.util.Map;

public class DefaultSymbolDef implements SymbolDef {

	private Map<String,Object> symbol = null;
	
	public DefaultSymbolDef(Map<String,Object> symbol){
		this.symbol = symbol;
	}
	
	@Override
	public String getModelId() {
		return symbol.get("modelid")==null?"":String.valueOf(symbol.get("modelid"));
	}

	@Override
	public String getSymbolId() {
		return symbol.get("symbolid")==null?"":String.valueOf(symbol.get("symbolid"));
	}

	@Override
	public String getSymbolValue() {
		return symbol.get("symbolvalue")==null?"":String.valueOf(symbol.get("symbolvalue"));
	}

	@Override
	public String getSymbolName() {
		return symbol.get("symbolName")==null?"":String.valueOf(symbol.get("symbolName"));
	}
	
	

//	@Override
//	public String getDevTypeId() {
//		return symbol.get("devtypeid")==null?"":String.valueOf(symbol.get("devtypeid"));
//	}

}
