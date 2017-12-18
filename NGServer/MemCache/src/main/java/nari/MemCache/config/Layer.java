package nari.MemCache.config;

import nari.Xml.bundle.annotation.XmlAttribute;

public class Layer {

	@XmlAttribute(name="id")
	private String layerId;
	
	@XmlAttribute(name="filter")
	private String filter;

	@XmlAttribute(name="exclude")
	private String exclude;
	
	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getExclude() {
		return exclude;
	}

	public void setExclude(String exclude) {
		this.exclude = exclude;
	}
	
}
