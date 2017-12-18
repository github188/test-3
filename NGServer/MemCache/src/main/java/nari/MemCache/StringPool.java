package nari.MemCache;

public interface StringPool {

	public PoolPointer addPool(String field,byte[] data) throws Exception;
	
	public boolean hasSpace(long size) throws Exception;
	
	public long size() throws Exception;
}
