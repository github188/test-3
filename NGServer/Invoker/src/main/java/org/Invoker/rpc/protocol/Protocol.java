package org.Invoker.rpc.protocol;

import org.Invoker.Identity;
import org.Invoker.rpc.Adaptive;
import org.Invoker.rpc.SPI;
import org.Invoker.rpc.exception.InvokerException;
import org.Invoker.rpc.exporter.Exporter;
import org.Invoker.rpc.invoker.Invoker;

@SPI(value="invoker")
public interface Protocol {

	@Adaptive
	public <T> Exporter<T> export(Invoker<T> invoker) throws InvokerException;
	
	@Adaptive
	public <T> Invoker<T> refer(Class<T> type, Identity url) throws InvokerException;
}
