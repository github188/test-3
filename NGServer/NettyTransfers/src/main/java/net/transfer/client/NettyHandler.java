package net.transfer.client;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nari.Logger.Logger;
import nari.Logger.LoggerManager;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class NettyHandler extends SimpleChannelHandler {
	
	private Logger logger = LoggerManager.getLogger(this.getClass());
	
	private final Map<SocketAddress, Channel> channels = new ConcurrentHashMap<SocketAddress, Channel>();
	
	private ChannelHandler handler = null;
	
	public NettyHandler(ChannelHandler handler){
		this.handler = handler;
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		channels.put(ctx.getChannel().getRemoteAddress(), NettyChannel.get(ctx.getChannel(),handler));
		handler.connected(NettyChannel.get(ctx.getChannel(),handler));
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,ChannelStateEvent e) throws Exception {
		channels.remove(ctx.getChannel().getRemoteAddress());
		handler.disconnected(NettyChannel.get(ctx.getChannel(),handler));
		logger.info("server disconnect"+ctx.getChannel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		handler.caught(NettyChannel.get(ctx.getChannel(),handler), e.getCause());
	}

	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		super.writeRequested(ctx, e);
		handler.sent(NettyChannel.get(ctx.getChannel(),handler), e.getMessage());
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		handler.received(NettyChannel.get(ctx.getChannel(),handler), e.getMessage());
	}
	
	public Map<SocketAddress, Channel> getChannels(){
		return channels;
	}
}
