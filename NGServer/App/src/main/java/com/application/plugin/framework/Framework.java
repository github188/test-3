package com.application.plugin.framework;

import com.application.plugin.State;
import com.application.plugin.bundle.BundleException;

public interface Framework {
	
//	public void init(FrameworkListener[] listeners) throws BundleException;
	
	public void init(DevelopModel model) throws BundleException;
	
	public void start() throws BundleException;
	
	public void stop() throws BundleException;

	public State getState();
}
