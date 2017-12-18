package org.Invoker.remoting.codec;

import java.io.IOException;

import org.Invoker.remoting.buffer.ChannelBuffer;
import org.Invoker.remoting.exchanger.Channel;
import org.Invoker.rpc.Adaptive;
import org.Invoker.rpc.SPI;

@SPI
public interface Codec {

	@Adaptive
	public void encode(Channel channel,ChannelBuffer buffer,Object message) throws IOException;
	
	@Adaptive
	public Object decode(Channel channel,ChannelBuffer buffer) throws IOException;
	
}
