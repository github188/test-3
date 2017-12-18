package com.application.plugin.config;

import nari.Xml.bundle.annotation.NodeType;
import nari.Xml.bundle.annotation.XmlNode;

public class InvokerConfig {

	@XmlNode(name="registry",type=NodeType.Normal,clazz=RegistryConfig.class)
	private RegistryConfig registry;
	
	@XmlNode(name="http",type=NodeType.Normal,clazz=HttpConfig.class)
	private HttpConfig http;
	
	@XmlNode(name="socket",type=NodeType.Normal,clazz=SocketConfig.class)
	private SocketConfig socket;
	
	@XmlNode(name="webservice",type=NodeType.Normal,clazz=WebServiceConfig.class)
	private WebServiceConfig webService;

	public RegistryConfig getRegistry() {
		return registry;
	}

	public void setRegistry(RegistryConfig registry) {
		this.registry = registry;
	}

	public HttpConfig getHttp() {
		return http;
	}

	public void setHttp(HttpConfig http) {
		this.http = http;
	}

	public SocketConfig getSocket() {
		return socket;
	}

	public void setSocket(SocketConfig socket) {
		this.socket = socket;
	}

	public WebServiceConfig getWebService() {
		return webService;
	}

	public void setWebService(WebServiceConfig webService) {
		this.webService = webService;
	}
	
}
