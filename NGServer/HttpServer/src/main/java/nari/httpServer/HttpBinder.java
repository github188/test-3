package nari.httpServer;

import java.net.InetSocketAddress;

public interface HttpBinder {

//	public HttpServer bind(InetSocketAddress address, ServletProvider provider);
	
	public HttpServer bind(InetSocketAddress address, HttpHandler handler,ServletProvider provider);
	
}
