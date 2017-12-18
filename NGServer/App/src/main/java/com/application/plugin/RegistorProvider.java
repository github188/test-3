package com.application.plugin;

import java.util.concurrent.atomic.AtomicReference;

import com.application.plugin.service.DefaultServiceRegistry;
import com.application.plugin.service.ServiceRegistry;

public class RegistorProvider {

	private static ServiceRegistry registror = null;
	
	private static AtomicReference<ServiceRegistry> ref = new AtomicReference<ServiceRegistry>();
	
	public static ServiceRegistry get(){
		
		if(ref.get()==null){
			registror = new DefaultServiceRegistry();
			ref.compareAndSet(null, registror);
		}
		
		return ref.get();
	}
}
