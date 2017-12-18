package org.Invoker.remoting.exchanger;

import java.net.InetSocketAddress;

public interface Channel extends Endpoint{

	public InetSocketAddress getRemoteAddress();
	
	public boolean isConnected() throws RemotingException;
	
	boolean hasAttribute(String key);

	public Object getAttribute(String key);

	public void setAttribute(String key,Object value);
    
	public void removeAttribute(String key);
}
