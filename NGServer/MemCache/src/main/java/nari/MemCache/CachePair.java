package nari.MemCache;

public interface CachePair {

	public void addKeyValuePair(String key,Object value,IndexType indexType) throws Exception;
	
	public Object getValue(String key) throws Exception;
	
	public String[] keys() throws Exception;
	
	public boolean containsKey(String key) throws Exception;
	
	public IndexType getIndexType(String key) throws Exception;
}
