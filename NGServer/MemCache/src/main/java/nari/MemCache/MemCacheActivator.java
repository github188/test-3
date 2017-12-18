//package nari.MemCache;
//
//import java.util.concurrent.atomic.AtomicReference;
//
//import nari.Dao.interfaces.DbAdaptor;
//
//import com.application.plugin.Activator;
//import com.application.plugin.ActivatorReg;
//import com.application.plugin.AttributeKey;
//import com.application.plugin.BundleContext;
//import com.application.plugin.Provider;
//import com.application.plugin.Version;
//import com.application.plugin.bundle.BundleConfig;
//import com.application.plugin.bundle.BundleException;
//import com.application.plugin.service.ServiceFilter;
//import com.application.plugin.service.ServiceReference;
//import com.application.plugin.tracker.TrackerListener;
//
//@ActivatorReg(name="MemCacheActivator")
//public class MemCacheActivator implements Activator,TrackerListener{
//
//	public static DbAdaptor dbAdaptor = DbAdaptor.NONE;
//	
//	private CacheEngine engine;
//	
//	public MemCacheActivator() {
//
//	}
//	
//	@Override
//	public boolean init(BundleConfig context) throws BundleException {
//		context.getServiceTracker().track(DbAdaptor.class, this);
//		return true;
//	}
//
//	@Override
//	public boolean start(BundleContext context) throws BundleException {
//		return true;
//	}
//
//	@Override
//	public boolean stop(BundleContext context) throws BundleException {
//		try {
//			if(engine!=null){
//				engine.stop();
//			}
//		} catch (Exception e) {
//			throw new BundleException(e.getMessage(),e);
//		}
//		return true;
//	}
//
//	@Override
//	public <T> void serviceAdd(ServiceReference<T> ref,BundleContext context) throws BundleException {
//		T dbins = (T)ref.get();
//		if(dbins instanceof DbAdaptor){
//			dbAdaptor = (DbAdaptor)dbins;
//			
//			engine = new StdCacheEngine();
//			
//			try {
//				if(engine.init()){
//					engine.start();
//				}
//			} catch (Exception e) {
//				throw new BundleException(e.getMessage(),e);
//			}
//			
//			context.registService(CacheService.class, new Provider<CacheService>() {
//				
//				private final AtomicReference<CacheService> ref = new AtomicReference<CacheService>(null);
//				
//				@Override
//				public CacheService get() throws BundleException {
//					if(ref.get()==null){
//						CacheService cacheService = new CacheServiceImpl(engine);
//						ref.compareAndSet(null, cacheService);
//					}
//					return ref.get();
//				}
//
//				@Override
//				public AttributeKey getKey() throws BundleException {
//					return AttributeKey.key(CacheService.class, Version.defaultVersion());
//				}
//
//				@Override
//				public Version version() throws BundleException {
//					return Version.defaultVersion();
//				}
//
//				@Override
//				public ServiceFilter[] getFilter() throws BundleException {
//					return null;
//				}
//			});
//		}
//		
//	}
//
//	@Override
//	public <T> void serviceModify(ServiceReference<T> ref,BundleContext context) throws BundleException {
//		T dbins = (T)ref.get();
//		if(dbins instanceof DbAdaptor){
//			dbAdaptor = (DbAdaptor)dbins;
//		}
//	}
//
//	@Override
//	public <T> void serviceRemove(ServiceReference<T> ref,BundleContext context) throws BundleException {
//		T dbins = (T)ref.get();
//		if(dbins instanceof DbAdaptor){
//			dbAdaptor = (DbAdaptor)dbins;
//		}
//	}
//
//}
