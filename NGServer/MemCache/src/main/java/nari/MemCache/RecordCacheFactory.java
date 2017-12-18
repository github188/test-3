package nari.MemCache;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class RecordCacheFactory extends AbstractCacheFactory {

	private final AtomicReference<MemAllocater> ref = new AtomicReference<MemAllocater>(null);
	
	private List<CacheListener> listeners = null;
	
	public RecordCacheFactory() {
		
	}
	
	public RecordCacheFactory(List<CacheListener> listeners) {
		this.listeners = listeners;
	}
	
	@Override
	protected Cache doCreate(Key cacheKey,int poolSize) throws Exception {
		Cache cache = new RecordCache(cacheKey,poolSize,this);
		if(listeners!=null){
			for(CacheListener listener:listeners){
				listener.onCreate(cacheKey, cache);
			}
		}
		return cache;
	}

	@Override
	public MemAllocater getMemAllocater() throws Exception {
		if(ref.get()==null){
			MemAllocater allocater = new UnsafeMemAllocater();
			ref.compareAndSet(null, allocater);
		}
		return ref.get();
	}

	@Override
	protected Updater doCreateUpdater(Cache cache) throws Exception {
		return new MessageDrivenUpdater(cache);
	}

}
