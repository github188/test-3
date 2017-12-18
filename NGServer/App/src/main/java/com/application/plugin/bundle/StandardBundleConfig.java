package com.application.plugin.bundle;

import com.application.plugin.Provider;
import com.application.plugin.service.ServiceTracker;

public class StandardBundleConfig implements BundleConfig {

	private Provider<ServiceTracker> provider;
	
	private FilterChainBuilder builder = null;
	
	@SuppressWarnings("unused")
	private Bundle bundle = null;
	
	public StandardBundleConfig(Bundle bundle, Provider<ServiceTracker> provider){
		this.provider = provider;
		this.bundle = bundle;
	}
	
	public ServiceTracker getServiceTracker() throws BundleException{
		return provider.get();
	}

	@Override
	public FilterChainBuilder getFilterChian() {
		if(builder==null){
			builder = new DefaultFilterChainBuilder();
		}
		return builder;
	}

}
