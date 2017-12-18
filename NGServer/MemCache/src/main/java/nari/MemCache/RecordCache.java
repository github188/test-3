package nari.MemCache;

public class RecordCache extends AbstractCache {

	private CacheFactory factory;
	
	private Key cacheKey;
	
	public RecordCache(Key cacheKey,int poolSize,CacheFactory factory) {
		super(poolSize);
		this.cacheKey = cacheKey;
		this.factory = factory;	
	}
	
	@Override
	protected HandlerChain getCacheHandlerChain() throws Exception {
		HandlerChain chain = new CacheHandlerChain();
		chain.addFirst("indexInitHandler", new CacheIndexInitHandler());
		chain.addFirst("recordInitHandler", new CacheRecordInitHandler());
		return chain;
	}

	@Override
	public CacheFactory getFactory() throws Exception {
		return factory;
	}
	
	@Override
	public Key getKey() throws Exception {
		return cacheKey;
	}
}
