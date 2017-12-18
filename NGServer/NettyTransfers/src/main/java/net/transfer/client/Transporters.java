package net.transfer.client;

import java.util.concurrent.atomic.AtomicReference;

public class Transporters {

	private static final AtomicReference<Transporter> ref = new AtomicReference<Transporter>();
	
	public static Server bind(BindTicket ticket,ExchangerHandler handler) throws RemotingException {
        return getTransporter().bind(ticket,handler);
    }

    public static Client connect(ConnectTicket ticket,ExchangerHandler handler) throws RemotingException {
        return getTransporter().connect(ticket,handler);
    }

    private static Transporter getTransporter() {
       if(ref.get()==null){
    	   Transporter transporter = new NettyTransporter();
    	   ref.compareAndSet(null, transporter);
       }
       return ref.get();
    }
}
