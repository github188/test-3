package org.Invoker.rpc.registry;

import org.Invoker.RegistryIdentity;

public class ZookeeperRegistryFactory extends AbstractRegistryFactory {

	@Override
	protected RegistryService createRegistry(RegistryIdentity ident) {
		return new ZookeeperRegistry(ident);
	}

}
