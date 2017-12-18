package net.transfer.client;

public interface Exchanger {

	public ExchangerServer bind(BindTicket ticket,ExchangerHandler handler) throws RemotingException;
	
	public ExchangerClient connect(ConnectTicket ticket,ExchangerHandler handler) throws RemotingException;
}
