package org.Invoker.remoting.exchanger;


public interface ExchangerHandler extends ChannelHandler {
	
	public Object reply(ExchangerChannel channel, Object request) throws RemotingException;
	
}
