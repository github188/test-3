package org.Invoker.remoting.handler;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.Invoker.remoting.exchanger.Channel;
import org.Invoker.remoting.exchanger.ChannelHandler;
import org.Invoker.remoting.exchanger.InvokerFuture;
import org.Invoker.remoting.exchanger.ExchangerChannel;
import org.Invoker.remoting.exchanger.ExchangerHandler;
import org.Invoker.remoting.exchanger.RemotingException;
import org.Invoker.remoting.exchanger.Request;
import org.Invoker.remoting.exchanger.ResponseFuture;
import org.Invoker.rpc.invoker.Invocation;

public class HeaderExchangerChannel implements ExchangerChannel {

	private Channel channel = null;
	
	private final Map<String,Object> attribute = new HashMap<String,Object>();
	
	public HeaderExchangerChannel(Channel channel){
		this.channel = channel;
	}
	
	public ExchangerHandler getExchangeHandler() throws RemotingException{
		return (ExchangerHandler)channel.getChannelHandler();
	}

	public static HeaderExchangerChannel get(Channel ch) throws RemotingException{
        if (ch == null) {
            return null;
        }
        HeaderExchangerChannel ret = (HeaderExchangerChannel) ch.getAttribute("HeaderExchangerChannel");
        if (ret == null) {
            ret = new HeaderExchangerChannel(ch);
            if (ch.isConnected()) {
                ch.setAttribute("HeaderExchangerChannel", ret);
            }
        }
        return ret;
    }
	
	public static void remove(Channel ch) throws RemotingException{
		ch.removeAttribute("HeaderExchangerChannel");
		if(!ch.isClosed()){
			ch.close();
		}
	}
	
	public ResponseFuture request(Object request) throws RemotingException {
		return request(request,2000);
	}

	public ResponseFuture request(Object request, int timeout) throws RemotingException {
		Request req = new Request();
		req.setData(request);
		channel.send(req);
		
		InvokerFuture future = new InvokerFuture(req);
		if(request instanceof Invocation){
			future.setNotifier(((Invocation)request).getNotifier());
		}
		
		return future;
	}

	public InetSocketAddress getRemoteAddress() {
		return channel.getRemoteAddress();
	}

	public boolean isConnected() throws RemotingException {
		return channel.isConnected();
	}

	public void close() throws RemotingException {
		channel.close();
	}

	public void close(long timeout) throws RemotingException {
		channel.close(timeout);
	}

	public ChannelHandler getChannelHandler() throws RemotingException{
		return channel.getChannelHandler();
	}

	public InetSocketAddress getLocalAddress() throws RemotingException{
		return channel.getLocalAddress();
	}

	public boolean isClosed() throws RemotingException {
		return channel.isClosed();
	}

	public void send(Object message) throws RemotingException {
		channel.send(message);
	}

	public void send(Object message, long timeout) throws RemotingException {
		channel.send(message,timeout);
	}

	public Object getAttribute(String key) {
		return attribute.get(key);
	}

	public boolean hasAttribute(String key) {
		return attribute.containsKey(key);
	}

	public void removeAttribute(String key) {
		attribute.remove(key);
	}

	public void setAttribute(String key, Object value) {
		attribute.put(key, value);
	}

}
