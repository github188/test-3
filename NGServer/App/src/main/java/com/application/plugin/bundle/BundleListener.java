package com.application.plugin.bundle;

import com.application.plugin.BundleContext;

public interface BundleListener {

	public void onCreationComplate(BundleContext context) throws BundleException;
	
}
