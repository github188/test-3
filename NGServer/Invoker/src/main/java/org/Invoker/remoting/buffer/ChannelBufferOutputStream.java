package org.Invoker.remoting.buffer;

import java.io.IOException;
import java.io.OutputStream;

public class ChannelBufferOutputStream extends OutputStream {

	private ChannelBuffer buffer = null;
	
	private int writenBytes = 0;
	
	public ChannelBufferOutputStream(ChannelBuffer buffer){
		this.buffer = buffer;
		writenBytes = buffer.writerIndex();
	}
	
	@Override
	public void write(int b) throws IOException {
		buffer.writeByte((byte)b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		buffer.writeBytes(b, off, len);
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		buffer.writeBytes(b);
	}
	
	public int writtenBytes(){
		return buffer.writerIndex() - writenBytes;
	}
}
