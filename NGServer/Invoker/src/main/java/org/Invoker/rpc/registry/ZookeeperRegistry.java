package org.Invoker.rpc.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nari.Logger.LoggerManager;

import org.Invoker.RegistryIdentity;
import org.Invoker.rpc.extension.ExtensionLoader;

public class ZookeeperRegistry extends FailbackRegistry {

	private nari.Logger.Logger logger = LoggerManager.getLogger(this.getClass());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -152677796666890888L;

	private ZookeeperClient client = null;
	
	private ZookeeperTransporter transporter = ExtensionLoader.getExtensionLoader(ZookeeperTransporter.class).getExtension("zookeeper");
	
	private Map<String,ChildListener> map = new HashMap<String,ChildListener>();
	
	public ZookeeperRegistry(RegistryIdentity ident){
		client = transporter.connect(ident);
		client.addStateListener(new StateListener() {
			
			@Override
			public void stateChanged(int connected) {
				logger.info("zookeeper state is changed:"+connected);
			}
			
		});
	}
	
	@Override
	protected void doRegister(RegistryIdentity ident) {
		String path = ident.toRegistryPath();
		if(path==null || "".equals(path)){
			return;
		}
		client.create(path, true);
	}

	@Override
	protected void doSubscribe(final RegistryIdentity ident, final NotifyListener listener) {
		String path = ident.toRegistryPath();
		if(path==null || "".equals(path)){
			return;
		}
		
		client.create(path, false);
		
		if(map.get(path)==null){
			ChildListener clis = new ChildListener() {
				
				@Override
				public void childChanged(String path, List<String> children) {
					List<RegistryIdentity> list = toRegistry(ident,children);
					ZookeeperRegistry.this.notify(listener, list);
				}
				
			};
			map.put(path, clis);
		}
		
		List<String> paths = client.getChildren(path);
		if(paths!=null && paths.size()>0){
			List<RegistryIdentity> list = toRegistry(ident,paths);
			super.notify(listener, list);
		}
		client.addChildListener(path, map.get(path));
	}

	private List<RegistryIdentity> toRegistry(RegistryIdentity ident,List<String> children){
		List<RegistryIdentity> list = new ArrayList<RegistryIdentity>();
		RegistryIdentity t = ident.copy();
		for(String child:children){
			t.setContent(child);
			list.add(t);
		}
		return list;
	}
	@Override
	protected void doUnregister(RegistryIdentity ident) {
		String path = ident.toRegistryPath();
		if(path==null || "".equals(path)){
			return;
		}
		client.delete(path);
	}

	@Override
	protected void doUnsubscribe(RegistryIdentity ident, NotifyListener listener) {
		String path = ident.toRegistryPath();
		if(path==null || "".equals(path)){
			return;
		}
		client.removeChildListener(path, map.get(path));
	}

}
