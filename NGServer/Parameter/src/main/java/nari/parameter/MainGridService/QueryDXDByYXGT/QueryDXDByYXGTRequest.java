package nari.parameter.MainGridService.QueryDXDByYXGT;

import java.io.Serializable;

import nari.parameter.bean.GTpairCondition;

public class QueryDXDByYXGTRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 94415270068976016L;

	private GTpairCondition[] GTpairConditions = null;
	
	private String[] returnFields = null;
	
	/*
	 * 业务名称
	 */
	private String professionName = "";

	public GTpairCondition[] getGTpairConditions() {
		return GTpairConditions;
	}

	public void setGTpairConditions(GTpairCondition[] gTpairConditions) {
		GTpairConditions = gTpairConditions;
	}

	public String getProfessionName() {
		return professionName;
	}

	public void setProfessionName(String professionName) {
		this.professionName = professionName;
	}
	
	public String[] getReturnFields() {
		return returnFields;
	}

	public void setReturnFields(String[] returnFields) {
		this.returnFields = returnFields;
	}
	
	public boolean validate(){
		if(GTpairConditions == null || GTpairConditions.length == 0){
			return true;
		}
		return false;
	}
}
