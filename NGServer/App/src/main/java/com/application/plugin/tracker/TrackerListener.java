package com.application.plugin.tracker;

import com.application.plugin.BundleContext;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.ServiceReference;

public interface TrackerListener {

	public <T> void serviceAdd(ServiceReference<T> ref,BundleContext context) throws BundleException;
	
	public <T> void serviceModify(ServiceReference<T> ref,BundleContext context) throws BundleException;
	
	public <T> void serviceRemove(ServiceReference<T> ref,BundleContext context) throws BundleException;
	
}
