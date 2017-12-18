package org.Invoker.rpc.exporter;

import org.Invoker.remoting.exchanger.RemotingException;
import org.Invoker.rpc.invoker.Invoker;

public interface Exporter <T>{

	public Invoker<T> getInvoker();
	
	public void start();
	
	public boolean isStart();
	
	public void unexport() throws RemotingException;
	
	public String serverHost();
	
	public int serverPort();
	
	public String registryKey();
}
