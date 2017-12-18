package nari.Dao.bundle.bean;

import nari.Xml.bundle.annotation.XmlAttribute;

public class Jdbc {
	
	@XmlAttribute(name="driverClass")
	private String driverClass = "";
	
	@XmlAttribute(name="jdbcUrl")
	private String jdbcUrl = "";
	
	@XmlAttribute(name="user")
	private String user = "";
	
	@XmlAttribute(name="password")
	private String password = "";
	
	@XmlAttribute(name="active")
	private boolean active = false;
	
	@XmlAttribute(name="minPoolSize")
	private String minPoolSize = "";
	
	@XmlAttribute(name="maxPoolSize")
	private String maxPoolSize = "";
	
	@XmlAttribute(name="maxAdministrativeTaskTime")
	private String maxAdministrativeTaskTime = "";
	
	@XmlAttribute(name="numHelperThreads")
	private String numHelperThreads = "";
	
	@XmlAttribute(name="version")
	private String version = "";
	
	public final String getDriverClass() {
		return driverClass;
	}

	public final void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public final String getJdbcUrl() {
		return jdbcUrl;
	}

	public final void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public final String getUser() {
		return user;
	}

	public final void setUser(String user) {
		this.user = user;
	}

	public final String getPassword() {
		return password;
	}

	public final void setPassword(String password) {
		this.password = password;
	}

	public final boolean isActive() {
		return active;
	}

	public final void setActive(boolean active) {
		this.active = active;
	}

	public String getMinPoolSize() {
		return minPoolSize;
	}

	public void setMinPoolSize(String minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	public String getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(String maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public String getMaxAdministrativeTaskTime() {
		return maxAdministrativeTaskTime;
	}

	public void setMaxAdministrativeTaskTime(String maxAdministrativeTaskTime) {
		this.maxAdministrativeTaskTime = maxAdministrativeTaskTime;
	}

	public String getNumHelperThreads() {
		return numHelperThreads;
	}

	public void setNumHelperThreads(String numHelperThreads) {
		this.numHelperThreads = numHelperThreads;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
