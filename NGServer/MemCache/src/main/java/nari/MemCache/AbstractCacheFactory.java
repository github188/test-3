package nari.MemCache;


public abstract class AbstractCacheFactory implements CacheFactory {

	public AbstractCacheFactory() {
		
	}
	
	@Override
	public Cache createCache(Key cacheKey,int poolSize) throws Exception {
		Cache cache = doCreate(cacheKey,poolSize);
		
		cache.init();
		cache.start();
		return cache;
	}

	@Override
	public Updater createUpdater(Cache cache) throws Exception {
		return doCreateUpdater(cache);
	}
	
	protected abstract Cache doCreate(Key cacheKey,int poolSize) throws Exception;
	
	protected abstract Updater doCreateUpdater(Cache cache) throws Exception;
}
