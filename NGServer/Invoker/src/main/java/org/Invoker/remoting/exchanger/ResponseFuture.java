package org.Invoker.remoting.exchanger;

public interface ResponseFuture {

	public Object get();
	
	public Object get(long timeout);
	
	public boolean isDone();
}
