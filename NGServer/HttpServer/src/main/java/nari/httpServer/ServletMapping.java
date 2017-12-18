package nari.httpServer;

import java.util.Map;

import javax.servlet.Servlet;

public interface ServletMapping {

	public Servlet getServlet();
	
	public String getServletName();
	
	public String getMapping();
	
	public Map<String,Object> getInitParameter();
	
	public int getInitOrder();
}
