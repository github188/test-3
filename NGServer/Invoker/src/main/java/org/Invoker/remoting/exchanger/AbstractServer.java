package org.Invoker.remoting.exchanger;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.Invoker.SocketServerIdentity;

public abstract class AbstractServer extends AbstractEndpoint implements Server {

	private InetSocketAddress bindAddress;
	
	private SocketServerIdentity identity = null;
	
	public AbstractServer(SocketServerIdentity ident,ChannelHandler handler) throws RemotingException{
		super(handler);
		this.identity = ident;
		String ip = "127.0.0.1";;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		bindAddress = new InetSocketAddress(ip, ident.getPort());
        doOpen();
	}
	
	protected InetSocketAddress getBindAddress(){
		return bindAddress;
	}
	
	protected SocketServerIdentity getIdentity(){
		return identity;
	}
	
	protected abstract void doOpen() throws RemotingException;
}
