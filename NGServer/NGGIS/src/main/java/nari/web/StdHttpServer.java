package nari.web;

import java.net.InetSocketAddress;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration.Dynamic;

import nari.httpServer.AbstractHttpServer;
import nari.httpServer.FilterMapping;
import nari.httpServer.HttpHandler;
import nari.httpServer.ServletMapping;
import nari.httpServer.ServletProvider;

public class StdHttpServer extends AbstractHttpServer {

	private ServletContext context;
	
	public StdHttpServer(InetSocketAddress address, HttpHandler handler, ServletProvider provider,ServletContext context) {
		super(address, handler, provider);
		this.context = context;
	}

	@Override
	public void doStart() {
//		WebContextInitializer.provider = getServletProvider();
//		WebContextInitializer.latch.countDown();
		initContext();
	}

	private void initContext(){
		ServletContextListener[] listeners = getServletProvider().getContextListener();
		if(listeners!=null && listeners.length>0){ 
			for(ServletContextListener listener:listeners){
				context.addListener(listener);
			}
		}
		
		ServletMapping[] mappings = getServletProvider().getServletMapping();
		if(mappings!=null && mappings.length>0){
			for(ServletMapping map:mappings){
				Dynamic servlet = context.addServlet(map.getServletName(), map.getServlet());
				servlet.addMapping(map.getMapping());
				servlet.setLoadOnStartup(map.getInitOrder());
			}
		}
		
		FilterMapping[] filterMappings = getServletProvider().getFilterMapping();
		if(filterMappings!=null && filterMappings.length>0){
			for(FilterMapping map:filterMappings){
				javax.servlet.FilterRegistration.Dynamic filter = context.addFilter(map.getFilterName(), map.getServletFilter());
				EnumSet<DispatcherType> set = EnumSet.noneOf(DispatcherType.class);
				set.add(DispatcherType.REQUEST);
				
				filter.addMappingForUrlPatterns(null, true, "/*");
			}
		}
		
	}
}
