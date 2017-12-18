package org.Invoker.rpc.listener;

import org.Invoker.rpc.SPI;
import org.Invoker.rpc.invoker.Invoker;

@SPI
public interface InvokerListener {

	public void referred(Invoker<?> invoker);
	
	public void destroy(Invoker<?> invoker);
}
