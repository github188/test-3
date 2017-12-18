package org.Invoker.rpc.registry;

import java.io.Serializable;
import java.util.List;

import org.Invoker.Identity;
import org.Invoker.RegistryIdentity;

public interface RegistryService extends Serializable {

	public void register(RegistryIdentity url);
	
	public void unregister(RegistryIdentity url);
	
	public void subscribe(RegistryIdentity url, NotifyListener listener);
	
	public void unsubscribe(RegistryIdentity url, NotifyListener listener);
	
	public List<Identity> lookup(RegistryIdentity url);
}
