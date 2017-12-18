package nari.MemCache;

public class MemoryCacheFactory extends AbstractCacheFactory {

	@Override
	protected Cache doCreate(Key cacheKey,int poolSize) throws Exception {
		return new DirectMemoryCache(cacheKey,poolSize,this);
	}

	@Override
	public MemAllocater getMemAllocater() throws Exception {
		return new UnsafeMemAllocater();
	}

	@Override
	protected Updater doCreateUpdater(Cache cache) throws Exception {
		return new MessageDrivenUpdater(cache);
	}

}
