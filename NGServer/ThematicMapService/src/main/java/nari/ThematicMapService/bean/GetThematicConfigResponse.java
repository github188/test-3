package nari.ThematicMapService.bean;

import nari.parameter.code.ReturnCode;

public class GetThematicConfigResponse {
	
	private ThematicDocument[] thematicDocuments;
	
	private ReturnCode code = null;
	
	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}

	public ThematicDocument[] getThematicDocuments() {
		return thematicDocuments;
	}

	public void setThematicDocuments(ThematicDocument[] thematicDocuments) {
		this.thematicDocuments = thematicDocuments;
	}

}