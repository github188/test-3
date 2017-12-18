package nari.httpServer;

import javax.servlet.Filter;

public interface FilterMapping {

	public Class<? extends Filter> getServletFilter();
	
	public String getFilterName();
	
	public String getMapping();
}
