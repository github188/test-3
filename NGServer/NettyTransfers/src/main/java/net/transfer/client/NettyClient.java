package net.transfer.client;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class NettyClient extends AbstractClient implements Client{

	private ClientBootstrap bootstrap;
    
	private volatile org.jboss.netty.channel.Channel channel;
	
	private ConnectTicket ticket;
	
	private InetSocketAddress remoteAddress;
	
	public NettyClient(ConnectTicket ticket,ExchangerHandler handler) throws RemotingException{
		super(NettyClient.wrapperHandler(handler));
		this.ticket = ticket;
		doOpen();
		doConnect();
	}

	@Override
	protected void doConnect() throws RemotingException {
		long timeout = ticket.getConnectTimeout();
		remoteAddress = new InetSocketAddress(ticket.getRemoteAddress(),ticket.getPort());
		
        ChannelFuture future = bootstrap.connect(remoteAddress);
        try{
            boolean ret = future.awaitUninterruptibly(timeout, TimeUnit.MILLISECONDS);
            
            if (ret && future.isSuccess()) {
            	org.jboss.netty.channel.Channel newChannel = future.getChannel();
                newChannel.setInterestOps(org.jboss.netty.channel.Channel.OP_READ_WRITE);
                try {
                	org.jboss.netty.channel.Channel oldChannel = NettyClient.this.channel;
                    if (oldChannel != null) {
                    	oldChannel.close();
                    }
                } finally {
                	NettyClient.this.channel = newChannel;
                    if (NettyClient.this.isClosed()) {
                        try {
                            newChannel.close();
                        } finally {
                            NettyClient.this.channel = null;
                        }
                    }
                }
            } else if (future.getCause() != null) {
            	throw new RuntimeException("failed to connect to server "+ getRemoteAddress() + ", error message is:" + future.getCause().getMessage());
            } else {
            	throw new RuntimeException("failed to connect to server "+ getRemoteAddress() + ", error message is:" + future.getCause().getMessage());
            }
        }finally{
            if (!isConnected()) {
                future.cancel();
            }
        }
	}

	@Override
	protected void doOpen() throws RemotingException {
		long timeout = ticket.getConnectTimeout();
		
		ChannelFactory channelFactory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(new NamedThreadFactory()),Executors.newCachedThreadPool(new NamedThreadFactory()), Runtime.getRuntime().availableProcessors()+1);
		bootstrap = new ClientBootstrap(channelFactory);
        bootstrap.setOption("keepAlive", true);
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("connectTimeoutMillis", timeout);
        final NettyHandler nettyHandler = new NettyHandler(this);
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
                NettyCodecAdapter adapter = new NettyCodecAdapter(NettyClient.this);
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder", adapter.getDecoder());
                pipeline.addLast("encoder", adapter.getEncoder());
                pipeline.addLast("handler", nettyHandler);
                return pipeline;
            }
        });
	}
	
	public static ChannelHandler wrapperHandler(ExchangerHandler handler) {
		ChannelHandler exchangerHandler = new HeaderExchangerHandler(handler);
		ChannelHandler executorHandler = new ExecutorHandler(exchangerHandler);
		ChannelHandler heartbeatHandler = new HeartBeatHandler(executorHandler);
		return heartbeatHandler;
	}
	
	static class NamedThreadFactory implements ThreadFactory {
		public Thread newThread(Runnable r) {
			return new Thread(r);
		}
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
	public Channel getChannel() throws RemotingException{
		org.jboss.netty.channel.Channel c = channel;
		if(c==null || !c.isConnected()){
			return null;
		}
		return NettyChannel.get(c,this);
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return remoteAddress;
	}
}
