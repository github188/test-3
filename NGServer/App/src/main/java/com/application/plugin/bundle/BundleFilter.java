package com.application.plugin.bundle;

import com.application.plugin.BundleContext;
import com.application.plugin.bundle.DefaultFilterChain.NextFilter;

public interface BundleFilter {

	public void fireExceptionCaught(Throwable exception,NextFilter filter) throws BundleException;
	
	public void fireBundleInit(BundleContext context,NextFilter filter) throws BundleException;
	
	public void fireBundleStart(BundleContext context,NextFilter filter) throws BundleException;
	
	public void fireBundleStop(BundleContext context,NextFilter filter) throws BundleException;
	
	public void onPreAdd(FilterChain chain,String filterName) throws BundleException;
	
	public void onPostAdd(FilterChain chain,String filterName) throws BundleException;
	
	public void onPreRemove(FilterChain chain,String filterName) throws BundleException;
	
	public void onPostRemove(FilterChain chain,String filterName) throws BundleException;
}
