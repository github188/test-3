package nari.MemCache;

public interface Pointer {
	
	public int getPointerId();

	public long getAddress() throws Exception;
	
	public long getSize() throws Exception;
	
	public MemoryBlock getMemBlock() throws Exception;
	
//	public void addFieldIndex(String fieldName,FieldIndex index) throws Exception;
	
	public void addIndexKey(String fieldName,Object key) throws Exception;
	
	public Object getIndexKey(String fieldName) throws Exception;
	
	public Object getFieldValue(String field,FieldType fieldType) throws Exception;
	
	public void setFieldValue(String field,FieldType fieldType,Object val) throws Exception;
	
//	public Pointer next(String indexField) throws Exception;
//	
//	public void setNext(String indexField,Pointer ptr) throws Exception;
//	
//	public Pointer prev(String indexField) throws Exception;
//	
//	public void setPrev(String indexField,Pointer ptr) throws Exception;
	
	public Object getCache() throws Exception;
	
//	public void setOriginalObj(Object obj) throws Exception;
//	
//	public Object getOriginalObj() throws Exception;
	
	public void release() throws Exception;
	
	public void addPoolPointer(PoolPointer ppt) throws Exception;
	
	public PoolPointer getPoolPointer(String field) throws Exception;
	
	public Cache getMemCache() throws Exception;
}
