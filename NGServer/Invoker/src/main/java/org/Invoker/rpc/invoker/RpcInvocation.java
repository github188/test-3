package org.Invoker.rpc.invoker;

import java.lang.reflect.Method;

public class RpcInvocation implements Invocation {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6464092400073978612L;

	private String methodName;
	
	private Object[] arguments;
	
	private Invoker<?> invoker;
	
	private Class<?> interfaces ;
	
	private Class<?>[] types;
	
	private boolean isAsync = false;
	
	private boolean isSent = false;
	
	private InvokerNotify notifier = null;
	
	public RpcInvocation(Invoker<?> invoker,Method method,Object[] arguments){
		this(invoker,method,arguments,false,false);
	}
	
	public RpcInvocation(Invoker<?> invoker,Method method,Object[] arguments,boolean isAsync,boolean isSent){
		this.methodName = method.getName();
		this.types = method.getParameterTypes();
		this.arguments = arguments;
		this.interfaces = invoker.getInterface();
		this.isAsync = isAsync;
		this.isSent = isSent;
	}
	
	public RpcInvocation(Invoker<?> invoker,Method method,Object[] arguments,InvokerNotify notifier){
		this(invoker,method,arguments,true,false);
		this.notifier = notifier;
	}
	
	public RpcInvocation(){
		
	}
	
	public Object[] getArguments() {
		return arguments;
	}

	public Invoker<?> getInvoker() {
		return invoker;
	}

	public String getMethdName() {
		return methodName;
	}

	public Class<?>[] getParamterTypes() {
		return types;
	}

	@Override
	public Class<?> getInterface() {
		return interfaces;
	}

	public boolean isAsync(){
		return isAsync;
	}
	
	public boolean isSent(){
		return isSent;
	}

	@Override
	public InvokerNotify getNotifier() {
		return notifier;
	}
}
