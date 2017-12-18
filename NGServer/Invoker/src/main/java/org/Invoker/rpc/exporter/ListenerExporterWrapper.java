package org.Invoker.rpc.exporter;

import java.util.List;

import org.Invoker.remoting.exchanger.RemotingException;
import org.Invoker.rpc.invoker.Invoker;
import org.Invoker.rpc.listener.ExporterListener;

public class ListenerExporterWrapper<T> implements Exporter<T> {

	private Exporter<T> exporter = null;
	
	private List<ExporterListener> listeners = null;
	
	public ListenerExporterWrapper(Exporter<T> exporter,List<ExporterListener> listeners){
		if(exporter==null){
			throw new IllegalArgumentException("exporter is null");
		}
		this.exporter = exporter;
		this.listeners = listeners;
		for(ExporterListener listener:listeners){
			listener.exported(exporter);
		}
	}
	
	public Invoker<T> getInvoker() {
		return exporter.getInvoker();
	}

	public void unexport() throws RemotingException {
		try {
			exporter.unexport();
		} finally{
			for(ExporterListener listener:listeners){
				listener.unexported(exporter);
			}
		}
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
