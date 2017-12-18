package nari.parameter.geojson;

import java.util.Map;

public class GeoJsonCRS {

	private Map<String,Object> properties = GeoJsonTranslater.getTranslater().getDefaultCRSProperty();

	private String type = "name";
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	
}
