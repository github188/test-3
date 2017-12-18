package nari.MongoQuery;

import nari.Xml.bundle.annotation.XmlAttribute;

public class MongoConfig {
	
	public static final MongoConfig NONE = new MongoConfig();
	
	@XmlAttribute(name="active")
	private boolean active = false;
	
	@XmlAttribute(name="mongoAddress")
	private String mongoAddress = "";
	
	@XmlAttribute(name="mongoPort")
	private String mongoPort = "";
	
	@XmlAttribute(name="encryption")
	private boolean encryption = false;
	
	@XmlAttribute(name="mongoDbName")
	private String mongoDbName = "";

	@XmlAttribute(name="connectionsPerHost")
	private String connectionsPerHost;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

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

	public boolean isEncryption() {
		return encryption;
	}

	public void setEncryption(boolean encryption) {
		this.encryption = encryption;
	}

	public String getMongoDbName() {
		return mongoDbName;
	}

	public void setMongoDbName(String mongoDbName) {
		this.mongoDbName = mongoDbName;
	}

	public String getConnectionsPerHost() {
		return connectionsPerHost;
	}

	public void setConnectionsPerHost(String connectionsPerHost) {
		this.connectionsPerHost = connectionsPerHost;
	}	
}
