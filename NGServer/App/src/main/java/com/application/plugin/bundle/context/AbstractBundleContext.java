package com.application.plugin.bundle.context;

import org.Invoker.Identity;
import org.Invoker.rpc.exporter.Exporter;
import org.Invoker.rpc.invoker.Invoker;
import org.Invoker.rpc.protocol.Protocol;

import com.application.plugin.AttributeKey;
import com.application.plugin.RegistorProvider;
import com.application.plugin.Version;
import com.application.plugin.bundle.Bundle;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.ServiceReference;
import com.application.plugin.service.ServiceRegistry;

public abstract class AbstractBundleContext {

	public AbstractBundleContext(Bundle bundle){
		
	}
	
	protected ServiceRegistry getRegistry(){
		return RegistorProvider.get();
	}
	
	protected <T> void register(Class<T> clazz,ServiceReference<T> ref,Version version) throws BundleException{
		AttributeKey key = AttributeKey.key(clazz, version);
		register(key,ref);
	}
	
	protected <T> void register(AttributeKey key,ServiceReference<T> ref) throws BundleException{
		getRegistry().register(key,ref);
	}
	
	protected <T> ServiceReference<T> find(AttributeKey key) throws BundleException{
		return getRegistry().find(key);
	}
	
	protected <T> ServiceReference<T> find(Class<T> clazz,Version version) throws BundleException{
		AttributeKey key = AttributeKey.key(clazz, version);
		return find(key);
	}
	
	protected <T> void unRegister(AttributeKey key) throws BundleException{
		getRegistry().unregister(key);
	}
	
	protected <T> Exporter<T> export(Invoker<T> exportInvoker,Protocol protocol) throws BundleException{
		return getRegistry().export(exportInvoker,protocol);
	}
	
	protected <T> T refer(Identity ident,Class<T> clazz,Protocol wrapperProtocol,org.Invoker.rpc.proxy.ProxyFactory proxyFactory) throws BundleException{
		Invoker<T> invoker = wrapperProtocol.refer(clazz, ident);
		return (T)proxyFactory.getProxy(invoker);
	}
}
