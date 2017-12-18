package org.Invoker.rpc.invoker;

import java.util.List;

import org.Invoker.Identity;
import org.Invoker.rpc.balance.LoadBalance;
import org.Invoker.rpc.cluster.Directory;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.result.Result;

public class FailoverClusterInvoker<T> extends AbstractClusterInvoker<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1866560583268149800L;
	
	private Directory<T> directory = null;
	
	public FailoverClusterInvoker(Directory<T> directory){
		super(directory);
		this.directory = directory;
	}

	@Override
	protected Result doInvoker(List<Invoker<T>> invokers, Invocation inv, LoadBalance balance) throws InvokerException{
		Invoker<T> invoker = balance.select(invokers, inv);
		Result result = invoker.invoke(inv);
		if(result!=null && result.hasException()){
			invoker = reSelect(invokers, balance, invoker, inv);
			result = invoker.invoke(inv);
		}
		return result;
	}
	
	private Invoker<T> reSelect(List<Invoker<T>> invokers,LoadBalance balance,Invoker<T> invoker,Invocation inv){
		Invoker<T> newinvoker = null;
		do {
			newinvoker = balance.select(invokers, inv);
		} while (newinvoker!=invoker);
		return newinvoker;
	}

	@Override
	public Identity getIdentity() {
		return directory.getIdentity();
	}
	
}
