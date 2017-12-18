package org.Invoker.rpc.invoker;

import java.io.Serializable;

import org.Invoker.Identity;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.result.Result;

public interface Invoker <T> extends Serializable{
	
	public Class<T> getInterface();
	
	public Result invoke(Invocation inv) throws InvokerException;
	
	public void destroy() throws InvokerException;
	
	public Identity getIdentity();
	
}
