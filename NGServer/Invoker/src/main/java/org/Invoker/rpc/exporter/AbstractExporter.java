package org.Invoker.rpc.exporter;

import org.Invoker.remoting.exchanger.RemotingException;
import org.Invoker.rpc.invoker.Invoker;

public abstract class AbstractExporter<T> implements Exporter<T> {

	private Invoker<T> invoker = null;
	
	public AbstractExporter(Invoker<T> invoker){
		this.invoker = invoker;
	}
	
	public Invoker<T> getInvoker() {
		return invoker;
	}

	public void unexport() throws RemotingException {
		invoker.destroy();
	}

}
