package com.application.plugin;

public class DefaultResult implements Result {

	private Throwable exception = null;
	
	private Object result = null;
	
	private Invoker invoker = null;
	
	public DefaultResult(Invoker invoker,Object result,Throwable exception){
		this.result = result;
		this.exception = exception;
		this.invoker = invoker;
	}
	
	@Override
	public Object getValue() {
		return result;
	}

	@Override
	public Throwable getException() {
		return exception;
	}

	@Override
	public boolean hasException() {
		return exception!=null;
	}

	@Override
	public Object recreate() throws Throwable {
		if(exception!=null){
			throw exception;
		}
		return result;
	}

	@Override
	public void setValue(Object value) {
		this.result = value;
	}

	public Invoker getInvoker(){
		return invoker;
	}
}
