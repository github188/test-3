package com.application.plugin;

public interface Result {

	public Object getValue();

	public void setValue(Object value);
	
	public Throwable getException();

	public boolean hasException();

	public Object recreate() throws Throwable;
	
}
