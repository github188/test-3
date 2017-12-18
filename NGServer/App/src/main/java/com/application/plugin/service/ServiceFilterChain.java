package com.application.plugin.service;

import com.application.plugin.Invoker;
import com.application.plugin.Result;
import com.application.plugin.bundle.BundleException;

public interface ServiceFilterChain {

	public void firePrevInvoke(Invoker invoker) throws BundleException;
	
	public void firePostInvoke(Invoker invoker,Result result) throws BundleException;
	
	public boolean contains(String name);

	public void addFirst(String name, ServiceFilter filter);

	public void addLast(String name, ServiceFilter filter);

	public ServiceFilter remove(String name);

	public void remove(ServiceFilter filter);

	public void clear() throws Exception;
}
