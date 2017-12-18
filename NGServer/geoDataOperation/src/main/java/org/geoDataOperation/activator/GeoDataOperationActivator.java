package org.geoDataOperation.activator;

import nari.Dao.interfaces.DbAdaptor;
import nari.Logger.Logger;
import nari.Logger.LoggerManager;

import org.geoDataOperation.interfaces.GeoDataOperationInterfaces;
import org.geoDataOperation.interfaces.impl.GeoDataOperationInterfacesImpl;

import com.application.plugin.Activator;
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

/**
 * Hello world!
 *
 */
public class GeoDataOperationActivator implements Activator,TrackerListener{
	
	private Logger logger = LoggerManager.getLogger(this.getClass());
	
//	public static ModelService modelService = ModelService.NONE;
	public static DbAdaptor dbAdaptor = DbAdaptor.NONE;
	
	@Override
	public boolean init(BundleConfig context) throws BundleException {
//		context.getServiceTracker().track(ModelService.class, this);
		context.getServiceTracker().track(DbAdaptor.class, this);
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
		context.export(GeoDataOperationInterfaces.class, new Provider<GeoDataOperationInterfaces>() {

			@Override
			public GeoDataOperationInterfaces get() throws BundleException {
				return new GeoDataOperationInterfacesImpl();
			}

			@Override
			public AttributeKey getKey() throws BundleException {
				return AttributeKey.key(GeoDataOperationInterfaces.class, Version.defaultVersion());
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
//		T ins = (T)ref.get();
//		if(ins instanceof ModelService){
//			modelService = (ModelService)ins;
//		}
		
		T dbins = (T)ref.get();
		if(dbins instanceof DbAdaptor){
			dbAdaptor = (DbAdaptor)dbins;
//			//运行单位缓存
//			String sql = "select a.bmmc,a.isc_id,b.bmmc as wsgs from ISC_SPECIALORG_UNIT_LOCEXT a left join ISC_SPECIALORG_UNIT_LOCEXT b on a.sswsid = b.isc_id";
//			List<Map<String,Object>> YXDWList = null;
//			try {
//				long a =System.currentTimeMillis();
//				YXDWList = dbAdaptor.findAllMap(sql);
//				long b =System.currentTimeMillis();
//				System.out.println("初始化运行单位网省信息:"+(b-a)+"ms");
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
//			//将其放出map中(Id:YXDWMessage)
//			for(int i=0;i<YXDWList.size();i++){
//				Map<String,Object> eachMap = YXDWList.get(i);
//				YXDWMessage yxdwMessage = new YXDWMessage();
//				String iscId = String.valueOf(eachMap.get("isc_id"));
//				String bmmc = String.valueOf(eachMap.get("bmmc"));
//				String wsgs = String.valueOf(eachMap.get("wsgs"));
//				yxdwMessage.setBmmc(bmmc);
//				yxdwMessage.setWsgs(wsgs);
//				YXDWMap.put(iscId, yxdwMessage);
//			}
		}
	}

	@Override
	public <T> void serviceModify(ServiceReference<T> ref,BundleContext context) throws BundleException {
		T dbins = (T)ref.get();
		if(dbins instanceof DbAdaptor){
			dbAdaptor = (DbAdaptor)dbins;
		}
	}

	@Override
	public <T> void serviceRemove(ServiceReference<T> ref,BundleContext context) throws BundleException {
		T dbins = (T)ref.get();
		if(dbins instanceof DbAdaptor){
			dbAdaptor = (DbAdaptor)dbins;
		}
	}
}
