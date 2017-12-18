package net.transfer.client;

import java.io.IOException;
import java.io.OutputStream;

public class ChannelBufferOutputStream extends OutputStream {

//	private ByteBuffer buffer = null;
//	
//	private int writenBytes = 0;
	
	private byte[] arr;
	
	private int offset;
	
	private int index = 0;
	
//	public ChannelBufferOutputStream(ByteBuffer buffer){
//		this.buffer = buffer;
//		writenBytes = buffer.position();
//	}
	
	public ChannelBufferOutputStream(byte[] arr,int offset){
//		this.buffer = buffer;
//		writenBytes = buffer.position();
		this.arr = arr;
		this.offset = offset;
	}
	
	@Override
	public void write(int b) throws IOException {
//		buffer.put((byte)b);
		if(index==arr.length-1){
			byte[] newArr = new byte[arr.length*2];
			System.arraycopy(arr, 0, newArr, offset, arr.length);
			arr = newArr;
		}
		arr[offset+index] = (byte)b;
		index++;
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
//		buffer.writeBytes(b, off, len);
//		buffer.put(b, off, len);
		if(index+len==arr.length-1){
			byte[] newArr = new byte[arr.length+len];
			System.arraycopy(arr, 0, newArr, offset, arr.length);
			arr = newArr;
		}
		for(int i=off;i<len;i++){
			arr[offset+index] = b[i];
			index++;
		}
	}
	
	@Override
	public void write(byte[] b) throws IOException {
//		buffer.writeBytes(b);
//		buffer.put(b);
		if(index+b.length==arr.length-1){
			byte[] newArr = new byte[arr.length+b.length];
			System.arraycopy(arr, 0, newArr, offset, arr.length);
			arr = newArr;
		}
		for(int i=0;i<b.length;i++){
			arr[offset+index] = b[i];
			index++;
		}
	}
	
	public int writtenBytes(){
		return index-offset;
	}
}
