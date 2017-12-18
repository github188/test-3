package nari.MemCache;

import java.lang.reflect.Field;

public interface MemAllocater {
	
	public long allocateMemory(long size) throws Exception;
	
	public void freeMemory(long address) throws Exception;
	
	public long sizeOf(Object obj) throws Exception;
	
	public Pointer putCache(Object cacheObject,long address) throws Exception;
	
	public Object getCache(long address) throws Exception;
	
	public long getOffset(Object obj,String field) throws Exception;
	
	public long getOffset(Object obj,Field field) throws Exception;
	
	public int getIntVolatile(Object obj, long offset) throws Exception;
	
	public long getLongVolatile(Object obj, long offset) throws Exception;
	
	public double getDoubleVolatile(Object obj, long offset) throws Exception;
	
	public short getShortVolatile(Object obj, long offset) throws Exception;
	
	public float getFloatVolatile(Object obj, long offset) throws Exception;
	
	public boolean getBooleanVolatile(Object obj, long offset) throws Exception;
	
	public byte getByteVolatile(Object obj, long offset) throws Exception;
	
	public char getCharVolatile(Object obj, long offset) throws Exception;
	
	public Object getObjectVolatile(Object obj, long offset) throws Exception;
	
	public void putBooleanVolatile(Object obj, long offset, boolean value) throws Exception;
	
	public void putIntVolatile(Object obj, long offset, int value) throws Exception;
	
	public void putLongVolatile(Object obj, long offset, long value) throws Exception;
	
	public void putDoubleVolatile(Object obj, long offset, double value) throws Exception;
	
	public void putShortVolatile(Object obj, long offset, short value) throws Exception;
	
	public void putFloatVolatile(Object obj, long offset, float value) throws Exception;
	
	public void putByteVolatile(Object obj, long offset, byte value) throws Exception;
	
	public void putCharVolatile(Object obj, long offset, char value) throws Exception;
	
	public void putObjectVolatile(Object obj, long offset, Object value) throws Exception;
	
	public void putByte(long address, byte value) throws Exception;
	
	public byte getByte(long address) throws Exception;
}
