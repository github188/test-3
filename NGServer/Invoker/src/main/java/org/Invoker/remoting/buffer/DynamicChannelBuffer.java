package org.Invoker.remoting.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DynamicChannelBuffer extends AbstractChannelBuffer {

	private byte[] array ;
	
	private int capacity = 0;
	
	public DynamicChannelBuffer(int capacity) {
		this(new byte[capacity]);
		this.capacity = capacity;
	}
	
	public DynamicChannelBuffer(byte[] array){
		this(array,0,0);
	}
	
	public DynamicChannelBuffer(byte[] array,int readerIndex,int writerIndex){
		this.array = array;
		setIndex(readerIndex,writerIndex);
	}
	
	@Override
	public int capacity() {
		return capacity;
	}

	@Override
	public byte getByte(int index) {
		return array[index];
	}

	@Override
	public void getBytes(int index, byte[] dst, int dstIndex, int length) {
		System.arraycopy(array, index, dst, dstIndex, length);
	}

	@Override
	public byte[] array() {
		return array;
	}

	@Override
	public int arrayOffset() {
		return 0;
	}

	@Override
	public void setByte(int index, int value) {
		array[index] = (byte)value;
	}

	@Override
	public void setBytes(int index, byte[] src, int srcIndex, int length) {
		System.arraycopy(src, srcIndex, array, index, length);
	}

	private void checkBuffer(int length){
		if(writableBytes()<length){
			int cap = writerIndex()+length;
			byte[] newArr = new byte[cap];
			System.arraycopy(array, 0, newArr, 0, array.length);
			array = newArr;
			capacity = array.length;
		}
	}
	
	@Override
	public void writeByte(int value) {
		checkBuffer(1);
		super.writeByte(value);
	}
	
	@Override
	public void writeBytes(byte[] src) {
		checkBuffer(src.length);
		super.writeBytes(src);
	}

	@Override
	public void writeBytes(byte[] src, int index, int length) {
		checkBuffer(length);
		super.writeBytes(src, index, length);
	}

	@Override
	public void writeBytes(ByteBuffer src) {
		checkBuffer(src.capacity());
		super.writeBytes(src);
	}

	@Override
	public void writeBytes(ChannelBuffer src) {
		checkBuffer(src.capacity());
		super.writeBytes(src);
	}

	@Override
	public void writeBytes(ChannelBuffer src, int length) {
		checkBuffer(length);
		super.writeBytes(src, length);
	}

	@Override
	public void writeBytes(ChannelBuffer src, int srcIndex, int length) {
		checkBuffer(length);
		super.writeBytes(src, srcIndex, length);
	}

	@Override
	public int writeBytes(InputStream src, int length) throws IOException {
		checkBuffer(length);
		return super.writeBytes(src, length);
	}
}
