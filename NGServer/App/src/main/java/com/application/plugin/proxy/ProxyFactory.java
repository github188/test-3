package com.application.plugin.proxy;

import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.ServiceFilterChain;
import com.application.plugin.service.ServiceReference;

public interface ProxyFactory {

	public <T> T getProxy(ServiceReference<?> ref,ServiceFilterChain filterChain) throws BundleException;
}
