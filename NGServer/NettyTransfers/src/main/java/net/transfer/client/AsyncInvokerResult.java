package net.transfer.client;

import java.io.Serializable;

public class AsyncInvokerResult implements Result, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 280736686743038431L;
	
	private ResponseFuture future = null;
	
	private Object result = null;
	
	private Throwable t = null;
	
	private boolean finished = false;
	
	public AsyncInvokerResult(ResponseFuture future){
		this.future = future;
	}
	
	@Override
	public Object getValue() {
		waitForFinish();
		return result;
	}

	@Override
	public Throwable getException() {
		waitForFinish();
		return t;
	}

	@Override
	public boolean hasException() {
		waitForFinish();
		return t==null;
	}

	@Override
	public Object recreate() throws Throwable {
		waitForFinish();
		if(t!=null){
			throw t;
		}
		return getValue();
	}

	private void waitForFinish(){
		if(!finished){
			Response res = (Response)future.get();
			if(res==null){
				finished = true;
				return;
			}
			if(res.getStatus()!=Response.OK){
				t = new InvokerException(res.getErrorMessage());
				finished = true;
				return;
			}
			result = res.getResult();
			finished = true;
		}
	}
}
