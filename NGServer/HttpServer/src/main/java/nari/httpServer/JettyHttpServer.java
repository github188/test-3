package nari.httpServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContextListener;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.FilterHolder;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.thread.QueuedThreadPool;

public class JettyHttpServer extends AbstractHttpServer {
	
	private Server server = null;
	
	protected Logger log = Logger.getLogger(JettyHttpServer.class.getName());
	
	public JettyHttpServer(InetSocketAddress address, HttpHandler handler,ServletProvider provider) {
		super(address,handler,provider);
		try {
			QueuedThreadPool threadPool = new QueuedThreadPool();
	        threadPool.setDaemon(true);
	        threadPool.setMaxThreads(getServletProvider().getMaxThreadPool());
	        threadPool.setMinThreads(getServletProvider().getMinThreadPool());
	        
	        SelectChannelConnector connector = new SelectChannelConnector();
	        connector.setUseDirectBuffers(false);
	        connector.setHost(getOSAddress());
	        connector.setPort(address.getPort());
	        

	        server = new Server();
	        server.setThreadPool(threadPool);
	        server.addConnector(connector);
	        
	        
			Context root = new Context(server, getServletProvider().getServletContextName(), Context.DEFAULT);

			ServletContextListener[] listeners = getServletProvider().getContextListener();
			if(listeners!=null && listeners.length>0){ 
				for(ServletContextListener listener:listeners){
					root.addEventListener(listener);
				}
			}
			
			ServletMapping[] mappings = getServletProvider().getServletMapping();
			if(mappings!=null && mappings.length>0){
				for(ServletMapping map:mappings){
					ServletHolder holder = new ServletHolder(map.getServlet());
					holder.setName(map.getServletName());
					if(map.getInitParameter()!=null){
						holder.setInitParameters(map.getInitParameter());
					}
					holder.setInitOrder(map.getInitOrder());
					root.addServlet(holder, map.getMapping());
				}
			}
			
			FilterMapping[] filterMappings = getServletProvider().getFilterMapping();
			if(filterMappings!=null && filterMappings.length>0){
				for(FilterMapping map:filterMappings){
					FilterHolder filterHolder = new FilterHolder(map.getServletFilter());
					filterHolder.setName(map.getFilterName());
					root.addFilter(filterHolder, map.getMapping(), org.mortbay.jetty.Handler.DEFAULT);
				}
			}
			
			root.setWelcomeFiles(getServletProvider().getWelcomePages());

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void doStart() {
		try {
			server.start();
//			server.join();
			log.info("JettyHttpServer start success!");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private String getOSAddress(){
		String osName = System.getProperty("os.name");
		String ip = "";
		if(osName.contains("win")){
			ip = getWindowsIP();
		}else{
			ip = getLinuxIP();
		}
		return ip;
	}
	
	private String getWindowsIP(){
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		return "127.0.0.1";
	}
	
	private String getLinuxIP(){
		String realIp = "127.0.0.1";
		try {
			 Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
			 while(nis.hasMoreElements()){
				 NetworkInterface n = nis.nextElement();
				 Enumeration<InetAddress> a = n.getInetAddresses();
				 while(a.hasMoreElements()){
					 InetAddress addr = a.nextElement();
					 String ip = addr.getHostAddress();
					 if(matchAddress(ip) || ip.equals("127.0.0.1")){
						 continue;
					 }
					 realIp = ip;
				 }
			 }
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		return realIp;
	}
	
	private boolean matchAddress(String ip){
		Pattern p = Pattern.compile("^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*$");
		Matcher m = p.matcher(ip);
		
		if(m.find()){
			return true;
		}
		return false;
	}
}
