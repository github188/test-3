package org.Invoker.remoting.buffer;

import java.nio.ByteBuffer;

public class NettyChannelBuffer extends AbstractChannelBuffer {

	private final byte[] array ;
	
	public NettyChannelBuffer(int capacity){
		this(new byte[capacity],0,0);
	}
	
	public NettyChannelBuffer(byte[] array){
		this(array,0,array.length);
	}
	
	public NettyChannelBuffer(byte[] array,int readerIndex,int writerIndex){
		this.array = array;
		setIndex(readerIndex,writerIndex);
	}
	
	public ByteBuffer toByteBuffer(int offset,int length){
		return ByteBuffer.wrap(array, offset, length);
	}
	
	public int capacity(){
		return array.length;
	}
	
	public byte[] array(){
		return array;
	}

	public int arrayOffset() {
		return 0;
	}

	public byte getByte(int index) {
		return array[index];
	}

	public void getBytes(int index, byte[] dst, int dstIndex, int length) {
		System.arraycopy(array, index, dst, dstIndex, length);
	}
	
	@Override
	public void setByte(int index, int value) {
		array[index] = (byte)value;
	}

	@Override
	public void setBytes(int index, byte[] src, int srcIndex, int length) {
		System.arraycopy(src, srcIndex, array, index, length);
	}

}
