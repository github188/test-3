//package nari.httpServer;
//
//import java.util.HashMap;
//import java.util.Map;
//import org.apache.jasper.servlet.JspServlet;
//import org.eclipse.jetty.util.thread.QueuedThreadPool;
//import org.mortbay.jetty.Server;
//import org.mortbay.jetty.servlet.Context;
//import org.mortbay.jetty.servlet.DefaultServlet;
//import org.mortbay.jetty.servlet.FilterHolder;
//import org.mortbay.jetty.servlet.ServletHandler;
//import org.mortbay.jetty.servlet.ServletHolder;
//import org.springframework.js.resource.ResourceServlet;
//import org.springframework.web.context.ContextLoaderListener;
//import org.springframework.web.filter.DelegatingFilterProxy;
//import org.springframework.web.servlet.DispatcherServlet;
//
//public class mvc {
//
//	/**
//	 * 服务器启动。
//	 */
//	public void start() {
//		QueuedThreadPool threadPool = new QueuedThreadPool();
//        threadPool.setDaemon(true);
//        threadPool.setMaxThreads(2);
//        threadPool.setMinThreads(2);
//
//        SelectChannelConnector connector = new SelectChannelConnector();
//        connector.setHost("");
//        connector.setPort("");
//
//        Server server = new Server();
//        server.setThreadPool(threadPool);
//        server.addConnector(connector);
//        
//		Context root = new Context(server, "/booking-mvc", Context.SESSIONS);
//
//		ContextLoaderListener listener = new ContextLoaderListener();
//
//		// Map<String, String> initParams = new HashMap<String, String>();
//		// initParams.put("contextConfigLocation",
//		// "/WEB-INF/config/web-application-config.xml");
//		// root.setInitParams(initParams);
//		// root.setResourceBase("E:/sources/spring/spring-webflow-2.0.5.RELEASE/projects/spring-webflow-samples/booking-mvc/src/main/webapp");
//		root.addEventListener(listener);
//
//		ServletHolder holder = new ServletHolder(new ResourceServlet());
//		
//		root.addServlet(holder, "/resources/*");
//
//		AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
//		webContext.register(SpringMVC.class, ViewConfiguration.class);
//		webContext.scan("");
//		holder = new ServletHolder(new DispatcherServlet(webContext));
//		
//		
////		holder.setInitParameter("contextConfigLocation", "/WEB-INF/config/web-application-config.xml");
//		root.addServlet(holder, "/spring/*");
//
//		holder = new ServletHolder(new JspServlet());
//		root.addServlet(holder, "*.jsp");
//
//		holder = new ServletHolder(new DefaultServlet());
//		root.addServlet(holder, "*.html");
//
//		root.setWelcomeFiles(new String[] { "index.html" });
//
//		FilterHolder filterHolder = new FilterHolder(DelegatingFilterProxy.class);
//		filterHolder.setName("springSecurityFilterChain");
//		root.addFilter(filterHolder, "/*", org.mortbay.jetty.Handler.DEFAULT);
//
//		//----------------------
//		ServletHandler servletHandler = new ServletHandler();
//        ServletHolder servletHolder = servletHandler.addServletWithMapping(DispatcherServlet.class, "/*");
//        servletHolder.setInitOrder(2);
//        
//        server.addHandler(servletHandler);
//		try {
//
//			server.start();
//			server.join();
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//}