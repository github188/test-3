package nari.parameter.BaseService.GetMapClassList;

import java.io.Serializable;

public class GetMapClassListRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7104750982502731253L;
	
	private int mapId = 0;

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

}
