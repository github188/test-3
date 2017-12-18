package nari.parameter.BaseService.getDocumentModel;

import java.io.Serializable;

public class GetDocumentModelRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6820759043044313678L;


	private String[] mapId = null;
	
	private String[] returnField = null;

	public String[] getMapId() {
		return mapId;
	}

	public void setMapId(String[] mapId) {
		this.mapId = mapId;
	}

	public String[] getReturnField() {
		return returnField;
	}

	public void setReturnField(String[] returnField) {
		this.returnField = returnField;
	}
	
}
