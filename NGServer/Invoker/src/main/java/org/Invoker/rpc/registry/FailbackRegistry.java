package org.Invoker.rpc.registry;

import java.util.ArrayList;
import java.util.List;

import org.Invoker.Identity;
import org.Invoker.RegistryIdentity;

public abstract class FailbackRegistry extends AbstractRegistry {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1293390962549864253L;

	private List<Identity> failRegistry = new ArrayList<Identity>();
	
	private List<Identity> failSubscribe = new ArrayList<Identity>();
	
	private List<Identity> failUnRegistry = new ArrayList<Identity>();
	
	private List<Identity> failUnSubscribe = new ArrayList<Identity>();
	
	public List<Identity> lookup(RegistryIdentity ident) {
		return null;
	}

	public void register(RegistryIdentity ident) {
		super.register(ident);
		failRegistry.remove(ident);
		failUnRegistry.remove(ident);
		try {
			doRegister(ident);
		} catch (Exception e) {
			failRegistry.add(ident);
			e.printStackTrace();
		}
		
	}

	public void subscribe(RegistryIdentity ident, NotifyListener listener) {
		super.subscribe(ident, listener);
		failSubscribe.remove(ident);
		failUnSubscribe.remove(ident);
		try {
			doSubscribe(ident,listener);
		} catch (Exception e) {
			failSubscribe.add(ident);
			e.printStackTrace();
		}
	}

	public void unregister(RegistryIdentity ident) {
		failRegistry.remove(ident);
		failUnRegistry.remove(ident);
		try {
			doUnregister(ident);
		} catch (Exception e) {
			failUnRegistry.add(ident);
		}
		super.unregister(ident);
	}

	public void unsubscribe(RegistryIdentity ident, NotifyListener listener) {
		failSubscribe.remove(ident);
		failUnSubscribe.remove(ident);
		try {
			doUnsubscribe(ident, listener);
		} catch (Exception e) {
			failUnSubscribe.add(ident);
		}
		super.unsubscribe(ident, listener);
	}
	
	public void notify(NotifyListener listener,List<RegistryIdentity> idents){
		this.doNotify(listener, idents);
	}
	
	private void doNotify(NotifyListener listener,List<RegistryIdentity> idents){
		super.notify(listener, idents);
	}
	
	protected abstract void doRegister(RegistryIdentity ident);

    protected abstract void doUnregister(RegistryIdentity ident);

    protected abstract void doSubscribe(RegistryIdentity ident, NotifyListener listener);

    protected abstract void doUnsubscribe(RegistryIdentity ident, NotifyListener listener);
}
