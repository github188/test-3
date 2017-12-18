package org.Invoker.remoting.exchanger;

import java.util.concurrent.Executor;

import org.Invoker.Identity;
import org.Invoker.rpc.Adaptive;
import org.Invoker.rpc.extension.ExtensionLoader;

@Adaptive
public class ThreadPoolAdaptive implements ThreadPool {

	@Override
	public Executor getExecutor(Identity ident) {
		return ExtensionLoader.getExtensionLoader(ThreadPool.class).getExtension("cache").getExecutor(ident);
	}

}
