package com.application.plugin.bundle;


public interface FilterChainBuilder {

	public void buildFilterChain(FilterChain filterChain);
	
	public void addFilter(String name,BundleFilter filter);
	
	public void removeFilter(String name);
}
