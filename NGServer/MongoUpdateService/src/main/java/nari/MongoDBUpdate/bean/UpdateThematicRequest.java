package nari.MongoDBUpdate.bean;

public class UpdateThematicRequest {
	//UpdateService/udateFeederDev?input="{"mapId":"","documentId":["",""...]}"
	
	private String mapId = "";
	
	//为空更新所有mapid的
	private String[] documentId;

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public String[] getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String[] documentId) {
		this.documentId = documentId;
	}

}
