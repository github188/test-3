package nari.MemCache;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationCacheConfig implements CacheConfig {

	private List<CacheListener> listeners = new ArrayList<CacheListener>();
	
	@Override
	public void addCacheListener(CacheListener listener) throws Exception {
		listeners.add(listener);
	}

	@Override
	public List<CacheListener> getCacheListeners() throws Exception {
		return listeners;
	}

}
