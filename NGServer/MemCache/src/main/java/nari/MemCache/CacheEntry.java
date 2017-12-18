package nari.MemCache;

public interface CacheEntry {

	public boolean hasNext() throws Exception;
	
	public int size() throws Exception;
	
	public Pointer next() throws Exception;
}
