package net.transfer.client;

public class HeaderExchanger implements Exchanger {

	public ExchangerServer bind(BindTicket ticket,ExchangerHandler handler) throws RemotingException {
		return new HeaderExchangeServer(Transporters.bind(ticket,handler));
	}

	public ExchangerClient connect(ConnectTicket ticket,ExchangerHandler handler) throws RemotingException {
		return new HeaderExchangeClient(Transporters.connect(ticket,handler));
	}

}
