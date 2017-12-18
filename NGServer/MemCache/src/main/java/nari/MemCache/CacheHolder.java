package nari.MemCache;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CacheHolder {

	private final ConcurrentMap<Key,Cache> caches = new ConcurrentHashMap<Key,Cache>();
	
	public CacheHolder(){
		
	}
	
	public void addCache(Cache[] caches) throws Exception{
		for(Cache cache:caches){
			addCache(cache);
		}
	}
	
	public Cache addCache(Cache cache) throws Exception{
//		Cache wrapperCache = new CacheWrapper(cache,this);
//		caches.putIfAbsent(wrapperCache.getKey(), wrapperCache);
		caches.putIfAbsent(cache.getKey(), cache);
		return cache;
	}
	
	public Cache remove(Key cacheKey) throws Exception{
		return caches.remove(cacheKey);
	}
	
	public Cache take(Key cacheKey) throws Exception{
		return caches.get(cacheKey);
	}
	
	public Iterator<Cache> takeAll() throws Exception{
		return caches.values().iterator();
	}
	
	public Cache replace(Cache cache) throws Exception{
//		Cache wrapper = caches.replace(cache.getKey(), new CacheWrapper(cache,this));
		Cache wrapper = caches.replace(cache.getKey(), cache);
		return wrapper;
	}
	
	public boolean containsKey(Key cacheKey) throws Exception{
		return caches.containsKey(cacheKey);
	}
	
	public int getCacheCount() throws Exception{
		return caches.size();
	}
	
	public boolean shutdown() throws Exception{
		Cache cache = null;
		for(Map.Entry<Key, Cache> entry:caches.entrySet()){
			cache = caches.remove(entry.getKey());
			if(cache!=null){
				cache.stop();
			}
		}
		return true;
	}
	
}
