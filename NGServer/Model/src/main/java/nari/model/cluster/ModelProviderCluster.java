package nari.model.cluster;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

import nari.model.device.SelectorProvider;

public class ModelProviderCluster extends AbstractProviderCluster {

	private SelectorProvider[] providerClusters;
	
	private final AtomicReference<SelectorProvider> currentProvider = new AtomicReference<SelectorProvider>(null);
	
	public ModelProviderCluster(SelectorProvider[] providerClusters) {
		if(providerClusters==null){
			return;
		}
		
		Arrays.sort(providerClusters, new Comparator<SelectorProvider>() {
			
			@Override
			public int compare(SelectorProvider sp1, SelectorProvider sp2) {
				if(sp1.getPriority()>sp2.getPriority()){
					return 1;
				}
				if(sp1.getPriority()<sp2.getPriority()){
					return -1;
				}
				return 0;
			}
			
		});
		
		this.providerClusters = providerClusters;
		
		currentProvider.getAndSet(providerClusters[providerClusters.length-1]);
	}

	@Override
	protected SelectorProvider select() {
		return currentProvider.get();
	}

	@Override
	protected void update() {
		SelectorProvider provider = currentProvider.get();
		
		if(!provider.selfCheck()){
			for(int i=providerClusters.length-1;i>=0;i--){
				if(currentProvider.get()!=providerClusters[i]){
					currentProvider.getAndSet(providerClusters[i]);
					break;
				}
			}
		}else{
			for(SelectorProvider p:providerClusters){
				if(p!=currentProvider.get() && p.selfCheck() && p.getPriority()>provider.getPriority()){
					currentProvider.getAndSet(p);
					break;
				}
			}
		}
		
	}
}
