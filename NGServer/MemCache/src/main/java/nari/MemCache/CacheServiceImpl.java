package nari.MemCache;

import nari.MemCache.matcher.QueryMatcher;

public class CacheServiceImpl implements CacheService {

	private CacheTracker tracker;
	
	public CacheServiceImpl(CacheEngine engine) {
		try {
			tracker = engine.getTracker();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public CacheEntry search(String cacheId,QueryMatcher matcher) throws Exception {
		Cache cache = tracker.select(Key.val(cacheId));
		
		if(cache==null){
			return null;
		}
		return cache.search(matcher);
	}

}
