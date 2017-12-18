package com.application.plugin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ActivatorClassLoader extends URLClassLoader {

	private List<URL> list = new ArrayList<URL>();
	
//	private List<File> classFiles = new ArrayList<String>();
	
	public ActivatorClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}
	
	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		Enumeration<URL> enums = this.getParent().getResources(name);
		while(enums.hasMoreElements()){
			URL url = enums.nextElement();
			if(url.getProtocol().equals("jar")){
				list.add(url);
			}else if(url.getProtocol().equals("file")){
				try {
//					String file = url.toURI().toURL().toString();
					File f = new File(url.toURI());
					listClass(f);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		try {
			Enumeration<URL> enums1 = this.getParent().getResources("../lib");
			while(enums1.hasMoreElements()){
				URL url = enums1.nextElement();
				if(url.getProtocol().equals("jar")){
					list.add(url);
				}else if(url.getProtocol().equals("file")){
					try {
						File f = new File(url.toURI());
						listClass(f);
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			
		}
		
		return new Enumeration<URL>() {

			private int index = 0;

            public URL nextElement() {
                if (index>=list.size()) {
                    throw new NoSuchElementException();
                }
                URL u = list.get(index);
                index++;
                return u;
            }

            public boolean hasMoreElements() {
                return index<list.size();
            }
			
		};
	}
	
	private void listClass(File file){
		if(file.isDirectory()){
			File[] list = file.listFiles();
			for(File f:list){
				listClass(f);
			}
		}else{
			try {
				URL url = file.toURI().toURL();
				if(file.getName().endsWith(".jar")){
					JarFile jar=null;
					try {
						String path = url.toURI().toURL().toString();
						jar = new JarFile(new File(url.toURI()), true);
						
						JarEntry entry = jar.getJarEntry("META-INF/activator/activator");
						if(entry!=null){
							Enumeration<JarEntry> net = jar.entries();
							while(net.hasMoreElements()){
								URL u = new URL("jar:"+path+"!/"+net.nextElement().getName());
								if(!list.contains(u)){
									list.add(u);
								}
							}
						}
					} catch (URISyntaxException ex) {
						ex.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					if(!list.contains(url)){
						list.add(url);
					}
				}
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
