package org.Invoker.rpc.invoker;

import java.util.List;

import org.Invoker.rpc.balance.LoadBalance;
import org.Invoker.rpc.cluster.Directory;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.extension.ExtensionLoader;
import org.Invoker.rpc.result.Result;
import org.Invoker.rpc.result.RpcResult;

public abstract class AbstractClusterInvoker<T> implements Invoker<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9211288089823796054L;
	
	private Directory<T> directory = null;
	
	public AbstractClusterInvoker(Directory<T> directory){
		this.directory = directory;
	}
	
	public void destroy() {
		directory.destory();
	}

	public Class<T> getInterface() {
		return directory.getInterface();
	}

	public Result invoke(Invocation inv) throws InvokerException {
		List<Invoker<T>> invokers = list(inv);
		if(invokers==null || invokers.size()==0){
			return new RpcResult(new InvokerException("no server found"));
		}
		LoadBalance balance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("random");
		
		return doInvoker(invokers,inv,balance);
	}

	private List<Invoker<T>> list(Invocation inv){
		return directory.list(inv);
	}
	
	protected abstract Result doInvoker(List<Invoker<T>> invokers,Invocation inv,LoadBalance balance) throws InvokerException;
}
