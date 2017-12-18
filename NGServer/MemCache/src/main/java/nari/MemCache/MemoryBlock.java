package nari.MemCache;

import java.lang.reflect.Field;

public interface MemoryBlock {

	public long getBlockSize() throws Exception;
	
	public long getMemAddress() throws Exception;
	
	public long position() throws Exception;
	
	public boolean hasSpace(long size) throws Exception;
	
	public long space() throws Exception;
	
	public long freeSpace() throws Exception;
	
//	public Pointer putCache(int id,Object obj,CacheTicket ticket) throws Exception;
	
	public Pointer putCache(int id,Object obj,Cache cache) throws Exception;
	
	public Object getCache(Pointer ptr) throws Exception;
	
	public void putInt(Object obj,String field,int value) throws Exception;
	
	public void putLong(Object obj,String field,long value) throws Exception;
	
	public void putDouble(Object obj,String field,double value) throws Exception;
	
	public void putFloat(Object obj,String field,float value) throws Exception;
	
	public void putShort(Object obj,String field,short value) throws Exception;
	
	public void putBoolean(Object obj,String field,boolean value) throws Exception;
	
	public void putObject(Object obj,String field,Object value) throws Exception;
	
	public void putString(String obj,String field,String value) throws Exception;
	
	public int getInt(Object obj,String field) throws Exception;
	
	public long getLong(Object obj,String field) throws Exception;
	
	public double getDouble(Object obj,String field) throws Exception;
	
	public float getFloat(Object obj,String field) throws Exception;
	
	public short getShort(Object obj,String field) throws Exception;
	
	public boolean getBoolean(Object obj,String field) throws Exception;
	
	public Object getObject(Object obj,String field) throws Exception;
	
	public String getString(Object obj,String field) throws Exception;
	
	public int getInt(Object obj,Field field) throws Exception;
	
	public long getLong(Object obj,Field field) throws Exception;
	
	public double getDouble(Object obj,Field field) throws Exception;
	
	public float getFloat(Object obj,Field field) throws Exception;
	
	public short getShort(Object obj,Field field) throws Exception;
	
	public boolean getBoolean(Object obj,Field field) throws Exception;
	
	public Object getObject(Object obj,Field field) throws Exception;
	
	public String getString(Object obj,Field field) throws Exception;
	
	public UnusedPointer getUnusedMem() throws Exception;
	
	public void releaseObject(Pointer ptr) throws Exception;
}
