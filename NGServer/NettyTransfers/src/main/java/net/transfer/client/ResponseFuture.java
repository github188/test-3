package net.transfer.client;

public interface ResponseFuture {

	public Object get();
	
	public Object get(long timeout);
	
	public boolean isDone();
}
