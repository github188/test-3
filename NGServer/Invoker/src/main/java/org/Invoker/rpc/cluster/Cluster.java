package org.Invoker.rpc.cluster;

import org.Invoker.rpc.Adaptive;
import org.Invoker.rpc.SPI;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.invoker.Invoker;

@SPI("failover")
public interface Cluster {

	@Adaptive
	public <T> Invoker<T> join(Directory<T> directory) throws InvokerException;
}
