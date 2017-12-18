package org.Invoker.rpc.protocol;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.Invoker.DelegateIdentity;
import org.Invoker.Identity;
import org.Invoker.SocketClientIdentity;
import org.Invoker.SocketServerIdentity;
import org.Invoker.remoting.exchanger.Channel;
import org.Invoker.remoting.exchanger.ExchangerChannel;
import org.Invoker.remoting.exchanger.ExchangerClient;
import org.Invoker.remoting.exchanger.ExchangerHandler;
import org.Invoker.remoting.exchanger.ExchangerServer;
import org.Invoker.remoting.exchanger.Exchangers;
import org.Invoker.remoting.exchanger.RemotingException;
import org.Invoker.remoting.handler.HeaderExchangerChannel;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.exporter.DefaultExporter;
import org.Invoker.rpc.exporter.Exporter;
import org.Invoker.rpc.invoker.DefaultInvoker;
import org.Invoker.rpc.invoker.Invocation;
import org.Invoker.rpc.invoker.Invoker;
import org.Invoker.rpc.result.Result;
import org.apache.log4j.Logger;

public class InvokerProtocol extends AbstractProtocol {

	protected final Logger log = Logger.getLogger(InvokerProtocol.class);
	
	private ServerHolder holder = ServerHolder.getHolder();
	
	private ClientHolder clientHolder = ClientHolder.getHolder();
	
	private final ConcurrentMap<Class<?>, Exporter<?>> exportMap = new ConcurrentHashMap<Class<?>, Exporter<?>>();
	
	private ExchangerHandler requestHandler = new ExchangerHandler() {
		
		public void sent(Channel channel, Object message) throws RemotingException {
			
		}
		
		public void received(Channel channel, Object message) throws RemotingException {
			if(message instanceof Invocation){
				reply((ExchangerChannel)channel,message);
			}
		}
		
		public void disconnected(Channel channel) throws RemotingException {
			HeaderExchangerChannel.remove(channel);
			log.info("disconnected remote address #"+channel.getRemoteAddress());
		}
		
		public void connected(Channel channel) throws RemotingException {
			log.info("connected remote address #"+channel.getRemoteAddress());
		}
		
		public void caught(Channel channel, Throwable exception) throws RemotingException {
			throw new RemotingException("exception caught from remote address #"+channel.getRemoteAddress(),exception);
		}
		
		public Object reply(ExchangerChannel channel, Object inv) throws RemotingException {
			if(inv instanceof Invocation){
				Invocation invocation = (Invocation)inv;
				Exporter<?> exp = findExporter(invocation.getInterface());
				Result result = exp.getInvoker().invoke(invocation);
				return result.getValue();
			}
			return null;
		}
	};
	
	private Exporter<?> findExporter(Class<?> interfaces){
		return exportMap.get(interfaces);
	}
	
	@Override
	public <T> Exporter<T> doExport(Invoker<T> invoker) throws InvokerException {
		ExchangerServer server = initServer((SocketServerIdentity)invoker.getIdentity());
		Exporter<T> exporter = new DefaultExporter<T>(invoker,server);
		exportMap.put(invoker.getInterface(), exporter);
		return exporter;
	}

	@Override
	public <T> Invoker<T> doRefer(Class<T> type, Identity ident) throws InvokerException {
		DelegateIdentity delegate = null;
		if(ident instanceof DelegateIdentity){
			delegate = (DelegateIdentity)ident;
		}
		
		SocketClientIdentity identity = (SocketClientIdentity)delegate.getIdent();
		String remoteAddress = delegate.getContent();
		
		InetSocketAddress address = null;
		if(!"".equals(remoteAddress)){
			String ip = remoteAddress.split(":")[0];
			int port = Integer.parseInt(remoteAddress.split(":")[1]);
			identity.setRemoteHost(ip);
			identity.setRemotePort(port);
			address = new InetSocketAddress(ip, port);
		}
		
		DefaultInvoker<T> invoker = new DefaultInvoker<T>(type,initClinet(type,identity,address));
		return invoker;
	}
	
	private ExchangerClient initClinet(Class<?> type,SocketClientIdentity ident,InetSocketAddress address) throws InvokerException{
		if(address==null){
			return null;
		}
		if(clientHolder.get(address)==null){
			ExchangerClient client = null;
			try {
				client = Exchangers.connect(ident, requestHandler);
			} catch (RemotingException e) {
				e.printStackTrace();
				throw new InvokerException(e.getMessage(),e);
			}
			client.increament();
			clientHolder.add(address, client);
		}else{
			clientHolder.get(address).increament();
		}
		return clientHolder.get(address);
	}
	
	private ExchangerServer initServer(SocketServerIdentity indent) throws InvokerException{
		if(holder.get()==null){
			ExchangerServer server = null;
			try {
				server = Exchangers.bind(indent, requestHandler);
			} catch (RemotingException e) {
				e.printStackTrace();
				throw new InvokerException(e.getMessage(),e);
			}
			holder.serServer(server);
		}
		return holder.get();
	}
}
