package nari.Obsevation;

import nari.Dao.interfaces.DbAdaptor;
import nari.MemCache.Cache;
import nari.MemCache.CacheEngine;
import nari.MemCache.CacheTracker;
import nari.MemCache.Key;
import nari.Obsevation.service.impl.ObsevationServiceImpl;
import nari.Obsevation.service.interfaces.ObsevationService;
import nari.model.device.ModelService;
import nari.model.geometry.GeometryService;
import nari.model.symbol.SymbolAdapter;

import com.application.plugin.Activator;
import com.application.plugin.ActivatorReg;
import com.application.plugin.AttributeKey;
import com.application.plugin.BundleContext;
import com.application.plugin.ExportConfig;
import com.application.plugin.Provider;
import com.application.plugin.ServiceType;
import com.application.plugin.Version;
import com.application.plugin.bundle.BundleConfig;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.ServiceFilter;
import com.application.plugin.service.ServiceReference;
import com.application.plugin.tracker.TrackerListener;

@ActivatorReg(name="ObsevationActivator")
public class ObsevationActivator implements Activator,TrackerListener{
	public static ModelService modelService = ModelService.NONE;
	public static DbAdaptor dbAdaptor = DbAdaptor.NONE;
	public static SymbolAdapter symboladapter = SymbolAdapter.NONE;
	public static GeometryService geoService = GeometryService.NONE;
	
	private static CacheEngine engine = null;
	
	@Override
	public boolean init(BundleConfig context) throws BundleException {
		context.getServiceTracker().track(ModelService.class, this);
		context.getServiceTracker().track(DbAdaptor.class, this);
		context.getServiceTracker().track(SymbolAdapter.class, this);
		context.getServiceTracker().track(GeometryService.class, this);
		return true;
	}

	@Override
	public boolean start(BundleContext context) throws BundleException {
		ExportConfig config = new ExportConfig(){

			@Override
			public ServiceType getServiceType() {
				return ServiceType.HTTP;
			}
			
		};
		context.export(ObsevationService.class, new Provider<ObsevationService>() {

			@Override
			public ObsevationServiceImpl get() throws BundleException {
				return new ObsevationServiceImpl();
			}

			@Override
			public AttributeKey getKey() throws BundleException {
				return AttributeKey.key(ObsevationService.class, Version.defaultVersion());
			}

			@Override
			public Version version() throws BundleException {
				return  Version.defaultVersion();
			}


			@Override
			public ServiceFilter[] getFilter() throws BundleException {
				return null;
			}
			
			
		}, config);
		
		return true;
	}

	@Override
	public boolean stop(BundleContext context) throws BundleException {
		return true;
	}

	@Override
	public <T> void serviceAdd(ServiceReference<T> ref,BundleContext context) throws BundleException {
		T ins = (T)ref.get();
		if(ins instanceof ModelService){
			modelService = (ModelService)ins;
		}
		
		T symIns = (T)ref.get();
		if(symIns instanceof SymbolAdapter){
			symboladapter = (SymbolAdapter)symIns;
		}
		
		T dbins = (T)ref.get();
		if(dbins instanceof DbAdaptor){
			dbAdaptor = (DbAdaptor)dbins;
			engine = new StdCacheEngine();
			
			try {
				if(engine.init()){
					engine.start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		T geoIns = (T)ref.get();
		if(geoIns instanceof GeometryService){
			geoService = (GeometryService)geoIns;
		}
	}

	@Override
	public <T> void serviceModify(ServiceReference<T> ref,BundleContext context) throws BundleException {
		
	}

	@Override
	public <T> void serviceRemove(ServiceReference<T> ref,BundleContext context) throws BundleException {
		
	}

	public static Cache getCache(String cacheId,String dydj) throws Exception{
		CacheTracker tracker = ObsevationActivator.engine.getTracker();
		Cache cache = tracker.select(Key.val(cacheId+"-"+dydj));
		
		return cache;
	}
}
