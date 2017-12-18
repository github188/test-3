package net.transfer.client;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ChannelBufferInputStream extends InputStream {

	private ByteBuffer buffer = null;
	
	private int readIndex = 0;
	
	public ChannelBufferInputStream(ByteBuffer buffer,int len){
		this.buffer = buffer;
		readIndex = buffer.position();
	}
	
	@Override
	public int read() throws IOException {
		return (byte)buffer.get();
	}
	
	@Override
	public int read(byte[] b) throws IOException {
		buffer.get(b);
		int size = buffer.position() - readIndex;
		readIndex = buffer.position();
		return size;
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int i = buffer.remaining();
		int l = len<i?len:i;
		buffer.get(b, off, l);
		int size = buffer.position() - readIndex;
		readIndex = buffer.position();
		return size;
	}
	
}
