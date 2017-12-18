package com.application.plugin.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.application.plugin.AbstractProxyFactory;
import com.application.plugin.DefaultInvoker;
import com.application.plugin.DefaultResult;
import com.application.plugin.Invoker;
import com.application.plugin.Result;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.ServiceFilterChain;

public class JdkProxyFactory extends AbstractProxyFactory {

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T getProxy(final Object obj,final Class<?>[] interfaces,final ServiceFilterChain filterChain) throws BundleException{
		
		return (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), interfaces, new InvocationHandler() {
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				
				Invoker invoker = new DefaultInvoker(proxy, interfaces, method, args);
				filterChain.firePrevInvoke(invoker);
				
				Object value = null;
				Throwable t = null;
				try {
					value = method.invoke(obj, args);
				} catch (Exception e) {
					e.printStackTrace();
					t = e;
				}
				
				Result result = new DefaultResult(invoker,value,t);
				filterChain.firePostInvoke(invoker,result);
		        return value;
			}
		});
	}

	
}
