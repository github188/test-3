package nari.BaseService.bean;

import nari.parameter.code.ReturnCode;

public class GetAreaGeoMessageResponse {

	private AreaGeoMessage[] areaGeoMessages = null;
	
	private ReturnCode code = null;

	public AreaGeoMessage[] getAreaGeoMessages() {
		return areaGeoMessages;
	}

	public void setAreaGeoMessages(AreaGeoMessage[] areaGeoMessages) {
		this.areaGeoMessages = areaGeoMessages;
	}

	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}
}
