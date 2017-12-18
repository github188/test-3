package nari.MongoDBUpdate.bean;

public class UdateFeederDevRequest {
	//UpdateService/udateFeederDev?input="{"OID":"","SBID":"","pmodelId":""}"
	
	
	private String OID = "";

	private String SBID = "";
	
	private String pmodelId = "";

	public String getOID() {
		return OID;
	}

	public void setOID(String oID) {
		OID = oID;
	}

	public String getSBID() {
		return SBID;
	}

	public void setSBID(String sBID) {
		SBID = sBID;
	}

	public String getPmodelId() {
		return pmodelId;
	}

	public void setPmodelId(String pmodelId) {
		this.pmodelId = pmodelId;
	}
	
}
