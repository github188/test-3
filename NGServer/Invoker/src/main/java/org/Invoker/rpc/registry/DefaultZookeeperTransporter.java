package org.Invoker.rpc.registry;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.Invoker.RegistryIdentity;

public class DefaultZookeeperTransporter implements ZookeeperTransporter {

	private final ConcurrentMap<String, ZookeeperClient> map = new ConcurrentHashMap<String, ZookeeperClient>();
	
	@Override
	public ZookeeperClient connect(RegistryIdentity ident) {
		String key = ident.getHost()+":"+ident.getPort();
		if(map.get(key)==null){
			ZookeeperClient client = new ZkclientZookeeperClient(ident,key);
			map.putIfAbsent(key, client);
		}
		return map.get(key);
	}

}
