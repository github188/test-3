package nari.model.bean;

public class KVSymbolDef implements SymbolDef {

	private String modelId;
	
	private String symbolId;
	
	private String symbolValue;
	
	private String symbolName;
	
	public KVSymbolDef(String modelId,String symbolId,String symbolValue) {
		this.modelId = modelId;
		this.symbolId = symbolId;
		this.symbolValue = symbolValue;
	}
	
	@Override
	public String getModelId() {
		return modelId;
	}

	@Override
	public String getSymbolId() {
		return symbolId;
	}

	@Override
	public String getSymbolValue() {
		return symbolValue;
	}

	@Override
	public String getSymbolName() {
		return symbolName;
	}

}
