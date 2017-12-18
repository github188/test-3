package org.Invoker.rpc.exporter;

import org.Invoker.remoting.exchanger.RemotingException;
import org.Invoker.rpc.invoker.Invoker;

public class ExporterWrapper<T> implements Exporter<T> {

	private Exporter<T> exporter = null;
	
	private Invoker<T> invoker = null;
	
	public ExporterWrapper(Exporter<T> exporter,Invoker<T> invoker){
		this.exporter = exporter;
		this.invoker = invoker;
	}
	
	public Invoker<T> getInvoker() {
		return invoker;
	}

	public void unexport() throws RemotingException {
		exporter.unexport();
	}

	@Override
	public void start() {
		exporter.start();
	}

	@Override
	public boolean isStart() {
		return exporter.isStart();
	}

	@Override
	public String serverHost() {
		return exporter.serverHost();
	}

	@Override
	public int serverPort() {
		return exporter.serverPort();
	}

	@Override
	public String registryKey() {
		return exporter.registryKey();
	}
	
}
