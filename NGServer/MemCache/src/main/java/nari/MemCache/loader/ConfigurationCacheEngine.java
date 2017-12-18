package nari.MemCache.loader;

import java.io.InputStream;

import nari.Dao.interfaces.DbAdaptor;
import nari.MemCache.CacheConfig;
import nari.MemCache.CacheEngine;
import nari.MemCache.CacheLoader;
import nari.MemCache.ConfigurationCacheConfig;

public class ConfigurationCacheEngine extends CacheEngine {

	private String configName;
	
	private InputStream stream;
	
	private DbAdaptor db;
	
	private CacheConfig cacheConfig = null;
	
	public ConfigurationCacheEngine(String configName,InputStream stream,DbAdaptor db) {
		this.configName = configName;
		this.stream = stream;
		this.db = db;
	}
	
	@Override
	protected CacheLoader getLoader() throws Exception {
		return new ConfigurationCacheLoader(configName,stream,db,cacheConfig);
	}

	@Override
	protected CacheConfig getConfig() throws Exception {
		if(cacheConfig==null){
			cacheConfig = new ConfigurationCacheConfig();
		}
		return cacheConfig;
	}
	
}
