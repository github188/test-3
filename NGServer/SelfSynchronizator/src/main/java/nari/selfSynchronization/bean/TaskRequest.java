package nari.selfSynchronization.bean;

public class TaskRequest {


	private String OID = "";

	private String SBID = "";
	
	private String pmodelId = "";
	
	private String mapId = "";
	
	private String documentId = "";

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

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	
}

