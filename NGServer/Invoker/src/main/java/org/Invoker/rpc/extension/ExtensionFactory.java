package org.Invoker.rpc.extension;

import org.Invoker.rpc.Adaptive;
import org.Invoker.rpc.SPI;

@SPI
public interface ExtensionFactory {

	@Adaptive
	public <T> T getExtension(Class<T> type,String name);
}
