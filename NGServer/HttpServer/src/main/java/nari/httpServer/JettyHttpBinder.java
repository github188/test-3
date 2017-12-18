package nari.httpServer;

import java.net.InetSocketAddress;

public class JettyHttpBinder implements HttpBinder{

//	public HttpServer bind(InetSocketAddress address, ServletProvider provider) {
//		return new JettyHttpServer(address,provider);
//	}

	public HttpServer bind(InetSocketAddress address, HttpHandler handler,ServletProvider provider) {
		return new JettyHttpServer(address,handler,provider);
	}
}
