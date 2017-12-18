package org.Invoker.remoting.exchanger;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;

import org.Invoker.remoting.handler.HeaderExchangerChannel;

public class HeaderExchangeServer implements ExchangerServer{

	private Server server = null;
	
	public HeaderExchangeServer(Server server){
		this.server = server;
	}
	
	public ExchangerChannel getExchangerChannel(InetSocketAddress remoteAddress) throws RemotingException{
		Channel ch = server.getChannel(remoteAddress);
		return HeaderExchangerChannel.get(ch);
	}

	public Collection<ExchangerChannel> getExchangerChannels() throws RemotingException{
		Collection<Channel> chs = server.getChannels();
		
		Collection<ExchangerChannel> echs = new ArrayList<ExchangerChannel>();
		for(Channel channel:chs){
			echs.add(HeaderExchangerChannel.get(channel));
		}
		return echs;
	}

	public void close() throws RemotingException {
		server.close();
	}

	public void close(long timeout) throws RemotingException {
		server.close(timeout);
	}

	public ChannelHandler getChannelHandler() throws RemotingException{
		return server.getChannelHandler();
	}

	public InetSocketAddress getLocalAddress() throws RemotingException{
		return server.getLocalAddress();
	}

	public boolean isClosed() throws RemotingException {
		return server.isClosed();
	}

	public void send(Object message) throws RemotingException {
		server.send(message);
	}

	public void send(Object message, long timeout) throws RemotingException {
		server.send(message,timeout);
	}

}
