package org.Invoker.remoting.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public interface ChannelBuffer {

	public int capacity();

	public void clear();

	public ChannelBuffer copy();

	public ChannelBuffer copy(int index, int length);

    public byte getByte(int index);

    public void getBytes(int index, byte[] dst);

    public void getBytes(int index, byte[] dst, int dstIndex, int length);

    public void getBytes(int index, ChannelBuffer dst);

    public void getBytes(int index, ChannelBuffer dst, int length);

    public void getBytes(int index, ChannelBuffer dst, int dstIndex, int length);

    public void markReaderIndex();

    public void markWriterIndex();

    public boolean readable();

    public int readableBytes();

    public byte readByte();

    public void readBytes(byte[] dst);

    public void readBytes(byte[] dst, int dstIndex, int length);

    public void readBytes(ChannelBuffer dst);

    public void readBytes(ChannelBuffer dst, int length);

    public void readBytes(ChannelBuffer dst, int dstIndex, int length);

    public ChannelBuffer readBytes(int length);

    public void resetReaderIndex();

    public void resetWriterIndex();

    public int readerIndex();

    public void readerIndex(int readerIndex);

    public void readBytes(OutputStream dst, int length) throws IOException;

    public void setByte(int index, int value);

    public void setBytes(int index, byte[] src);

    public void setBytes(int index, byte[] src, int srcIndex, int length);

    public void setBytes(int index, ChannelBuffer src);

    public void setBytes(int index, ChannelBuffer src, int length);

    public void setBytes(int index, ChannelBuffer src, int srcIndex, int length);

    public int setBytes(int index, InputStream src, int length) throws IOException;

    public void setIndex(int readerIndex, int writerIndex);

    public void skipBytes(int length);

    public ByteBuffer toByteBuffer();

    public ByteBuffer toByteBuffer(int index, int length);

    public boolean writable();

    public int writableBytes();

    public void writeByte(int value);

    public void writeBytes(byte[] src);

    public void writeBytes(byte[] src, int index, int length);

    public void writeBytes(ByteBuffer src);

    public void writeBytes(ChannelBuffer src);

    public void writeBytes(ChannelBuffer src, int length);

    public void writeBytes(ChannelBuffer src, int srcIndex, int length);

    public int writeBytes(InputStream src, int length) throws IOException;

    public int writerIndex();

    public void writerIndex(int writerIndex);

    public byte[] array();

    public boolean hasArray();

    public int arrayOffset();
}
