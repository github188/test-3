package org.Invoker.rpc.protocol;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import nari.httpServer.HttpBinder;
import nari.httpServer.HttpContext;
import nari.httpServer.HttpServer;
import nari.httpServer.ServletProvider;

import org.Invoker.HttpServerIdentity;
import org.Invoker.Identity;
import org.Invoker.compiler.support.AdaptiveCompiler;
import org.Invoker.remoting.exchanger.RemotingException;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.exporter.AbstractExporter;
import org.Invoker.rpc.exporter.Exporter;
import org.Invoker.rpc.extension.ExtensionLoader;
import org.Invoker.rpc.invoker.Invoker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public abstract class HttpProtocol extends AbstractProtocol {

//	private nari.Logger.Logger logger = LoggerManager.getLogger(this.getClass());
	
	private HttpServer server = null;
	
	private final AtomicInteger serverRef = new AtomicInteger(0);
	
	private final List<Class<?>> serviceClass = new ArrayList<Class<?>>();
	
	public static final ConcurrentMap<String, Invoker<?>> invokerMap = new ConcurrentHashMap<>();
	 
	public HttpProtocol(){
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected <T> Exporter<T> doExport(final Invoker<T> invoker) throws InvokerException {
		final HttpServerIdentity identity = (HttpServerIdentity)invoker.getIdentity();
		final InetSocketAddress address = new InetSocketAddress(identity.getIp(),identity.getPort());
		
		String invokerId = UUID.randomUUID().toString();
		invokerMap.put(invokerId, invoker);
		
		Class<T> interfaces = invoker.getInterface();
		
		if(!interfaces.isInterface()){
			if(interfaces.getInterfaces().length!=1){
				throw new InvokerException(interfaces.getName() +" have too much interfaces");
			}
			interfaces = (Class<T>)interfaces.getInterfaces()[0];
		}
		
		
		String code = createAdaptiveClassCode(interfaces,invokerId);
//		logger.info(code);
		ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
		AdaptiveCompiler.setDefaultCompiler("jdk");
		org.Invoker.compiler.Compiler compiler = ExtensionLoader.getExtensionLoader(org.Invoker.compiler.Compiler.class).getAdaptiveExtension();
		Class<?> adapterClass = compiler.compile(code, classLoader);
		AdaptiveCompiler.setDefaultCompiler("");
		
		serviceClass.add(adapterClass);
		serverRef.incrementAndGet();
		
		
		return new AbstractExporter<T>(invoker) {
			@Override
			public void unexport() throws RemotingException {
				if(serverRef.decrementAndGet()==0){
					if(server!=null){
						server.close();
					}
				}
				super.unexport();
			}

			@Override
			public void start() {
				HttpBinder binder = HttpContext.getContext().getHttpBinder();
				
				if(binder==null){
					throw new RuntimeException("null http binder");
				}
				server = binder.bind(address, null,getServletProvider(identity,serviceClass));
				server.start();
			}

			@Override
			public boolean isStart() {
				return server!=null && !server.isClosed();
			}

			@Override
			public String serverHost() {
				return identity.getIp();
			}

			@Override
			public int serverPort() {
				return identity.getPort();
			}

			@Override
			public String registryKey() {
				RequestMapping requestMapping = invoker.getInterface().getAnnotation(RequestMapping.class);
				if(requestMapping==null){
					return "";
				}
				
				Class<?> clazz = invoker.getInterface();
				if(!clazz.isInterface()){
					clazz = (clazz.getInterfaces()==null||clazz.getInterfaces().length==0)?null:clazz.getInterfaces()[0];
				}
				if(clazz==null){
					return "";
				}
				
				String[] servives = requestMapping.value();
				if(servives==null || servives.length==0){
					return "";
				}
				
				String serviceName = servives[0].substring(1);
				String key = serverHost()+":"+serverPort()+"#"+serviceName;
				return key;
			}
		};
	}

	private String createAdaptiveClassCode(Class<?> type,String invokerId) {
        StringBuilder codeBuidler = new StringBuilder();
        Method[] methods = type.getMethods();
        
        
        codeBuidler.append("package " + type.getPackage().getName() + ".Adapter;");
        codeBuidler.append("\nimport org.springframework.stereotype.Controller;");
        codeBuidler.append("\nimport org.springframework.web.bind.annotation.RequestMapping;");
        codeBuidler.append("\nimport org.springframework.web.bind.annotation.ResponseBody;");
        codeBuidler.append("\nimport org.springframework.web.bind.annotation.RequestMethod;");
        codeBuidler.append("\nimport org.Invoker.rpc.extension.ExtensionLoader;");
        codeBuidler.append("\nimport org.Invoker.rpc.invoker.Invoker;");
        codeBuidler.append("\nimport org.Invoker.rpc.proxy.ProxyFactory;");
        codeBuidler.append("\n");
        
        Controller controller = type.getAnnotation(Controller.class);
        RequestMapping requestMapping = type.getAnnotation(RequestMapping.class);
        if(controller==null){
        	return "";
        }
        
        codeBuidler.append("@Controller(\"").append(controller.value()).append("\"").append(")");
        codeBuidler.append("\n");
        codeBuidler.append("@RequestMapping(");
        String[] maps = requestMapping.value();
		if(maps!=null && maps.length>0){
			codeBuidler.append("value={");
			int i = 0;
			for(String value:maps){
				codeBuidler.append("\"").append(value).append("\"");
				if(i<maps.length-1){
					codeBuidler.append(",");
					i++;
				}
			}
			codeBuidler.append("}");
		}
		codeBuidler.append(")");
		
        codeBuidler.append("\npublic class " + type.getSimpleName() + "$Adapter" + " implements " + type.getCanonicalName() + " {");
        
        codeBuidler.append("\nprivate final String invokerId = \"").append(invokerId).append("\";");
        codeBuidler.append("\n");
        
        codeBuidler.append("private final ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();");
        codeBuidler.append("\n");
        
        codeBuidler.append("public "+type.getSimpleName() + "$Adapter(){");
        codeBuidler.append("}\n");
        
        for (Method method : methods) {
            if(!Modifier.isPublic(method.getModifiers())){
            	continue;
            }
            
            
            Class<?> rt = method.getReturnType();
            Class<?>[] pts = method.getParameterTypes();
            Class<?>[] ets = method.getExceptionTypes();
            Annotation[] annos = method.getAnnotations();
            StringBuilder code = new StringBuilder(512);
            if(annos!=null && annos.length>0){
            	for(Annotation an:annos){
                	if(RequestMapping.class==an.annotationType()){
                		RequestMapping map = method.getAnnotation(RequestMapping.class);
                		
                		code.append("@RequestMapping(");
                		
                		String[] values = map.value();
                		if(values!=null && values.length>0){
                			code.append("value={");
                			int i = 0;
                			for(String value:values){
                				code.append("\"").append(value).append("\"");
                				if(i<values.length-1){
                					code.append(",");
                					i++;
                				}
                			}
                			code.append("}");
                		}
                		
                		String[] consumes = map.consumes();
                		if(consumes!=null && consumes.length>0){
                			code.append(",");
                			code.append("consumes={");
                			int i = 0;
                			for(String value:consumes){
                				code.append("\"").append(value).append("\"");
                				if(i<values.length-1){
                					code.append(",");
                					i++;
                				}
                			}
                			code.append("}");
                		}
                		
                		String[] headers = map.headers();
                		if(headers!=null && headers.length>0){
                			code.append(",");
                			code.append("headers={");
                			int i = 0;
                			for(String value:headers){
                				code.append("\"").append(value).append("\"");
                				if(i<values.length-1){
                					code.append(",");
                					i++;
                				}
                			}
                			code.append("}");
                		}
                		
                		RequestMethod[] reqMethods = map.method();
                		if(reqMethods!=null && reqMethods.length>0){
                			code.append(",");
            				code.append("method={");
                			int i = 0;
                			for(RequestMethod m:reqMethods){
                				code.append("RequestMethod.").append(m.toString());
                				if(i<reqMethods.length-1){
                					code.append(",");
                					i++;
                				}
                			}
                			code.append("}");
                		}
                		
                		String name = map.name();
                		if(name!=null && !"".equals(name)){
                			code.append(",");
                			code.append("name=\"").append(name).append("\"");
                		}
                		
                		String[] params = map.params();
                		if(params!=null && params.length>0){
                			code.append(",");
                			code.append("params={");
                			int i = 0;
                			for(String value:params){
                				code.append("\"").append(value).append("\"");
                				if(i<values.length-1){
                					code.append(",");
                					i++;
                				}
                			}
                			code.append("}");
                		}
                		
                		String[] path = map.path();
                		if(path!=null && path.length>0){
                			code.append(",");
                			code.append("path={");
                			int i = 0;
                			for(String value:path){
                				code.append("\"").append(value).append("\"");
                				if(i<values.length-1){
                					code.append(",");
                					i++;
                				}
                			}
                			code.append("}");
                		}
                		
                		String[] produces = map.produces();
                		if(produces!=null && produces.length>0){
                			code.append(",");
                			code.append("produces={");
                			int i = 0;
                			for(String value:produces){
                				code.append("\"").append(value).append("\"");
                				if(i<values.length-1){
                					code.append(",");
                					i++;
                				}
                			}
                			code.append("}");
                		}
                		
                		code.append(")");
                	}else{
                		if(!an.annotationType().getName().startsWith("@")){
                			code.append("@");
                		}
                		code.append(an.annotationType().getName()).append("()");
                	}
                }
            }
            
            code.append("\n");
            code.append("public ");
            
            code.append(rt.getName()).append(" ").append(method.getName()).append("(");
            if(pts!=null && pts.length>0){
            	int i=0;
            	Annotation[][] anos = method.getParameterAnnotations();
                for(Class<?> pt:pts){
                	Annotation[] an=anos[i];
                	if(an!=null && an.length>0){
                		Annotation a=an[0];
                		if(a.annotationType()==RequestParam.class){
                			RequestParam r = (RequestParam)a;
                			String value = r.value();
                    		code.append("@org.springframework.web.bind.annotation.RequestParam(\"").append(value).append("\")");
                		}
                	}
                	
                	code.append(pt.getName()).append(" ").append("arg"+i);
                	if(i<pts.length-1){
                		code.append(",");
                		i++;
                	}
                }
            }
            code.append(")");
            
            if(ets!=null && ets.length>0){
            	code.append(" throws ");
            	int i=0;
            	for(Class<?> et:ets){
            		code.append(et.getName());
            		if(i<ets.length-1){
            			code.append(",");
            			i++;
            		}
            	}
            }
            
            code.append("{");
            code.append("\n");
            
//            code.append("try {\n");
            
            code.append("org.Invoker.rpc.invoker.Invoker<?> invoker = org.Invoker.rpc.protocol.HttpProtocol.invokerMap.get(invokerId);");
            code.append("\n");
            
            code.append("invoker.getIdentity().setAttribute(\"ProxyFactory\", invoker.getIdentity().getAttribute(\"ProxyFactory\",\"jdk\"));");
            code.append("\n");
            
            code.append(type.getName()).append(" proxy = ("+type.getName()+")proxyFactory.getProxy(invoker);");
            code.append("\n");
            
            code.append("return proxy.").append(method.getName()).append("(");
            for(int i=0;i<pts.length;i++){
            	code.append("arg"+i);
            	if(i<pts.length-1){
            		code.append(",");
            	}
            }
            code.append(");\n");
//            code.append("} catch (Exception e) {\n");
//			code.append("e.printStackTrace();");
//        	code.append("\n}\n");
//        	code.append("return null;");
            code.append("\n}");
            
            code.append("\n");
            
            codeBuidler.append(code.toString());
            
        }
        codeBuidler.append("\n}");
        return codeBuidler.toString();
	}
	
	@Override
	protected <T> Invoker<T> doRefer(Class<T> type, Identity ident) throws InvokerException {
		return getInvoker(type,ident);
	}

	public abstract <T> Invoker<T> getInvoker(Class<T> type, Identity ident);
	
	protected abstract <T> ServletProvider getServletProvider(HttpServerIdentity identity,List<Class<?>> serviceClass);
	
}
