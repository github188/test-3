package net.transfer.client;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public abstract class AbstractServer extends AbstractEndpoint implements Server {

	private InetSocketAddress bindAddress;
	
	public AbstractServer(BindTicket ticket,ChannelHandler handler) throws RemotingException{
		super(handler);
		String ip = ticket.getBindAddress();
		try {
			if(ip==null || "".equals(ip)){
				ip = InetAddress.getLocalHost().getHostAddress();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		bindAddress = new InetSocketAddress(ip, ticket.getBindPort());
        doOpen();
	}
	
	protected InetSocketAddress getBindAddress(){
		return bindAddress;
	}
	
	protected abstract void doOpen() throws RemotingException;
}
