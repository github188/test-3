package org.Invoker.rpc.protocol;

import java.util.ArrayList;
import java.util.List;

import org.Invoker.Identity;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.exporter.Exporter;
import org.Invoker.rpc.invoker.Invoker;

public abstract class AbstractProtocol implements Protocol {

	private List<Invoker<?>> invokers = new ArrayList<Invoker<?>>();
	
	public <T> Exporter<T> export(Invoker<T> invoker) throws InvokerException {
		Exporter<T> exporter = doExport(invoker);
		return exporter;
	}

	public <T> Invoker<T> refer(Class<T> type, Identity ident) throws InvokerException {
		Invoker<T> ref = doRefer(type, ident);
		invokers.add(ref);
		return ref;
	}
	
	protected abstract <T> Exporter<T> doExport(Invoker<T> invoker) throws InvokerException;
	
	protected abstract <T> Invoker<T> doRefer(Class<T> type, Identity ident) throws InvokerException;
	
}
