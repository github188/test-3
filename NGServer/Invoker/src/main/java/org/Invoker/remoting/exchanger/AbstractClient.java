package org.Invoker.remoting.exchanger;

import java.net.InetSocketAddress;

import org.Invoker.SocketClientIdentity;

public abstract class AbstractClient extends AbstractEndpoint implements Client {

	private SocketClientIdentity ident = null;
	
	public AbstractClient(SocketClientIdentity ident,ChannelHandler handler) throws RemotingException{
		super(handler);
		this.ident = ident;
		doOpen();
		doConnect();
	}
	
	public InetSocketAddress getRemoteAddress() {
		return new InetSocketAddress(ident.getRemoteHost(), ident.getRemotePort());
	}

	public boolean isConnected() throws RemotingException {
		return getChannel().isConnected();
	}
	
	protected SocketClientIdentity getIdentity(){
		return ident;
	}

	public abstract Channel getChannel() throws RemotingException;
	
	protected abstract void doOpen() throws RemotingException;
	
	protected abstract void doConnect() throws RemotingException;
}
