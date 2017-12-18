package com.application.plugin;

import java.lang.reflect.Method;

public interface Invoker {

	public Object getInvokeObject();
	
	public Method getInvokeMethod();
	
	public Class<?>[] getInterfaces();
	
	public Object[] getArguments();
	
	public void setArguments(Object[] args);
}
