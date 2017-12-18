package org.Invoker.rpc.registry;

import java.util.List;

import org.Invoker.RegistryIdentity;

public interface NotifyListener {

	public void notify(List<RegistryIdentity> idents);
}
