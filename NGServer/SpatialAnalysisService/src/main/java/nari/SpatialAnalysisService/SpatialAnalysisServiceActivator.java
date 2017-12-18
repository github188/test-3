package nari.SpatialAnalysisService;

import nari.SpatialAnalysisService.Service.impl.SpatialAnalysisServiceImpl;
import nari.SpatialAnalysisService.Service.interfaces.SpatialAnalysisService;
import nari.model.device.ModelService;
import nari.model.geometry.GeometryService;

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

@ActivatorReg(name="SpatialAnalysisServiceActivator")
public class SpatialAnalysisServiceActivator implements Activator,TrackerListener {
	public static ModelService modelService = ModelService.NONE;
	public static GeometryService geoService = GeometryService.NONE;
	
	@Override
	public boolean init(BundleConfig context) throws BundleException {
		context.getServiceTracker().track(ModelService.class, this);
		context.getServiceTracker().track(GeometryService.class, this);
		return true;
	}

	@Override
	public boolean start(BundleContext context) throws BundleException {
		ExportConfig config = new ExportConfig(){

			@Override
			public ServiceType getServiceType() {
				// TODO Auto-generated method stub
				return ServiceType.HTTP;
			}
			
		};
		context.export(SpatialAnalysisService.class, new Provider<SpatialAnalysisService>() {

			@Override
			public SpatialAnalysisServiceImpl get() throws BundleException {
				// TODO Auto-generated method stub
				return new SpatialAnalysisServiceImpl();
			}

			@Override
			public AttributeKey getKey() throws BundleException {
				return AttributeKey.key(SpatialAnalysisService.class, Version.defaultVersion());
			}

			@Override
			public Version version() throws BundleException {
				return  Version.defaultVersion();
			}


			@Override
			public ServiceFilter[] getFilter() throws BundleException {
				// TODO Auto-generated method stub
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
		
		T geoIns = (T)ref.get();
		if(geoIns instanceof GeometryService){
			geoService = (GeometryService)geoIns;
		}
		
	}

	@Override
	public <T> void serviceModify(ServiceReference<T> ref,BundleContext context) throws BundleException {
		T ins = (T)ref.get();
		if(ins instanceof ModelService){
			modelService = (ModelService)ins;
		}
		
		T geoIns = (T)ref.get();
		if(geoIns instanceof GeometryService){
			geoService = (GeometryService)geoIns;
		}
	}

	@Override
	public <T> void serviceRemove(ServiceReference<T> ref,BundleContext context) throws BundleException {
		T ins = (T)ref.get();
		if(ins instanceof ModelService){
			modelService = (ModelService)ins;
		}
		
		T geoIns = (T)ref.get();
		if(geoIns instanceof GeometryService){
			geoService = (GeometryService)geoIns;
		}
	}

}