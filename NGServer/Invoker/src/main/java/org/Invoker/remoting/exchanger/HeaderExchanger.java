package org.Invoker.remoting.exchanger;

import org.Invoker.SocketClientIdentity;
import org.Invoker.SocketServerIdentity;

public class HeaderExchanger implements Exchanger {

	public ExchangerServer bind(SocketServerIdentity ident, ExchangerHandler handler) throws RemotingException {
		return new HeaderExchangeServer(Transporters.bind(ident, handler));
	}

	public ExchangerClient connect(SocketClientIdentity ident, ExchangerHandler handler) throws RemotingException {
		return new HeaderExchangeClient(Transporters.connect(ident, handler));
	}

}
