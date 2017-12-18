package com.application.plugin.bundle.context;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

import nari.Xml.impl.ConfigSearchService;
import nari.Xml.interfaces.ConfigSearch;

import org.Invoker.HttpClientIdentity;
import org.Invoker.HttpServerIdentity;
import org.Invoker.RegistryIdentity;
import org.Invoker.SocketClientIdentity;
import org.Invoker.SocketServerIdentity;
import org.Invoker.WebServiceClientIdentity;
import org.Invoker.WebServiceServerIdentity;
import org.Invoker.remoting.exchanger.RemotingException;
import org.Invoker.rpc.exporter.Exporter;
import org.Invoker.rpc.extension.ExtensionLoader;
import org.Invoker.rpc.invoker.Invoker;
import org.Invoker.rpc.protocol.Protocol;

import com.application.plugin.BundleContext;
import com.application.plugin.ExportConfig;
import com.application.plugin.Provider;
import com.application.plugin.ReferConfig;
import com.application.plugin.ServiceType;
import com.application.plugin.Version;
import com.application.plugin.bundle.Bundle;
import com.application.plugin.bundle.BundleConfig;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.config.InvokerConfig;
import com.application.plugin.framework.DevelopModel;
import com.application.plugin.proxy.JdkProxyFactory;
import com.application.plugin.proxy.ProxyFactory;
import com.application.plugin.proxy.ProxyServiceRegistration;
import com.application.plugin.service.DefaultServiceRegistration;
import com.application.plugin.service.ServiceExporter;
import com.application.plugin.service.ServiceReference;
import com.application.plugin.service.ServiceRegistration;

public class StandardBundleContext extends AbstractBundleContext implements BundleContext {

	private static final AtomicReference<ProxyFactory> proxyRef = new AtomicReference<ProxyFactory>();
	
	private ProxyFactory factory = null;
	
	private Bundle bundle = null;
	
	private DevelopModel model;
	
	private static final AtomicReference<ResourceLoader> resourceLoader = new AtomicReference<ResourceLoader>();
	
	private static final AtomicReference<InvokerConfig> ref = new AtomicReference<InvokerConfig>();
	
	public StandardBundleContext(Bundle bundle,BundleConfig config,DevelopModel model) {
		super(bundle);
		this.bundle = bundle;
		this.model = model;
	}
	
	private ResourceLoader getResourceLoader(){
		if(resourceLoader.get()==null){
			ResourceLoaderFactory factory = new ResourceLoaderFactory();
			ResourceLoader loader = factory.createResourceLoader(model);
			if(loader!=null){
				resourceLoader.compareAndSet(null, loader);
			}
		}
		return resourceLoader.get();
	}
	
	public <T> boolean unRegisterService(ServiceReference<T> ref) throws BundleException {
		unRegister(ref.getKey());
		return true;
	}

	public <T> ServiceExporter<T> export(Class<T> serviceClass, Provider<T> provider, ExportConfig config) throws BundleException {
		Protocol wrapperProtocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
		org.Invoker.rpc.proxy.ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(org.Invoker.rpc.proxy.ProxyFactory.class).getAdaptiveExtension();
		
		if(ref.get()==null){
//			InputStream stream = getClass().getClassLoader().getResourceAsStream("config/invoker.xml");
			InputStream stream = getResourceAsStream("config/invoker.xml");
			ConfigSearch searcher = new ConfigSearchService();
			InvokerConfig invokerConfig = searcher.loadConfigCache("invoker",stream,"xml",InvokerConfig.class);
			ref.compareAndSet(null, invokerConfig);
		}
		
		InvokerConfig invokerConfig = ref.get();
		
		ServiceType serviceType = config.getServiceType();
		
		RegistryIdentity ri = new RegistryIdentity();
		ri.setHost(invokerConfig.getRegistry().getHost());
		ri.setPort(invokerConfig.getRegistry().getPort());
		if(provider.version()!=null){
			ri.setVersion(provider.version().getVersion());
		}
		ri.setCluster(invokerConfig.getRegistry().getCluster());
		
		switch (serviceType) {
			case HTTP:
				HttpServerIdentity http = new HttpServerIdentity();
//				http.setIp(invokerConfig.getHttp().getHost());
				http.setPort(invokerConfig.getHttp().getPort());
				http.setMaxThreadPool(invokerConfig.getHttp().getMaxThreadPool());
				http.setMinThreadPool(invokerConfig.getHttp().getMinThreadPool());
				ri.setProtocolIdentity(http);
				break;
			case SOCKET:
				SocketServerIdentity soc = new SocketServerIdentity();
				soc.setPort(invokerConfig.getSocket().getPort());
				soc.setThreadPoolSize(invokerConfig.getSocket().getPoolSize());
				ri.setProtocolIdentity(soc);
				break;
			case WEBSERVICE:
				WebServiceServerIdentity web = new WebServiceServerIdentity();
				web.setPort(invokerConfig.getWebService().getPort());
				web.setMaxThreadPool(invokerConfig.getWebService().getMaxThreadPool());
				web.setMinThreadPool(invokerConfig.getWebService().getMinThreadPool());
				ri.setProtocolIdentity(web);
				break;
			default:
				break;
		}
		
		ri.setAttribute("ProxyFactory", ri.getAttribute("ProxyFactory","jdk"));
		
		Invoker<T> exportInvoker = proxyFactory.getInvoker(provider.get(), serviceClass, ri);
		final Exporter<T> export = export(exportInvoker,wrapperProtocol);
		
		return new ServiceExporter<T>() {
			
			@Override
			public void unexport() throws BundleException {
				try {
					export.unexport();
				} catch (RemotingException e) {
					throw new BundleException(e.getMessage());
				}
			}
			
		};
	}

	public <T> T refer(Class<T> clazz, ReferConfig config) throws BundleException {
		Protocol wrapperProtocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
		org.Invoker.rpc.proxy.ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(org.Invoker.rpc.proxy.ProxyFactory.class).getAdaptiveExtension();
		
		InvokerConfig invokerConfig = ref.get();
		
		ServiceType serviceType = config.getServiceType();
		
		RegistryIdentity ri = new RegistryIdentity();
		ri.setHost(invokerConfig.getRegistry().getHost());
		ri.setPort(invokerConfig.getRegistry().getPort());
//		ri.setVersion("1.0");
		if(config.getVersion()!=null && !"".equals(config.getVersion())){
			ri.setVersion(config.getVersion());
		}
		ri.setCluster(invokerConfig.getRegistry().getCluster());
		
		switch (serviceType) {
			case HTTP:
				HttpClientIdentity http = new HttpClientIdentity();
				http.setVersion("1.0");
				ri.setProtocolIdentity(http);
				break;
			case SOCKET:
				SocketClientIdentity soc = new SocketClientIdentity();
				soc.setThreadPoolSize(invokerConfig.getSocket().getPoolSize());
				ri.setProtocolIdentity(soc);
				break;
			case WEBSERVICE:
				WebServiceClientIdentity web = new WebServiceClientIdentity();
				web.setConnectTimeout(5000);
				web.setReciveTimeout(36000);
				web.setMaxThreadPool(invokerConfig.getWebService().getMaxThreadPool());
				web.setMinThreadPool(invokerConfig.getWebService().getMinThreadPool());
				ri.setProtocolIdentity(web);
				break;
			default:
				break;
		}
		
		ri.setAttribute("ProxyFactory", ri.getAttribute("ProxyFactory","jdk"));
		return refer(ri,clazz,wrapperProtocol,proxyFactory);
	}
	
	public <T> ServiceReference<T> findReference(Class<T> serviceClass, Version version) throws BundleException {
		return find(serviceClass, version);
	}

	public Bundle getBundle() {
		return bundle;
	}

	public <T> T getService(ServiceReference<T> ref) throws BundleException {
		return ref.get();
	}

	public <T> ServiceRegistration<T> registService(Class<T> serviceClass, Provider<T> provider) throws BundleException {
		if(serviceClass.getInterfaces()==null){
			throw new BundleException(serviceClass.getName()+" do not have a interface");
		}
		ServiceRegistration<T> registration = new DefaultServiceRegistration<T>(serviceClass,provider);
		ServiceRegistration<T> reg = new ProxyServiceRegistration<T>(registration, getProxyFactory(),provider);
		register(provider.getKey(), reg.getReference());
		return reg;
	}

	private ProxyFactory getProxyFactory(){
		if(proxyRef.get()==null){
			factory = new JdkProxyFactory();
			proxyRef.compareAndSet(null, factory);
		}
		return proxyRef.get();
	}

	@Override
	public InputStream getResourceAsStream(String absolutePath) {
		
		ResourceLoader loader = getResourceLoader();
		
//		InputStream stream = null;
//		if(model==DevelopModel.DEVELOP){
//			String root = System.getProperty("user.dir");
//			File file = new File(root);
//			file = new File(file.getParentFile(),absolutePath);
//			if(file.exists() && file.isFile()){
//				try {
//					stream = new FileInputStream(file);
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}
//			}
//		}else if(model==DevelopModel.TOOLKIT){
//			stream = getClass().getClassLoader().getResourceAsStream(absolutePath);
//		}
		return loader.getResourceAsStream(absolutePath);
	}

	@Override
	public URL gettResourceURL(String absolutePath) {
		ResourceLoader loader = getResourceLoader();
		return loader.getResource(absolutePath);
	}

}
