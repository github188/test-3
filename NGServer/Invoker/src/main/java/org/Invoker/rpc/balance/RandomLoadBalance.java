package org.Invoker.rpc.balance;

import java.util.List;

import org.Invoker.rpc.invoker.Invocation;
import org.Invoker.rpc.invoker.Invoker;

public class RandomLoadBalance extends AbstractLoadBalance {

	@Override
	protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, Invocation inv) {
		int size = invokers.size();
		if(size==0){
			return null;
		}
		int seed = ((int)(size/10)+1)*10;
		int index = Math.abs((int)(Math.random()*seed-Math.abs(size-seed)));
		if(index>=invokers.size()){
			return invokers.get(0);
		}
		return invokers.get(index);
	}
	
}
