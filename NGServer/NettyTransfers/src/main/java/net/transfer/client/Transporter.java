package net.transfer.client;

public interface Transporter {

	public Client connect(ConnectTicket ticket,ExchangerHandler handler) throws RemotingException ;
	 
	public Server bind(BindTicket ticket,ExchangerHandler handler) throws RemotingException;
}
