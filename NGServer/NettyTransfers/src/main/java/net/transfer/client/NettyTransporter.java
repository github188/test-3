package net.transfer.client;

public class NettyTransporter implements Transporter {

	public Server bind(BindTicket ticket,ExchangerHandler handler) throws RemotingException {
		return new NettyServer(ticket,handler);
	}

	public Client connect(ConnectTicket ticket,ExchangerHandler handler) throws RemotingException {
		return new NettyClient(ticket,handler);
	}

}
