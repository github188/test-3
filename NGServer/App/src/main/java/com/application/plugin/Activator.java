package com.application.plugin;

import com.application.plugin.bundle.BundleConfig;
import com.application.plugin.bundle.BundleException;

public interface Activator {

	public boolean init(BundleConfig config) throws BundleException;
	
	public boolean start(BundleContext context) throws BundleException;
	
	public boolean stop(BundleContext context) throws BundleException;
	
}
