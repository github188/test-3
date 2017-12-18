package com.application.plugin.config;

import nari.Xml.bundle.annotation.XmlAttribute;

public class RegistryConfig {

	@XmlAttribute(name="host")
	private String host;
	
	@XmlAttribute(name="port")
	private int port;
	
	@XmlAttribute(name="protocol")
	private String protocol;
	
	@XmlAttribute(name="cluster")
	private String cluster;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}
	
}
