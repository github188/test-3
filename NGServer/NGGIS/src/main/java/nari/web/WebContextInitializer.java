package nari.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import nari.httpServer.HttpContext;

import org.springframework.web.WebApplicationInitializer;

import com.application.plugin.StandardFramework;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.framework.Framework;

public class WebContextInitializer implements WebApplicationInitializer{

//	private final ExecutorService exec = Executors.newSingleThreadExecutor();
	
//	public static final CountDownLatch latch = new CountDownLatch(1);
//	
//	public static final CountDownLatch waitLatch = new CountDownLatch(1);
	
//	public static ServletProvider provider = null;
	
	public WebContextInitializer() {
		
	}
	
	@Override
	public void onStartup(final ServletContext context) throws ServletException {
		HttpContext.getContext().setHttpBinder(new StdHttpBinder(context));
//		exec.submit(new Runnable() {
//			
//			@Override
//			public void run() {
//				try {
//					latch.await();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				
//				initContext(context);
//				
//				exec.shutdown();
//			}
//			
//		});
		
		Framework framework = new StandardFramework();
		try {
			framework.init(null);
			framework.start();
		} catch (BundleException e) {
			e.printStackTrace();
		}
		
//		try {
//			waitLatch.await();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

//	private void initContext(final ServletContext context){
//		ServletContextListener[] listeners = provider.getContextListener();
//		if(listeners!=null && listeners.length>0){ 
//			for(ServletContextListener listener:listeners){
//				context.addListener(listener);
//			}
//		}
//		
//		ServletMapping[] mappings = provider.getServletMapping();
//		if(mappings!=null && mappings.length>0){
//			for(ServletMapping map:mappings){
//				Dynamic servlet = context.addServlet(map.getServletName(), map.getServlet());
//				servlet.addMapping(map.getMapping());
//				servlet.setLoadOnStartup(map.getInitOrder());
//			}
//		}
//		
//		FilterMapping[] filterMappings = provider.getFilterMapping();
//		if(filterMappings!=null && filterMappings.length>0){
//			for(FilterMapping map:filterMappings){
//				context.addFilter(map.getFilterName(), map.getServletFilter());
//			}
//		}
//		
//		waitLatch.countDown();
//	}
	
}
