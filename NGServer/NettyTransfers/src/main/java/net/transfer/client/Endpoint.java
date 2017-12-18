package net.transfer.client;

import java.net.InetSocketAddress;

public interface Endpoint {

	public ChannelHandler getChannelHandler() throws RemotingException;
	
	public InetSocketAddress getLocalAddress() throws RemotingException;
	
	public void send(Object message) throws RemotingException;
	
	public void send(Object message,long timeout) throws RemotingException;
	
	public void close() throws RemotingException;
	
	public void close(long timeout) throws RemotingException;
	
	public boolean isClosed() throws RemotingException;
}
