package org.Invoker.remoting.exchanger;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

import org.Invoker.remoting.handler.HeaderExchangerChannel;

public class HeaderExchangeClient implements ExchangerClient {

	private ExchangerChannel channel = null;
	
	private final AtomicInteger ref = new AtomicInteger(0);
	
	public HeaderExchangeClient(Client client){
		channel = new HeaderExchangerChannel(client);
	}
	
	public void close() throws RemotingException {
		channel.close();
	}

	public void close(long timeout) throws RemotingException {
		channel.close(timeout);
	}

	public ChannelHandler getChannelHandler() throws RemotingException{
		return channel.getChannelHandler();
	}

	public InetSocketAddress getLocalAddress() throws RemotingException{
		return channel.getLocalAddress();
	}

	public boolean isClosed() throws RemotingException {
		return channel.isClosed();
	}

	public void send(Object message) throws RemotingException {
		channel.send(message);
	}

	public void send(Object message, long timeout) throws RemotingException {
		channel.send(message, timeout);
	}

	public InetSocketAddress getRemoteAddress() {
		return channel.getRemoteAddress();
	}

	public boolean isConnected() throws RemotingException {
		return channel.isConnected();
	}

	public ExchangerHandler getExchangeHandler() throws RemotingException{
		return channel.getExchangeHandler();
	}

	public ResponseFuture request(Object request) throws RemotingException {
		return channel.request(request);
	}

	public ResponseFuture request(Object request, int timeout) throws RemotingException {
		return channel.request(request, timeout);
	}

	public Object getAttribute(String key) {
		return null;
	}

	public boolean hasAttribute(String key) {
		return false;
	}

	public void removeAttribute(String key) {
		
	}

	public void setAttribute(String key, Object value) {
		
	}

	@Override
	public void increament() {
		ref.incrementAndGet();
	}

	@Override
	public void decrement() {
		ref.decrementAndGet();
	}

	@Override
	public int ref() {
		return ref.get();
	}

}
