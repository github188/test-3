package org.Invoker.remoting.exchanger;

import java.util.concurrent.ExecutorService;

import org.Invoker.Identity;
import org.Invoker.rpc.extension.ExtensionLoader;

public abstract class AbstractExecutorHandler implements ChannelHandler{

	private Identity ident = null;
	
	private ExecutorService executor = null;
	
	public AbstractExecutorHandler(Identity ident){
		this.ident = ident;
		executor = (ExecutorService)ExtensionLoader.getExtensionLoader(ThreadPool.class).getAdaptiveExtension().getExecutor(ident);
	}
	
	protected ExecutorService getExecutorService(){
		return executor;
	}
	
	protected void close(){
		executor.shutdown();
	}
	
	public Identity getIdentity(){
		return ident;
	}
}
