package com.application.plugin;

import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.ServiceFilter;

public interface Provider<T> {

	public T get() throws BundleException;
	
	public AttributeKey getKey() throws BundleException;
	
	public Version version() throws BundleException;
	
	public ServiceFilter[] getFilter() throws BundleException;
	
}
