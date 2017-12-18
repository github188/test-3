package org.Invoker.remoting.exchanger;

import org.Invoker.SocketClientIdentity;
import org.Invoker.SocketServerIdentity;
import org.Invoker.rpc.extension.ExtensionLoader;

public class Exchangers {

	public static ExchangerClient connect(SocketClientIdentity ident, ExchangerHandler handler) throws RemotingException {
        if (ident == null) {
            throw new IllegalArgumentException("ident is null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("handler is null");
        }
        
        return getExchanger("header").connect(ident, handler);
    }
	
	public static ExchangerServer bind(SocketServerIdentity ident,ExchangerHandler handler) throws RemotingException {
		if (ident == null) {
            throw new IllegalArgumentException("ident is null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("handler is null");
        }
        
		return getExchanger("header").bind(ident, handler);
	}

	public static Exchanger getExchanger(String type) {
        return ExtensionLoader.getExtensionLoader(Exchanger.class).getExtension(type);
    }
}
