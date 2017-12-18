package com.application.plugin.proxy;

import com.application.plugin.Provider;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.ServiceReference;
import com.application.plugin.service.ServiceRegistration;

public class ProxyServiceRegistration<T> implements ServiceRegistration<T> {

	private ServiceRegistration<T> reg = null;
	
	private ProxyFactory factory = null;
	
	private Provider<T> provider = null;
	
	public ProxyServiceRegistration(ServiceRegistration<T> reg,ProxyFactory factory,Provider<T> provider){
		this.reg = reg;
		this.factory = factory;
		this.provider = provider;
	}
	
	@Override
	public ServiceReference<T> getReference() throws BundleException {
		ServiceReference<T> ref = new ProxyServiceReference<T>(reg.getReference(),factory,provider);
		return ref;
	}

}
