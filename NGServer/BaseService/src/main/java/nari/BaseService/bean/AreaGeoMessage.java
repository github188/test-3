package nari.BaseService.bean;

public class AreaGeoMessage {

	private String areaId = "";
	
	private String geometryString = "";		//{"type":..,"coordinates":...}
	
	//范围信息
	private String extendString = "";		//

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getGeometryString() {
		return geometryString;
	}

	public void setGeometryString(String geometryString) {
		this.geometryString = geometryString;
	}

	public String getExtendString() {
		return extendString;
	}

	public void setExtendString(String extendString) {
		this.extendString = extendString;
	}
	
}
