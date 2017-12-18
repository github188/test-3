package net.transfer.client;

import java.io.IOException;

public interface DataOutput {

	public void writeBool(boolean v) throws IOException;

	public void writeByte(byte v) throws IOException;

	public void writeShort(short v) throws IOException;

	public void writeInt(int v) throws IOException;

	public void writeLong(long v) throws IOException;

	public void writeFloat(float v) throws IOException;

	public void writeDouble(double v) throws IOException;

	public void writeUTF(String v) throws IOException;

	public void writeBytes(byte[] v) throws IOException;

	public void writeBytes(byte[] v, int off, int len) throws IOException;

	public void flushBuffer() throws IOException;
	
}
