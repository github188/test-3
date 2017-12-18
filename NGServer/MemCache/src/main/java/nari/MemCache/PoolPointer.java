package nari.MemCache;

public interface PoolPointer {

	public int size() throws Exception;
	
	public long address() throws Exception;
	
	public String getFieldName() throws Exception;
}
