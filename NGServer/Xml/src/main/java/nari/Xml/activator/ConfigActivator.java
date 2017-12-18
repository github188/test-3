//package nari.Xml.activator;
//
//import java.util.concurrent.atomic.AtomicReference;
//
//import nari.Xml.impl.ConfigSearchService;
//import nari.Xml.interfaces.ConfigSearch;
//
//import com.application.plugin.Activator;
//import com.application.plugin.AttributeKey;
//import com.application.plugin.BundleContext;
//import com.application.plugin.Provider;
//import com.application.plugin.Version;
//import com.application.plugin.bundle.BundleConfig;
//import com.application.plugin.bundle.BundleException;
//import com.application.plugin.service.ServiceFilter;
//import com.application.plugin.service.ServiceRegistration;
//
//public class ConfigActivator implements Activator {
//
//	private ServiceRegistration<ConfigSearch> reg = null;
//	
//	@Override
//	public boolean init(BundleConfig config) throws BundleException {
//		return true;
//	}
//
//	@Override
//	public boolean start(BundleContext context) throws BundleException {
//		reg = context.registService(ConfigSearch.class, new Provider<ConfigSearch>() {
//
//			private final AtomicReference<ConfigSearch> ref = new AtomicReference<ConfigSearch>(ConfigSearch.NONE);
//			
//			@Override
//			public ConfigSearch get() throws BundleException {
//				if(ref.get()==ConfigSearch.NONE){
//					ConfigSearch search = new ConfigSearchService();
//					ref.compareAndSet(ConfigSearch.NONE, search);
//				}
//				return ref.get();
//			}
//
//			@Override
//			public AttributeKey getKey() throws BundleException {
//				return AttributeKey.key(ConfigSearch.class, Version.defaultVersion());
//			}
//
//			@Override
//			public Version version() throws BundleException {
//				return Version.defaultVersion();
//			}
//
//			@Override
//			public ServiceFilter[] getFilter() throws BundleException {
//				return null;
//			}
//			
//		});
//		return true;
//	}
//
//	@Override
//	public boolean stop(BundleContext context) throws BundleException {
//		if(reg!=null){
//			context.unRegisterService(reg.getReference());
//		}
//		return true;
//	}
//	
//}
