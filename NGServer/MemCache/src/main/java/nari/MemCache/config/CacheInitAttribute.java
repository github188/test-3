package nari.MemCache.config;

import java.util.List;

import nari.Xml.bundle.annotation.NodeType;
import nari.Xml.bundle.annotation.XmlAttribute;
import nari.Xml.bundle.annotation.XmlNode;

public class CacheInitAttribute {

	@XmlAttribute(name="active")
	private boolean active;
	
	@XmlAttribute(name="offsetValid")
	private boolean offsetValid;
	
	@XmlAttribute(name="threadPoolSize")
	private String threadPoolSize;
	
	@XmlNode(name="layer",type=NodeType.List,clazz=Layer.class)
	private List<Layer> layers;
	
	@XmlNode(name="field",type=NodeType.List,clazz=TypedField.class)
	private List<TypedField> fields;
	
	@XmlNode(name="loader",type=NodeType.Normal,clazz=LayerLoader.class)
	private LayerLoader loader;
	
	@XmlAttribute(name="index")
	private String index;
	
	@XmlAttribute(name="mergerFilter")
	private String mergerFilter;
	
	@XmlAttribute(name="merger")
	private boolean merger;

	public boolean isOffsetValid() {
		return offsetValid;
	}

	public void setOffsetValid(boolean offsetValid) {
		this.offsetValid = offsetValid;
	}

	public LayerLoader getLoader() {
		return loader;
	}

	public void setLoader(LayerLoader loader) {
		this.loader = loader;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public List<Layer> getLayers() {
		return layers;
	}

	public void setLayers(List<Layer> layers) {
		this.layers = layers;
	}

	public List<TypedField> getFields() {
		return fields;
	}

	public void setFields(List<TypedField> fields) {
		this.fields = fields;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getThreadPoolSize() {
		return threadPoolSize;
	}

	public void setThreadPoolSize(String threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	public String getMergerFilter() {
		return mergerFilter;
	}

	public void setMergerFilter(String mergerFilter) {
		this.mergerFilter = mergerFilter;
	}

	public boolean isMerger() {
		return merger;
	}

	public void setMerger(boolean merger) {
		this.merger = merger;
	}
	
}
