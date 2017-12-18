package org.Invoker.remoting.exchanger;

import org.Invoker.SocketClientIdentity;
import org.Invoker.SocketServerIdentity;
import org.Invoker.rpc.Adaptive;
import org.Invoker.rpc.SPI;

@SPI("netty")
public interface Transporter {

	@Adaptive
	public Client connect(SocketClientIdentity ident, ExchangerHandler handler) throws RemotingException ;
	 
	@Adaptive
	public Server bind(SocketServerIdentity ident, ExchangerHandler handler) throws RemotingException;
}
