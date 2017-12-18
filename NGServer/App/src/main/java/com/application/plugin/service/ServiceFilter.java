package com.application.plugin.service;

import com.application.plugin.Invoker;
import com.application.plugin.Result;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.DefaultServiceFilterChain.NextFilter;

public interface ServiceFilter {

	public void prevInvoke(Invoker invoker,NextFilter nextFilter) throws BundleException;
	
	public void postInvoke(Invoker invoker,Result result,NextFilter nextFilter) throws BundleException;
	
	public String getName();
}
