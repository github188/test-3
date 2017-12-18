package com.application.plugin.tracker;

import com.application.plugin.BundleContext;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.ServiceReference;

public interface TrackerNotify {

	public void notify(Class<?> serviceClass,ServiceReference<?> ref ,int notifyType,BundleContext context) throws BundleException;
	
	public static final int ADD = 1;
	
	public static final int MODIFY = 2;
	
	public static final int REMOVE = 4;
}
