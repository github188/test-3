package com.application.plugin.service;

import com.application.plugin.AttributeKey;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.tracker.TrackerListener;

public interface ServiceTracker {

	public void open() throws BundleException;
	
	public void track(Class<?> clazz ,TrackerListener listener) throws BundleException;
	
	public void track(Class<?> clazz, TrackerListener listener, AttributeKey key) throws BundleException;
	
}
