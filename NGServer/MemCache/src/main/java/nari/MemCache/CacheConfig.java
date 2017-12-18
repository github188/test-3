package nari.MemCache;

import java.util.List;

public interface CacheConfig {

	public void addCacheListener(CacheListener listener) throws Exception;
	
	public List<CacheListener> getCacheListeners() throws Exception;
}
