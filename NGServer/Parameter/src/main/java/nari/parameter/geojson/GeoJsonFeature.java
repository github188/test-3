package nari.parameter.geojson;

import java.util.Map;

public class GeoJsonFeature {
	
	private GeoJsonGeometry geometry;
	
	private Map<String,Object> properties;
	
	private String type = "Feature";
	
	public GeoJsonFeature(Map<String,Object> properties,GeoJsonGeometry geometry) {
		this.properties = properties;
		this.geometry = geometry;
	}

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

	public GeoJsonGeometry getGeometry() {
		return geometry;
	}

	public void setGeometry(GeoJsonGeometry geometry) {
		this.geometry = geometry;
	}

}
