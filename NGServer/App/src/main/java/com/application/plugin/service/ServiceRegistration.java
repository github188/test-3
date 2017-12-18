package com.application.plugin.service;

import com.application.plugin.bundle.BundleException;

public interface ServiceRegistration<T> {

	public ServiceReference<T> getReference() throws BundleException;
	
}
