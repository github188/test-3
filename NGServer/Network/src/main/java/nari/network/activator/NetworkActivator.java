package nari.network.activator;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;

import nari.Dao.interfaces.DbAdaptor;
import nari.Xml.impl.ConfigSearchService;
import nari.Xml.interfaces.ConfigSearch;
import nari.network.bean.NetworkConfig;
import nari.network.creator.NetworkCreator;
import nari.network.interfaces.NetworkAdaptor;
import nari.network.interfaces.impl.DefaultNetworkAdaptor;
import nari.network.structure.Network;

import com.application.plugin.Activator;
import com.application.plugin.ActivatorReg;
import com.application.plugin.AttributeKey;
import com.application.plugin.BundleContext;
import com.application.plugin.Provider;
import com.application.plugin.Version;
import com.application.plugin.bundle.BundleConfig;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.ServiceFilter;
import com.application.plugin.service.ServiceReference;
import com.application.plugin.service.ServiceRegistration;
import com.application.plugin.tracker.TrackerListener;

@ActivatorReg(name="NetworkActivator")
public class NetworkActivator implements Activator, TrackerListener {
	
	/**
	 * 数据库适配器
	 */
	private DbAdaptor dbAdaptor = null;
	
	/**
	 * 要发布的拓扑服务
	 */
	private ServiceRegistration<NetworkAdaptor> networkAdaptor = null;
	
	private NetworkConfig networkConfig = null;
	
	private boolean initialize = false;
	
	public NetworkActivator(){
		
	}

	@Override
	public boolean init(BundleConfig config) throws BundleException {
		config.getServiceTracker().track(DbAdaptor.class, this);
		return true;
	}

	@Override
	public boolean start(BundleContext context) throws BundleException {
		InputStream networkStream = context.getResourceAsStream("config/network.xml");
		ConfigSearch searcher = new ConfigSearchService();
		networkConfig = searcher.loadConfigCache("network", networkStream, "xml", NetworkConfig.class);
		
		// 发布拓扑服务
		if (dbAdaptor != null && dbAdaptor != DbAdaptor.NONE && 
				networkConfig != null && networkConfig.isActive() && 
				!initialize) {
			
			createNetworkAdaptor(context);
			
		}
		return true;
	}

	@Override
	public boolean stop(BundleContext context) throws BundleException {
		
		if (networkAdaptor != null){
			context.unRegisterService(networkAdaptor.getReference());
		}
		return true;
	}
	
	@Override
	public <T> void serviceAdd(ServiceReference<T> ref, BundleContext context) throws BundleException {
		T ins = ref.get();
		
		if(ins instanceof DbAdaptor) {
			dbAdaptor = (DbAdaptor)ins;
		}
		
		// 发布拓扑服务
		if (dbAdaptor != null && dbAdaptor != DbAdaptor.NONE && 
				networkConfig != null && networkConfig.isActive() && 
				!initialize) {
			
			createNetworkAdaptor(context);
			
		}
	}

	@Override
	public <T> void serviceModify(ServiceReference<T> ref, BundleContext context) throws BundleException {
		
	}

	@Override
	public <T> void serviceRemove(ServiceReference<T> ref, BundleContext context) throws BundleException {
		
	}
	
	private void createNetworkAdaptor(BundleContext context) throws BundleException {
		
		System.out.println("拓扑网络模块已激活，开始构建拓扑网络。");
		
		if (networkConfig.getThreadNum() > 10) {
			networkConfig.setThreadNum(10);
		}

		// 构建拓扑网络
		NetworkCreator networkCreater = new NetworkCreator(dbAdaptor, networkConfig);
		final Network network = networkCreater.createNetwork();
		
		// 发布拓扑服务
		networkAdaptor = context.registService(NetworkAdaptor.class, new Provider<NetworkAdaptor>() {

			private final AtomicReference<NetworkAdaptor> ref = new AtomicReference<NetworkAdaptor>(NetworkAdaptor.NONE);
			
			@Override
			public NetworkAdaptor get() throws BundleException {
				if(ref.get() == NetworkAdaptor.NONE){
					NetworkAdaptor ma = new DefaultNetworkAdaptor();
					ma.setNetwork(network);
					ref.compareAndSet(NetworkAdaptor.NONE, ma);
				}
				return ref.get();
			}

			@Override
			public AttributeKey getKey() throws BundleException {
				return AttributeKey.key(NetworkAdaptor.class, Version.defaultVersion());
			}

			@Override
			public Version version() throws BundleException {
				return Version.defaultVersion();
			}

			@Override
			public ServiceFilter[] getFilter() throws BundleException {
				return null;
			}
		});
		
		initialize = true;
	}
}