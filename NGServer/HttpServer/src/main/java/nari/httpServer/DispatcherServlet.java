package nari.httpServer;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4909594693731701278L;
	
	private static final AtomicReference<HttpHandler> ref = new AtomicReference<HttpHandler>();
	
	public static void addHttpHandler(HttpHandler processor) {
        if(ref.get()==null){
        	ref.compareAndSet(null, processor);
        }
    }
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(ref.get()==null){
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Service not found");
		}else{
			ref.get().handle(request, response);
		}
	}
}
