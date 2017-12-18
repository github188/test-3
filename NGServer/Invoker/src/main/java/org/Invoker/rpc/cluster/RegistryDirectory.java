package org.Invoker.rpc.cluster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nari.Logger.LoggerManager;

import org.Invoker.DelegateIdentity;
import org.Invoker.Identity;
import org.Invoker.RegistryIdentity;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.invoker.DelegateInvoker;
import org.Invoker.rpc.invoker.Invoker;
import org.Invoker.rpc.protocol.Protocol;
import org.Invoker.rpc.registry.NotifyListener;
import org.Invoker.rpc.registry.RegistryService;

public class RegistryDirectory<T> extends AbstractDirectory<T> implements NotifyListener,Serializable {

	private nari.Logger.Logger logger = LoggerManager.getLogger(this.getClass());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4200978893940087224L;

	private RegistryService registry = null;
	
	private Protocol protocol = null;
	
	private Class<T> type = null;
	
	private Identity ident = null;
	
	private Map<String,Invoker<T>> invokerMap = new HashMap<String,Invoker<T>>();
	
	public RegistryDirectory(Class<T> type,Identity ident,RegistryService registry,Protocol protocol){
		this.registry = registry;
		this.protocol = protocol;
		this.type = type;
		this.ident = ident;
	}

	public void subscribe(RegistryIdentity ident){
		registry.subscribe(ident, this);
	}
	
	public void notify(List<RegistryIdentity> idents) {
		logger.info("notify");
		Map<String,Invoker<T>> map = new HashMap<String,Invoker<T>>(invokerMap);
		for(Map.Entry<String,Invoker<T>> entry:map.entrySet()){
			if(idents.indexOf(entry.getKey())<0){
				entry.getValue().destroy();
				map.remove(entry.getKey());
			}
		}
		
		for(RegistryIdentity ident:idents){
			String content = ident.getContent();
			if("".equals(content)){
				continue;
			}
			Invoker<T> invoker = map.get(content);
			if(invoker==null){
				DelegateIdentity delegate = new DelegateIdentity(ident.getProtocolIdentity());
				delegate.addContent(content);
				invoker = new DelegateInvoker<T>(protocol.refer(type, delegate),ident);
				map.put(content, invoker);
			}
		}
		
		invokerMap = map;
	}
	
	public Class<T> getInterface(){
		return type;
	}

	public void destory() {
		destroyAllInvokers();
	}
	
	private void destroyAllInvokers(){
		Map<String,Invoker<T>> map = new HashMap<String,Invoker<T>>(invokerMap);
		for(Map.Entry<String,Invoker<T>> entry:map.entrySet()){
			entry.getValue().destroy();
			map.remove(entry.getKey());
		}
		invokerMap = map;
	}

	@Override
	public List<Invoker<T>> list() throws InvokerException {
		return new ArrayList<>(invokerMap.values());
	}

	@Override
	public Identity getIdentity() {
		return ident;
	}
}
