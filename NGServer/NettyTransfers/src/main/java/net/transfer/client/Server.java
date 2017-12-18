package net.transfer.client;

import java.net.InetSocketAddress;
import java.util.Collection;

public interface Server extends Endpoint {

	public Collection<Channel> getChannels() throws RemotingException;
	
	public Channel getChannel(InetSocketAddress remoteAddress) throws RemotingException;
	
}
