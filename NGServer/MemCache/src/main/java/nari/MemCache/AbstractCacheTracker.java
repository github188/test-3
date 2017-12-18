package nari.MemCache;

import java.util.Iterator;

public abstract class AbstractCacheTracker implements CacheTracker {

	private CacheHolder holder = null;
	
	public AbstractCacheTracker(CacheHolder holder) {
		this.holder = holder;
	}
	
	@Override
	public Cache select(Key cacheKey) throws Exception {
		return holder.take(cacheKey);
	}

	@Override
	public Cache[] selectAll() throws Exception {
		Iterator<Cache> it = holder.takeAll();
		if(it==null){
			return null;
		}
		
		Cache[] array = new Cache[holder.getCacheCount()];
		int i=0;
		while(it.hasNext()){
			array[i++] = it.next();
		}
		return array;
	}

	@Override
	public Cache createCache(Key cacheKey,int poolSize) throws Exception {
		if(holder.containsKey(cacheKey)){
			throw new IllegalArgumentException("cache exist");
		}
		Cache cache = getCacheFactory().createCache(cacheKey,poolSize);
		
		return holder.addCache(cache);
	}
	
	protected abstract CacheFactory getCacheFactory() throws Exception;
}
