package com.application.plugin.bundle;

public interface Bundle {

	public boolean init() throws BundleException;
	
	public boolean start() throws BundleException;
	
	public boolean stop() throws BundleException;
	
	public boolean initialized();
	
	public String getName();
	
	public String getId();
	
}
