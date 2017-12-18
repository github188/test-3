package org.Invoker.remoting.exchanger;

import java.util.concurrent.Executor;

import org.Invoker.Identity;
import org.Invoker.rpc.Adaptive;
import org.Invoker.rpc.SPI;

@SPI
public interface ThreadPool {
	
	@Adaptive
	public Executor getExecutor(Identity ident);
}
