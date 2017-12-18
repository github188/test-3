package org.Invoker.rpc.protocol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nari.httpServer.FilterMapping;
import nari.httpServer.HttpBinder;
import nari.httpServer.HttpServer;
import nari.httpServer.JettyHttpBinder;
import nari.httpServer.ServletMapping;
import nari.httpServer.ServletProvider;

import org.Invoker.DelegateIdentity;
import org.Invoker.Identity;
import org.Invoker.WebServiceClientIdentity;
import org.Invoker.WebServiceServerIdentity;
import org.Invoker.remoting.exchanger.RemotingException;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.exporter.AbstractExporter;
import org.Invoker.rpc.exporter.Exporter;
import org.Invoker.rpc.extension.ExtensionLoader;
import org.Invoker.rpc.invoker.Invoker;
import org.Invoker.rpc.invoker.WebServiceInvoker;
import org.Invoker.rpc.proxy.ProxyFactory;
import org.apache.cxf.bus.extension.ExtensionManagerBus;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transport.http.HTTPTransportFactory;
import org.apache.cxf.transport.http.HttpDestinationFactory;
import org.apache.cxf.transport.servlet.ServletController;
import org.apache.cxf.transport.servlet.ServletDestinationFactory;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

public class WebServiceProtocol extends AbstractProtocol {

	public static final int DEFAULT_PORT = 80;

    private final ExtensionManagerBus bus = new ExtensionManagerBus();

    private final HTTPTransportFactory transportFactory = new HTTPTransportFactory();
    
    private final ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();
	
    private HttpServer server = null;
    
    public WebServiceProtocol() {
        bus.setExtension(new ServletDestinationFactory(), HttpDestinationFactory.class);
    }

    public int getDefaultPort() {
        return DEFAULT_PORT;
    }
    
    private class WebServiceServlet extends HttpServlet{

		private static final long serialVersionUID = -361514258250700313L;
    	
		private volatile ServletController servletController;
		
		@Override
		public void init(ServletConfig config) throws ServletException {
			super.init(config);
		}
		
		
		@Override
		public void init() throws ServletException {
			super.init();
		}
		
		@Override
		protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			if (servletController == null) {
        		synchronized (this) {
        			if (servletController == null) {
            			servletController = new ServletController(transportFactory.getRegistry(), getServletConfig(), this);
        			}
				}
        	}
			servletController.invoke(request, response);
		}
		
    }

    
	@Override
	protected <T> Exporter<T> doExport(Invoker<T> invoker) throws InvokerException {
		HttpBinder binder = new JettyHttpBinder();
		Identity ident = invoker.getIdentity();
		final WebServiceServerIdentity identity = (WebServiceServerIdentity)ident;
		
		String ip = "127.0.0.1";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		InetSocketAddress address = new InetSocketAddress(ip,identity.getPort());
		
		invoker.getIdentity().setAttribute("ProxyFactory", invoker.getIdentity().getAttribute("ProxyFactory","jdk"));
		
		T ref = proxyFactory.getProxy(invoker);
		try {
			ServerFactoryBean serverFactoryBean = new ServerFactoryBean();
	        serverFactoryBean.setAddress("/"+invoker.getInterface().getSimpleName());
	    	serverFactoryBean.setServiceClass(invoker.getInterface());
	    	serverFactoryBean.setServiceBean(ref);
	    	serverFactoryBean.setBus(bus);
	        serverFactoryBean.setDestinationFactory(transportFactory);
	    	serverFactoryBean.create();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	
    	if(server==null){
    		server = binder.bind(address, null , new ServletProvider() {
    			
    			@Override
    			public String[] getWelcomePages() {
    				return null;
    			}
    			
    			@Override
    			public ServletMapping[] getServletMapping() {
    				return new ServletMapping[]{new ServletMapping(){

    					@Override
    					public Servlet getServlet() {
    						return new WebServiceServlet();
    					}

    					@Override
    					public String getServletName() {
    						return "cxfws";
    					}

    					@Override
    					public String getMapping() {
    						return "/services/*";
    					}

    					@Override
    					public Map<String, Object> getInitParameter() {
    						return null;
    					}

    					@Override
    					public int getInitOrder() {
    						return 1;
    					}
    					
    				}};
    			}
    			
    			@Override
    			public String getServletContextName() {
    				return "/NGService";
    			}
    			
    			@Override
    			public int getMinThreadPool() {
    				return identity.getMinThreadPool();
    			}
    			
    			@Override
    			public int getMaxThreadPool() {
    				return identity.getMaxThreadPool();
    			}
    			
    			@Override
    			public FilterMapping[] getFilterMapping() {
    				return null;
    			}
    			
    			@Override
    			public ServletContextListener[] getContextListener() {
    				return null;
    			}

				@Override
				public ServletContext getServletontext() {
					return null;
				}
    		});
    		
    		server.start();
    	}
		
		return new AbstractExporter<T>(invoker) {
			@Override
			public void unexport() throws RemotingException {
				if(server!=null){
					server.close();
				}
				super.unexport();
			}

			@Override
			public void start() {
				
			}

			@Override
			public boolean isStart() {
				return !server.isClosed();
			}

			@Override
			public String serverHost() {
				return identity.getHost();
			}

			@Override
			public int serverPort() {
				return identity.getPort();
			}

			@Override
			public String registryKey() {
				return identity.getHost()+identity.getPort();
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> Invoker<T> doRefer(Class<T> type, Identity ident) throws InvokerException {
		DelegateIdentity delegate = null;
		if(ident instanceof DelegateIdentity){
			delegate = (DelegateIdentity)ident;
		}
		
		WebServiceClientIdentity identity = (WebServiceClientIdentity)delegate.getIdent();
		
		String remoteAddress = delegate.getContent();
		if(!"".equals(remoteAddress)){
			String ip = remoteAddress.split(":")[0];
			int port = Integer.parseInt(remoteAddress.split(":")[1]);
			identity.setRemoteHost(ip);
			identity.setRemotePort(port);
		}
		
		ClientProxyFactoryBean proxyFactoryBean = new ClientProxyFactoryBean();
    	proxyFactoryBean.setAddress("http://"+identity.getRemoteHost()+":"+identity.getRemotePort()+"/NGService/services/"+type.getSimpleName());
    	proxyFactoryBean.setServiceClass(type);
    	proxyFactoryBean.setBus(bus);
    	T ref = (T) proxyFactoryBean.create();
    	Client proxy = ClientProxy.getClient(ref);  
		HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
		HTTPClientPolicy policy = new HTTPClientPolicy();
		policy.setConnectionTimeout(identity.getConnectTimeout());
		policy.setReceiveTimeout(identity.getReciveTimeout());
		conduit.setClient(policy);
		
		ident.setAttribute("ProxyFactory", ident.getAttribute("ProxyFactory","jdk"));
		
		Invoker<T> invoker = proxyFactory.getInvoker(ref, type, ident);
		
		return new WebServiceInvoker<T>(invoker,type);
	}

}
