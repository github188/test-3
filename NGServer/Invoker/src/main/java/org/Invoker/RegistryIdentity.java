package org.Invoker;

import java.io.Serializable;

import org.Invoker.rpc.exception.InvokerException;

public class RegistryIdentity extends Identity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5739396338149243011L;
	
	private String host = "";
	
	private int port = 0;
	
	private String cluster = "";

	private String registry = "";
	
	private String content;
	
	private Identity protocolIdentity = null;
	
	public RegistryIdentity(){
		setProtocol(Constants.REGISTRY);
	}
	
	public RegistryIdentity copy(){
		RegistryIdentity t = new RegistryIdentity();
		t.setCluster(cluster);
		t.setHost(host);
		t.setPort(port);
		t.setProtocol(getProtocol());
		t.setProtocolIdentity(protocolIdentity);
		t.setRegistry(registry);
		t.setVersion(getVersion());
		return t;
	}
	
	public String getHost() {
		if(host==null || "".equals(host)){
			throw new InvokerException("invalidate host");
		}
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		if(port<=0){
			throw new InvokerException("null port");
		}
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public Identity getProtocolIdentity() {
		return protocolIdentity;
	}

	public void setProtocolIdentity(Identity protocolIdentity) {
		this.protocolIdentity = protocolIdentity;
	}
	
	public String getRegistry() {
		return registry;
	}

	public void setRegistry(String registry) {
		this.registry = registry;
	}

	public String toRegistryPath(){
		return protocolIdentity==null?"":"/"+protocolIdentity.getProtocol()+"."+registry;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
