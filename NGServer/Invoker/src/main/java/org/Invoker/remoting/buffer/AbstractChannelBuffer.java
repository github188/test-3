package org.Invoker.remoting.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public abstract class AbstractChannelBuffer implements ChannelBuffer {

	private int writerIndex;
	
	private int readerIndex;
	
	public void setIndex(int readerIndex,int writerIndex){
		this.readerIndex = readerIndex;
		this.writerIndex = writerIndex;
	}

	public void clear() {
		readerIndex = 0;
		writerIndex = 0;
	}

	public ChannelBuffer copy() {
		return Buffers.wrapperBuffer(array());
	}

	public ChannelBuffer copy(int index, int length) {
		byte[] array = new byte[length];
		getBytes(index, array, 0, length);
		return Buffers.wrapperBuffer(array);
	}

	public void getBytes(int index, byte[] dst) {
		getBytes(index,dst,0,dst.length);
	}

	public void getBytes(int index, ChannelBuffer dst) {
		getBytes(index,dst,dst.capacity());
	}

	public void getBytes(int index, ChannelBuffer dst, int length) {
		getBytes(index, dst, 0, length);
	}

	public void getBytes(int index, ChannelBuffer dst, int dstIndex, int length) {
		byte[] array = new byte[length];
		getBytes(index,array,dstIndex,length);
		dst.writeBytes(array);
	}
	
	public boolean hasArray() {
		return false;
	}

	public void markReaderIndex() {
		
	}

	public void markWriterIndex() {
		
	}

	public byte readByte() {
		byte b = getByte(readerIndex());
		readerIndex(readerIndex()+1);
		return b;
	}

	public void readBytes(byte[] dst) {
		getBytes(readerIndex(), dst);
		readerIndex(readerIndex()+dst.length);
	}

	public void readBytes(byte[] dst, int dstIndex, int length) {
		getBytes(readerIndex(), dst, dstIndex, length);
		readerIndex(readerIndex()+length);
	}

	public void readBytes(ChannelBuffer dst) {
		readBytes(dst,dst.capacity());
	}

	public void readBytes(ChannelBuffer dst, int length) {
		readBytes(dst,0,dst.capacity());
	}

	public void readBytes(ChannelBuffer dst, int dstIndex, int length) {
		getBytes(readerIndex(), dst, dstIndex, length);
		readerIndex(readerIndex()+length);
	}

	public ChannelBuffer readBytes(int length) {
		ChannelBuffer dst = Buffers.dynamicBuffer(length);
		getBytes(readerIndex(), dst, length);
		readerIndex(readerIndex()+length);
		return dst;
	}

	public void readBytes(OutputStream dst, int length) throws IOException {
		byte[] arr = new byte[length];
		getBytes(readerIndex(), arr, 0, length);
		dst.write(arr, 0, length);
		readerIndex(readerIndex()+length);
	}

	public boolean readable() {
		return readableBytes()>0;
	}

	public int readableBytes() {
		return writerIndex - readerIndex;
	}

	public int readerIndex() {
		return readerIndex;
	}

	public void readerIndex(int readerIndex) {
		this.readerIndex = readerIndex;
	}

	public void resetReaderIndex() {
		readerIndex = 0;
	}

	public void resetWriterIndex() {
		writerIndex = 0;
	}

	public void setByte(int index, int value) {
		setByte(index, value);
	}

	public void setBytes(int index, byte[] src) {
		setBytes(index, src, 0, src.length);
	}

	public void setBytes(int index, byte[] src, int srcIndex, int length) {
		setBytes(index, src, srcIndex, length);
	}

	public void setBytes(int index, ChannelBuffer src) {
		setBytes(index, src, 0, src.capacity());
	}

	public void setBytes(int index, ChannelBuffer src, int length) {
		setBytes(index, src, 0, length);
	}

	public void setBytes(int index, ChannelBuffer src, int srcIndex, int length) {
		byte[] dst = new byte[length];
		src.getBytes(srcIndex, dst, 0, length);
		setBytes(index, dst, 0, length);
	}

	public int setBytes(int index, InputStream src, int length) throws IOException {
		byte[] b = new byte[length];
		src.read(b, 0, length);
		writeBytes(b);
		return b.length;
	}

	public void skipBytes(int length) {
		int index = readerIndex + length;
		if(index>writerIndex){
			throw new ArrayIndexOutOfBoundsException(index);
		}
		readerIndex = index;
	}

	public ByteBuffer toByteBuffer() {
		return toByteBuffer(readerIndex,readableBytes());
	}

	public ByteBuffer toByteBuffer(int index, int length) {
		byte[] b = new byte[length];
		getBytes(index, b, 0, length);
		return ByteBuffer.wrap(b);
	}

	public boolean writable() {
		return writableBytes()>0;
	}

	public int writableBytes() {
		return capacity() - writerIndex;
	}

	public void writeByte(int value) {
		setByte(writerIndex(), value);
		writerIndex(writerIndex()+1);
	}

	public void writeBytes(byte[] src) {
		writeBytes(src, 0, src.length);
	}

	public void writeBytes(byte[] src, int index, int length) {
		setBytes(writerIndex(), src, index, length);
		writerIndex(writerIndex()+length);
	}

	public void writeBytes(ByteBuffer src) {
		writeBytes(src.array());
	}

	public void writeBytes(ChannelBuffer src) {
		writeBytes(src, src.capacity());
	}

	public void writeBytes(ChannelBuffer src, int length) {
		writeBytes(src, 0, length);
	}

	public void writeBytes(ChannelBuffer src, int srcIndex, int length) {
		setBytes(writerIndex(), src, srcIndex, length);
		writerIndex(writerIndex()+length);
	}

	public int writeBytes(InputStream src, int length) throws IOException {
		byte[] b = new byte[length];
		src.read(b, 0, length);
		writeBytes(b);
		return b.length;
	}

	public int writerIndex() {
		return writerIndex;
	}

	public void writerIndex(int writerIndex) {
		this.writerIndex = writerIndex;
	}
	
}
