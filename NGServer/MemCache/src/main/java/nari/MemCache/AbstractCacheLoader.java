package nari.MemCache;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCacheLoader implements CacheLoader {

	private List<CacheListener> listeners = new ArrayList<CacheListener>();
	
	public AbstractCacheLoader() {
		
	}
	
	public boolean init() throws Exception {
		doInit();
		return true;
	}

	public boolean start() throws Exception {
		doStart();
		return true;
	}

	public boolean stop() throws Exception {
		doStop();
		return true;
	}

	@Override
	public Cache[] load() throws Exception {
		return loadCache(getCacheFactory(listeners));
	}
	
	@Override
	public CacheFactory getFactory() throws Exception {
		return null;
	}
	
	@Override
	public void addCacheListener(CacheListener listener) throws Exception {
		listeners.add(listener);
	}
	
	protected abstract Cache[] loadCache(CacheFactory factory) throws Exception;
	
	protected abstract boolean doInit() throws Exception;
	
	protected abstract boolean doStart() throws Exception;
	
	protected abstract boolean doStop() throws Exception;
	
	protected abstract CacheFactory getCacheFactory(List<CacheListener> listeners) throws Exception;
}
