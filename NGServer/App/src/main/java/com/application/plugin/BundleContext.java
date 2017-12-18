package com.application.plugin;

import java.io.InputStream;
import java.net.URL;

import com.application.plugin.bundle.Bundle;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.service.ServiceExporter;
import com.application.plugin.service.ServiceReference;
import com.application.plugin.service.ServiceRegistration;

public interface BundleContext {

	public <T> ServiceRegistration<T> registService(Class<T> serviceClass,Provider<T> provider) throws BundleException;
	
	public <T> ServiceReference<T> findReference(Class<T> serviceClass, Version version) throws BundleException;
	
	public <T> T getService(ServiceReference<T> ref) throws BundleException;
	
	public <T> boolean unRegisterService(ServiceReference<T> ref) throws BundleException;
	
	public <T> ServiceExporter<T> export(Class<T> serviceClass,Provider<T> provider ,ExportConfig config) throws BundleException;
	
	public <T> T refer(Class<T> clazz,ReferConfig config) throws BundleException;
	
	public Bundle getBundle();
	
	public InputStream getResourceAsStream(String absolutePath);
	
	public URL gettResourceURL(String absolutePath);
}
