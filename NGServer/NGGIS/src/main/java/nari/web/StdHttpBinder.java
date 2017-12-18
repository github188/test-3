package nari.web;

import java.net.InetSocketAddress;

import javax.servlet.ServletContext;

import nari.httpServer.HttpBinder;
import nari.httpServer.HttpHandler;
import nari.httpServer.HttpServer;
import nari.httpServer.ServletProvider;

public class StdHttpBinder implements HttpBinder {

	private ServletContext context;
	
	public StdHttpBinder(ServletContext context) {
		this.context = context;
	}
	
	@Override
	public HttpServer bind(InetSocketAddress address, HttpHandler handler, ServletProvider provider) {
		return new StdHttpServer(address,handler,provider,context);
	}

}
