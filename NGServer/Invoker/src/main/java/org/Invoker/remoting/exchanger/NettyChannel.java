package org.Invoker.remoting.exchanger;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.channel.ChannelFuture;

public class NettyChannel implements Channel {

	private static final ConcurrentHashMap<org.jboss.netty.channel.Channel,NettyChannel> channelMap = new ConcurrentHashMap<org.jboss.netty.channel.Channel,NettyChannel>();
	
	private final Map<String,Object> attribute = new HashMap<String,Object>();
	
	private org.jboss.netty.channel.Channel channel = null;
	
	private ChannelHandler handler = null;
	
	public NettyChannel(org.jboss.netty.channel.Channel channel,ChannelHandler handler){
		this.channel = channel;
		this.handler = handler;
	}
	
	public InetSocketAddress getRemoteAddress() {
		return (InetSocketAddress)channel.getRemoteAddress();
	}

	public boolean isConnected() throws RemotingException {
		return channel.isConnected();
	}

	public void close() throws RemotingException {
		attribute.clear();
		channel.disconnect();
		channel.close();
		channelMap.remove(channel);
	}

	public void close(long timeout) throws RemotingException {
		attribute.clear();
		Channel ch = channelMap.remove(channel);
		if(ch!=null && channel.isConnected()){
			ch.close();
		}
	}

	public ChannelHandler getChannelHandler() {
		return handler;
	}

	public InetSocketAddress getLocalAddress() {
		return (InetSocketAddress)channel.getLocalAddress();
	}

	public boolean isClosed() throws RemotingException {
		return !channel.isOpen();
	}

	public void send(Object message) throws RemotingException {
		try {
			ChannelFuture future = channel.write(message);
			Throwable t =future.getCause();
			if(t!=null){
				throw t;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RemotingException("faile to send message "+message+" to "+getRemoteAddress()+",exception "+e.getMessage(),e);
		}
	}

	public void send(Object message, long timeout) throws RemotingException {
		try {
			ChannelFuture future = channel.write(message);
			future.await(timeout);
			Throwable t =future.getCause();
			if(t!=null){
				throw t;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RemotingException("faile to send message "+message+" to "+getRemoteAddress()+",exception "+e.getMessage(),e);
		}
	}
	
	public static NettyChannel get(org.jboss.netty.channel.Channel channel,ChannelHandler handler){
		NettyChannel nc = channelMap.get(channel);
		if(nc==null){
			nc = new NettyChannel(channel,handler);
			nc = channelMap.putIfAbsent(channel, nc);
		}
		return channelMap.get(channel);
	}

	public Object getAttribute(String key) {
		return attribute.get(key);
	}

	public boolean hasAttribute(String key) {
		return attribute.containsKey(key);
	}

	public void removeAttribute(String key) {
		attribute.remove(key);
	}

	public void setAttribute(String key, Object value) {
		attribute.put(key, value);
	}

}
