package org.Invoker.rpc.registry;

import org.Invoker.RegistryIdentity;
import org.Invoker.rpc.Adaptive;
import org.Invoker.rpc.SPI;

@SPI
public interface RegistryFactory {

	@Adaptive
	public RegistryService getRegistry(RegistryIdentity ident);
}
