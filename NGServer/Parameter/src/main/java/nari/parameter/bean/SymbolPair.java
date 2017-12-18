package nari.parameter.bean;

import java.io.Serializable;

public class SymbolPair implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 893311369200010087L;

	//设备子类型
	private String modelId = "";
	
	//符号状态Id
	private String symbolId = "";
	
	//符号值
	private String symbolValue = "";
	
	//显示设备Id 
	private String devtypeId = "";

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getSymbolId() {
		return symbolId;
	}

	public void setSymbolId(String symbolId) {
		this.symbolId = symbolId;
	}

	public String getSymbolValue() {
		return symbolValue;
	}

	public void setSymbolValue(String symbolValue) {
		this.symbolValue = symbolValue;
	}

	public String getDevtypeId() {
		return devtypeId;
	}

	public void setDevtypeId(String devtypeId) {
		this.devtypeId = devtypeId;
	}

 

	
	
}
