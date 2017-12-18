package org.Invoker.rpc.cluster;

import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.invoker.FailoverClusterInvoker;
import org.Invoker.rpc.invoker.Invoker;

public class FailoverCluster implements Cluster {

	public <T> Invoker<T> join(Directory<T> directory) throws InvokerException {
		return new FailoverClusterInvoker<T>(directory);
	}

}
