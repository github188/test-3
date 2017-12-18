package nari.MapService;

import nari.MemCache.CacheConfig;
import nari.MemCache.CacheEngine;
import nari.MemCache.CacheLoader;

public class StdCacheEngine extends CacheEngine {

	public StdCacheEngine() {
		
	}
	
	@Override
	protected CacheLoader getLoader() throws Exception {
		return new RecordCacheLoader();
	}

	@Override
	protected CacheConfig getConfig() throws Exception {
		return null;
	}

}
