package org.Invoker.remoting.exchanger;

import java.net.InetSocketAddress;

public abstract class AbstractEndpoint extends AbstractChannelHandler implements Endpoint {

	private ChannelHandler handler = null;
	
	public AbstractEndpoint(ChannelHandler handler){
		super(handler);
		this.handler = handler;
	}
	
	public void close() throws RemotingException {
		close(5000);
	}

	public void close(long timeout) throws RemotingException {
		getChannel().close(timeout);
	}

	public ChannelHandler getChannelHandler() throws RemotingException{
		return handler;
	}

	public InetSocketAddress getLocalAddress() throws RemotingException{
		return getChannel().getLocalAddress();
	}

	public boolean isClosed() throws RemotingException {
		return getChannel().isClosed();
	}

	public void send(Object message) throws RemotingException {
		send(message,5000);
	}

	public void send(Object message, long timeout) throws RemotingException {
		getChannel().send(message, timeout);
	}

	public abstract Channel getChannel() throws RemotingException;
}
