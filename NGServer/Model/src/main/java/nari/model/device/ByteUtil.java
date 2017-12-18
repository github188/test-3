package nari.model.device;

public class ByteUtil {
	
	public static byte[] getBytes(short data) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte)(data & 0xFF);
		bytes[1] = (byte)((data & 0xFF00) >> 8);
		return bytes;
	}
	
	public static byte[] getBytes(char data) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte)data;
		bytes[1] = (byte)(data >> '\b');
		return bytes;
	}
	
	public static byte[] getBytes(int data) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte)(data & 0xFF);
		bytes[1] = (byte)((data & 0xFF00) >> 8);
		bytes[2] = (byte)((data & 0xFF0000) >> 16);
		bytes[3] = (byte)((data & 0xFF000000) >> 24);
		return bytes;
	}
	
	public static byte[] getBytes(long data) {
		byte[] bytes = new byte[8];
		bytes[0] = (byte)(int)(data & 0xFF);
		bytes[1] = (byte)(int)(data >> 8 & 0xFF);
		bytes[2] = (byte)(int)(data >> 16 & 0xFF);
		bytes[3] = (byte)(int)(data >> 24 & 0xFF);
		bytes[4] = (byte)(int)(data >> 32 & 0xFF);
		bytes[5] = (byte)(int)(data >> 40 & 0xFF);
		bytes[6] = (byte)(int)(data >> 48 & 0xFF);
		bytes[7] = (byte)(int)(data >> 56 & 0xFF);
		return bytes;
	}
	
	public static byte[] getBytes(float data) {
		int intBits = Float.floatToIntBits(data);
		return getBytes(intBits);
	}
	
	public static byte[] getBytes(double data) {
		long intBits = Double.doubleToLongBits(data);
		return getBytes(intBits);
	}
	
	public static short getShort(byte[] bytes) {
		return (short)(0xFF & bytes[0] | 0xFF00 & bytes[1] << 8);
	}
	
	public static char getChar(byte[] bytes) {
		return (char)(0xFF & bytes[0] | 0xFF00 & bytes[1] << 8);
	}
	
	public static int getInt(byte[] bytes) {
		return 0xFF & bytes[0] | 0xFF00 & bytes[1] << 8 | 0xFF0000 & bytes[2] << 16 | 0xFF000000 & bytes[3] << 24;
	}
	
	public static long getLong(byte[] bytes) {
		return 0xFF & bytes[0] | 0xFF00 & bytes[1] << 8 | 0xFF0000 & bytes[2] << 16 | 0xFF000000 & bytes[3] << 24 | 0x0 & bytes[4] << 32 | 0x0 & bytes[5] << 40 | 0x0 & bytes[6] << 48 | 0x0 & bytes[7] << 56;
	}
	
	public static float getFloat(byte[] bytes) {
		return Float.intBitsToFloat(getInt(bytes));
	}
	
	public static double getDouble(byte[] bytes) {
		long l = getLong(bytes);
		return Double.longBitsToDouble(l);
	}
	
	public static void main(String[] args) {
		System.out.println(getLong(new byte[]{'A','9','7','C','4','0','0','3','0','0','0','0','1','0','0','0'}));
	}
}