package com.application.plugin.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.Invoker.rpc.exporter.Exporter;
import org.Invoker.rpc.invoker.Invoker;
import org.Invoker.rpc.protocol.Protocol;

import com.application.plugin.AttributeKey;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.framework.Framework;

public class DefaultServiceRegistry implements ServiceRegistry {

	private ConcurrentMap<AttributeKey, ServiceReference<?>> cache = new ConcurrentHashMap<AttributeKey, ServiceReference<?>>();
	
	private Exporter<?> export = null;
	
	public DefaultServiceRegistry(){
		
	}
	
	@Override
	public <T> void register(AttributeKey key,ServiceReference<T> ref) throws BundleException {
		AttributeKey k = key;
		if(!cache.containsKey(key)){
			cache.putIfAbsent(k, ref);
		}
	}

	@Override
	public <T> void unregister(AttributeKey key) throws BundleException {
		if(cache.containsKey(key)){
			cache.remove(key);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> ServiceReference<T> find(AttributeKey key) throws BundleException {
		return (ServiceReference<T>) cache.get(key);
	}

	@Override
	public void onInit(Framework framework) throws BundleException {
		
	}

	@Override
	public void onStart(Framework framework) throws BundleException {
		if(export!=null && !export.isStart()){
			export.start();
		}
	}

	@Override
	public void onStop(Framework framework) throws BundleException {
		
	}

	@Override
	public void onExceptionCaught(Framework framework, Throwable t) throws BundleException {
		
	}

	@Override
	public <T> Exporter<T> export(Invoker<T> exportInvoker, Protocol protocol) throws BundleException {
		Exporter<T> export = protocol.export(exportInvoker);
		this.export = export;
		return export;
	}
}
