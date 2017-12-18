package com.application.plugin.service;

public interface ServiceListener<T> {

	public void serviceRegisted(ServiceReference<T> ref);
	
	public void serviceUnregisted(ServiceReference<T> ref);
}
