package org.Invoker.rpc.protocol;

import org.Invoker.rpc.Adaptive;
import org.Invoker.rpc.extension.ExtensionFactory;
import org.Invoker.rpc.extension.ExtensionLoader;

@Adaptive
public class SPIExtensionFactory implements ExtensionFactory {

	@Override
	public <T> T getExtension(Class<T> type, String name) {
		return ExtensionLoader.getExtensionLoader(type).getExtension(name);
	}

}
