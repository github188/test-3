package com.application.plugin.bundle;

import com.application.plugin.service.ServiceTracker;

public interface BundleConfig {

//	public void addListener(BundleListener listener) ;
//	
//	public void removeListener(BundleListener listener);
//	
//	public BundleListener[] getListeners();
	
	public ServiceTracker getServiceTracker() throws BundleException;
	
	public FilterChainBuilder getFilterChian();
}
