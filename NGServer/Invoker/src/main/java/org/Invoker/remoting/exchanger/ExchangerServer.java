package org.Invoker.remoting.exchanger;

import java.net.InetSocketAddress;
import java.util.Collection;

import org.Invoker.rpc.SPI;

@SPI("headerexchanger")
public interface ExchangerServer extends Endpoint{

	public Collection<ExchangerChannel> getExchangerChannels() throws RemotingException;
	
	public ExchangerChannel getExchangerChannel(InetSocketAddress remoteAddress) throws RemotingException;
}
