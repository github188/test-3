package com.application.plugin.service;

import com.application.plugin.Key;
import com.application.plugin.bundle.BundleException;

public interface ServiceReference<T> extends Key{
	
	public T get() throws BundleException;
	
	public Class<T> getReferenceClass();
	
}
