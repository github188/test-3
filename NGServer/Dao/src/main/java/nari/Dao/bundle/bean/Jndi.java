package nari.Dao.bundle.bean;

import nari.Xml.bundle.annotation.XmlAttribute;

public class Jndi {
	
	@XmlAttribute(name="jndiName")
	private String jndiName = "";
	
	@XmlAttribute(name="resourceRef")
	private boolean resourceRef = false;
	
	@XmlAttribute(name="url")
	private String url = "";
	
	@XmlAttribute(name="initial")
	private String initial = "";
	
	@XmlAttribute(name="active")
	private boolean active = false;
	
	@XmlAttribute(name="version")
	private String version = "";

	public final String getJndiName() {
		return jndiName;
	}

	public final void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	public final boolean isResourceRef() {
		return resourceRef;
	}

	public final void setResourceRef(boolean resourceRef) {
		this.resourceRef = resourceRef;
	}

	public final String getUrl() {
		return url;
	}

	public final void setUrl(String url) {
		this.url = url;
	}

	public final String getInitial() {
		return initial;
	}

	public final void setInitial(String initial) {
		this.initial = initial;
	}

	public final boolean isActive() {
		return active;
	}

	public final void setActive(boolean active) {
		this.active = active;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
