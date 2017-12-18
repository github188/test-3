package net.transfer.client;

public class HeartBeatHandler implements ChannelHandler {

	private ChannelHandler handler = null;
	
	public HeartBeatHandler(ChannelHandler handler){
		this.handler = handler;
	}
	
	@Override
	public void connected(Channel channel) throws RemotingException {
		handler.connected(channel);
	}

	@Override
	public void disconnected(Channel channel) throws RemotingException {
		handler.disconnected(channel);
	}

	@Override
	public void sent(Channel channel, Object message) throws RemotingException {
		handler.sent(channel, message);
	}

	@Override
	public void received(Channel channel, Object message) throws RemotingException {
		handler.received(channel, message);
	}

	@Override
	public void caught(Channel channel, Throwable exception) throws RemotingException {
		handler.caught(channel, exception);
	}

}
