package org.Invoker;

import java.io.Serializable;

public class WebServiceClientIdentity extends Identity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5968698835026760243L;

	private String host = "";
	
	private int port = 0;
	
	private long connectTimeout = 0;
	
	private long reciveTimeout = 0;
	
	private int maxThreadPool;
	
	private int minThreadPool;
	
	public WebServiceClientIdentity(){
		setProtocol(Constants.WEBSERVICE);
	}

	public String getRemoteHost() {
		return host;
	}

	public void setRemoteHost(String host) {
		this.host = host;
	}

	public int getRemotePort() {
		return port;
	}

	public void setRemotePort(int port) {
		this.port = port;
	}

	public long getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(long connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public long getReciveTimeout() {
		return reciveTimeout;
	}

	public void setReciveTimeout(long reciveTimeout) {
		this.reciveTimeout = reciveTimeout;
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
