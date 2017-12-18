package net.transfer.client;

import java.util.concurrent.atomic.AtomicReference;

public class Exchangers {

	private static final AtomicReference<Exchanger> ref = new AtomicReference<Exchanger>();
	
	public static ExchangerClient connect(ConnectTicket ticket,ExchangerHandler handler) throws RemotingException {
        if (handler == null) {
            throw new IllegalArgumentException("handler is null");
        }
        
        return getExchanger().connect(ticket,handler);
    }
	
	public static ExchangerServer bind(BindTicket ticket,ExchangerHandler handler) throws RemotingException {
        if (handler == null) {
            throw new IllegalArgumentException("handler is null");
        }
        
		return getExchanger().bind(ticket,handler);
	}

	private static Exchanger getExchanger() {
        if(ref.get()==null){
        	Exchanger exchanger = new HeaderExchanger();
        	ref.compareAndSet(null, exchanger);
        }
        return ref.get();
    }
}
