package nari.MemCache;

public interface CacheLoader extends CacheLifecycle {

	public Cache[] load() throws Exception;
	
	public CacheFactory getFactory() throws Exception;
	
	public void addCacheListener(CacheListener listener) throws Exception;
}
