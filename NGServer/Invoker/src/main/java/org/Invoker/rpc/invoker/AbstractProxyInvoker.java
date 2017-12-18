package org.Invoker.rpc.invoker;

import org.Invoker.Identity;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.result.Result;
import org.Invoker.rpc.result.RpcResult;

public abstract class AbstractProxyInvoker<T> implements Invoker<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5987145494719010164L;

	private Class<T> clazz = null;
	
	private Identity ident = null;
	
	public AbstractProxyInvoker(Class<T> type, Identity ident){
		this.clazz = type;
		this.ident = ident;
	}
	
	public Class<T> getInterface() {
		return clazz;
	}

	public Result invoke(Invocation inv) throws InvokerException {
		try {
			return new RpcResult(doInvoke(inv));
		} catch (Throwable e) {
			e.printStackTrace();
			return new RpcResult(e);
		}
	}

	public void destroy() {
		
	}

	@Override
	public Identity getIdentity() {
		return ident;
	}

	public abstract Object doInvoke(Invocation inv) throws Throwable;
}
