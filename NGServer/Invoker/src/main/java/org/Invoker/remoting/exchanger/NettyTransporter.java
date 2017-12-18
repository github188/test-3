package org.Invoker.remoting.exchanger;

import org.Invoker.SocketClientIdentity;
import org.Invoker.SocketServerIdentity;

public class NettyTransporter implements Transporter {

	public Server bind(SocketServerIdentity ident, ExchangerHandler handler) throws RemotingException {
		return new NettyServer(ident,handler);
	}

	public Client connect(SocketClientIdentity ident, ExchangerHandler handler) throws RemotingException {
		return new NettyClient(ident,handler);
	}

}
