package org.Invoker.remoting.exchanger;

import org.Invoker.SocketClientIdentity;
import org.Invoker.SocketServerIdentity;
import org.Invoker.rpc.Adaptive;
import org.Invoker.rpc.SPI;

@SPI("header")
public interface Exchanger {

	@Adaptive
	public ExchangerServer bind(SocketServerIdentity ident,ExchangerHandler handler) throws RemotingException;
	
	@Adaptive
	public ExchangerClient connect(SocketClientIdentity ident,ExchangerHandler handler) throws RemotingException;
}
