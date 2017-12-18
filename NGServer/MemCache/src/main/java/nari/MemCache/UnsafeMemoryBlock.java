package nari.MemCache;

import java.lang.reflect.Field;

public class UnsafeMemoryBlock implements MemoryBlock {

	private MemAllocater allocater;
	
//	private AtomicLong offset = new AtomicLong(0);
	
	private long offset = 0;
	
	private long address;
	
	private long size;
	
//	private final UnusedPointer unused = new UnusedPointer();
	
	public UnsafeMemoryBlock(MemAllocater allocater,long size) {
		this.allocater = allocater;
		this.size = size;
		try {
			this.address = allocater.allocateMemory(size);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("fail to allocate more memory of size "+size, e);
		}
	}
	
	@Override
	public long getBlockSize() throws Exception {
		return size;
	}

	@Override
	public long getMemAddress() throws Exception {
		return address;
	}

	@Override
	public long position() throws Exception {
		return offset;
	}

	@Override
	public boolean hasSpace(long size) throws Exception {
		return this.size - offset<size?false:true;
	}

	@Override
	public long space() throws Exception {
		return size;
	}

	@Override
	public long freeSpace() throws Exception {
		return size - offset;
	}

//	@Override
//	public Pointer putCache(int id,Object obj,CacheTicket ticket) throws Exception {
//		Pointer ptr = allocater.putCache(obj, address+offset);
//		
//		offset = offset + ptr.getSize();
//		
//		unused.use(ptr);
//		return new WrapperMemPointer(id,ptr,this,ticket.getCacheBodyClass(),ticket.getCacheBodyFields(),allocater);
//	}
	
	@Override
	public Pointer putCache(int id,Object obj,Cache cahce) throws Exception {
		Pointer ptr = allocater.putCache(obj, address+offset);
		
		offset = offset + ptr.getSize();
		
//		unused.use(ptr);
		return new WrapperMemPointer(id,ptr,this,cahce,allocater);
	}

	@Override
	public UnusedPointer getUnusedMem() throws Exception {
		return null;
	}

	@Override
	public Object getCache(Pointer ptr) throws Exception {
		return allocater.getCache(ptr.getAddress());
	}

	@Override
	public void putInt(Object obj, String field, int value) throws Exception {
		long offset = allocater.getOffset(obj, field);
		allocater.putIntVolatile(obj, offset, value);
	}

	@Override
	public void putLong(Object obj, String field, long value) throws Exception {
		long offset = allocater.getOffset(obj, field);
		allocater.putLongVolatile(obj, offset, value);
	}

	@Override
	public void putDouble(Object obj, String field, double value)throws Exception {
		long offset = allocater.getOffset(obj, field);
		allocater.putDoubleVolatile(obj, offset, value);
	}

	@Override
	public void putFloat(Object obj, String field, float value) throws Exception {
		long offset = allocater.getOffset(obj, field);
		allocater.putFloatVolatile(obj, offset, value);
	}

	@Override
	public void putShort(Object obj, String field, short value) throws Exception {
		long offset = allocater.getOffset(obj, field);
		allocater.putShortVolatile(obj, offset, value);
	}

	@Override
	public void putBoolean(Object obj, String field, boolean value) throws Exception {
		long offset = allocater.getOffset(obj, field);
		allocater.putBooleanVolatile(obj, offset, value);
	}

	@Override
	public void putObject(Object obj, String field, Object value) throws Exception {
		long offset = allocater.getOffset(obj, field);
		allocater.putObjectVolatile(obj, offset, value);
	}
	
	@Override
	public void putString(String str, String field, String value) throws Exception {
		long offset = allocater.getOffset(str, "value");
		if(value==null){
			allocater.putObjectVolatile(str, offset, null);
		}else{
			char[] c = value.toCharArray();
			allocater.putObjectVolatile(str, offset, c);
		}
	}

	@Override
	public int getInt(Object obj,String field) throws Exception {
		long offset = allocater.getOffset(obj, field);
		return allocater.getIntVolatile(obj, offset);
	}

	@Override
	public long getLong(Object obj,String field) throws Exception {
		long offset = allocater.getOffset(obj, field);
		return allocater.getLongVolatile(obj, offset);
	}

	@Override
	public double getDouble(Object obj,String field) throws Exception {
		long offset = allocater.getOffset(obj, field);
		return allocater.getDoubleVolatile(obj, offset);
	}

	@Override
	public float getFloat(Object obj,String field) throws Exception {
		long offset = allocater.getOffset(obj, field);
		return allocater.getFloatVolatile(obj, offset);
	}

	@Override
	public short getShort(Object obj,String field) throws Exception {
		long offset = allocater.getOffset(obj, field);
		return allocater.getShortVolatile(obj, offset);
	}

	@Override
	public boolean getBoolean(Object obj,String field) throws Exception {
		long offset = allocater.getOffset(obj, field);
		return allocater.getBooleanVolatile(obj, offset);
	}

	@Override
	public Object getObject(Object obj, String field) throws Exception {
		long offset = allocater.getOffset(obj, field);
		return allocater.getObjectVolatile(obj, offset);
	}

	@Override
	public String getString(Object obj, String field) throws Exception {
		long offset = allocater.getOffset(obj, field);
		return (String)allocater.getObjectVolatile(obj, offset);
	}

	@Override
	public void releaseObject(Pointer ptr) throws Exception {
//		unused.free(ptr);
	}

	@Override
	public int getInt(Object obj, Field field) throws Exception {
		long offset = allocater.getOffset(obj, field);
		return allocater.getIntVolatile(obj, offset);
	}

	@Override
	public long getLong(Object obj, Field field) throws Exception {
		long offset = allocater.getOffset(obj, field);
		return allocater.getLongVolatile(obj, offset);
	}

	@Override
	public double getDouble(Object obj, Field field) throws Exception {
		long offset = allocater.getOffset(obj, field);
		return allocater.getDoubleVolatile(obj, offset);
	}

	@Override
	public float getFloat(Object obj, Field field) throws Exception {
		long offset = allocater.getOffset(obj, field);
		return allocater.getFloatVolatile(obj, offset);
	}

	@Override
	public short getShort(Object obj, Field field) throws Exception {
		long offset = allocater.getOffset(obj, field);
		return allocater.getShortVolatile(obj, offset);
	}

	@Override
	public boolean getBoolean(Object obj, Field field) throws Exception {
		long offset = allocater.getOffset(obj, field);
		return allocater.getBooleanVolatile(obj, offset);
	}

	@Override
	public Object getObject(Object obj, Field field) throws Exception {
		long offset = allocater.getOffset(obj, field);
		return allocater.getObjectVolatile(obj, offset);
	}

	@Override
	public String getString(Object obj, Field field) throws Exception {
		long offset = allocater.getOffset(obj, field);
		return (String)allocater.getObjectVolatile(obj, offset);
	}

}
