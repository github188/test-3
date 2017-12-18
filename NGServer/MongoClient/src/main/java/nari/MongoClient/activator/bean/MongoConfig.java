package nari.MongoClient.activator.bean;

import nari.Xml.bundle.annotation.XmlAttribute;

public class MongoConfig {
	
	public static final MongoConfig NONE = new MongoConfig();
	
	@XmlAttribute(name="mongoAddress")
	private String mongoAddress = "";
	
	@XmlAttribute(name="mongoPort")
	private String mongoPort = "";
	
	@XmlAttribute(name="mongoDbName")
	private String mongoDbName = "";
	
	@XmlAttribute(name="active")
	private boolean active = false;

	public String getMongoAddress() {
		return mongoAddress;
	}

	public void setMongoAddress(String mongoAddress) {
		this.mongoAddress = mongoAddress;
	}

	public String getMongoPort() {
		return mongoPort;
	}

	public void setMongoPort(String mongoPort) {
		this.mongoPort = mongoPort;
	}

	public String getMongoDbName() {
		return mongoDbName;
	}

	public void setMongoDbName(String mongoDbName) {
		this.mongoDbName = mongoDbName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
