package com.application.plugin.config;

import nari.Xml.bundle.annotation.XmlAttribute;

public class SocketConfig {

	@XmlAttribute(name="port")
	private int port;
	
	@XmlAttribute(name="protocol")
	private String procotol;
	
	@XmlAttribute(name="poolSize")
	private int poolSize;
	
	@XmlAttribute(name="connectTimeout")
	private int connectTimeout;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getProcotol() {
		return procotol;
	}

	public void setProcotol(String procotol) {
		this.procotol = procotol;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	
}
