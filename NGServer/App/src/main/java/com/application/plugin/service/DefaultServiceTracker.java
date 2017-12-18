package com.application.plugin.service;

import com.application.plugin.AttributeKey;
import com.application.plugin.BundleContext;
import com.application.plugin.Version;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.tracker.TrackerListener;
import com.application.plugin.tracker.TrackerNotify;
import com.application.plugin.tracker.TrackerSupport;

public class DefaultServiceTracker implements ServiceTracker ,TrackerNotify{

	private final TrackerSupport support = new TrackerSupport();
	
	public void open() throws BundleException {
		
	}

	public void track(Class<?> clazz, TrackerListener listener) throws BundleException {
		support.addTrackerListener(clazz, listener,AttributeKey.key(clazz, Version.defaultVersion()));
	}

	public void track(Class<?> clazz, TrackerListener listener, AttributeKey key) throws BundleException{
		support.addTrackerListener(clazz, listener, key);
	}
	
	public void notify(Class<?> serviceClass, ServiceReference<?> ref ,int notifyType,BundleContext context) throws BundleException{
		if((notifyType&TrackerNotify.ADD)==TrackerNotify.ADD){
			support.serviceAdd(ref,context);
		}else if((notifyType&TrackerNotify.MODIFY)==TrackerNotify.MODIFY){
			support.serviceModify(ref,context);
		}else if((notifyType&TrackerNotify.REMOVE)==TrackerNotify.REMOVE){
			support.serviceRemove(ref,context);
		}
	}
}
