package com.application.plugin.tracker;

import java.io.InputStream;
import java.net.URL;

import com.application.plugin.BundleContext;
import com.application.plugin.ExportConfig;
import com.application.plugin.Provider;
import com.application.plugin.ReferConfig;
import com.application.plugin.Version;
import com.application.plugin.bundle.Bundle;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.ServiceExporter;
import com.application.plugin.service.ServiceReference;
import com.application.plugin.service.ServiceRegistration;

public class TrackerBundleContext<V> implements BundleContext{

	private TrackerNotify notifier = null;
	
	private BundleContext context = null;
	
	public TrackerBundleContext(Provider<V> provider,BundleContext context) throws BundleException {
		this.context = context ;
		V ins = provider.get();
		if(ins instanceof TrackerNotify){
			notifier = (TrackerNotify)ins;
		}
	}
	
	public <T> boolean unRegisterService(ServiceReference<T> ref) throws BundleException {
		if(notifier!=null){
			notifier.notify(ref.getReferenceClass(), ref, TrackerNotify.REMOVE,this);
		}
		return context.unRegisterService(ref);
	}

	public <T> ServiceReference<T> findReference(Class<T> serviceClass, Version version) throws BundleException {
		return context.findReference(serviceClass, version);
	}

	public Bundle getBundle() {
		return context.getBundle();
	}

	public <T> T getService(ServiceReference<T> ref) throws BundleException {
		return context.getService(ref);
	}

	public <T> ServiceRegistration<T> registService(Class<T> serviceClass, Provider<T> provider) throws BundleException {
		ServiceRegistration<T> reg = context.registService(serviceClass, provider);
		if(notifier!=null && reg!=null){
			notifier.notify(serviceClass, reg.getReference(), TrackerNotify.ADD,this);
		}
		return reg;
	}

	public <T> ServiceExporter<T> export(Class<T> serviceClass, Provider<T> provider , ExportConfig config) throws BundleException {
		return context.export(serviceClass, provider, config);
	}

	public <T> T refer(Class<T> clazz, ReferConfig config) throws BundleException {
		return context.refer(clazz, config);
	}

	@Override
	public InputStream getResourceAsStream(String absolutePath) {
		return context.getResourceAsStream(absolutePath);
	}

	@Override
	public URL gettResourceURL(String absolutePath) {
		return context.gettResourceURL(absolutePath);
	}

}
