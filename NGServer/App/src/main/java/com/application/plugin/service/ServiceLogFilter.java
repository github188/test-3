package com.application.plugin.service;

import java.util.logging.Logger;

import com.application.plugin.Invoker;
import com.application.plugin.Result;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.DefaultServiceFilterChain.NextFilter;

public class ServiceLogFilter implements ServiceFilter {

	private final Logger log = Logger.getLogger("ServiceLogFilter");
	
	@Override
	public void prevInvoke(Invoker invoker, NextFilter nextFilter) throws BundleException{
		log.info(invoker.getInvokeMethod().getName());
		nextFilter.prevInvoke(invoker);
	}

	@Override
	public void postInvoke(Invoker invoker,Result result, NextFilter nextFilter) throws BundleException{
		if(result.hasException()){
			log.info(result.getException().getMessage());
		}
		nextFilter.postInvoke(invoker,result);
	}

	@Override
	public String getName() {
		return "logfilter";
	}

}
