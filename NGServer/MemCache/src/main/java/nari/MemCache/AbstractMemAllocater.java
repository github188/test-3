package nari.MemCache;

import java.lang.reflect.Field;

public abstract class AbstractMemAllocater implements MemAllocater {

	@Override
	public long allocateMemory(long size) throws Exception {
		return allocateDirectMem(size);
	}
	
	@Override
	public void freeMemory(long address) throws Exception {
		freeDirectMem(address);
	}
	
	@Override
	public long sizeOf(Object obj) throws Exception {
		return getObjectSize(obj);
	}
	
	@Override
	public Pointer putCache(Object cacheObject, long address) throws Exception {
		return doPutCache(cacheObject,address);
	}
	
	@Override
	public Object getCache(long address) throws Exception {
		return doGetCache(address);
	}
	
	@Override
	public long getOffset(Object obj, String field) throws Exception {
		return getObjectOffset(obj,field);
	}
	
	@Override
	public long getOffset(Object obj, Field field) throws Exception {
		return getObjectOffset(obj,field);
	}
	
	protected abstract long getObjectOffset(Object obj, String field) throws Exception;
	
	protected abstract long getObjectOffset(Object obj, Field field) throws Exception;
	
	protected abstract long getObjectSize(Object obj) throws Exception;
	
	protected abstract long allocateDirectMem(long size) throws Exception;
	
	protected abstract void freeDirectMem(long address) throws Exception;
	
	public abstract Pointer doPutCache(Object cacheObject, long address) throws Exception;
	
	public abstract Object doGetCache(long address) throws Exception;
}
