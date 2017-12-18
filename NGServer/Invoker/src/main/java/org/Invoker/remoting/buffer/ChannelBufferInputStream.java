package org.Invoker.remoting.buffer;

import java.io.IOException;
import java.io.InputStream;

public class ChannelBufferInputStream extends InputStream {

	private ChannelBuffer buffer = null;
	
	private int readIndex = 0;
	
	public ChannelBufferInputStream(ChannelBuffer buffer,int len){
		this.buffer = buffer;
		this.buffer.readerIndex(len);
		readIndex = buffer.readerIndex();
	}
	
	@Override
	public int read() throws IOException {
		return (byte)buffer.readByte();
	}
	
	@Override
	public int read(byte[] b) throws IOException {
		buffer.readBytes(b);
		int size = buffer.readerIndex() - readIndex;
		readIndex = buffer.readerIndex();
		return size;
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int i = buffer.readableBytes();
		int l = len<i?len:i;
		buffer.readBytes(b, off, l);
		int size = buffer.readerIndex() - readIndex;
		readIndex = buffer.readerIndex();
		return size;
	}
	
}
