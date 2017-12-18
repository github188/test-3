package com.application.plugin;

import java.lang.reflect.Method;

public class DefaultInvoker implements Invoker {

	private Object invokeObject = null;
	
	private Method invokeMethod = null;
	
	private Class<?>[] interfaces = null;
	
	private Object[] arguments = null;
	
	public DefaultInvoker(Object invokeObject,Class<?>[] interfaces,Method invokeMethod,Object[] arguments){
		this.invokeObject = invokeObject;
		this.interfaces = interfaces;
		this.invokeMethod = invokeMethod;
		this.arguments = arguments;
	}
	
	@Override
	public Object getInvokeObject() {
		return invokeObject;
	}

	@Override
	public Method getInvokeMethod() {
		return invokeMethod;
	}

	@Override
	public Class<?>[] getInterfaces() {
		return interfaces;
	}

	@Override
	public Object[] getArguments() {
		return arguments;
	}

	@Override
	public void setArguments(Object[] args) {
		arguments = args;
	}

}
