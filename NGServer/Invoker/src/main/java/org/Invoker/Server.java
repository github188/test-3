package org.Invoker;

import org.Invoker.rpc.extension.ExtensionLoader;
import org.Invoker.rpc.invoker.Invoker;
import org.Invoker.rpc.protocol.Protocol;
import org.Invoker.rpc.proxy.ProxyFactory;

public class Server {

	public static void main(String[] args) {
		
		Server s = new Server();
		s.start();
		synchronized (s) {
			try {
				s.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void start(){
		Protocol wrapperProtocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
		ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();
		
		RegistryIdentity ri = new RegistryIdentity();
		ri.setHost("10.144.113.216");
		ri.setPort(2181);
		ri.setVersion("1.0");
		ri.setCluster(Constants.FAILOVER);
		
		
		SocketServerIdentity ss = new SocketServerIdentity();
		ss.setPort(10008);
		ss.setThreadPoolSize(Runtime.getRuntime().availableProcessors()+1);
		ri.setProtocolIdentity(ss);
		
		ri.setAttribute("ProxyFactory", ri.getAttribute("ProxyFactory","jdk"));
		
		Invoker<HelloWorld> exportInvoker = proxyFactory.getInvoker(new HelloWorldImpl(), HelloWorld.class, ri);
		
		wrapperProtocol.export(exportInvoker);
		
//		Exporter<HelloWorld> export = wrapperProtocol.export(exportInvoker);
//		try {
//			export.unexport();
//		} catch (RemotingException e) {
//			e.printStackTrace();
//		}
		
//		WebServiceServerIdentity ww = new WebServiceServerIdentity();
//		ww.setMaxThreadPool(Runtime.getRuntime().availableProcessors()*2+1);
//		ww.setMinThreadPool(Runtime.getRuntime().availableProcessors()+1);
//		ww.setPort(9091);
//		ri.setProtocolIdentity(ww);
//		
//		Invoker<HelloWorld> exportInvoker = proxyFactory.getInvoker(new HelloWorldImpl(), HelloWorld.class, ri);
//		
//		wrapperProtocol.export(exportInvoker);
//		System.out.println();
	}
}
