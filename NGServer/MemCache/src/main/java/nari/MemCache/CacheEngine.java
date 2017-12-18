package nari.MemCache;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public abstract class CacheEngine {

	private final AtomicReference<CacheLoader> loaderRef = new AtomicReference<CacheLoader>(null);
	
	private CacheHolder holder = null;
	
	private final AtomicReference<CacheTracker> tracker = new AtomicReference<CacheTracker>(null);
	
	private final AtomicBoolean init = new AtomicBoolean(false);
	
	private final AtomicBoolean start = new AtomicBoolean(false);
	
	private final AtomicBoolean stop = new AtomicBoolean(false);
	
	public CacheEngine() {
		
	}
	
	public boolean init() throws Exception{
		if(init.get()){
			throw new RuntimeException("engine has been init");
		}
		if(holder==null){
			holder = new CacheHolder();
		}
		
		CacheLoader loader = getCacheLoader();
		if(loader!=null){
			loader.init();
		}
		
		init.compareAndSet(false, true);
		return true;
	}
	
	public boolean start() throws Exception{
		if(start.get()){
			throw new RuntimeException("engine has been start");
		}
		
		CacheLoader loader = getCacheLoader();
		if(loader!=null){
			loader.start();
			
			Cache[] caches = loader.load();
			
			if(caches!=null && caches.length>0){
				holder.addCache(caches);
			}
		}
		
		start.compareAndSet(false, true);
		return true;
	}

	public boolean stop() throws Exception{
		if(stop.get()){
			throw new RuntimeException("engine has been stop");
		}
		
		CacheLoader loader = getCacheLoader();
		if(loader!=null){
			loader.stop();
		}
		
		holder.shutdown();
		stop.compareAndSet(false, true);
		return true;
	}
	
	private CacheLoader getCacheLoader() throws Exception{
		if(loaderRef.get()==null){
			CacheLoader loader = getLoader();
			loaderRef.compareAndSet(null, loader);
		}
		return loaderRef.get();
	}
	
	public boolean isInit() throws Exception{
		return init.get();
	}
	
	public boolean isStart() throws Exception{
		return start.get();
	}
	
	public boolean isStop() throws Exception{
		return stop.get();
	}
	
	public CacheTracker getTracker() throws Exception{
		if(tracker.get()==null){
			CacheTracker sel = new MemCacheTracker(holder,new MemoryCacheFactory());
			tracker.compareAndSet(null, sel);
		}
		return tracker.get();
	}
	
	public CacheConfig getCacheConfig() throws Exception{
		return getConfig();
	}
	
	protected abstract CacheLoader getLoader() throws Exception;
	
	protected abstract CacheConfig getConfig() throws Exception;
}
