package org.Invoker.rpc.router;

import java.util.List;

import org.Invoker.Identity;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.invoker.Invocation;
import org.Invoker.rpc.invoker.Invoker;

public interface Router {
	
	public <T> List<Invoker<T>> route(List<Invoker<T>> invokers, Identity url, Invocation invocation) throws InvokerException;
	
}
