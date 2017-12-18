package nari.MemCache;

public interface CacheTracker {

	public Cache select(Key cacheKey) throws Exception;
	
	public Cache[] selectAll() throws Exception;
	
	public Cache createCache(Key cacheKey,int poolSize) throws Exception;
}
