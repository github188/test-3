package nari.TopoAnalysisService;

import nari.Xml.bundle.annotation.XmlAttribute;

public class Topo {

	@XmlAttribute(name="remoteAddress")
	private String remoteAddress;
	
	@XmlAttribute(name="remotePort")
	private String remotePort;
	
	@XmlAttribute(name="timeout")
	private String timeout;

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public String getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(String remotePort) {
		this.remotePort = remotePort;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	
}
