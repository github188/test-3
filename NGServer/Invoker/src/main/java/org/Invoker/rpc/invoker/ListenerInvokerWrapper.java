package org.Invoker.rpc.invoker;

import java.util.List;

import org.Invoker.Identity;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.listener.InvokerListener;
import org.Invoker.rpc.result.Result;

public class ListenerInvokerWrapper<T> implements Invoker<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6518143149529093636L;

	private Invoker<T> invoker = null;
	
	private List<InvokerListener> listeners = null;
	
	public ListenerInvokerWrapper(Invoker<T> invoker,List<InvokerListener> listeners){
		if(invoker==null){
			throw new IllegalArgumentException("invoker is null");
		}
		this.invoker = invoker;
		this.listeners = listeners;
		
		for(InvokerListener listener:listeners){
			listener.referred(invoker);
		}
	}
	
	public Class<T> getInterface() {
		return invoker.getInterface();
	}

	public Result invoke(Invocation inv) throws InvokerException {
		return invoker.invoke(inv);
	}
	
	public void destroy(){
		try {
			invoker.destroy();
		} finally{
			for(InvokerListener listener:listeners){
				listener.destroy(invoker);
			}
		}
	}

	@Override
	public Identity getIdentity() {
		return invoker.getIdentity();
	}

}
