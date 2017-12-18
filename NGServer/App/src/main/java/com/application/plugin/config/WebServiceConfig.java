package com.application.plugin.config;

import nari.Xml.bundle.annotation.XmlAttribute;

public class WebServiceConfig {

	@XmlAttribute(name="port")
	private int port;
	
	@XmlAttribute(name="protocol")
	private String protocol;
	
	@XmlAttribute(name="maxThreadPool")
	private int maxThreadPool;
	
	@XmlAttribute(name="minThreadPool")
	private int minThreadPool;

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

	public int getMaxThreadPool() {
		return maxThreadPool;
	}

	public void setMaxThreadPool(int maxThreadPool) {
		this.maxThreadPool = maxThreadPool;
	}

	public int getMinThreadPool() {
		return minThreadPool;
	}

	public void setMinThreadPool(int minThreadPool) {
		this.minThreadPool = minThreadPool;
	}
	
}
