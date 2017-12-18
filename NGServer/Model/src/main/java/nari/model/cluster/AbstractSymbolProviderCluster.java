package nari.model.cluster;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import nari.model.device.SymbolProviderCluster;
import nari.model.symbol.SymbolProvider;

public abstract class AbstractSymbolProviderCluster implements SymbolProviderCluster {

	private ScheduledExecutorService scheduler = null;
	
	private final AtomicBoolean init = new AtomicBoolean(false);
	
	public AbstractSymbolProviderCluster(){
		
	}
	
	@Override
	public SymbolProvider getProvider() {
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
	
	protected abstract SymbolProvider select();
	
	protected abstract void update();
}
