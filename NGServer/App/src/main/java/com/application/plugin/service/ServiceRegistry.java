package com.application.plugin.service;

import org.Invoker.rpc.exporter.Exporter;
import org.Invoker.rpc.invoker.Invoker;
import org.Invoker.rpc.protocol.Protocol;

import com.application.plugin.AttributeKey;
import com.application.plugin.bundle.BundleException;
import com.application.plugin.framework.FrameworkListener;

public interface ServiceRegistry extends FrameworkListener{

	public <T> void register(AttributeKey key,ServiceReference<T> ref) throws BundleException;
	
	public <T> void unregister(AttributeKey key) throws BundleException;
	
	public <T> ServiceReference<T> find(AttributeKey key) throws BundleException;
	
	public <T> Exporter<T> export(Invoker<T> exportInvoker,Protocol protocol) throws BundleException;
}
