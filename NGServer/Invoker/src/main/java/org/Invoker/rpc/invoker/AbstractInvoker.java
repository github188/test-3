package org.Invoker.rpc.invoker;

import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.result.Result;

public abstract class AbstractInvoker<T> implements Invoker<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4077211045416615062L;
	
	private Class<T> interfaceType = null;
	
	public AbstractInvoker(Class<T> interfaceType){
		this.interfaceType = interfaceType;
	}
	
	public Class<T> getInterface() {
		return interfaceType;
	}

	public Result invoke(Invocation inv) throws InvokerException {
		return doInvoke(inv);
	}

	protected abstract Result doInvoke(Invocation inv) throws InvokerException;
}
