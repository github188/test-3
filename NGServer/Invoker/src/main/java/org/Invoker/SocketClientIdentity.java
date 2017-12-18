package org.Invoker;

import java.io.Serializable;

public class SocketClientIdentity extends Identity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7634293738075315035L;

	public SocketClientIdentity(){
		setProtocol(Constants.SOCKET);
	}
	
	private String host = "";
	
	private int port = 80;
	
	private long connectTimeout = 5000;
	
	private int threadPoolSize = 0;
	
	public String getRemoteHost() {
		return host;
	}

	public void setRemoteHost(String ip) {
		this.host = ip;
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

	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}
	
}
