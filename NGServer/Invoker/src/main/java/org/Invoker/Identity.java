package org.Invoker;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Identity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6407856063049274224L;

	private String protocol = "";
	
	private String version = "base";
	
	private final Map<String,String> valueCache = new HashMap<String,String>();

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setAttribute(String key,String value){
		valueCache.put(key, value);
	}
	
	public String getAttribute(String key,String defaultValue){
		if(valueCache.get(key)==null || "".equals(valueCache.get(key))){
			return defaultValue;
		}
		return valueCache.get(key);
	}
	
}
