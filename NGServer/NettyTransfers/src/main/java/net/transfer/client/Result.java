package net.transfer.client;

public interface Result {

	public Object getValue();

	public Throwable getException();

	public boolean hasException();

	public Object recreate() throws Throwable;
}
