package nari.MemCache;

public interface IndexCluster {

	public void register(String indexField, IndexType indexType,FieldType fieldType,PointerCluster ptrCluster) throws Exception;
	
	public void add(String indexField,Pointer ptr) throws Exception;
	
	public String[] getIndexFields() throws Exception;
	
	public FieldIndex getIndex(String indexField) throws Exception;
	
	public void modify(Pointer ptr,Value value) throws Exception;
	
	public void remove(Pointer ptr) throws Exception;
	
	public PointerCluster getPointerCluster() throws Exception ;
}
