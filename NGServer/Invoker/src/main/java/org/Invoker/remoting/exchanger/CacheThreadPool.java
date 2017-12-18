package org.Invoker.remoting.exchanger;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.Invoker.Identity;

public class CacheThreadPool implements ThreadPool {

	private final AtomicReference<ExecutorService> ref = new AtomicReference<ExecutorService>();
	
	@Override
	public Executor getExecutor(Identity ident) {
		if(ref.get()==null){
			ExecutorService executor = Executors.newCachedThreadPool(new ThreadFactory() {
				
				private final AtomicInteger ai = new AtomicInteger(0);
				
				@Override
				public Thread newThread(Runnable r) {
					String name = "ExecutorHandler-@-"+ai.getAndIncrement();
					Thread t = new Thread(r,name);
					t.setDaemon(true);
					return t;
				}
				
			});
			ref.compareAndSet(null, executor);
		}
		return ref.get();
	}
	
}
