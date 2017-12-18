package org.Invoker.rpc.invoker;

import java.io.Serializable;

public interface Invocation extends Serializable{

	public String getMethdName();
	
	public Class<?>[] getParamterTypes();
	
	public Object[] getArguments();
	
	public Class<?> getInterface();
	
	public boolean isAsync();
	
	public boolean isSent();
	
	public InvokerNotify getNotifier();
}
