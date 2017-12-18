package org.Invoker;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class WebServiceServerIdentity extends Identity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7133667274724214646L;

	private String host = "";
	
	private int port = 0;

	private int maxThreadPool = 0;
	
	private int minThreadPool = 0;
	
	public WebServiceServerIdentity(){
		setProtocol(Constants.WEBSERVICE);
	}
	
	public String getHost() {
		try {
			host = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
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
