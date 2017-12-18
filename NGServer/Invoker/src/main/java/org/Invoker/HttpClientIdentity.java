package org.Invoker;

import java.io.Serializable;

public class HttpClientIdentity extends Identity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7433117152621769321L;

	public HttpClientIdentity(){
		setProtocol(Constants.HTTP);
	}
	
}
