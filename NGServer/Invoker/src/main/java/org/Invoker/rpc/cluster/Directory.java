package org.Invoker.rpc.cluster;

import java.util.List;

import org.Invoker.Identity;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.invoker.Invocation;
import org.Invoker.rpc.invoker.Invoker;

public interface Directory<T> {

	public List<Invoker<T>> list(Invocation invocation) throws InvokerException;
	
	public Class<T> getInterface();
	
	public void destory();
	
	public Identity getIdentity();
}
