package org.Invoker.remoting.buffer;

import java.nio.ByteBuffer;

public class ByteBufferChannelBuffer extends AbstractChannelBuffer{

	private ByteBuffer buffer = null;
	
	private int capacity ;
	
	public ByteBufferChannelBuffer(ByteBuffer buffer){
		this.buffer = buffer.slice();
		capacity = buffer.remaining();
		writerIndex(capacity);
	}

	public byte[] array() {
		return buffer.array();
	}

	public int arrayOffset() {
		return buffer.arrayOffset();
	}

	public int capacity() {
		return capacity;
	}

	public byte getByte(int index) {
		return buffer.get(index);
	}

	public void getBytes(int index, byte[] dst, int dstIndex, int length) {
		buffer.get(dst, index, length);
	}
}
