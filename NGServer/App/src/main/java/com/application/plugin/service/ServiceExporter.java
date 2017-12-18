package com.application.plugin.service;

import com.application.plugin.bundle.BundleException;

public interface ServiceExporter<T> {

	public void unexport() throws BundleException ;
	
}
