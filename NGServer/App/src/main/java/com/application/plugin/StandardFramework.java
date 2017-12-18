package com.application.plugin;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.application.plugin.bundle.Bundle;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.bundle.Bundles;
import com.application.plugin.framework.AbstraceFramework;

public class StandardFramework extends AbstraceFramework {

	private final List<Bundle> bundles = new ArrayList<Bundle>();
	
	public StandardFramework(){
		
	}
	
	@Override
	protected void doInit() throws BundleException {
		String fileName = "";
        try {
            Enumeration<URL> urls=null;
            ClassLoader parent = Thread.currentThread().getContextClassLoader();
            
            ClassLoader classLoader = new ActivatorClassLoader(new URL[]{}, parent);
            
            if (parent != null) {
                urls = classLoader.getResources(fileName);
               
            } 
            if (urls != null) {

                while (urls.hasMoreElements()) {

                	URL url = urls.nextElement().toURI().toURL();
                	String classFile = process(url);
                	if("".equals(classFile)){
                		continue;
                	}
                	Class<?> clazz = null;
             
                	
                	try {
                		clazz = Class.forName(classFile, false, classLoader);
					} catch (Exception e) {
						e.printStackTrace();
					}
                	
                	if(clazz!=null){
                		ActivatorReg activator = clazz.getAnnotation(ActivatorReg.class);
                		if(activator!=null){
                			Bundle bundle = Bundles.createBundle(activator.name(), clazz, getModel());
                        	if(!bundle.initialized()){
                        		bundle.init();
                        	}
                        	if(!bundles.contains(bundle)){
                        		bundles.add(bundle);
                        	}
                		}
                	}
                }
            }
        } catch (Throwable t) {
        	throw new BundleException("",t);
        }
	}
	
	private String process(URL url){
		String file = url.getFile();
		if(!file.endsWith(".class") && !file.endsWith(".jar")){
			return "";
		}
		String classFile = "";
		if(file.indexOf("!")>=0){
			classFile = file.split("!")[1];
		}else if(file.indexOf("classes")>=0){
			classFile = file.split("classes")[1];
		}
		if(classFile==null || "".equals(classFile)){
			return "";
		}
		if(classFile.startsWith("/")){
			classFile = classFile.substring(1);
		}
		classFile = classFile.replace("/", ".");
		classFile = classFile.replace(".class", "");
		
		if(classFile.startsWith("org.apache")){
			return "";
		}
		return classFile;
	}

	@Override
	protected void doStart() throws BundleException {
		for(Bundle bundle:bundles){
			bundle.start();
		}
	}

	@Override
	protected void doStop() throws BundleException {
		for(Bundle bundle:bundles){
			bundle.stop();
		}
	}
	
}