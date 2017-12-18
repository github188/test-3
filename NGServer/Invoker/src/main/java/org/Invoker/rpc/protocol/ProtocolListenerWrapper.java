package org.Invoker.rpc.protocol;

import java.util.Collections;

import org.Invoker.Identity;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.exporter.Exporter;
import org.Invoker.rpc.exporter.ListenerExporterWrapper;
import org.Invoker.rpc.extension.ExtensionLoader;
import org.Invoker.rpc.invoker.Invoker;
import org.Invoker.rpc.invoker.ListenerInvokerWrapper;
import org.Invoker.rpc.listener.ExporterListener;
import org.Invoker.rpc.listener.InvokerListener;

public class ProtocolListenerWrapper implements Protocol {

	private Protocol protocol = null;
	
	public ProtocolListenerWrapper(Protocol protocol){
		this.protocol = protocol;
	}
	
	public <T> Exporter<T> export(Invoker<T> invoker) throws InvokerException {
		return new ListenerExporterWrapper<T>(protocol.export(invoker),Collections.unmodifiableList(ExtensionLoader.getExtensionLoader(ExporterListener.class).getActivateExtension("provider")));
	}

	public <T> Invoker<T> refer(Class<T> type, Identity url) throws InvokerException {
		return new ListenerInvokerWrapper<T>(protocol.refer(type, url),Collections.unmodifiableList(ExtensionLoader.getExtensionLoader(InvokerListener.class).getActivateExtension("custorm")));
	}

}
