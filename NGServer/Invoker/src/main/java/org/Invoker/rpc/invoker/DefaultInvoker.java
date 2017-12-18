package org.Invoker.rpc.invoker;

import org.Invoker.Identity;
import org.Invoker.remoting.exchanger.ExchangerClient;
import org.Invoker.remoting.exchanger.RemotingException;
import org.Invoker.remoting.exchanger.Response;
import org.Invoker.remoting.exchanger.ResponseFuture;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.result.AsyncInvokerResult;
import org.Invoker.rpc.result.Result;
import org.Invoker.rpc.result.RpcResult;

public class DefaultInvoker<T> extends AbstractInvoker<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6762811839592982469L;
	
	private ExchangerClient client = null;
	
	public DefaultInvoker(Class<T> interfaceType,ExchangerClient client){
		super(interfaceType);
		this.client = client;
	}
	
	@Override
	protected Result doInvoke(Invocation inv) throws InvokerException {
		boolean isAsync = inv.isAsync();
		boolean isSend = inv.isSent();
		try {
			if(isAsync){
				ResponseFuture future = client.request(inv);
				return new AsyncInvokerResult(future);
			}else if(isSend){
				client.send(inv);
				return new RpcResult();
			}else{
				Response res = (Response)client.request(inv).get();
				return (Result)res.getResult();
			}
		} catch (RemotingException e) {
			e.printStackTrace();
			throw new InvokerException(e.getMessage(),e);
		}
	}

	public void destroy() throws InvokerException{
		if(client!=null){
			client.decrement();
			if(client.ref()==0){
				try {
					client.close();
				} catch (RemotingException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public Identity getIdentity() {
		return null;
	}

}
