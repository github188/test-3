package net.transfer.client;

import java.net.InetSocketAddress;
import java.util.Collection;

public interface ExchangerServer extends Endpoint{

	public Collection<ExchangerChannel> getExchangerChannels() throws RemotingException;
	
	public ExchangerChannel getExchangerChannel(InetSocketAddress remoteAddress) throws RemotingException;
}
