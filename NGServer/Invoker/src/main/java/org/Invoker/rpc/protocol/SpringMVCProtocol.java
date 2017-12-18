package org.Invoker.rpc.protocol;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;

import nari.httpServer.FilterMapping;
import nari.httpServer.ServletMapping;
import nari.httpServer.ServletProvider;

import org.Invoker.DelegateIdentity;
import org.Invoker.HttpClientIdentity;
import org.Invoker.HttpServerIdentity;
import org.Invoker.Identity;
import org.Invoker.rpc.invoker.HttpInvoker;
import org.Invoker.rpc.invoker.Invoker;
import org.Invoker.rpc.invoker.NoneInvoker;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class SpringMVCProtocol extends HttpProtocol {

	public SpringMVCProtocol() {
		
	}
	
	@Override
	protected <T> ServletProvider getServletProvider(final HttpServerIdentity identity, final List<Class<?>> serviceClass) {
		return new ServletProvider() {
			
			@Override
			public String[] getWelcomePages() {
				return new String[]{"index.html","index.jsp","index.htm"};
			}
			
			@Override
			public ServletMapping[] getServletMapping() {

				ServletMapping[] mappings = new ServletMapping[1];
//				mappings[1] = new ServletMapping() {
//				
//				@Override
//				public String getServletName() {
//					return "resourceServlet";
//				}
//				
//				@Override
//				public Servlet getServlet() {
//					ResourceServlet servlet = new ResourceServlet();
//					servlet.setDefaultUrl("/");
//					servlet.setContentType("text/html");
//					return servlet;
//				}
//				
//				@Override
//				public String getMapping() {
//					return "/resource/*";
//				}
//
//				@Override
//				public Map<String, Object> getInitParameter() {
//					Map<String, Object> params = new HashMap<String,Object>();
////					params.put("dirAllowed", "true");
//					params.put("resource", "/WEB-INF/resources");
//					return params;
//				}
//
//				@Override
//				public int getInitOrder() {
//					return 2;
//				}
//			};
				
//				mappings[0] = new ServletMapping() {
//					
//					@Override
//					public String getServletName() {
//						return "DefaultServlet";
//					}
//					
//					@Override
//					public Servlet getServlet() {
//						DefaultServlet servlet = new DefaultServlet();
//						return servlet;
//					}
//					
//					@Override
//					public String getMapping() {
//						return "/html/*";
//					}
//
//					@Override
//					public Map<String, Object> getInitParameter() {
//						Map<String, Object> params = new HashMap<String,Object>();
//						params.put("dirAllowed", "true");
////						params.put("resource", "/WEB-INF/resources");
//						return params;
//					}
//
//					@Override
//					public int getInitOrder() {
//						return 0;
//					}
//				};
				
				mappings[0] = new ServletMapping() {
					
					@Override
					public String getServletName() {
						return "mvcServlet";
					}
					
					@Override
					public Servlet getServlet() {
						AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
						webContext.register(SpringMVC.class, ViewConfiguration.class);
						
						Class<?>[] clazzs = new Class<?>[serviceClass.size()];
						clazzs = serviceClass.toArray(clazzs);
						webContext.register(clazzs);
						return new DispatcherServlet(webContext);
					}
					
					@Override
					public String getMapping() {
						return "/*";
					}

					@Override
					public Map<String, Object> getInitParameter() {
						return null;
					}

					@Override
					public int getInitOrder() {
						return 1;
					}
				};
				
//				mappings[2] = new ServletMapping() {
//					
//					@Override
//					public String getServletName() {
//						return "defServlet";
//					}
//					
//					@Override
//					public Servlet getServlet() {
//						return new DefaultServlet();
//					}
//					
//					@Override
//					public String getMapping() {
//						return "/page/*";
//					}
//
//					@Override
//					public Map<String, Object> getInitParameter() {
//						Map<String,Object> params = new HashMap<String,Object>();
//						params.put("resourceBase", "src/main/webapp");
//						params.put("cacheType", "nio");
//						return null;
//					}
//
//					@Override
//					public int getInitOrder() {
//						return 0;
//					}
//				};
				
				return mappings;
			}
			
			@Override
			public String getServletContextName() {
				return "/NGServer";
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
				FilterMapping[] mappings = new FilterMapping[1];
				
				mappings[0] = new FilterMapping() {
					
					@Override
					public Class<? extends Filter> getServletFilter() {
						return CharsetEncodeFilter.class;
					}
					
					@Override
					public String getMapping() {
						return "/*";
					}
					
					@Override
					public String getFilterName() {
						return "encodingFilter";
					}
				};
				
//				mappings[0] = new FilterMapping() {
//					
//					@Override
//					public Class<? extends Filter> getServletFilter() {
//						return DelegatingFilterProxy.class;
//					}
//					
//					@Override
//					public String getMapping() {
//						return "/*";
//					}
//					
//					@Override
//					public String getFilterName() {
//						return "springSecurityFilterChain";
//					}
//				};
				
				return mappings;
			}
			
			@Override
			public ServletContextListener[] getContextListener() {
//				ServletContextListener[] listeners = new ServletContextListener[1];
//				listeners[0] = new ContextLoaderListener();
//				return listeners;
				return null;
			}

			@Override
			public ServletContext getServletontext() {
				return null;
			}
		};
	}

	@Override
	public <T> Invoker<T> getInvoker(Class<T> type, Identity ident) {
		DelegateIdentity delegate = null;
		if(ident instanceof DelegateIdentity){
			delegate = (DelegateIdentity)ident;
		}
		
		HttpClientIdentity identity = (HttpClientIdentity)delegate.getIdent();
		String key = delegate.getContent();
		
		if(key==null || "".equals(key)){
			return new NoneInvoker<>();
		}
		
		String[] s = key.split("#");
		if(s.length!=2){
			return new NoneInvoker<>();
		}
		String address = s[0];
		String serviceName = s[1];
		
		String uri = "http://"+address+"/NGServer/"+serviceName+"/";
		
		Map<Method,RequestMapping> methodMap = new HashMap<Method,RequestMapping>();
		
		Method[] methods =type.getMethods();
		for(Method method:methods){
			RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
			if(requestMapping==null){
				continue;
			}
			
			methodMap.put(method, requestMapping);
		}
		
		return new HttpInvoker<T>(uri,methodMap,type,identity);
	}
	
}
