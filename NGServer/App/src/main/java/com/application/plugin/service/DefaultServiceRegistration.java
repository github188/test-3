package com.application.plugin.service;

import java.util.concurrent.atomic.AtomicReference;

import com.application.plugin.AttributeKey;
import com.application.plugin.Provider;
import com.application.plugin.bundle.BundleException;

public class DefaultServiceRegistration<T> implements ServiceRegistration<T> {

	private Class<T> serviceClass = null;
	
	private Provider<T> provider = null;
	
	private ServiceReference<T> serviceRef = null;
	
	private AtomicReference<ServiceReference<T>> ref = new AtomicReference<ServiceReference<T>>();
	
	public DefaultServiceRegistration(Class<T> serviceClass, Provider<T> provider) {
		this.serviceClass = serviceClass;
		this.provider = provider;
	}
	
	public ServiceReference<T> getReference() throws BundleException {
		if(ref.get()==null){
			serviceRef = new ServiceReference<T>() {

				private AttributeKey key = provider.getKey();
				
				@Override
				public T get() throws BundleException {
					return provider.get();
				}

				@Override
				public Class<T> getReferenceClass() {
					return serviceClass;
				}

				@Override
				public AttributeKey getKey() throws BundleException{
					return key;
				}
				
			};
			ref.compareAndSet(null, serviceRef);
		}
		
		return ref.get();
	}

}
