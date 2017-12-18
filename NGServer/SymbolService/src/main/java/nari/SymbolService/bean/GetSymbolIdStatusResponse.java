package nari.SymbolService.bean;

import net.sf.json.JSONObject;


public class GetSymbolIdStatusResponse {

	private JSONObject modelSymbol = null;

	public JSONObject getModelSymbol() {
		return modelSymbol;
	}

	public void setModelSymbol(JSONObject modelSymbol) {
		this.modelSymbol = modelSymbol;
	}
	
}
