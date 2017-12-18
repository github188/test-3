package nari.httpServer;

import java.net.InetSocketAddress;

public interface HttpServer {

	public HttpHandler getHttpHandler();
	
	public ServletProvider getServletProvider();
    
	public InetSocketAddress getLocalAddress();
    
	public void close();
    
	public void close(int timeout);
    
	public boolean isBound();
    
	public boolean isClosed();
	
	public void start();
	
}
