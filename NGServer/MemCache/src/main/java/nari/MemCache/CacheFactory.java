package nari.MemCache;


public interface CacheFactory {
	
	public Cache createCache(Key cacheKey,int poolSize) throws Exception;
	
	public MemAllocater getMemAllocater() throws Exception;
	
	public Updater createUpdater(Cache cache) throws Exception;
}
