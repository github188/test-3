package org.Invoker.rpc.exporter;

import java.util.concurrent.atomic.AtomicInteger;

import org.Invoker.SocketServerIdentity;
import org.Invoker.remoting.exchanger.ExchangerServer;
import org.Invoker.remoting.exchanger.RemotingException;
import org.Invoker.rpc.invoker.Invoker;

public class DefaultExporter<T> extends AbstractExporter<T> {
	
	private SocketServerIdentity identity = null;
	
	private ExchangerServer server;
	
	private static final AtomicInteger ref = new AtomicInteger(0);
	
	public DefaultExporter(Invoker<T> invoker,ExchangerServer server){
		super(invoker);
		this.server = server;
		if(invoker.getIdentity() instanceof SocketServerIdentity){
			identity = (SocketServerIdentity)invoker.getIdentity();
		}
		ref.incrementAndGet();
	}

	@Override
	public void start() {
		
	}

	@Override
	public boolean isStart() {
		return false;
	}
	
	@Override
	public void unexport() throws RemotingException{
		super.unexport();
		if(ref.decrementAndGet()==0){
			server.close();
		}
	}

	@Override
	public String serverHost() {
		return identity==null?"":identity.getHost();
	}

	@Override
	public int serverPort() {
		return identity==null?0:identity.getPort();
	}

	@Override
	public String registryKey() {
		return serverHost()+":"+serverPort();
	}
}
