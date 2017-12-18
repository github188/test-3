package org.geoDataOperation.bean;

import com.vividsolutions.jts.geom.Geometry;

public class GeometryMessage {

	//属性
	private GeometryProperty[] propertites = null;
	
	//地理信息
	private Geometry geometry = null;

	public GeometryProperty[] getPropertites() {
		return propertites;
	}

	public void setPropertites(GeometryProperty[] propertites) {
		this.propertites = propertites;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	
}
