package com.application.plugin.service;

public interface ServiceFilterChainBuilder {

	public void buildFilterChain(ServiceFilterChain filterChain);
	
	public void addFilter(String name,ServiceFilter filter);
	
	public void removeFilter(String name);
}
