package org.Invoker.remoting.exchanger;

public class AbstractChannelHandler implements ChannelHandler {

	private ChannelHandler handler;
	
	public AbstractChannelHandler(ChannelHandler handler){
		this.handler = handler;
	}
	
	public void caught(Channel channel, Throwable exception) throws RemotingException {
		handler.caught(channel, exception);
	}

	public void connected(Channel channel) throws RemotingException {
		handler.connected(channel);
	}

	public void disconnected(Channel channel) throws RemotingException {
		handler.disconnected(channel);
	}

	public void received(Channel channel, Object message) throws RemotingException {
		handler.received(channel, message);
	}

	public void sent(Channel channel, Object message) throws RemotingException {
		handler.sent(channel, message);
	}

}
