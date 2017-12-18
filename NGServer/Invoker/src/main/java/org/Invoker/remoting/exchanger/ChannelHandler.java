package org.Invoker.remoting.exchanger;

public interface ChannelHandler{
	
	public void connected(Channel channel) throws RemotingException;

	public void disconnected(Channel channel) throws RemotingException;

	public void sent(Channel channel, Object message) throws RemotingException;

	public void received(Channel channel, Object message) throws RemotingException;

	public void caught(Channel channel, Throwable exception) throws RemotingException;
}
