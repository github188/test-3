package org.Invoker.rpc.cluster;

import java.util.List;

import org.Invoker.Identity;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.invoker.Invoker;

public class ClusterDirectory<T> extends AbstractDirectory<T> {

	
	public ClusterDirectory(List<Invoker<T>> invokers){
		
	}

	public void destory() {
		
	}

	public Class<T> getInterface() {
		return null;
	}

	@Override
	public List<Invoker<T>> list() throws InvokerException {
		return null;
	}

	@Override
	public Identity getIdentity() {
		return null;
	}

}
