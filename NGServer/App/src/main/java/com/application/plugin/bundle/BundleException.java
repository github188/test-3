package com.application.plugin.bundle;

public class BundleException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2171800129973392378L;

	public BundleException() {
		super();
	}
	
	public BundleException(String message){
		super(message);
	}
	
	public BundleException(String message,Throwable t){
		super(message,t);
	}
	
	
}
