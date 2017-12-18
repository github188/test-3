package nari.ThematicMapService.bean;

public class GetThematicConfigRequest {

	// 图类型
	private String mapId = "";

	// 图实例
	private String documentId = "";
	
	//图所属设备id
	private String parentId = "";
	
	//图所属设备modelid
	private String parentModelId = "";

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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentModelId() {
		return parentModelId;
	}

	public void setParentModelId(String parentModelId) {
		this.parentModelId = parentModelId;
	}

}
