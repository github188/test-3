package nari.parameter.geojson;

public class GeoJsonGeometry {

	private Object[] coordinates;
	
	private String type;
	
	public GeoJsonGeometry(String type,Object[] coordinates) {
		this.type = type;
		this.coordinates = coordinates;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object[] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Object[] coordinates) {
		this.coordinates = coordinates;
	}

}
