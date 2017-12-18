package net.transfer.client;

import java.nio.ByteBuffer;
import java.util.Arrays;

import nari.Logger.Logger;
import nari.Logger.LoggerManager;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public class NettyCodecAdapter {

	private Logger logger = LoggerManager.getLogger(this.getClass());
	
	private final ChannelHandler encoder = new InternalEncoder();
	
	private final ChannelHandler decoder = new InternalDecoder();
	
	private final Codec codec = new ExchangerCodec();
	
	private net.transfer.client.ChannelHandler handler;
	
	public NettyCodecAdapter(net.transfer.client.ChannelHandler handler){
		this.handler = handler;
	}
	
	class InternalEncoder extends OneToOneEncoder{

		@Override
		protected Object encode(ChannelHandlerContext ctx, Channel channel, Object message) throws Exception {
//			ByteBuffer buffer = ByteBuffer.allocate(512);
			NettyChannel ch = NettyChannel.get(channel,handler);
			
			ByteBuffer buffer = codec.encode(ch, message);
//			ByteBuffer buffer = ByteBuffer.wrap(new byte[]{0, 6, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 50, 51, 51, 52, 52, 53});
//			buffer.flip();
			logger.info(Arrays.toString(buffer.array()));
			return ChannelBuffers.wrappedBuffer(buffer.array());
		}
		
	}
	
	class InternalDecoder extends SimpleChannelUpstreamHandler{
		
		private ByteBuffer buffer = ByteBuffer.allocate(0);
		
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
			
			ByteBuffer msgBuffer;
            if (buffer.hasRemaining()) {
                int size = buffer.position() + input.readableBytes();
                msgBuffer = ByteBuffer.allocate(size);
                msgBuffer.put(buffer);
                msgBuffer.position(buffer.position());
                msgBuffer.put(input.toByteBuffer());
                
//              msgBuffer.writeBytes(buffer, buffer.readableBytes());
//              msgBuffer.writeBytes(input.toByteBuffer());
            } else {
            	msgBuffer = ByteBuffer.wrap(input.toByteBuffer().array());
            }

            Object msg;
            int saveReaderIndex=-1;
            try {
                do {
                    saveReaderIndex = msgBuffer.position();
                    try {
                        msg = codec.decode(channel, msgBuffer);
                    } catch (Exception ex) {
                        buffer = ByteBuffer.allocate(0);
                        throw ex;
                    }
                    if (msg == DecodeResult.MORE_MESSAGE) {
                    	msgBuffer.position(saveReaderIndex);
                        break;
                    } else {
                        if (saveReaderIndex == msgBuffer.position()) {
                        	buffer = ByteBuffer.allocate(0);
                        }
                        if (msg != null) {
                        	Channels.fireMessageReceived(ctx, msg, e.getRemoteAddress());
                        }
                    }
                } while (msgBuffer.hasRemaining());
            } finally {
                if (msgBuffer.hasRemaining()) {
                    buffer = msgBuffer;
                } else {
                	buffer = ByteBuffer.allocate(0);
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
