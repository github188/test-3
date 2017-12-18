package org.Invoker.remoting.buffer;

import java.nio.ByteBuffer;

public class Buffers {

	public static final ChannelBuffer EMPTY = Buffers.buffer(0);
	
	public static ChannelBuffer buffer(int capacity) {
		return new NettyChannelBuffer(capacity);
	}
	
	public static ChannelBuffer wrapperBuffer(byte[] array){
		return new NettyChannelBuffer(array);
	}
	
	public static ChannelBuffer wrapperBuffer(byte[] array, int off, int len){
		byte[] arr = new byte[len];
		System.arraycopy(array, off, arr, 0, len);
		return new NettyChannelBuffer(arr);
	}
	
	public static ChannelBuffer dynamicBuffer(int capacity){
		return new DynamicChannelBuffer(capacity);
	}
	
	public static ChannelBuffer wrapperBuffer(ByteBuffer buffer){
		if(buffer.hasArray()){
			return wrapperBuffer(buffer.array(),buffer.arrayOffset()+buffer.position(),buffer.remaining());
		}else{
			return new ByteBufferChannelBuffer(buffer);
		}
	}
}
