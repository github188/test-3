package nari.MemCache;

import nari.MemCache.matcher.QueryMatcher;

public interface CacheService {

	public CacheEntry search(String cacheId,QueryMatcher matcher) throws Exception;
}
