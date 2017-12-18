package net.transfer.client;


public interface ExchangerHandler extends ChannelHandler {
	
	public Object reply(ExchangerChannel channel, Object request) throws RemotingException;
	
}
