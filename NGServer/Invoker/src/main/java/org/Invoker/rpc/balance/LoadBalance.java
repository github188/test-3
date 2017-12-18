package org.Invoker.rpc.balance;

import java.util.List;

import org.Invoker.rpc.Adaptive;
import org.Invoker.rpc.SPI;
import org.Invoker.rpc.invoker.Invocation;
import org.Invoker.rpc.invoker.Invoker;

@SPI
public interface LoadBalance {

	@Adaptive
	public <T> Invoker<T> select(List<Invoker<T>> invokers,Invocation inv);
}
