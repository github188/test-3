package org.Invoker.rpc.cluster;

import java.util.List;

import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.invoker.Invocation;
import org.Invoker.rpc.invoker.Invoker;

public abstract class AbstractDirectory<T> implements Directory<T> {

	public List<Invoker<T>> list(Invocation invocation) throws InvokerException {
		return list();
	}
	
	public abstract List<Invoker<T>> list() throws InvokerException;
}
