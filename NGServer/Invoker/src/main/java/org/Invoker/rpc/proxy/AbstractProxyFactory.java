package org.Invoker.rpc.proxy;

import java.lang.reflect.Method;

import org.Invoker.Identity;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.invoker.AbstractProxyInvoker;
import org.Invoker.rpc.invoker.Invocation;
import org.Invoker.rpc.invoker.Invoker;
import org.Invoker.rpc.invoker.InvokerNotify;

public abstract class AbstractProxyFactory implements ProxyFactory {

	public <T> Invoker<T> getInvoker(final T proxy, final Class<T> type, Identity ident) throws InvokerException {
		return new AbstractProxyInvoker<T>(type, ident){
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -5951825305167500721L;

			@Override
			public Object doInvoke(Invocation inv) throws Throwable {
				Method method = type.getMethod(inv.getMethdName(), inv.getParamterTypes());
				Object result = method.invoke(proxy, inv.getArguments());
				return result;
			}
			
		};
	}

	public <T> T getProxy(Invoker<T> invoker) throws InvokerException {
		Class<?> i = invoker.getInterface();
		Class<?>[] interfaces = new Class<?>[1];
		interfaces[0] = i;
		return getProxy(invoker,interfaces,false);
	}
	
	@Override
	public <T> T getProxy(Invoker<T> invoker,InvokerNotify notifier) throws InvokerException {
		Class<?> i = invoker.getInterface();
		Class<?>[] interfaces = new Class<?>[1];
		interfaces[0] = i;
		return getProxy(invoker,interfaces,notifier);
	}
	
	@Override
	public <T> T getProxy(Invoker<T> invoker, boolean isSent) throws InvokerException {
		Class<?> i = invoker.getInterface();
		Class<?>[] interfaces = new Class<?>[1];
		interfaces[0] = i;
		return getProxy(invoker,interfaces,isSent);
	}
	
	protected abstract <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces, boolean isSent) throws InvokerException;
	
	protected abstract <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces, InvokerNotify notifier) throws InvokerException;
}
