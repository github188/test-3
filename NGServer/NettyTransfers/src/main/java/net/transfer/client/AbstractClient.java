package net.transfer.client;

import java.net.InetSocketAddress;

public abstract class AbstractClient extends AbstractEndpoint implements Client {

	public AbstractClient(ChannelHandler handler) throws RemotingException{
		super(handler);
//		doOpen();
//		doConnect();
	}
	
	public boolean isConnected() throws RemotingException {
		Channel channel = getChannel();
		if(channel == null){
			return false;
		}
		return getChannel().isConnected();
	}
	
	public abstract Channel getChannel() throws RemotingException;
	
	protected abstract void doOpen() throws RemotingException;
	
	protected abstract void doConnect() throws RemotingException;
	
	public abstract InetSocketAddress getRemoteAddress();
	
}
