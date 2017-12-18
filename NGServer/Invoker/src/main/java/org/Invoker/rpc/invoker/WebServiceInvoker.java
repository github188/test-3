package org.Invoker.rpc.invoker;

import org.Invoker.Identity;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.result.Result;
import org.Invoker.rpc.result.RpcResult;

public class WebServiceInvoker<T> extends AbstractInvoker<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2166005970023912728L;
	
	private Invoker<T> invoker = null;
	
	public WebServiceInvoker(Invoker<T> invoker,Class<T> interfaceType){
		super(interfaceType);
		this.invoker = invoker;
	}
	
	@Override
	public void destroy() throws InvokerException {
		invoker.destroy();
	}

	@Override
	public Identity getIdentity() {
		return invoker.getIdentity();
	}

	@Override
	protected Result doInvoke(Invocation inv) throws InvokerException {
		Object val = null;
		Result res = null;
		try {
			val = invoker.invoke(inv);
			res = new RpcResult(val);
		} catch (Exception e) {
			e.printStackTrace();
			res = new RpcResult(e);
		}
		return res;
	}
}
