package org.Invoker.rpc.invoker;

import org.Invoker.Identity;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.result.Result;

public class NoneInvoker<T> implements Invoker<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3656893586636489379L;

	@Override
	public Class<T> getInterface() {
		return null;
	}

	@Override
	public Result invoke(Invocation inv) throws InvokerException {
		return null;
	}

	@Override
	public void destroy() {
		
	}

	@Override
	public Identity getIdentity() {
		return null;
	}

}
