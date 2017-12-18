package nari.httpServer;

import java.util.concurrent.atomic.AtomicReference;

public class HttpContext {

	private static final AtomicReference<HttpContext> context = new AtomicReference<>(new HttpContext(new JettyHttpBinder()));
	
	private HttpBinder binder = null;
	
	public HttpContext(HttpBinder binder){
		this.binder = binder;
	}
	
	public static HttpContext getContext(){
		return context.get();
	}
	
	public HttpBinder getHttpBinder(){
		return binder;
	}
	
	public void setHttpBinder(HttpBinder binder){
		this.binder = binder;
	}
}
