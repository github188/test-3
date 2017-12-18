package org.Invoker.remoting.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import org.Invoker.remoting.exchanger.NettyChannel;
import org.Invoker.rpc.extension.ExtensionLoader;

public class NettyCodecAdapter {

	private final ChannelHandler encoder = new InternalEncoder();
	
	private final ChannelHandler decoder = new InternalDecoder();
	
	private final org.Invoker.remoting.exchanger.ChannelHandler handler;
	
	private final Codec codec = ExtensionLoader.getExtensionLoader(Codec.class).getExtension("exchanger");
	
	public NettyCodecAdapter(org.Invoker.remoting.exchanger.ChannelHandler handler){
		this.handler = handler;
	}
	
	class InternalEncoder extends OneToOneEncoder{

		@Override
		protected Object encode(ChannelHandlerContext ctx, Channel channel, Object message) throws Exception {
			org.Invoker.remoting.buffer.ChannelBuffer buffer = org.Invoker.remoting.buffer.Buffers.dynamicBuffer(512);
			NettyChannel ch = NettyChannel.get(channel,handler);
			
			codec.encode(ch, buffer, message);
			
			return ChannelBuffers.wrappedBuffer(buffer.toByteBuffer());
		}
		
	}
	
	class InternalDecoder extends SimpleChannelUpstreamHandler{
		
		private org.Invoker.remoting.buffer.ChannelBuffer buffer = org.Invoker.remoting.buffer.Buffers.EMPTY;
		
		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
			Object obj = e.getMessage();
			if(!(obj instanceof ChannelBuffer)){
				ctx.sendUpstream(e);
				return ;
			}
			
			ChannelBuffer input = (ChannelBuffer)obj;
			int readable = input.readableBytes();
			if(readable<=0){
				return ;
			}
			
			NettyChannel channel = NettyChannel.get(ctx.getChannel(),handler);
			
			org.Invoker.remoting.buffer.ChannelBuffer msgBuffer;
            if (buffer.readable()) {
                int size = buffer.readableBytes() + input.readableBytes();
                msgBuffer = org.Invoker.remoting.buffer.Buffers.buffer(size);
                msgBuffer.writeBytes(buffer, buffer.readableBytes());
                msgBuffer.writeBytes(input.toByteBuffer());
            } else {
            	msgBuffer = org.Invoker.remoting.buffer.Buffers.wrapperBuffer(input.toByteBuffer());
            }

            Object msg;
            int saveReaderIndex=-1;
            try {
                do {
                    saveReaderIndex = msgBuffer.readerIndex();
                    try {
                        msg = codec.decode(channel, msgBuffer);
                    } catch (Exception ex) {
                        buffer = org.Invoker.remoting.buffer.Buffers.EMPTY;
                        throw ex;
                    }
                    if (msg == DecodeResult.MORE_MESSAGE) {
                    	msgBuffer.readerIndex(saveReaderIndex);
                        break;
                    } else {
                        if (saveReaderIndex == msgBuffer.readerIndex()) {
                            buffer = org.Invoker.remoting.buffer.Buffers.EMPTY;
                        }
                        if (msg != null) {
                        	Channels.fireMessageReceived(ctx, msg, e.getRemoteAddress());
                        }
                    }
                } while (msgBuffer.readable());
            } finally {
                if (msgBuffer.readable()) {
                    buffer = msgBuffer;
                } else {
                	buffer = org.Invoker.remoting.buffer.Buffers.EMPTY;
                }
            }
		}
	}
	
	public ChannelHandler getEncoder(){
		return encoder;
	}
	
	public ChannelHandler getDecoder(){
		return decoder;
	}
	
}
