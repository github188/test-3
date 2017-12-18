package nari.SymbolService.bean;

public class GetSymbolIdStatusRequest {

	//SymbolService/getSymbolIdStatus?input={modelId:["",""...]}
	private String[] modelId = null;

	public String[] getModelId() {
		return modelId;
	}

	public void setModelId(String[] modelId) {
		this.modelId = modelId;
	}
	
}
