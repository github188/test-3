package org.Invoker.rpc.protocol;

import java.util.concurrent.atomic.AtomicReference;

import org.Invoker.remoting.exchanger.ExchangerServer;

public class ServerHolder {

	private static final AtomicReference<ServerHolder> holder = new AtomicReference<ServerHolder>();
	
	private ExchangerServer server = null;
	
	public ExchangerServer get(){
		return server;
	}
	
	public void serServer(ExchangerServer server){
		this.server = server;
	}
	
	public static ServerHolder getHolder(){
		if(holder.get()==null){
			ServerHolder hd = new ServerHolder();
			holder.compareAndSet(null, hd);
		}
		return holder.get();
	}
}
