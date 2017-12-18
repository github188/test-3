package org.Invoker.rpc.protocol;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

import org.Invoker.remoting.exchanger.ExchangerClient;

public class ClientHolder {

	private static final AtomicReference<ClientHolder> holder = new AtomicReference<ClientHolder>();
	
	private final ConcurrentMap<InetSocketAddress, ExchangerClient> cache = new ConcurrentHashMap<InetSocketAddress, ExchangerClient>();
	
	public ExchangerClient get(InetSocketAddress remoteAddress){
		return cache.get(remoteAddress);
	}
	
	public void add(InetSocketAddress remoteAddress,ExchangerClient client){
		cache.put(remoteAddress, client);
	}
	
	public static ClientHolder getHolder(){
		if(holder.get()==null){
			ClientHolder ch = new ClientHolder();
			holder.compareAndSet(null, ch);
		}
		return holder.get();
	}
	
}
