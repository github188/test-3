package org.Invoker.rpc.protocol;

import java.util.concurrent.ConcurrentHashMap;

import org.Invoker.Identity;
import org.Invoker.RegistryIdentity;
import org.Invoker.rpc.cluster.Cluster;
import org.Invoker.rpc.cluster.RegistryDirectory;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.exporter.Exporter;
import org.Invoker.rpc.exporter.ExporterWrapper;
import org.Invoker.rpc.extension.ExtensionLoader;
import org.Invoker.rpc.invoker.AbstractProxyInvoker;
import org.Invoker.rpc.invoker.Invocation;
import org.Invoker.rpc.invoker.Invoker;
import org.Invoker.rpc.registry.RegistryFactory;
import org.Invoker.rpc.registry.RegistryService;

public class RegistryProtocol implements Protocol {

	private RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getAdaptiveExtension();
	
	private final ConcurrentHashMap<String,Exporter<?>> bounds = new ConcurrentHashMap<String,Exporter<?>>();
	
	public <T> Exporter<T> export(final Invoker<T> invoker) throws InvokerException {
		Identity ident = invoker.getIdentity();
		if(ident==null || !(ident instanceof RegistryIdentity)){
			return null;
		}
		final RegistryIdentity identity = (RegistryIdentity)ident;
		
		final Exporter<T> exporter = doExport(invoker,identity);
		
//		String address = exporter.serverHost()+":"+exporter.serverPort();
		
		String address = exporter.registryKey();
		
		String name = "";
		if(!invoker.getInterface().isInterface()){
			Class<?>[] interfaces = invoker.getInterface().getInterfaces();
			if(interfaces!=null && interfaces.length>0){
				name = interfaces[0].getName();
			}
		}else{
			name = invoker.getInterface().getName();
		}
		
		identity.setRegistry(toPath(name,address,identity.getVersion()));
		
		identity.getProtocolIdentity().setAttribute("RegistryFactory", identity.getAttribute("RegistryFactory","zookeeper"));
		final RegistryService registry = registryFactory.getRegistry(identity);
		registry.register(identity);
		
		return new Exporter<T>(){

			public Invoker<T> getInvoker() {
				return exporter.getInvoker();
			}

			public void unexport() {
				try {
					registry.unregister(identity);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void start() {
				exporter.start();
			}

			@Override
			public boolean isStart() {
				return exporter.isStart();
			}

			@Override
			public String serverHost() {
				return identity.getHost();
			}

			@Override
			public int serverPort() {
				return identity.getPort();
			}

			@Override
			public String registryKey() {
				return exporter.registryKey();
			}
		};
	}

	@SuppressWarnings("unchecked")
	private <T> Exporter<T> doExport(final Invoker<T> invoker,RegistryIdentity identity){
		Protocol protocol = null;
		if(identity.getProtocolIdentity()==null){
			protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getDefaultExtension();
		}else{
			protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(identity.getProtocolIdentity().getProtocol());
		}
		if(protocol==null){
			return null;
		}
		
		String version = identity.getVersion();
		Invoker<T> newInvoker = new AbstractProxyInvoker<T>(invoker.getInterface(), identity.getProtocolIdentity()){
			
			private static final long serialVersionUID = -8915484431496105587L;

			@Override
			public Object doInvoke(Invocation inv) throws Throwable {
				return invoker.invoke(inv);
			}
			
		};
		
		String key = getInvokerKey(newInvoker,version);
		Exporter<T> exporter = (Exporter<T>)bounds.get(key);
		if(exporter==null){
			synchronized (bounds) {
				exporter = (Exporter<T>)bounds.get(key);
				exporter = new ExporterWrapper<T>(protocol.export(newInvoker),newInvoker);
				bounds.putIfAbsent(key, exporter);
			}
		}
		return exporter;
	}
	
	private String toPath(String p,String address,String version){
		p = p+"."+version+"/"+address;
		return p;
	}
	
	private String getInvokerKey(final Invoker<?> invoker,String version){
		return invoker.getInterface()+"@"+version;
	}
	
	public <T> Invoker<T> refer(Class<T> type, Identity ident) throws InvokerException {
		if(ident==null || !(ident instanceof RegistryIdentity)){
			return null;
		}
		RegistryIdentity identity = (RegistryIdentity)ident;
		
		Protocol protocol = null;
		if(identity.getProtocolIdentity()==null){
			protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getDefaultExtension();
		}else{
			protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(identity.getProtocolIdentity().getProtocol());
		}
		
		Cluster cluster = null;
		if(identity.getCluster()==null || "".equals(identity.getCluster())){
			cluster = ExtensionLoader.getExtensionLoader(Cluster.class).getDefaultExtension();
		}else{
			cluster = ExtensionLoader.getExtensionLoader(Cluster.class).getExtension(identity.getCluster());
		}
		
		if(protocol==null || cluster==null){
			return null;
		}
		
		identity.getProtocolIdentity().setAttribute("RegistryFactory", identity.getProtocolIdentity().getAttribute("RegistryFactory","zookeeper"));
		
		RegistryService registry = registryFactory.getRegistry(identity);
		
		RegistryDirectory<T> directory = new RegistryDirectory<T>(type,identity,registry,protocol);
		
		identity.setRegistry(type.getName()+"."+identity.getVersion());
		
		directory.subscribe(identity);
		return cluster.join(directory);
	}

}
