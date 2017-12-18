package nari.httpServer;

import java.net.InetSocketAddress;

public abstract class AbstractHttpServer implements HttpServer{

	private HttpHandler handler = null;
	
	private ServletProvider provider = null;
	
	private InetSocketAddress address = null;
	
	private boolean isClosed = false;
	
	public AbstractHttpServer(InetSocketAddress address, HttpHandler handler,ServletProvider provider){
		this.handler = handler;
		this.address = address;
		this.provider = provider;
	}
	
	public HttpHandler getHttpHandler() {
		return handler;
	}

	public InetSocketAddress getLocalAddress() {
		return address;
	}

	public void close() {
		isClosed = true;
	}

	public void close(int timeout) {
		close();
	}

	public boolean isBound() {
		return true;
	}

	public boolean isClosed() {
		return isClosed;
	}

	@Override
	public ServletProvider getServletProvider() {
		return provider;
	}

	@Override
	public void start() {
		ServerAttribute.init();
		doStart();
	}
	
	public abstract void doStart();
}
