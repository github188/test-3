package nari.MongoDBUpdate.bean;

import nari.parameter.code.ReturnCode;

public class UpdateStationDevResponse {

	private String[] updatedTable = null;
	
	private ReturnCode code = null;

	public ReturnCode getCode() {
		return code;
	}

	public void setCode(ReturnCode code) {
		this.code = code;
	}

	public String[] getUpdatedTable() {
		return updatedTable;
	}

	public void setUpdatedTable(String[] updatedTable) {
		this.updatedTable = updatedTable;
	}
	
}
