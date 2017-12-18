package nari.MongoDBUpdate;

import java.util.HashMap;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.MongoDBUpdate.Util.FieldDef;
import nari.MongoDBUpdate.service.impl.MongoUpdateServiceImpl;
import nari.MongoDBUpdate.service.interfaces.MongoUpdateService;
import nari.model.device.ModelService;
import nari.model.geometry.GeometryService;
import nari.model.symbol.SymbolAdapter;
import nari.network.interfaces.NetworkAdaptor;
import net.transfer.client.ExchangerClient;

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

@ActivatorReg(name="UpdateServiceActivator")
public class UpdateServiceActivator implements Activator,TrackerListener{
	
	public static ModelService modelService = ModelService.NONE;
	
	public static DbAdaptor dbAdaptor = DbAdaptor.NONE;
	
	public static Map<String, Map<String, FieldDef>> fieldDefMaps = new HashMap<String, Map<String, FieldDef>>();
	
	@Override
	public boolean init(BundleConfig context) throws BundleException {
		context.getServiceTracker().track(ModelService.class, this);
		context.getServiceTracker().track(GeometryService.class, this);
		context.getServiceTracker().track(SymbolAdapter.class, this);
		context.getServiceTracker().track(DbAdaptor.class, this);
		context.getServiceTracker().track(NetworkAdaptor.class, this);
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
		
		context.export(MongoUpdateService.class, new Provider<MongoUpdateService>() {

			@Override
			public MongoUpdateService get() throws BundleException {
				return new MongoUpdateServiceImpl();
			}

			@Override
			public AttributeKey getKey() throws BundleException {
				return AttributeKey.key(MongoUpdateService.class, Version.defaultVersion());
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
	public <T> void serviceAdd(ServiceReference<T> ref, BundleContext context) throws BundleException {

		T ins = (T) ref.get();
		if (ins instanceof DbAdaptor){
			dbAdaptor = (DbAdaptor)ins;
		} else if (ins instanceof ModelService){
			modelService = (ModelService)ins;
		}
		
	}

	@Override
	public <T> void serviceModify(ServiceReference<T> ref,BundleContext context) throws BundleException {

		T ins = (T) ref.get();
		if (ins instanceof DbAdaptor){
			dbAdaptor = (DbAdaptor)ins;
		} else if (ins instanceof ModelService){
			modelService = (ModelService)ins;
		}
		
	}

	@Override
	public <T> void serviceRemove(ServiceReference<T> ref,BundleContext context) throws BundleException {
		
		T ins = (T) ref.get();
		if (ins instanceof DbAdaptor){
			dbAdaptor = (DbAdaptor)ins;
		} else if (ins instanceof ModelService){
			modelService = (ModelService)ins;
		}
		
	}

}
