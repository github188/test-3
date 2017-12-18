package com.application.plugin.bundle;

import com.application.plugin.BundleContext;

public interface FilterChain {

	public void fireExceptionCaught(Throwable exception);
	
	public void fireBundleInit(BundleContext context);
	
	public void fireBundleStart(BundleContext context) ;
	
	public void fireBundleStop(BundleContext context) ;
	
//	public void onPreAdd(FilterChain chain,String filterName) throws BundleException;
//	
//	public void onPostAdd(FilterChain chain,String filterName) throws BundleException;
//	
//	public void onPreRemove(FilterChain chain,String filterName) throws BundleException;
//	
//	public void onPostRemove(FilterChain chain,String filterName) throws BundleException;
	
	public boolean contains(String name);

	public boolean contains(BundleFilter filter);

	public void addFirst(String name, BundleFilter filter);

	public void addLast(String name, BundleFilter filter);

	public BundleFilter remove(String name);

	public void remove(BundleFilter filter);

	public void clear() throws Exception;
}
