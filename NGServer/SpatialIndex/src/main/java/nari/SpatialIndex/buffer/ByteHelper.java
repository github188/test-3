package nari.SpatialIndex.buffer;

import java.nio.ByteBuffer;

public class ByteHelper {

	public static double getDouble(byte[] bytes){
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		return buf.getDouble();
	}
	
	public static int getInt(byte[] bytes){
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		return buf.getInt();
	}
	
	public static String getString(byte[] bytes){
		return new String(bytes);
	}
	
	public static byte[] toByte(int data){
		ByteBuffer buf = ByteBuffer.allocate(4);
		buf.putInt(data);
		buf.flip();
		return buf.array();
	}
	
	public static byte[] toByte(long data){
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putLong(data);
		buf.flip();
		return buf.array();
	}

	public static byte[] toByte(double data){
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.putDouble(data);
		buf.flip();
		return buf.array();
	}
	
	public static byte[] toByte(short data){
		ByteBuffer buf = ByteBuffer.allocate(2);
		buf.putShort(data);
		buf.flip();
		return buf.array();
	}
	
	public static byte[] toByte(float data){
		ByteBuffer buf = ByteBuffer.allocate(4);
		buf.putFloat(data);
		buf.flip();
		return buf.array();
	}
	
}
