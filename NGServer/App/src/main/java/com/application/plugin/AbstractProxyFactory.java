package com.application.plugin;

import com.application.plugin.bundle.BundleException;
import com.application.plugin.proxy.ProxyFactory;
import com.application.plugin.service.ServiceFilterChain;
import com.application.plugin.service.ServiceReference;

public abstract class AbstractProxyFactory implements ProxyFactory{
	
	@Override
	public <T> T getProxy(ServiceReference<?> ref,ServiceFilterChain filterChain) throws BundleException{
		Class<?> clazz = ref.getReferenceClass();
		Class<?>[] interfaces = null;
		if(clazz.isInterface()){
			interfaces = new Class<?>[]{clazz};
		}else{
			interfaces = clazz.getInterfaces();
		}
		return getProxy(ref.get(),interfaces,filterChain);
	}

	protected abstract <T> T getProxy(Object obj,Class<?>[] interfaces,ServiceFilterChain filterChain) throws BundleException;
}
