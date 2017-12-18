package nari.MemCache;

public class MemCacheTracker extends AbstractCacheTracker {

	private CacheFactory factory;
	
	public MemCacheTracker(CacheHolder holder,CacheFactory factory) {
		super(holder);
		this.factory = factory;
	}

	@Override
	protected CacheFactory getCacheFactory() throws Exception {
		return factory;
	}
}
