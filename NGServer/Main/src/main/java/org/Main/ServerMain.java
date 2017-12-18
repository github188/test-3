package org.Main;

import com.application.plugin.StandardFramework;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.framework.Framework;

public class ServerMain {
	
    public static void main(String[] args) {
        
    	ServerMain server = new ServerMain();
    	System.out.println(System.getProperty("user.dir"));
    	server.start();
    	synchronized (ServerMain.class) {
    		try {
    			ServerMain.class.wait();
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
		}
    	
    }
    
    public void start(){
    	System.out.println("start");
    	
    	Framework framework = new StandardFramework();
		try {
			framework.init(null);
			framework.start();
		} catch (BundleException e) {
			e.printStackTrace();
		}
    }
}
