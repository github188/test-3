package org.Invoker.remoting.exchanger;

import org.Invoker.SocketClientIdentity;
import org.Invoker.SocketServerIdentity;
import org.Invoker.rpc.extension.ExtensionLoader;

public class Transporters {

	public static Server bind(SocketServerIdentity ident, ExchangerHandler handler) throws RemotingException {
        if (ident == null) {
            throw new IllegalArgumentException("ident is null");
        }
        return getTransporter("netty").bind(ident, handler);
    }

    public static Client connect(SocketClientIdentity ident, ExchangerHandler handler) throws RemotingException {
        if (ident == null) {
            throw new IllegalArgumentException("ident is null");
        }
        return getTransporter("netty").connect(ident, handler);
    }

    public static Transporter getTransporter(String type) {
        return ExtensionLoader.getExtensionLoader(Transporter.class).getExtension(type);
    }
}
