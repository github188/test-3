package org.Invoker;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.Invoker.rpc.exception.InvokerException;

public class HttpServerIdentity extends Identity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3864276933013647832L;

	public HttpServerIdentity(){
		setProtocol(Constants.HTTP);
	}
	
	private String ip = "";
	
	private int port = 0;
	
	private int maxThreadPool = 0;
	
	private int minThreadPool = 0;

	public String getIp() {
//		if(ip==null || "".equals(ip)){
//			return "localhost";
//		}
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}

//	public void setIp(String ip) {
//		this.ip = ip;
//	}

	public int getPort() {
		if(port<=0){
			throw new InvokerException("null port");
		}
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getMaxThreadPool() {
		if(maxThreadPool<=0){
			return Runtime.getRuntime().availableProcessors()*2+1;
		}
		return maxThreadPool;
	}

	public void setMaxThreadPool(int maxThreadPool) {
		this.maxThreadPool = maxThreadPool;
	}

	public int getMinThreadPool() {
		if(minThreadPool<=0){
			return Runtime.getRuntime().availableProcessors()+1;
		}
		return minThreadPool;
	}

	public void setMinThreadPool(int minThreadPool) {
		this.minThreadPool = minThreadPool;
	}

}
