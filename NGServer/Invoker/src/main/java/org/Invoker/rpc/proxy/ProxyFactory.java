package org.Invoker.rpc.proxy;

import org.Invoker.Identity;
import org.Invoker.rpc.Adaptive;
import org.Invoker.rpc.SPI;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.invoker.Invoker;
import org.Invoker.rpc.invoker.InvokerNotify;

@SPI
public interface ProxyFactory {

	@Adaptive
	public <T> T getProxy(Invoker<T> invoker) throws InvokerException;
	
	@Adaptive
	public <T> T getProxy(Invoker<T> invoker,InvokerNotify notifier) throws InvokerException;
	
	@Adaptive
	public <T> T getProxy(Invoker<T> invoker, boolean isSent) throws InvokerException;
	
	@Adaptive
	public  <T> Invoker<T> getInvoker(T proxy, Class<T> type, Identity ident) throws InvokerException;
}
