package com.application.plugin.proxy;

import java.util.concurrent.atomic.AtomicReference;

import com.application.plugin.AttributeKey;
import com.application.plugin.Provider;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.DefaultServiceFilterChain;
import com.application.plugin.service.DefaultServiceFilterChainBuilder;
import com.application.plugin.service.ServiceFilter;
import com.application.plugin.service.ServiceFilterChain;
import com.application.plugin.service.ServiceFilterChainBuilder;
import com.application.plugin.service.ServiceLogFilter;
import com.application.plugin.service.ServiceReference;

public class ProxyServiceReference<T> implements ServiceReference<T> {

	private ServiceReference<T> ref = null;
	
	private ProxyFactory factory = null;
	
	private Provider<T> provider = null;
	
	private ServiceFilter logFilter = null;
	
	private final AtomicReference<T> instanceRef = new AtomicReference<T>();
	
	public ProxyServiceReference(ServiceReference<T> ref,ProxyFactory factory,Provider<T> provider){
		this.ref = ref;
		this.factory = factory;
		this.provider = provider;
	}
	
	@Override
	public AttributeKey getKey() throws BundleException {
		return ref.getKey();
	}

	@Override
	public T get() throws BundleException {
		if(instanceRef.get()==null){
			ServiceFilter[] filters = provider.getFilter();
			
			ServiceFilterChainBuilder builder = new DefaultServiceFilterChainBuilder();
			if(filters!=null && filters.length>0){
				for(ServiceFilter filter:filters){
					builder.addFilter(filter.getName(), filter);
				}
			}
			
			logFilter = new ServiceLogFilter();
			
			builder.addFilter(logFilter.getName(), logFilter);
			
			ServiceFilterChain filterChain = new DefaultServiceFilterChain();
			builder.buildFilterChain(filterChain);
			T instance = factory.getProxy(ref,filterChain);
			instanceRef.compareAndSet(null, instance);
		}
		
		return instanceRef.get();
	}

	@Override
	public Class<T> getReferenceClass() {
		return ref.getReferenceClass();
	}

}
