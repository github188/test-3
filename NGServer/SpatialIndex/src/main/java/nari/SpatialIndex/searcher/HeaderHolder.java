package nari.SpatialIndex.searcher;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class HeaderHolder {

//	private static final AtomicReference<HeaderHolder> holder = new AtomicReference<HeaderHolder>();
	
//	private final static ConcurrentMap<String,HeaderHolder> holder = new ConcurrentHashMap<String,HeaderHolder>();
	
	private final ConcurrentHashMap<String,LayerHeader> cache = new ConcurrentHashMap<String,LayerHeader>();
	
//	private final ReadWriteLock r= new ReentrantReadWriteLock();
//	
//	private final Lock readLock = r.readLock();
//	
//	private final Lock writeLock = r.writeLock();
	
	private volatile static AtomicReference<HeaderHolder> ref = new AtomicReference<HeaderHolder>();
	
	
	public static HeaderHolder get(/**String holderId**/){
		if(ref.get()==null){
			HeaderHolder hh = new HeaderHolder();
			ref.compareAndSet(null, hh);
		}
		return ref.get();
	}
	
	public void put(String layerId,LayerHeader header) throws Exception{
		synchronized (cache) {
			cache.putIfAbsent(layerId, header);
		}
	}
	
	public LayerHeader getHeader(String layerId) throws Exception{
		synchronized (cache) {
			return cache.get(layerId);
		}
	}
}
