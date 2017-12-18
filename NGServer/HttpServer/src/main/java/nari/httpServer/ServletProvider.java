package nari.httpServer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;

public interface ServletProvider {
	
	public String[] getWelcomePages();
	
	public String getServletContextName();
	
	public int getMaxThreadPool();
	
	public int getMinThreadPool();
	
	public ServletMapping[] getServletMapping();
	
	public ServletContextListener[] getContextListener();
	
	public FilterMapping[] getFilterMapping();
	
	public ServletContext getServletontext();
}
