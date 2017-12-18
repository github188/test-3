package com.application.plugin.framework;

import com.application.plugin.bundle.BundleException;

public interface FrameworkListener {

	public void onInit(Framework framework) throws BundleException;
	
	public void onStart(Framework framework) throws BundleException;
	
	public void onStop(Framework framework) throws BundleException;
	
	public void onExceptionCaught(Framework framework,Throwable t) throws BundleException;
}
