package nari.MemCache;

public interface CacheListener {

	public void onCreate(Key cacheKey,Cache cache) throws Exception;
}
