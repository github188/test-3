package nari.MemCache;

import java.util.List;

public class DirectMemoryCacheLoader extends AbstractCacheLoader {

	@Override
	protected Cache[] loadCache(CacheFactory factory) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean doInit() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean doStart() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean doStop() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected CacheFactory getCacheFactory(List<CacheListener> listeners) throws Exception {
		return new DiskFlashCacheFactory();
	}

}
