package nari.parameter.bean;


//拓扑设备
public class TopoObjectId {

	private String modelId;
	
	private String oid ;
	
//	//对象带电状态(0.不带电,1.带点,2不需要该参数)
//	private String eleStatus = "2";
//	
//	//端子带点信息(若为变压器,需加上电压等级)
//	private String temEleStatus = null;

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

//	public String getEleStatus() {
//		return eleStatus;
//	}
//
//	public void setEleStatus(String eleStatus) {
//		this.eleStatus = eleStatus;
//	}
//
//	public String getTemEleStatus() {
//		return temEleStatus;
//	}
//
//	public void setTemEleStatus(String temEleStatus) {
//		this.temEleStatus = temEleStatus;
//	}

	
}
