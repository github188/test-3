package nari.model.cluster;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import nari.model.device.SelectorProvider;

public abstract class AbstractProviderCluster implements ProviderCluster{

	private ScheduledExecutorService scheduler = null;
	
	private final AtomicBoolean init = new AtomicBoolean(false);
	
	public AbstractProviderCluster(){
		
	}
	
	@Override
	public SelectorProvider getProvider() {
		if(!init.get()){
			scheduler = Executors.newSingleThreadScheduledExecutor();
			scheduler.scheduleAtFixedRate(new Runnable() {
				
				@Override
				public void run() {
					update();
				}
				
			}, 30, 60, TimeUnit.SECONDS);
			
			init.compareAndSet(false, true);
		}
		return select();
	}
	
	protected abstract SelectorProvider select();
	
	protected abstract void update();
}
