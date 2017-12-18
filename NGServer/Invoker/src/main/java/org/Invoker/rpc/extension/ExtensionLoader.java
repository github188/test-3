package org.Invoker.rpc.extension;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nari.Logger.LoggerManager;

import org.Invoker.Identity;
import org.Invoker.rpc.Activate;
import org.Invoker.rpc.Adaptive;
import org.Invoker.rpc.SPI;

public class ExtensionLoader<T> {
	
	private static nari.Logger.Logger logger = LoggerManager.getLogger(ExtensionLoader.class);
	
	private final Class<?> type;
	
//	private final ExtensionFactory extensionFactory;
	
	private final ConcurrentHashMap<String,Holder<Object>> instances = new ConcurrentHashMap<String,Holder<Object>>();
	
	private static final ConcurrentHashMap<Class<?>, ExtensionLoader<?>> loaders = new ConcurrentHashMap<Class<?>,ExtensionLoader<?>>();
	
	private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<Map<String,Class<?>>>();
	
	private final ConcurrentHashMap<Class<?>,Object> extInstances = new ConcurrentHashMap<Class<?>,Object>();
	
	private final List<Class<?>> wrapperExtensions = new ArrayList<Class<?>>();
	
	private final ConcurrentHashMap<String,Activate> activates = new ConcurrentHashMap<String,Activate>();
	
	private volatile Class<?> adaptiveClass = null;
	
	private String cachedDefaultName = null;
	
	private ExtensionLoader(Class<?> type){
		this.type = type;
//		extensionFactory = (type==ExtensionFactory.class)?null:ExtensionLoader.getExtensionLoader(ExtensionFactory.class).getAdaptiveExtension();
		initClasses();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type){
		if(type==null){
			throw new IllegalArgumentException("extension is null");
		}
		
		if(!type.isAnnotationPresent(SPI.class)){
			logger.info(type.getName());
			throw new IllegalArgumentException("extension is not SPI");
		}
		
		ExtensionLoader<T> loader = (ExtensionLoader<T>)loaders.get(type);
		if(loader==null){
			loaders.putIfAbsent(type, new ExtensionLoader<T>(type));
			loader = (ExtensionLoader<T>)loaders.get(type);
		}
		
		return loader;
	}
	
	private void initClasses(){
		Map<String,Class<?>> classes = cachedClasses.get();
		if(classes==null){
			classes = loadExtensionClasses();
			cachedClasses.set(classes);
		}
	}
	
	private Map<String,Class<?>> loadExtensionClasses(){
		if(type.isAnnotationPresent(SPI.class)){
			cachedDefaultName = type.getAnnotation(SPI.class).value();
		}
		
		Map<String,Class<?>> extensionClasses = new HashMap<String,Class<?>>();
		String fileName = "META-INF/invoker/"+type.getName();
        try {
            Enumeration<java.net.URL> urls = null;
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            if (classLoader != null) {
                urls = classLoader.getResources(fileName);
            } else {
                urls = ClassLoader.getSystemResources(fileName);
            }
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    java.net.URL url = urls.nextElement();
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
                        try {
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                final int ci = line.indexOf('#');
                                if (ci >= 0){
                                	line = line.substring(0, ci);
                                }
                                line = line.trim();
                                if (line.length() > 0) {
                                    try {
                                        String name = null;
                                        //http=org.Invoker.rpc.protocol.HttpProtocol
                                        int i = line.indexOf('=');
                                        if (i > 0) {
                                            name = line.substring(0, i).trim();
                                            line = line.substring(i + 1).trim();
                                        }
                                        if (line.length() > 0) {
                                            Class<?> clazz = Class.forName(line, true, classLoader);
                                            if (! type.isAssignableFrom(clazz)) {
                                                throw new IllegalStateException("Error when load extension class(interface: " + type + ", class line: " + clazz.getName() + "), class "  + clazz.getName() + "is not subtype of interface.");
                                            }
                                            if (clazz.isAnnotationPresent(Adaptive.class)) {
                                                if(adaptiveClass == null) {
                                                	adaptiveClass = clazz;
                                                } else if (! adaptiveClass.equals(clazz)) {
                                                    throw new IllegalStateException("More than 1 adaptive class found: " + adaptiveClass.getClass().getName() + ", " + clazz.getClass().getName());
                                                }
                                            } else {
                                                try {
                                                    clazz.getConstructor(type);
                                                    wrapperExtensions.add(clazz);
                                                } catch (NoSuchMethodException e) {
                                                    clazz.getConstructor();
                                                    Activate activate = clazz.getAnnotation(Activate.class);
                                                    if (activate != null) {
                                                    	activates.put(name, activate);
                                                    }
                                                    Class<?> c = extensionClasses.get(name);
                                                    if (c == null) {
                                                        extensionClasses.put(name, clazz);
                                                    } else if (c != clazz) {
                                                        throw new IllegalStateException("Duplicate extension " + type.getName() + " name " + name + " on " + c.getName() + " and " + clazz.getName());
                                                    }
                                                }
                                            }
                                        }
                                    } catch (Throwable t) {
                                    	t.printStackTrace();
                                    }
                                }
                            }
                        } finally {
                            reader.close();
                        }
                    } catch (Throwable t) {
                    	t.printStackTrace();
                    }
                }
            }
        } catch (Throwable t) {
        	t.printStackTrace();
        }
		
		return extensionClasses;
	}
	
	public List<String> getSupportedExtensions(){
		Map<String,Class<?>> classes = cachedClasses.get();
		List<String> list = new ArrayList<String>();
		for(Map.Entry<String, Class<?>> entry:classes.entrySet()){
			list.add(entry.getKey());
		}
		return Collections.unmodifiableList(list);
	}
	
	public T getDefaultExtension(){
		return getExtension(cachedDefaultName);
	}
	
	@SuppressWarnings("unchecked")
	public T getAdaptiveExtension(){
		try {
			return injectExtension((T)getAdaptiveExtensionClass().newInstance());
		} catch (Exception e) {
			throw new IllegalStateException("no adaptive extension",e);
		}
	}
	
	private Class<?> getAdaptiveExtensionClass(){
		if(adaptiveClass!=null){
			return adaptiveClass;
		}
		adaptiveClass = wrapperAdaptiveClass();
		return adaptiveClass;
	}
	
	private Class<?> wrapperAdaptiveClass(){
		String code = createAdaptiveExtensionClassCode();
		ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
		org.Invoker.compiler.Compiler compiler = ExtensionLoader.getExtensionLoader(org.Invoker.compiler.Compiler.class).getAdaptiveExtension();
		return compiler.compile(code, classLoader);
	}
	
	 private String createAdaptiveExtensionClassCode() {
        StringBuilder codeBuidler = new StringBuilder();
        Method[] methods = type.getMethods();
        boolean hasAdaptiveAnnotation = false;
        for(Method m : methods) {
            if(m.isAnnotationPresent(Adaptive.class)) {
                hasAdaptiveAnnotation = true;
                break;
            }
        }
        if(!hasAdaptiveAnnotation){
            throw new IllegalStateException("No adaptive method on extension " + type.getName() + ", refuse to create the adaptive class!");
        }
        
        codeBuidler.append("package " + type.getPackage().getName() + ";");
        codeBuidler.append("\nimport " + ExtensionLoader.class.getName() + ";");
        codeBuidler.append("\npublic class " + type.getSimpleName() + "$Adpative" + " implements " + type.getCanonicalName() + " {");
        
        for (Method method : methods) {
            Class<?> rt = method.getReturnType();
            Class<?>[] pts = method.getParameterTypes();
            Class<?>[] ets = method.getExceptionTypes();

            Adaptive adaptiveAnnotation = method.getAnnotation(Adaptive.class);
            StringBuilder code = new StringBuilder(512);
            if (adaptiveAnnotation == null) {
                code.append("throw new UnsupportedOperationException(\"method ").append(method.toString()).append(" of interface ").append(type.getName()).append(" is not adaptive method!\");");
            } else {
                int urlTypeIndex = -1;
                for (int i = 0; i < pts.length; ++i) {
                    if (pts[i].equals(Identity.class)) {
                        urlTypeIndex = i;
                        break;
                    }
                }
                if (urlTypeIndex != -1) {
                    String s = String.format("\nif (arg%d == null) throw new IllegalArgumentException(\"url == null\");", urlTypeIndex);
                    code.append(s);
                    
                    s = String.format("\n%s url = arg%d;", Identity.class.getName(), urlTypeIndex); 
                    code.append(s);
                }else {
                    String attribMethod = null;
                    LBL_PTS:
                    for (int i = 0; i < pts.length; ++i) {
                        Method[] ms = pts[i].getMethods();
                        for (Method m : ms) {
                            String name = m.getName();
                            if ((name.startsWith("get") || name.length() > 3) && Modifier.isPublic(m.getModifiers())&& !Modifier.isStatic(m.getModifiers())&& m.getParameterTypes().length == 0&& m.getReturnType() == Identity.class) {
                                urlTypeIndex = i;
                                attribMethod = name;
                                break LBL_PTS;
                            }
                        }
                    }
                    if(attribMethod == null) {
                        throw new IllegalStateException("fail to create adative class for interface " + type.getName() + ": not found url parameter or url attribute in parameters of method " + method.getName());
                    }
                    
                    String s = String.format("\nif (arg%d == null) throw new IllegalArgumentException(\"%s argument == null\");", urlTypeIndex, pts[urlTypeIndex].getName());
                    code.append(s);
                    s = String.format("\nif (arg%d.%s() == null) throw new IllegalArgumentException(\"%s argument %s() == null\");", urlTypeIndex, attribMethod, pts[urlTypeIndex].getName(), attribMethod);
                    code.append(s);

                    s = String.format("%s url = arg%d.%s();",Identity.class.getName(), urlTypeIndex, attribMethod); 
                    code.append(s);
                }
                
                String[] value = adaptiveAnnotation.value();
                if(value.length == 0) {
//                    char[] charArray = type.getSimpleName().toCharArray();
//                    StringBuilder sb = new StringBuilder(128);
//                    for (int i = 0; i < charArray.length; i++) {
//                        if(Character.isUpperCase(charArray[i])) {
//                            if(i != 0) {
//                                sb.append(".");
//                            }
//                            sb.append(Character.toLowerCase(charArray[i]));
//                        }
//                        else {
//                            sb.append(charArray[i]);
//                        }
//                    }
//                    value = new String[] {sb.toString()};
                	value = new String[] {type.getSimpleName()};
                }
                
                boolean hasInvocation = false;
                for (int i = 0; i < pts.length; ++i) {
                    if (pts[i].getName().equals("org.Invoker.rpc.invoker.Invocation")) {
                        String s = String.format("\nif (arg%d == null) throw new IllegalArgumentException(\"invocation == null\");", i);
                        code.append(s);
                        s = String.format("\nString methodName = arg%d.getMethodName();", i); 
                        code.append(s);
                        hasInvocation = true;
                        break;
                    }
                }
                
                String defaultExtName = cachedDefaultName;
                String getNameCode = null;
                for (int i = value.length - 1; i >= 0; --i) {
                    if(i == value.length - 1) {
                        if(null != defaultExtName) {
                            if(!"Protocol".equals(value[i])){
                                if (hasInvocation) {
                                    getNameCode = String.format("url.getMethodParameter(methodName, \"%s\", \"%s\")", value[i], defaultExtName);
                                } else {
                                    getNameCode = String.format("url.getAttribute(\"%s\", \"%s\")", value[i], defaultExtName);
                                }
                            } else {
                                getNameCode = String.format("( url.getProtocol() == null ? \"%s\" : url.getProtocol() )", defaultExtName);
                            }
                        } else {
                            if(!"Protocol".equals(value[i])){
                                if (hasInvocation) {
                                    getNameCode = String.format("url.getMethodParameter(methodName, \"%s\", \"%s\")", value[i], defaultExtName);
                                } else {
                                    getNameCode = String.format("url.getAttribute(\"%s\")", value[i]);
                                }
                            }else{
                                getNameCode = "url.getProtocol()";
                            }
                        }
                    }
                    else {
                        if(!"Protocol".equals(value[i])){
                            if (hasInvocation) {
                                getNameCode = String.format("url.getMethodParameter(methodName, \"%s\", \"%s\")", value[i], defaultExtName);
                            } else{
                            	getNameCode = String.format("url.getAttribute(\"%s\", %s)", value[i], getNameCode);
                            }
                        }else{
                            getNameCode = String.format("url.getProtocol() == null ? (%s) : url.getProtocol()", getNameCode);
                        }
                    }
                }
                code.append("\nString extName = ").append(getNameCode).append(";");
                String s = String.format("\nif(extName == null || \"\".equals(extName)) " + "throw new IllegalStateException(\"Fail to get extension(%s) name from url(\" + url.toString() + \") use keys(%s)\");",type.getName(), Arrays.toString(value));
                code.append(s);
                
                s = String.format("\n%s extension = (%<s)%s.getExtensionLoader(%s.class).getExtension(extName);", type.getName(), ExtensionLoader.class.getSimpleName(), type.getName());
                code.append(s);
                
                if (!rt.equals(void.class)) {
                    code.append("\nreturn ");
                }

                s = String.format("extension.%s(", method.getName());
                code.append(s);
                for (int i = 0; i < pts.length; i++) {
                    if (i != 0){
                        code.append(", ");
                    }
                    code.append("arg").append(i);
                }
                code.append(");");
            }
            
            codeBuidler.append("\npublic " + rt.getCanonicalName() + " " + method.getName() + "(");
            for (int i = 0; i < pts.length; i ++) {
                if (i > 0) {
                    codeBuidler.append(", ");
                }
                codeBuidler.append(pts[i].getCanonicalName());
                codeBuidler.append(" ");
                codeBuidler.append("arg" + i);
            }
            codeBuidler.append(")");
            if (ets.length > 0) {
                codeBuidler.append(" throws ");
                for (int i = 0; i < ets.length; i ++) {
                    if (i > 0) {
                        codeBuidler.append(", ");
                    }
                    codeBuidler.append(ets[i].getCanonicalName());
                }
            }
            codeBuidler.append(" {");
            codeBuidler.append(code.toString());
            codeBuidler.append("\n}");
        }
        codeBuidler.append("\n}");
        return codeBuidler.toString();
	}
	 
	@SuppressWarnings("unchecked")
	public T getExtension(String name){
		Holder<Object> holder = instances.get(name);
		if(holder==null){
			instances.putIfAbsent(name,new Holder<Object>());
			holder = instances.get(name);
		}
		Object instance = holder.get();
		if(instance==null){
			synchronized (holder) {
				instance = holder.get();
				if(instance==null){
					instance = createExtension(name);
					holder.set(instance);
				}
			}
		}
		
		return (T)instance;
	}
	
	public List<T> getActivateExtension(String group){
		List<T> exts = new ArrayList<T>();
		for(Map.Entry<String, Activate> entry:activates.entrySet()){
			String name = entry.getKey();
            Activate activate = entry.getValue();
            if(Arrays.binarySearch(activate.group(), group)>=0){
            	exts.add(getExtension(name));
            }
		}
		return exts;
	}
	
	@SuppressWarnings("unchecked")
	private T createExtension(String name){
		Class<?> clazz = cachedClasses.get().get(name);
		T instance = (T)extInstances.get(clazz);
		if(instance==null){
			try {
				extInstances.putIfAbsent(clazz, (T)clazz.newInstance());
				instance = (T)extInstances.get(clazz);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			injectExtension(instance);
			synchronized (wrapperExtensions) {
				if(wrapperExtensions!=null && wrapperExtensions.size()>0){
					for(Class<?> wrapperClass:wrapperExtensions){
						try {
							instance = injectExtension((T)wrapperClass.getConstructor(type).newInstance(instance));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return instance;
	}
	
	private T injectExtension(T instance) {
        try {
//            if (extensionFactory != null) {
//                for (Method method : instance.getClass().getMethods()) {
//                    if (method.getName().startsWith("set") && method.getParameterTypes().length == 1 && Modifier.isPublic(method.getModifiers())) {
//                        Class<?> pt = method.getParameterTypes()[0];
//                        try {
//                            String property = method.getName().length() > 3 ? method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4) : "";
//                            Object object = extensionFactory.getExtension(pt, property);
//                            if (object != null) {
//                                method.invoke(instance, object);
//                            }
//                        } catch (Exception e) {
//                        	
//                        }
//                    }
//                }
//            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return instance;
    }
}
