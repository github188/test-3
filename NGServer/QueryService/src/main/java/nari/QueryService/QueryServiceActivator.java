package nari.QueryService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nari.Dao.interfaces.DbAdaptor;
import nari.QueryService.Service.impl.QueryServiceImpl;
import nari.QueryService.Service.interfaces.QueryService;
import nari.model.TableName;
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

@ActivatorReg(name="QueryServiceActivator")
public class QueryServiceActivator implements Activator,TrackerListener{
	public static ModelService modelService = ModelService.NONE;
	public static GeometryService geoService = GeometryService.NONE;
	public static DbAdaptor dbAdaptor = DbAdaptor.NONE;
	public static SymbolAdapter symboladapter = SymbolAdapter.NONE;
	
	public static Map<String,String> modelRefleMap = new HashMap<String,String>();		//modelid--classid缓存
	
	@Override
	public boolean init(BundleConfig context) throws BundleException {
		context.getServiceTracker().track(ModelService.class, this);
		context.getServiceTracker().track(GeometryService.class, this);
		context.getServiceTracker().track(DbAdaptor.class, this);
		context.getServiceTracker().track(SymbolAdapter.class, this);
		
		
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
		context.export(QueryService.class, new Provider<QueryService>() {

			@Override
			public QueryService get() throws BundleException {
				return new QueryServiceImpl();
			}

			@Override
			public AttributeKey getKey() throws BundleException {
				return AttributeKey.key(QueryService.class, Version.defaultVersion());
			}

			@Override
			public Version version() throws BundleException {
				return Version.defaultVersion();
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
		
		T geoIns = (T)ref.get();
		if(geoIns instanceof GeometryService){
			geoService = (GeometryService)geoIns;
		}
		
		T symIns = (T)ref.get();
		if(symIns instanceof SymbolAdapter){
			symboladapter = (SymbolAdapter)symIns;
		}
		
		T dbins = (T)ref.get();
		if(dbins instanceof DbAdaptor){
			dbAdaptor = (DbAdaptor)dbins;
			
			//modelid--classid缓存
			modelRefleMap = cacheModelRefle();
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
		
		T symIns = (T)ref.get();
		if(symIns instanceof SymbolAdapter){
			symboladapter = (SymbolAdapter)symIns;
		}
		
		T dbins = (T)ref.get();
		if(dbins instanceof DbAdaptor){
			dbAdaptor = (DbAdaptor)dbins;
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
		
		T symIns = (T)ref.get();
		if(symIns instanceof SymbolAdapter){
			symboladapter = (SymbolAdapter)symIns;
		}
		
		T dbins = (T)ref.get();
		if(dbins instanceof DbAdaptor){
			dbAdaptor = (DbAdaptor)dbins;
		}
	}

	
	private Map<String,String> cacheModelRefle(){
		Map<String,String> map = new HashMap<String,String>();
		String sql = "select * from " + TableName.CONF_MODELMETA;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try {
			list = dbAdaptor.findAllMap(sql);
		} catch (SQLException e) {
			System.out.println("缓存cacheModelRefle()出错");
			e.printStackTrace();
		}
		for(Map<String,Object> record:list){
			String modelId = String.valueOf(record.get("modelid"));
			String classId = String.valueOf(record.get("classid"));
			if(!map.containsKey(modelId)){
				map.put(modelId, classId);
			}
		}
		return map;
	}
}
