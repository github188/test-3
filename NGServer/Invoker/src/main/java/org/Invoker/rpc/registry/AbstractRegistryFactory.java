package org.Invoker.rpc.registry;

import org.Invoker.RegistryIdentity;

public abstract class AbstractRegistryFactory implements RegistryFactory {

	public RegistryService getRegistry(RegistryIdentity ident) {
		RegistryService regService = createRegistry(ident);
		return regService;
	}

	protected abstract RegistryService createRegistry(RegistryIdentity ident);
}
