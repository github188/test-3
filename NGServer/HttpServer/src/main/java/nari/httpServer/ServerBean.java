package nari.httpServer;

import nari.Xml.bundle.annotation.XmlAttribute;

public class ServerBean {
	
	@XmlAttribute(name="charter")
	private String charter = "";
	
	@XmlAttribute(name="indexDir")
	private String indexDir = "";

	public String getCharter() {
		return charter;
	}

	public void setCharter(String charter) {
		this.charter = charter;
	}

	public String getIndexDir() {
		return indexDir;
	}

	public void setIndexDir(String indexDir) {
		this.indexDir = indexDir;
	}
}
