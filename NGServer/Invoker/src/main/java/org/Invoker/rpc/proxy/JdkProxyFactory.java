package org.Invoker.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.invoker.Invoker;
import org.Invoker.rpc.invoker.InvokerNotify;
import org.Invoker.rpc.invoker.RpcInvocation;
import org.Invoker.rpc.result.Result;

public class JdkProxyFactory extends AbstractProxyFactory {

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T getProxy(final Invoker<T> invoker, Class<?>[] interfaces,final boolean isSent) throws InvokerException {
		T ins = (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), interfaces, new InvocationHandler() {
			
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				String methodName = method.getName();
		        Class<?>[] parameterTypes = method.getParameterTypes();
		        if ("toString".equals(methodName) && parameterTypes.length == 0) {
		            return invoker.toString();
		        }
		        if ("hashCode".equals(methodName) && parameterTypes.length == 0) {
		            return invoker.hashCode();
		        }
		        if ("equals".equals(methodName) && parameterTypes.length == 1) {
		            return invoker.equals(args[0]);
		        }
		        Result result = invoker.invoke(new RpcInvocation(invoker,method, args, false, isSent));
		        Object obj = null;
		        if(result.getValue()!=null && result.getValue() instanceof Result){
		        	result = (Result)result.getValue();
		        	if(result!=null){
		        		obj = result.recreate();
		        	}
		        }else{
		        	obj = result==null?null:result.recreate();
		        }
		        return obj;
			}
		});
		return ins;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T getProxy(final Invoker<T> invoker, Class<?>[] interfaces, final InvokerNotify notifier) throws InvokerException {
		T ins = (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), interfaces, new InvocationHandler() {
			
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				String methodName = method.getName();
		        Class<?>[] parameterTypes = method.getParameterTypes();
		        if ("toString".equals(methodName) && parameterTypes.length == 0) {
		            return invoker.toString();
		        }
		        if ("hashCode".equals(methodName) && parameterTypes.length == 0) {
		            return invoker.hashCode();
		        }
		        if ("equals".equals(methodName) && parameterTypes.length == 1) {
		            return invoker.equals(args[0]);
		        }
		        invoker.invoke(new RpcInvocation(invoker,method, args, notifier));
//		        Object obj = null;
//		        if(result.getValue()!=null && result.getValue() instanceof Result){
//		        	result = (Result)result.getValue();
//		        	if(result!=null){
//		        		obj = result.recreate();
//		        	}
//		        }else{
//		        	obj = result==null?null:result.recreate();
//		        }
		        return null;
			}
		});
		return ins;
	}

}
