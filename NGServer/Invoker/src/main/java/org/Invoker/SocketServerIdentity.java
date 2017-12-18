package org.Invoker;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SocketServerIdentity extends Identity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7634293738075315035L;

	public SocketServerIdentity(){
		setProtocol(Constants.SOCKET);
	}
	
	private String host = "";
	
	private int port = 80;
	
	private int threadPoolSize = 0;

	public int getPort() {
		return port;
	}

	public String getHost() {
		try {
			host = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}
	
}
