package org.Invoker.rpc.balance;

import java.util.List;

import org.Invoker.rpc.invoker.Invocation;
import org.Invoker.rpc.invoker.Invoker;

public abstract class AbstractLoadBalance implements LoadBalance {

	public <T> Invoker<T> select(List<Invoker<T>> invokers, Invocation inv) {
		if(invokers.size()==1){
			return invokers.get(0);
		}
		return doSelect(invokers,inv);
	}

	protected abstract <T> Invoker<T> doSelect(List<Invoker<T>> invokers,Invocation inv);
	
}
