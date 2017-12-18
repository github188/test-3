package org.Invoker;

import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.extension.ExtensionLoader;
import org.Invoker.rpc.invoker.Invoker;
import org.Invoker.rpc.invoker.InvokerNotify;
import org.Invoker.rpc.protocol.Protocol;
import org.Invoker.rpc.proxy.ProxyFactory;

public class Client {

	public static void main(String[] args) {
		Protocol wrapperProtocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
		ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();
		
		RegistryIdentity ri = new RegistryIdentity();
		ri.setHost("10.144.113.216");
		ri.setPort(2181);
		ri.setVersion("1.0");
		ri.setCluster(Constants.FAILOVER);
		
		SocketClientIdentity ss = new SocketClientIdentity();
		ss.setThreadPoolSize(Runtime.getRuntime().availableProcessors()+1);
		ss.setConnectTimeout(5000);
//		ss.setRemoteHost("192.168.1.5");
//		ss.setRemotePort(10008);
		ri.setProtocolIdentity(ss);
		ri.setAttribute("ProxyFactory", ri.getAttribute("ProxyFactory","jdk"));
		
		Invoker<HelloWorld> invoker = wrapperProtocol.refer(HelloWorld.class, ri);
		
		HelloWorld ins = (HelloWorld)proxyFactory.getProxy(invoker);
		proxyFactory.getProxy(invoker, new InvokerNotify() {
			
			private static final long serialVersionUID = -3349285654200818855L;

			@Override
			public void notify(Object result, Throwable e) throws InvokerException {
				
			}
		});
		System.out.println("recive from server:"+ins.say("zhangwenli"));
		
//		Map<String,Object> m = new HashMap<String,Object>();
//		m.put("key", "hello");
//		Map<String,Object> map = ins.maps("aaa", m);
//		System.out.println(map);
		
		Object value = ins.getObject("obj", "getObject()");
		System.out.println(value);
		
		invoker.destroy();
		
//		WebServiceClientIdentity ww = new WebServiceClientIdentity();
//		ww.setConnectTimeout(5000);
//		ww.setMaxThreadPool(20);
//		ww.setMinThreadPool(10);
//		ww.setReciveTimeout(500000);
//		ri.setProtocolIdentity(ww);
//		
//		Invoker<HelloWorld> invoker = wrapperProtocol.refer(HelloWorld.class, ri);
//		
//		HelloWorld ins = (HelloWorld)proxyFactory.getProxy(invoker);
//		for(int i=0;i<10;i++){
//			System.out.println(ins.say("zl"));
//		}
		
		
	}
}
