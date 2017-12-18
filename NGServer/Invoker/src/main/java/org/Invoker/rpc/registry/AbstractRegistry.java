package org.Invoker.rpc.registry;

import java.util.ArrayList;
import java.util.List;

import org.Invoker.Identity;
import org.Invoker.RegistryIdentity;

public abstract class AbstractRegistry implements RegistryService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8389053826370161354L;

	private List<Identity> registrys = new ArrayList<Identity>();
	
	private List<Identity> subscribes = new ArrayList<Identity>();
	
	public void notify(NotifyListener listener,List<RegistryIdentity> idents){
		listener.notify(idents);
	}
	
	public void register(RegistryIdentity ident){
		registrys.add(ident);
	}
	
	public void unregister(RegistryIdentity ident){
		registrys.remove(ident);
	}
	
	public void subscribe(RegistryIdentity ident, NotifyListener listener){
		subscribes.add(ident);
	}
	
	public void unsubscribe(RegistryIdentity ident, NotifyListener listener){
		subscribes.remove(ident);
	}
	
	public List<Identity> lookup(RegistryIdentity ident){
		
		return null;
	}
}
