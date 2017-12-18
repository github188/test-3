package org.Invoker.rpc.invoker;

import org.Invoker.Identity;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.result.Result;

public class DelegateInvoker<T> implements Invoker<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6876882760594132760L;

	private Invoker<T> invoker = null;
	
	private Identity providerIdentity = null;
	
	public DelegateInvoker(Invoker<T> invoker,Identity providerIdentity){
		this.invoker = invoker;
		this.providerIdentity = providerIdentity;
	}
	
	@Override
	public Class<T> getInterface() {
		return invoker.getInterface();
	}

	@Override
	public Result invoke(Invocation inv) throws InvokerException {
		return invoker.invoke(inv);
	}

	@Override
	public void destroy() {
		invoker.destroy();
	}

	@Override
	public Identity getIdentity() {
		return providerIdentity;
	}

}
