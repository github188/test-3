package nari.MemCache;

public interface FieldIndex extends CacheLifecycle {

	public void add(Object indexKey,Pointer ptr) throws Exception;
	
//	public void add(Object indexKey,int ptrId) throws Exception;
	
	public void modify(String keyField,Object indexKey,Pointer ptr) throws Exception;
	
	public void remove(String keyField,Object indexKey,Pointer ptr) throws Exception;
	
	public Pointer[] get(Object indexKey) throws Exception;
	
	public Pointer[] get(Object indexKey,boolean precise) throws Exception;
	
	public String getIndexField() throws Exception;
	
	public IndexType getIndexType() throws Exception;
	
	public FieldType getFieldType() throws Exception;
	
	public void persistence() throws Exception;
}
