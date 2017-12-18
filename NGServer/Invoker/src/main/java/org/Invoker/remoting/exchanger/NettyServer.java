package org.Invoker.remoting.exchanger;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import org.Invoker.Identity;
import org.Invoker.SocketServerIdentity;
import org.Invoker.remoting.codec.NettyCodecAdapter;

public class NettyServer extends AbstractServer implements Server {

	private ServerBootstrap bootstrap;
	
	private org.jboss.netty.channel.Channel channel;
	
	private Map<SocketAddress, Channel> channels = null;
	
	public NettyServer(SocketServerIdentity ident, ExchangerHandler handler) throws RemotingException{
		super(ident,NettyServer.wrapperHandler(ident,handler));
	}

	@Override
	protected void doOpen() throws RemotingException {
		ExecutorService accept = Executors.newCachedThreadPool(new NamedThreadFactory());
        ExecutorService worker = Executors.newCachedThreadPool(new NamedThreadFactory());
        ChannelFactory channelFactory = new NioServerSocketChannelFactory(accept, worker, getIdentity().getThreadPoolSize()==0?Runtime.getRuntime().availableProcessors()+1:getIdentity().getThreadPoolSize());
        bootstrap = new ServerBootstrap(channelFactory);
        
        final NettyHandler nettyHandler = new NettyHandler(this);
        channels = nettyHandler.getChannels();
        
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
                NettyCodecAdapter adapter = new NettyCodecAdapter(NettyServer.this);
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder", adapter.getDecoder());
                pipeline.addLast("encoder", adapter.getEncoder());
                pipeline.addLast("handler", nettyHandler);
                return pipeline;
            }
        });
        channel = bootstrap.bind(getBindAddress());
	}
	
	
	public Channel getChannel(InetSocketAddress remoteAddress) throws RemotingException{
		return channels.get(remoteAddress);
	}

	public Collection<Channel> getChannels() throws RemotingException{
		Collection<Channel> chs = new ArrayList<Channel>();
		
		for(Map.Entry<SocketAddress,Channel> entry:channels.entrySet()){
			if(entry.getValue().isConnected()){
				chs.add(entry.getValue());
			}else{
				channels.remove(entry.getKey());
			}
		}
		return chs;
	}

	public static ChannelHandler wrapperHandler(Identity ident,ExchangerHandler handler){
		ChannelHandler exchangerHandler = new HeaderExchangerHandler(handler);
		ChannelHandler executorHandler = new ExecutorHandler(ident,exchangerHandler);
		ChannelHandler heartbeatHandler = new HeartBeatHandler(executorHandler);
		return heartbeatHandler;
	}
	
	static class NamedThreadFactory implements ThreadFactory{
		public Thread newThread(Runnable r) {
			return new Thread(r);
		}
	}

	@Override
	public Channel getChannel() {
		org.jboss.netty.channel.Channel c = channel;
		if(c==null || !c.isConnected()){
			return null;
		}
		return NettyChannel.get(c,this);
	}
}
